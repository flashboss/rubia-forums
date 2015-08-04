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
import static it.vige.rubia.ui.JSFUtil.getBundleMessage;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Integer.parseInt;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;

/**
 * Created on May 2, 2006
 * 
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named
@SessionScoped
public class EditPost extends PostAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4314224138089009378L;

	@EJB
	private ForumsModule forumsModule;

	private boolean isFirstPost;

	public boolean isFirstPost() {
		return isFirstPost;
	}

	// action processing
	// methods-----------------------------------------------------------------------------------------------------
	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String start() {
		String navState = null;
		try {
			int postId = -1;
			String p = getParameter(p_postId);
			if (p != null && p.trim().length() > 0) {
				postId = parseInt(p);
			}

			// grab the post information
			if (postId != -1) {
				// re-initialize this controller to edit the specified post
				cleanup();

				// get the post from the module
				Post post = forumsModule.findPostById(postId);
				Topic topic = post.getTopic();

				// set the selected post's topic id
				topicId = topic.getId().intValue();
				this.postId = postId;

				// set the subject of the post
				subject = post.getMessage().getSubject();

				// set the message of the post
				message = post.getMessage().getText();

				// set the topicType
				topicType = topic.getType();

				// setup poll related information
				setupPoll(topic.getPoll());

				attachments = forumsModule.findAttachments(post);

				// setup the attachment related information
				List<Post> posts = forumsModule.findPostsByTopicId(topic);

				isFirstPost = false;
				if (posts.get(0).getId().intValue() == post.getId().intValue()) {
					isFirstPost = true;
				}
			}

			navState = START_EDIT_POST;
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
	public String deletePoll() {
		String navState = null;
		try {
			// cleanout poll related data from this controller
			question = null;
			options = new TreeMap<String, String>();
			activeDuration = 0;

			navState = SUCCESS;
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
	public String execute() {
		String navState = null;
		boolean success = false;
		try {
			// setup the business objects to be updated
			Post post = forumsModule.findPostById(postId);
			forumsModule.updateAttachments(attachments, post);

			// TODO: cleanup this forums update process............move this as
			// a private method
			// setup attachment information
			Topic topic = post.getTopic();

			// make sure this topic is not locked
			if (topic.getStatus() == TOPIC_LOCKED) {
				// should not allow posting a reply since the topic is locked
				throw new Exception(getBundleMessage(BUNDLE_NAME, TOPIC_LOCKED_ERR_KEY));
			}

			// setup the message/subject related data
			Message message = createMessage();
			message.setText(this.message);
			message.setSubject(subject);

			// update the message/subject/topicType data on the business objects
			post.setMessage(message);
			if (isFirstPost) {
				topic.setSubject(subject);
				topic.setType(topicType);
			}

			// miscellaneous post related update
			post.setEditCount(post.getEditCount() + 1);
			post.setEditDate(new Date());

			// TODO: cleanup this poll update process............move this as a
			// private method
			// setup poll information
			List<PollOption> localPollOptions = new LinkedList<PollOption>();
			for (String key : options.keySet()) {
				PollOption pollOption = createPollOption(topic.getPoll());
				pollOption.setQuestion(options.get(key));
				pollOption.setVotes(0);
				localPollOptions.add(pollOption);
			}

			// update poll information
			if (topic.getPoll() == null || topic.getPoll().getTitle() == null
					|| topic.getPoll().getTitle().trim().length() == 0) {
				// no existing poll information found in the database
				if (localPollOptions.size() > 0 && question != null && question.trim().length() > 0) {
					// need to add a new poll to this topic
					Poll poll = createPoll();
					poll.setTitle(question);
					poll.setLength(activeDuration);
					poll.setOptions(localPollOptions);
					validatePoll(poll);
					forumsModule.addPollToTopic(topic, poll);
				}
			} else {
				// existing poll information is available in the database
				if (localPollOptions.size() > 0) {
					// this is a diff update..............................

					// setup the poll to be updated in the database
					Poll poll = createPoll();
					poll.setTitle(question);
					poll.setLength(activeDuration);
					poll.setVoted(topic.getPoll().getVoted());
					poll.setCreationDate(topic.getPoll().getCreationDate());

					for (PollOption newPollOption : localPollOptions) {
						Iterator<PollOption> stored = topic.getPoll().getOptions().iterator();
						while (stored.hasNext()) {
							PollOption oldPollOption = (PollOption) stored.next();
							if (oldPollOption != null
									&& oldPollOption.getQuestion().equals(newPollOption.getQuestion())) {
								newPollOption.setVotes(oldPollOption.getVotes());
								break;
							}
						}
					}
					poll.setOptions(localPollOptions);

					forumsModule.addPollToTopic(topic, poll);
				} else {
					// remove the poll from the database...poll was removed
					// during this editPost process
					topic.setPoll(null);
				}
			}
			forumsModule.update(topic);
			forumsModule.update(post);
			// set the proper navigation state
			navState = SUCCESS;

			success = true;
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
