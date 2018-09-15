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
import static it.vige.rubia.PortalUtil.createPoll;
import static it.vige.rubia.PortalUtil.createPollOption;
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.JSFUtil.getPoster;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;

//myfaces

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named
@SessionScoped
public class NewTopic extends PostAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4942677404675560696L;
	@EJB
	private ForumsModule forumsModule;
	@EJB
	private UserModule userModule;

	// ----------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah
	 * 
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String start() {
		String navState = null;
		try {
			int forumId = -1;
			String f = getParameter(p_forumId);
			if (f != null && f.trim().length() > 0) {
				forumId = parseInt(f);
			}

			// grab the forum where this topic will be added
			if (forumId != -1) {
				// re-initialize this controller to add a new topic
				// to the specified forum
				cleanup();

				// set the selected forum's id
				this.forumId = forumId;

				// get the poll title if one was specified
				question = getParameter(p_poll_title);
			}

			navState = START_NEW_TOPIC;
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * Execute
	 * 
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String execute() {
		String navState = null;
		boolean success = false;
		try {
			// setup the message
			MessageBean message = createMessage();
			message.setText(removeBorder(this.message));
			message.setSubject(subject);

			// setup the forum and the corresponding poster
			ForumBean forum = forumsModule.findForumById(forumId);
			PosterBean poster = getPoster(userModule, forumsModule);

			// setup the poll related information
			PollBean poll = createPoll();
			if (question != null && question.trim().length() > 0) {
				poll.setTitle(question);
				poll.setLength(activeDuration);
				List<PollOptionBean> pollOptions = new LinkedList<PollOptionBean>();
				for (String option : options.keySet()) {
					PollOptionBean pollOption = createPollOption(poll);
					pollOption.setQuestion((String) options.get(option));
					pollOption.setVotes(0);
					pollOptions.add(pollOption);
				}
				poll.setOptions(pollOptions);
				validatePoll(poll);
			}

			poster.incrementPostCount();
			// actually create the topic in this forum
			// use this method when poll and attachments are actually integrated
			TopicBean topicBean = new TopicBean();
			topicBean.setForum(forum);
			PostBean postBean = new PostBean();
			postBean.setMessage(message);
			postBean.setAttachments(attachments);
			postBean.setCreateDate(new Date());
			topicBean.setPosts(asList(new PostBean[] { postBean }));
			topicBean.setPoster(poster);
			topicBean.setPoll(poll);
			topicBean.setType(topicType);
			forumsModule.createTopic(topicBean);

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
}
