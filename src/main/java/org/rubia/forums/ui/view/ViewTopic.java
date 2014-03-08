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
package org.rubia.forums.ui.view;

import static java.lang.Integer.parseInt;
import static org.rubia.forums.feeds.FeedConstants.ATOM;
import static org.rubia.forums.feeds.FeedConstants.RSS;
import static org.rubia.forums.feeds.FeedConstants.TOPIC;
import static org.rubia.forums.ui.ForumUtil.getParameter;
import static org.rubia.forums.ui.PortalUtil.createFeedLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.rubia.forums.ForumsModule;
import org.rubia.forums.auth.AuthorizationListener;
import org.rubia.forums.auth.SecureActionForum;
import org.rubia.forums.model.Post;
import org.rubia.forums.model.Topic;
import org.rubia.forums.ui.BaseController;
import org.rubia.forums.ui.action.PreferenceController;
import org.rubia.forums.util.CurrentTopicPage;

/**
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
@Named("topic")
@RequestScoped
public class ViewTopic extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5205743830388129653L;

	@Inject
	private ForumsModule forumsModule;
	/**
	 * user preference related data
	 */
	@Inject
	private PreferenceController userPreferences;

	@Inject
	private CurrentTopicPage currentTopicPage;

	private Topic topic;
	private long postDays; // This is value of selectItem describing time
							// constraint for displayed posts
	private List<Post> topics = new ArrayList<Post>();
	private DataModel<Post> topicsDataModel = new ListDataModel<Post>(topics);

	public int getLastPageNumber() {
		if (topic != null)
			return topic.getReplies() / userPreferences.getPostsPerTopic() + 1;
		else
			return 1;
	}

	public DataModel<Post> getTopicsDataModel() {
		return topicsDataModel;
	}

	public void setTopicsDataModel(DataModel<Post> topicsDataModel) {
		this.topicsDataModel = topicsDataModel;
	}

	/**
	 * 
	 *
	 */
	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * 
	 *
	 */
	public void setPostDays(long postDays) {
		this.postDays = postDays;
	}

	/**
     * 
     *
     */
	public long getPostDays() {
		return postDays;
	}

	/**
    * 
    *
    */
	public boolean isPollPresent() {
		boolean isPollPresent = false;

		if (topic.getPoll() != null && topic.getPoll().getId() != null
				&& topic.getPoll().getId().intValue() > 0
				&& topic.getPoll().getOptions() != null
				&& !topic.getPoll().getOptions().isEmpty()) {
			isPollPresent = true;
		}

		return isPollPresent;
	}

	/**
    * 
    *
    */
	public boolean isBallotView() {
		boolean isBallotView = true; // in ballot view by default

		String result = getParameter(p_results);
		if (result != null && result.trim().length() > 0) {
			isBallotView = false;
		}

		return isBallotView;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getRssFeed() {
		return createFeedLink(RSS, TOPIC, topic.getId());
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getAtomFeed() {
		return createFeedLink(ATOM, TOPIC, topic.getId());
	}

	/**
    * 
    *
    */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Post> getTopics() {
		return topics;
	}

	// ------------user
	// preferences-------------------------------------------------------------------------------------------------------------
	/**
	 * @return Returns the userPreferences.
	 */
	public PreferenceController getUserPreferences() {
		return userPreferences;
	}

	/**
	 * @param userPreferences
	 *            The userPreferences to set.
	 */
	public void setUserPreferences(PreferenceController userPreferences) {
		this.userPreferences = userPreferences;
	}

	/**
	 * This method gets topicId from parameters and tries to find topic with
	 * that id.
	 */
	@PostConstruct
	public void execute() throws Exception {
		// parse input data
		int topicId = -1;
		Integer postId = -1;
		String t = getParameter(p_topicId);
		String p = getParameter(p_postId);

		if (t != null && t.trim().length() > 0) {
			topicId = parseInt(t);
		}

		if (p != null && p.trim().length() > 0) {
			postId = new Integer(p);
			Post post = forumsModule.findPostById(postId);
			topicId = post.getTopic().getId().intValue();
			topic = post.getTopic();
		}
		refreshTopic(topicId);
	}

	public boolean isLocked() {
		return topic.getStatus() == TOPIC_LOCKED;
	}

	public boolean isForumLocked() {
		return topic.getForum().getStatus() == FORUM_LOCKED;
	}

	public void refreshTopic(int topicId) throws Exception {
		// process the topic information
		if (topicId != -1) {
			String postOrder = userPreferences.getPostOrder();
			if (topic == null) {
				topic = forumsModule.findTopicById(topicId);
			}
			topic.setViewCount(topic.getViewCount() + 1);
			forumsModule.update(this.topic);

			int postCount = topic.getReplies() + 1;

			if (postCount > 0) {

				// setup the pageNavigator
				// total number of entries to be split up into pages
				// currently selected page being displayed, first page by
				// default
				if (postOrder.compareToIgnoreCase("ascending") == 0) {

					Collection<Post> pagePosts = forumsModule.findPostIdsAsc(
							topic, 0, 0);
					topics = forumsModule
							.findPostsByIdsAscFetchAttachmentsAndPosters(pagePosts);

				} else {

					Collection<Post> pagePosts = forumsModule.findPostIdsDesc(
							topic, 0, 0);
					topics = forumsModule
							.findPostsByIdsDescFetchAttachmentsAndPosters(pagePosts);

				}
				topicsDataModel = new ListDataModel<Post>(topics);
				String pageNumber = getParameter(p_page);
				if (pageNumber != null)
					currentTopicPage.setPage(parseInt(pageNumber) + 1);
			}
		}
	}
}
