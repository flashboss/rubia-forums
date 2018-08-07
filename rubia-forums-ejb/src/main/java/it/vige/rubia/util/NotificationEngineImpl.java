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
package it.vige.rubia.util;

import static it.vige.rubia.Constants.WATCH_MODE_EMBEDED;
import static it.vige.rubia.Constants.WATCH_MODE_LINKED;
import static it.vige.rubia.Constants.WATCH_MODE_NONE;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.jboss.logging.Logger.getLogger;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.component.UIViewRoot;
import javax.inject.Named;
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

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.PortalUtil;
import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.UIContext;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.WatchBean;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Singleton
@Named
public class NotificationEngineImpl implements NotificationEngine {

	private static Logger log = getLogger(NotificationEngineImpl.class);
	
	@EJB
	private ForumsACLProvider forumsACLProvider;

	@EJB
	private UserModule userModule;

	@EJB
	private UserProfileModule userProfileModule;

	private ForumsModule forumsModule;
	private String from;
	private ScheduledThreadPoolExecutor executor;
	private TransactionManager tm;

	public NotificationEngineImpl() {
		try {
			InitialContext ctx = new InitialContext();
			tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
			executor = new ScheduledThreadPoolExecutor(100);
		} catch (NamingException e) {
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
	}

	public void schedule(Integer postId, int mode, String absViewURL, String absReplyURL) {
		try {

			if (postId == null || mode == -1) {
				return;
			}

			// Getting ResourceBundle with current Locale
			// Too bad for now we support notifications sent in the locale of
			// the poster :-(

			UIViewRoot uiRoot = getCurrentInstance().getViewRoot();
			Locale locale = uiRoot.getLocale();
			ClassLoader ldr = Thread.currentThread().getContextClassLoader();
			ResourceBundle bundle = ResourceBundle.getBundle("ResourceJSF", locale, ldr);

			// Create task
			NotificationTask task = new NotificationTask(absViewURL, absReplyURL, postId, mode, bundle);

			// Register at the end of the current tx to broadcast notifications
			Transaction tx = tm.getTransaction();
			tx.registerSynchronization(task);
		} catch (Exception e) {
			log.error(e);
		}
	}

	private String getFrom(PostBean post) {
		StringBuffer fromBuf = null;
		User user = userModule.findUserById(post.getPoster().getUserId());
		if ((userProfileModule.getProperty(user, User.INFO_USER_NAME_GIVEN) != null)
				&& (userProfileModule.getProperty(user, User.INFO_USER_NAME_FAMILY) != null)) {
			fromBuf = new StringBuffer(userProfileModule.getProperty(user, User.INFO_USER_NAME_GIVEN) + " "
					+ userProfileModule.getProperty(user, User.INFO_USER_NAME_FAMILY) + " <");
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

		NotificationTask(String viewURL, String replyURL, final Integer postId, int mode, ResourceBundle bundle) {
			this.mode = mode;
			this.postId = postId;
			this.viewURL = viewURL;
			this.replyURL = replyURL;
			this.bundle = bundle;
		}

		public void run() {
			try {
				PostBean post = forumsModule.findPostById(postId);
				TopicBean topic = post.getTopic();
				ForumBean forum = topic.getForum();
				CategoryBean category = forum.getCategory();

				MessageBean message = post.getMessage();
				String from = getFrom(post);
				// Hold the notified users to avoid duplicated
				Set<Object> notifieds = new HashSet<Object>();

				// If this is not an anonymous post, put the user in the
				// notified list
				// he won't be notified of his own action
				User poster = userModule.findUserById(post.getPoster().getUserId());
				if (poster != null) {
					notifieds.add(poster.getId());
				}
				StringWriter out = new StringWriter();

				String forumEmbededArgsSubject = "[" + forum.getName() + "] - " + message.getSubject()
						+ (mode == MODE_REPOST ? " (Repost)" : "");

				String forumEmbededArgsText = out.toString() + "<br /><br />\n" + bundle.getString("EMAIL_VIEWORIGINAL")
						+ " : " + "<a href=\"" + viewURL.toString() + "\">" + viewURL.toString() + "</a>\n"
						+ "<br /><br />\n" + bundle.getString("EMAIL_REPLY") + " : " + "<a href=\""
						+ replyURL.toString() + "\">" + replyURL.toString() + "</a>" + "<br /><br />\n";

				// For now it is just a copy from embeded mode maybe in future
				// we will differentiate it.
				String forumLinkedArgsSubject = forumEmbededArgsSubject;

				String forumLinkedArgsText = bundle.getString("EMAIL_LINKED_MODE_INFO") + " <b>" + forum.getName()
						+ "</b> <br /><br />\n" + bundle.getString("EMAIL_VIEWORIGINAL") + " : " + "<a href=\""
						+ viewURL.toString() + "\">" + viewURL.toString() + "</a>\n" + "<br /><br />\n"
						+ bundle.getString("EMAIL_REPLY") + " : " + "<a href=\"" + replyURL.toString() + "\">"
						+ replyURL.toString() + "</a>" + "<br /><br />\n";

				// Notify the forum watchers
				for (Iterator<WatchBean> i = forum.getWatches().iterator(); i.hasNext();) {
					try {
						WatchBean watch = i.next();

						// If user don't want to be notified by e-mail then
						// continue with next watch.
						if (watch.getMode() == WATCH_MODE_NONE) {
							continue;
						}
						User watcher = userModule.findUserById(watch.getPoster().getUserId());
						Object watcherId = watcher.getId();

						if (!notifieds.contains(watcherId) && !watcherId.equals(PortalUtil.getUserNA().getId())) {

							boolean securityFlag = true;

							// Creating security context for the user
							UIContext securityContext = new UIContext(watcher);

							// Checking if user has privileges to read category
							securityContext.setFragment("acl://readCategory");
							securityContext.setContextData(new Object[] { category });
							securityFlag = forumsACLProvider.hasAccess(securityContext) && securityFlag;

							// Checking if user has privileges to read forum
							securityContext.setFragment("acl://readForum");
							securityContext.setContextData(new Object[] { forum });
							securityFlag = forumsACLProvider.hasAccess(securityContext) && securityFlag;

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
					}
				}

				String topicEmbededArgsSubject = "[" + forum.getName() + "] - " + message.getSubject()
						+ (mode == MODE_REPOST ? " (Repost)" : "");

				String topicEmbededArgsText = out.toString() + "<br /><br />\n" + bundle.getString("EMAIL_VIEWORIGINAL")
						+ " : " + "<a href=\"" + viewURL.toString() + "\">" + viewURL.toString() + "</a>\n"
						+ "<br /><br />\n" + bundle.getString("EMAIL_REPLY") + " : " + "<a href=\""
						+ replyURL.toString() + "\">" + replyURL.toString() + "</a>" + "<br /><br />\n";

				// For now it is just a copy from embeded mode maybe in future
				// we will differentiate it.
				String topicLinkedArgsSubject = topicEmbededArgsSubject;

				String topicLinkedArgsText = bundle.getString("EMAIL_LINKED_MODE_INFO") + ": <b>" + topic.getSubject()
						+ "</b><br /><br />\n" + bundle.getString("EMAIL_VIEWORIGINAL") + " : " + "<a href=\""
						+ viewURL.toString() + "\">" + viewURL.toString() + "</a>\n" + "<br /><br />\n"
						+ bundle.getString("EMAIL_REPLY") + " : " + "<a href=\"" + replyURL.toString() + "\">"
						+ replyURL.toString() + "</a>" + "<br /><br />\n";

				// Notify the topic watchers
				if (mode == MODE_REPLY) {
					// Notify the reply watchers
					for (WatchBean watch : topic.getWatches()) {
						try {

							// If user don't want to be notified by e-mail then
							// continue with next watch.
							if (watch.getMode() == WATCH_MODE_NONE) {
								continue;
							}

							User watcher = userModule.findUserById(watch.getPoster().getUserId());
							Object watcherId = watcher.getId();
							if (!notifieds.contains(watcherId) && !watcherId.equals(PortalUtil.getUserNA().getId())) {
								boolean securityFlag = true;

								// Creating security context for the user
								UIContext securityContext = new UIContext(watcher);

								// Checking if user has privileges to read
								// category
								securityContext.setFragment("acl://readCategory");
								securityContext.setContextData(new Object[] { category });
								securityFlag = forumsACLProvider.hasAccess(securityContext) && securityFlag;

								// Checking if user has privileges to read forum
								securityContext.setFragment("acl://readForum");
								securityContext.setContextData(new Object[] { forum });
								securityFlag = forumsACLProvider.hasAccess(securityContext) && securityFlag;

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
						}
					}
				}
			} catch (IllegalArgumentException e) {
			} catch (ModuleException e1) {
				log.error(e1);
			}
		}

		public void beforeCompletion() {
		}

		private void notify(User watcher, String from, String subject, String text) {

			Session session = null;

			try {
				session = (Session) PortableRemoteObject.narrow(new InitialContext().lookup("java:Mail"),
						Session.class);
				try {

					StringBuffer buffer = null;
					Address[] to = null;
					MimeMessage m = new MimeMessage(session);
					String email = userProfileModule.getProperty(watcher, User.INFO_USER_EMAIL_REAL).toString();
					if (email != null) {
						m.setFrom(new InternetAddress(from));
						to = new InternetAddress[] { new InternetAddress(email) };
						m.setRecipients(RecipientType.TO, to);
						m.setSubject(subject);

						m.setSentDate(new Date());
						buffer = new StringBuffer();

						buffer.append(text);
						buffer.append(bundle.getString("EMAIL_FOOTER_MESSAGE"));

						m.setContent(buffer.toString(), "text/html; charset=\"UTF-8\"");
						Transport.send(m);

					}
				} catch (MessagingException e) {
				}
			} catch (NamingException e) {
			}
		}

		@Override
		public void afterCompletion(int status) {

		}
	}
}