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
import static org.rubia.forums.ui.JSFUtil.getBundleMessage;
import static org.rubia.forums.ui.JSFUtil.handleException;
import static org.rubia.forums.ui.PortalUtil.createMessage;
import static org.rubia.forums.ui.PortalUtil.createPoll;
import static org.rubia.forums.ui.PortalUtil.createPollOption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.rubia.forums.ForumsModule;
import org.rubia.forums.auth.AuthorizationListener;
import org.rubia.forums.auth.SecureActionForum;
import org.rubia.forums.model.Attachment;
import org.rubia.forums.model.Message;
import org.rubia.forums.model.Poll;
import org.rubia.forums.model.PollOption;
import org.rubia.forums.model.Post;
import org.rubia.forums.model.Topic;

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

	@Inject
	private ForumsModule forumsModule;

	private boolean isFirstPost;

	public boolean isFirstPost() {
		return isFirstPost;
	}

	// action processing
	// methods-----------------------------------------------------------------------------------------------------
	/**
     * 
     *
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

				// setup the attachment related information
				setupAttachments(post.getAttachments());
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
     * 
     *
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
     * 
     *
     */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String execute() {
		String navState = null;
		boolean success = false;
		try {
			// setup the business objects to be updated
			Post post = forumsModule.findPostById(postId);
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
			List<PollOption> localPollOptions = new ArrayList<PollOption>();
			for (String key : options.keySet()) {
				PollOption pollOption = createPollOption(topic.getPoll());
				pollOption.setQuestion(options.get(key));
				pollOption.setVotes(0);
				localPollOptions.add(pollOption);
			}

			// update poll information
			if (topic.getPoll() == null || topic.getPoll().getTitle() == null || topic.getPoll().getTitle().trim().length() == 0) {
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
							if (oldPollOption != null && oldPollOption.getQuestion().equals(newPollOption.getQuestion())) {
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

			// TODO: cleanup this forums update process............move this as
			// a private method
			// setup attachment information
			Collection<Attachment> attachments = produceAttachments(this.attachments);
			Collection<Attachment> removeFilter = new ArrayList<Attachment>();
			Collection<Attachment> newAttachments = new ArrayList<Attachment>();

			// filter out the newly added attachments
			if (attachments != null) {
				for (Attachment cour : attachments) {
					if (cour.getId() == null) {
						newAttachments.add(cour); // adds this new attachment to
													// the
													// database
						removeFilter.add(cour); // remove this newly added
												// attachments from the list
						// to be compared against the list of stored attachments
						// for
						// update/remove
					}
				}
				attachments.removeAll(removeFilter);

				// update/remove currently stored attachments on this post
				Iterator<Attachment> storedAttachments = post.getAttachments().iterator();
				removeFilter = new ArrayList<Attachment>();
				while (storedAttachments.hasNext()) {
					Attachment storedAttachment = (Attachment) storedAttachments.next();

					// not sure why a record like this is popping out from the
					// hibernate layer
					if (storedAttachment.getId() == null) {
						removeFilter.add(storedAttachment); // clean this record
															// up
						continue;
					}

					// find this storedAttachment in the list of ui attachments.
					// ones that are found are to be updated. once not found
					// need to
					// be removed
					Iterator<Attachment> uiAttachments = attachments.iterator();
					if (uiAttachments == null || !uiAttachments.hasNext()) {
						// basically all attachments on the ui were deleted
						removeFilter.add(storedAttachment);
						break;
					}
					while (uiAttachments.hasNext()) {
						Attachment local = (Attachment) uiAttachments.next();
						if (local.getId().intValue() == storedAttachment.getId().intValue()) {
							// update this attachment
							storedAttachment.setComment(local.getComment());
							break;
						}
						// check if this stored attachment should be removed
						if (!uiAttachments.hasNext()) {
							// if i get here, no match was made with the list of
							// ui
							// attachments
							// hence this particular stored attachment must be
							// removed
							removeFilter.add(storedAttachment);
						}
					}
				}
				post.getAttachments().removeAll(removeFilter);

				// add the newly added attachments
				for (Attachment itr : newAttachments) {
					post.addAttachment(itr);
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
