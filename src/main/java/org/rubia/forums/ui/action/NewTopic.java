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
package org.rubia.forums.ui.action;

import static java.lang.Integer.parseInt;
import static org.rubia.forums.ui.ForumUtil.getParameter;
import static org.rubia.forums.ui.JSFUtil.handleException;
import static org.rubia.forums.ui.PortalUtil.createMessage;
import static org.rubia.forums.ui.PortalUtil.createPoll;
import static org.rubia.forums.ui.PortalUtil.createPollOption;
import static org.rubia.forums.ui.PortalUtil.getPoster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.rubia.forums.ForumsModule;
import org.rubia.forums.auth.AuthorizationListener;
import org.rubia.forums.auth.SecureActionForum;
import org.rubia.forums.auth.UserModule;
import org.rubia.forums.model.Forum;
import org.rubia.forums.model.Message;
import org.rubia.forums.model.Poll;
import org.rubia.forums.model.PollOption;
import org.rubia.forums.model.Poster;

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
	@Inject
	private ForumsModule forumsModule;
	@Inject
	private UserModule userModule;

	// ----------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah
	 * 
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

	// --------execute-------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String execute() {
		String navState = null;
		boolean success = false;
		try {
			// setup the message
			Message message = createMessage();
			message.setText(this.message);
			message.setSubject(subject);

			// setup the forum and the corresponding poster
			Forum forum = forumsModule.findForumById(forumId);
			Poster poster = getPoster(userModule, forumsModule);

			// setup the poll related information
			Poll poll = createPoll();
			if (question != null && question.trim().length() > 0) {
				poll.setTitle(question);
				poll.setLength(activeDuration);
				List<PollOption> pollOptions = new ArrayList<PollOption>();
				for (String option : options.keySet()) {
					PollOption pollOption = createPollOption(poll);
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
			forumsModule.createTopic(forum, message, new Date(), poster, poll, // poll
					produceAttachments(attachments), // attachments
					topicType);

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
