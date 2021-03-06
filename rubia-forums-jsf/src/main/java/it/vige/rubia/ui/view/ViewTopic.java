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
package it.vige.rubia.ui.view;

import static it.vige.rubia.feeds.FeedConstants.ATOM;
import static it.vige.rubia.feeds.FeedConstants.RSS;
import static it.vige.rubia.feeds.FeedConstants.TOPIC;
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.JSFUtil.createFeedLink;
import static java.lang.Integer.parseInt;
import static jakarta.faces.context.FacesContext.getCurrentInstance;
import static jakarta.faces.event.PhaseId.RESTORE_VIEW;
import static org.jboss.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.PhaseId;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptors;

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.action.PreferenceController;
import it.vige.rubia.util.CurrentTopicPage;

/**
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
@Named("topic")
@RequestScoped
public class ViewTopic extends BaseController {

	private static final long serialVersionUID = 5205743830388129653L;
	private static Logger log = getLogger(ViewTopic.class);

	@EJB
	private ForumsModule forumsModule;
	/**
	 * user preference related data
	 */
	@Inject
	private PreferenceController userPreferences;

	@Inject
	private CurrentTopicPage currentTopicPage;

	private TopicBean topic;
	private long postDays; // This is value of selectItem describing time
							// constraint for displayed posts
	private List<PostBean> topics = new ArrayList<PostBean>();
	private DataModel<PostBean> topicsDataModel = new ListDataModel<PostBean>(topics);

	public int getLastPageNumber() {
		if (topic != null)
			return topic.getReplies() / userPreferences.getPostsPerTopic() + 1;
		else
			return 1;
	}

	public DataModel<PostBean> getTopicsDataModel() {
		return topicsDataModel;
	}

	public void setTopicsDataModel(DataModel<PostBean> topicsDataModel) {
		this.topicsDataModel = topicsDataModel;
	}

	/**
	 * @return the current topic
	 */
	public TopicBean getTopic() {
		return topic;
	}

	public void setTopic(TopicBean topic) {
		this.topic = topic;
	}

	/**
	 * @param postDays the post days
	 */
	public void setPostDays(long postDays) {
		this.postDays = postDays;
	}

	/**
	 * @return the post days
	 */
	public long getPostDays() {
		return postDays;
	}

	/**
	 * @return true if a poll is present in the topic
	 */
	public boolean isPollPresent() {
		boolean isPollPresent = false;

		if (topic.getPoll() != null && topic.getPoll().getId() != null && topic.getPoll().getId().intValue() > 0
				&& topic.getPoll().getOptions() != null && !topic.getPoll().getOptions().isEmpty()) {
			isPollPresent = true;
		}

		return isPollPresent;
	}

	/**
	 * @return true if a ballot is present in the topic
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
	 * @return the posts of the current topic
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<PostBean> getTopics() {
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
	 * @param userPreferences The userPreferences to set.
	 */
	public void setUserPreferences(PreferenceController userPreferences) {
		this.userPreferences = userPreferences;
	}

	/**
	 * This method gets topicId from parameters and tries to find topic with that
	 * id.
	 */
	@PostConstruct
	public void execute() {
		// parse input data
		int topicId = -1;
		Integer postId = -1;
		String t = getParameter(p_topicId);
		String p = getParameter(p_postId);

		if (t != null && t.trim().length() > 0) {
			topicId = parseInt(t);
		}
		try {
			if (p != null && p.trim().length() > 0) {
				postId = parseInt(p);
				PostBean post = forumsModule.findPostById(postId);
				topicId = post.getTopic().getId().intValue();
				topic = post.getTopic();
			}
			refreshTopic(topicId);
		} catch (Exception e) {
			log.error(e);
		}
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
			PhaseId phaseId = getCurrentInstance().getCurrentPhaseId();
			if (phaseId.equals(RESTORE_VIEW))
				topic.setViewCount(topic.getViewCount() + 1);
			forumsModule.update(topic);

			int postCount = topic.getReplies() + 1;

			if (postCount > 0) {

				// setup the pageNavigator
				// total number of entries to be split up into pages
				// currently selected page being displayed, first page by
				// default
				if (postOrder.compareToIgnoreCase("ascending") == 0) {

					Collection<Integer> pagePosts = forumsModule.findPostIdsAsc(topic, 0, 0);
					topics = forumsModule.findPostsByIdsAscFetchAttachmentsAndPosters(pagePosts);

				} else {

					Collection<Integer> pagePosts = forumsModule.findPostIdsDesc(topic, 0, 0);
					topics = forumsModule.findPostsByIdsDescFetchAttachmentsAndPosters(pagePosts);

				}
				topicsDataModel = new ListDataModel<PostBean>(topics);
				String pageNumber = getParameter(p_page);
				if (pageNumber != null)
					currentTopicPage.setPage(parseInt(pageNumber));
			}
		}
	}
}
