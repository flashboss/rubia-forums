/******************************************************************************
 * Vige, Home of Professional Open Source Copyright 2010, Vige, and           *
 * individual contributors by the @authors tag. See the copyright.txt in the  *
 * distribution for a full listing of individual contributors.                *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may    *
 * not use this file except in compliance with the License. You may obtain    *
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0        *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package org.rubia.forums.util;

import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.rubia.forums.ui.Constants.WATCH_MODE_EMBEDED;
import static org.rubia.forums.ui.Constants.WATCH_MODE_LINKED;
import static org.rubia.forums.ui.Constants.WATCH_MODE_NONE;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.faces.component.UIViewRoot;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.solder.logging.TypedCategory;
import org.rubia.forums.ForumsModule;
import org.rubia.forums.ModuleException;
import org.rubia.forums.auth.ForumsACLProvider;
import org.rubia.forums.auth.JSFUIContext;
import org.rubia.forums.auth.User;
import org.rubia.forums.auth.UserModule;
import org.rubia.forums.auth.UserProfileModule;
import org.rubia.forums.log.NotificationEngineLog;
import org.rubia.forums.model.Category;
import org.rubia.forums.model.Forum;
import org.rubia.forums.model.Message;
import org.rubia.forums.model.Post;
import org.rubia.forums.model.Topic;
import org.rubia.forums.model.Watch;
import org.rubia.forums.ui.PortalUtil;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Singleton
@Named
public class NotificationEngine {

	@Inject
	@TypedCategory(NotificationEngine.class)
	private NotificationEngineLog logger;

	@Inject
	private ForumsACLProvider forumsACLProvider;

	@Inject
	private UserModule userModule;

	@Inject
	private UserProfileModule userProfileModule;

	// Types of post
	public static final int MODE_POST = 0;
	public static final int MODE_REPLY = 1;
	public static final int MODE_REPOST = 2;

	private ForumsModule forumsModule;
	private String from;
	private ScheduledThreadPoolExecutor executor;
	private TransactionManager tm;

	public NotificationEngine() {
		try {
			InitialContext ctx = new InitialContext();
			tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
			executor = new ScheduledThreadPoolExecutor(100);
		} catch (NamingException e) {
			logger.error("Cannot create notification interceptor", e);
		}
	}

	public void stop() {
		executor.shutdown();
		executor = null;
		tm = null;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void scheduleForNotification(Integer postId, int mode) {
		// TODO: IMPLEMENT NOTIFICATION FOR STANDALONE VERSION OF FORUMS.
		logger.warn("NOTIFICATION FOR STANDALONE HAS NOT BEEN YET IMPLEMENTED");
	}

	public void schedule(Integer postId, int mode, String absViewURL,
			String absReplyURL) {
		try {

			if (postId == null || mode == -1) {
				logger.warn("Request didn't have needed parameters.");
				return;
			}

			// Getting ResourceBundle with current Locale
			// Too bad for now we support notifications sent in the locale of
			// the poster :-(

			UIViewRoot uiRoot = getCurrentInstance().getViewRoot();
			Locale locale = uiRoot.getLocale();
			ClassLoader ldr = Thread.currentThread().getContextClassLoader();
			ResourceBundle bundle = ResourceBundle.getBundle("ResourceJSF",
					locale, ldr);

			// Create task
			NotificationTask task = new NotificationTask(absViewURL,
					absReplyURL, postId, mode, bundle);

			// Register at the end of the current tx to broadcast notifications
			Transaction tx = tm.getTransaction();
			tx.registerSynchronization(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFrom(Post post) {
		StringBuffer fromBuf = null;
		User user = userModule.findUserById(post.getPoster().getUserId());
		if ((userProfileModule.getProperty(user, User.INFO_USER_NAME_GIVEN) != null)
				&& (userProfileModule.getProperty(user,
						User.INFO_USER_NAME_FAMILY) != null)) {
			fromBuf = new StringBuffer(userProfileModule.getProperty(user,
					User.INFO_USER_NAME_GIVEN)
					+ " "
					+ userProfileModule.getProperty(user,
							User.INFO_USER_NAME_FAMILY) + " <");
		} else {
			fromBuf = new StringBuffer(user.getUserName() + " <");
		}
		fromBuf.append(from + ">");
		return fromBuf.toString();
	}

	/**
	 * The notification task.
	 */
	class NotificationTask implements Runnable, Synchronization {

		private final int mode;
		private final Integer postId;
		private final ResourceBundle bundle;
		private final String viewURL;
		private final String replyURL;

		NotificationTask(String viewURL, String replyURL, final Integer postId,
				int mode, ResourceBundle bundle) {
			this.mode = mode;
			this.postId = postId;
			this.viewURL = viewURL;
			this.replyURL = replyURL;
			this.bundle = bundle;
		}

		public void run() {
			try {
				Post post = forumsModule.findPostById(postId);
				Topic topic = post.getTopic();
				Forum forum = topic.getForum();
				Category category = forum.getCategory();

				Message message = post.getMessage();
				String from = getFrom(post);
				// Hold the notified users to avoid duplicated
				Set<Object> notifieds = new HashSet<Object>();

				// If this is not an anonymous post, put the user in the
				// notified list
				// he won't be notified of his own action
				User poster = userModule.findUserById(post.getPoster()
						.getUserId());
				if (poster != null) {
					notifieds.add(poster.getId());
				}
				StringWriter out = new StringWriter();

				String forumEmbededArgsSubject = "[" + forum.getName() + "] - "
						+ message.getSubject()
						+ (mode == MODE_REPOST ? " (Repost)" : "");

				String forumEmbededArgsText = out.toString() + "<br /><br />\n"
						+ bundle.getString("EMAIL_VIEWORIGINAL") + " : "
						+ "<a href=\"" + viewURL.toString() + "\">"
						+ viewURL.toString() + "</a>\n" + "<br /><br />\n"
						+ bundle.getString("EMAIL_REPLY") + " : "
						+ "<a href=\"" + replyURL.toString() + "\">"
						+ replyURL.toString() + "</a>" + "<br /><br />\n";

				// For now it is just a copy from embeded mode maybe in future
				// we will differentiate it.
				String forumLinkedArgsSubject = forumEmbededArgsSubject;

				String forumLinkedArgsText = bundle
						.getString("EMAIL_LINKED_MODE_INFO")
						+ " <b>"
						+ forum.getName()
						+ "</b> <br /><br />\n"
						+ bundle.getString("EMAIL_VIEWORIGINAL")
						+ " : "
						+ "<a href=\""
						+ viewURL.toString()
						+ "\">"
						+ viewURL.toString()
						+ "</a>\n"
						+ "<br /><br />\n"
						+ bundle.getString("EMAIL_REPLY")
						+ " : "
						+ "<a href=\""
						+ replyURL.toString()
						+ "\">"
						+ replyURL.toString() + "</a>" + "<br /><br />\n";

				// Notify the forum watchers
				for (Iterator<Watch> i = forum.getWatches().iterator(); i
						.hasNext();) {
					try {
						Watch watch = i.next();

						// If user don't want to be notified by e-mail then
						// continue with next watch.
						if (watch.getMode() == WATCH_MODE_NONE) {
							continue;
						}
						User watcher = userModule.findUserById(watch
								.getPoster().getUserId());
						Object watcherId = watcher.getId();

						if (!notifieds.contains(watcherId)
								&& !watcherId.equals(PortalUtil.getUserNA()
										.getId())) {

							boolean securityFlag = true;

							// Creating security context for the user
							JSFUIContext securityContext = new JSFUIContext(
									watcher, getCurrentInstance());

							// Checking if user has privileges to read category
							securityContext.setFragment("acl://readCategory");
							securityContext
									.setContextData(new Object[] { category });
							securityFlag = forumsACLProvider
									.hasAccess(securityContext) && securityFlag;

							// Checking if user has privileges to read forum
							securityContext.setFragment("acl://readForum");
							securityContext
									.setContextData(new Object[] { forum });
							securityFlag = forumsACLProvider
									.hasAccess(securityContext) && securityFlag;

							if (securityFlag) {
								notifieds.add(watcherId);
								String subject = null;
								String text = null;

								if (watch.getMode() == WATCH_MODE_EMBEDED) {
									subject = forumEmbededArgsSubject;
									text = forumEmbededArgsText;
								} else if (watch.getMode() == WATCH_MODE_LINKED) {
									subject = forumLinkedArgsSubject;
									text = forumLinkedArgsText;
								}

								notify(watcher, from, subject, text);

							} else {
								// Not authorized anymore, we remove the watch
								forumsModule.removeWatch(watch);
							}
						}
					} catch (Exception e) {
						logger.error("Cannot send an email notification", e);
					}
				}

				String topicEmbededArgsSubject = "[" + forum.getName() + "] - "
						+ message.getSubject()
						+ (mode == MODE_REPOST ? " (Repost)" : "");

				String topicEmbededArgsText = out.toString() + "<br /><br />\n"
						+ bundle.getString("EMAIL_VIEWORIGINAL") + " : "
						+ "<a href=\"" + viewURL.toString() + "\">"
						+ viewURL.toString() + "</a>\n" + "<br /><br />\n"
						+ bundle.getString("EMAIL_REPLY") + " : "
						+ "<a href=\"" + replyURL.toString() + "\">"
						+ replyURL.toString() + "</a>" + "<br /><br />\n";

				// For now it is just a copy from embeded mode maybe in future
				// we will differentiate it.
				String topicLinkedArgsSubject = topicEmbededArgsSubject;

				String topicLinkedArgsText = bundle
						.getString("EMAIL_LINKED_MODE_INFO")
						+ ": <b>"
						+ topic.getSubject()
						+ "</b><br /><br />\n"
						+ bundle.getString("EMAIL_VIEWORIGINAL")
						+ " : "
						+ "<a href=\""
						+ viewURL.toString()
						+ "\">"
						+ viewURL.toString()
						+ "</a>\n"
						+ "<br /><br />\n"
						+ bundle.getString("EMAIL_REPLY")
						+ " : "
						+ "<a href=\""
						+ replyURL.toString()
						+ "\">"
						+ replyURL.toString() + "</a>" + "<br /><br />\n";

				// Notify the topic watchers
				if (mode == MODE_REPLY) {
					// Notify the reply watchers
					for (Watch watch : topic.getWatches()) {
						try {

							// If user don't want to be notified by e-mail then
							// continue with next watch.
							if (watch.getMode() == WATCH_MODE_NONE) {
								continue;
							}

							User watcher = userModule.findUserById(watch
									.getPoster().getUserId());
							Object watcherId = watcher.getId();
							if (!notifieds.contains(watcherId)
									&& !watcherId.equals(PortalUtil.getUserNA()
											.getId())) {
								boolean securityFlag = true;

								// Creating security context for the user
								JSFUIContext securityContext = new JSFUIContext(
										watcher, getCurrentInstance());

								// Checking if user has privileges to read
								// category
								securityContext
										.setFragment("acl://readCategory");
								securityContext
										.setContextData(new Object[] { category });
								securityFlag = forumsACLProvider
										.hasAccess(securityContext)
										&& securityFlag;

								// Checking if user has privileges to read forum
								securityContext.setFragment("acl://readForum");
								securityContext
										.setContextData(new Object[] { forum });
								securityFlag = forumsACLProvider
										.hasAccess(securityContext)
										&& securityFlag;

								if (securityFlag) {
									// Authorized
									notifieds.add(watcherId);
									String subject = null;
									String text = null;

									if (watch.getMode() == WATCH_MODE_EMBEDED) {
										subject = topicEmbededArgsSubject;
										text = topicEmbededArgsText;
									} else if (watch.getMode() == WATCH_MODE_LINKED) {
										subject = topicLinkedArgsSubject;
										text = topicLinkedArgsText;
									}

									notify(watcher, from, subject, text);
								} else {
									// Not authorized anymore, we remove the
									// watch
									forumsModule.removeWatch(watch);
								}
							}
						} catch (Exception e) {
							logger.error("Cannot send email notification", e);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				logger.error("IllegalArgumentException", e);
			} catch (ModuleException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		public void beforeCompletion() {
		}

		private void notify(User watcher, String from, String subject,
				String text) {

			Session session = null;

			try {
				session = (Session) PortableRemoteObject
						.narrow(new InitialContext().lookup("java:Mail"),
								Session.class);
				try {

					StringBuffer buffer = null;
					Address[] to = null;
					MimeMessage m = new MimeMessage(session);
					String email = userProfileModule.getProperty(watcher,
							User.INFO_USER_EMAIL_REAL).toString();
					if (email != null) {
						m.setFrom(new InternetAddress(from));
						to = new InternetAddress[] { new InternetAddress(email) };
						m.setRecipients(RecipientType.TO, to);
						m.setSubject(subject);

						m.setSentDate(new Date());
						buffer = new StringBuffer();

						buffer.append(text);
						buffer.append(bundle.getString("EMAIL_FOOTER_MESSAGE"));

						m.setContent(buffer.toString(),
								"text/html; charset=\"UTF-8\"");
						Transport.send(m);

					}
				} catch (MessagingException e) {
					logger.error("MessagingException", e);
				}
			} catch (NamingException e) {
				logger.error("NamingException", e);
			}
		}

		@Override
		public void afterCompletion(int status) {

		}
	}
}