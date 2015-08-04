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
package it.vige.rubia.ui.action;

import static it.vige.rubia.PortalUtil.createMessage;
import static it.vige.rubia.PortalUtil.getPoster;
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.JSFUtil.getBundleMessage;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Integer.parseInt;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.ui.view.ViewTopic;
import it.vige.rubia.util.CurrentTopicPage;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@Named
@SessionScoped
public class ReplyTopic extends PostAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6918238036659797067L;

	@EJB
	private ForumsModule forumsModule;

	@EJB
	private UserModule userModule;

	@Inject
	private ViewTopic viewTopic;

	@Inject
	private CurrentTopicPage currentTopicPage;

	private Topic topic;

	// ----------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah
	 * 
	 */

	@PostConstruct
	public void prestart() {
		start();
	}

	public String start() {
		String navState = null;
		try {
			int forumId = -1;
			int topicId = -1;
			int postId = -1;
			String f = getParameter(p_forumId);
			String t = getParameter(p_topicId);
			String p = getParameter(p_postId);
			if (f != null && f.trim().length() > 0) {
				forumId = parseInt(f);
			}
			if (t != null && t.trim().length() > 0) {
				topicId = parseInt(t);
			}
			if (p != null && p.trim().length() > 0) {
				postId = parseInt(p);
			}

			Topic topic = null;

			if (topicId == -1 && postId != -1) {
				// If topicId was not given and postId is available we get
				// topicId from Post
				Post post = forumsModule.findPostById((postId));
				if (post != null) {
					topic = post.getTopic();
					topicId = topic.getId().intValue();
				}
			} else {
				topic = forumsModule.findTopicById(topicId);
			}

			if (forumId == -1 && topic != null) {
				forumId = topic.getForum().getId().intValue();
			}

			// re-initialize this controller to add a reply post in a topic
			// to the specified forum
			cleanup();

			// set the selected forum's id and topic's id
			this.forumId = forumId;
			this.topicId = topicId;

			// set the subject information
			subject = RE + topic.getSubject();

			navState = START_REPLY;

		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String startInstantReplyPreview() {
		String navState = null;
		String message = getMessage();
		navState = start();
		setMessage(message);
		preview();
		return navState;
	}

	/**
	 * Execute
	 * 
	 * @return the navigation state of the application
	 */
	public String execute() {
		String navState = null;
		boolean success = false;
		try {
			Message message = createMessage();
			message.setText(this.message);
			message.setSubject(subject);

			// setup the forum and the corresponding poster
			Forum forum = forumsModule.findForumById(forumId);
			topic = forumsModule.findTopicById(topicId);
			Poster poster = getPoster(forumsModule, userModule);

			// make sure this topic is not locked
			if (topic.getStatus() == TOPIC_LOCKED) {
				// should not allow posting a reply since the topic is locked
				throw new Exception(getBundleMessage(BUNDLE_NAME, TOPIC_LOCKED_ERR_KEY));
			}

			// actually post a reply to this topic in the forum
			poster.incrementPostCount();
			forumsModule.createPost(topic, forum, message, new Date(), poster, attachments // attachments
			);
			currentTopicPage.setPage(viewTopic.getLastPageNumber());

			// setup the navigation state
			navState = SUCCESS;

			success = true;
		} catch (MessageValidationException e) {
			// handle proper validation error with a proper message...not just a
			// generic message..
			// just use generic error page for the proof of concept
			// set the custom exception such that e.toString() results in the
			// proper message
			handleException(e);
		} catch (PollValidationException e) {
			// handle proper validation error with a proper message...not just a
			// generic message..
			// just use generic error page for the proof of concept
			// set the custom exception such that e.toString() results in the
			// proper message
			handleException(e);
		} catch (Exception e) {
			handleException(e);
		} finally {
			// cleanup if necessary
			if (success) {
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String executeInstantReply() {
		String navState;

		// setup the instantReply message
		String message = getMessage();
		start();
		setMessage(message);

		// make the actual post
		execute();
		try {
			viewTopic.setTopic(topic);
			viewTopic.refreshTopic(topic.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			handleException(e);
		}

		// setup proper navigation
		navState = "instantReply";
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String startQuote() {
		String navState = start();
		try {
			String p = getParameter(p_postId);
			if (p != null && p.trim().length() > 0) {
				postId = parseInt(p);
			}

			// setup the quote information
			Post post = forumsModule.findPostById(postId);
			Poster poster = getPoster(forumsModule, userModule);
			String userName = userModule.findUserById(poster.getUserId()).getUserName();
			message = userName + "<" + QUOTE + ">" + post.getMessage().getText() + "</" + QUOTE + "></br>";
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}
}
