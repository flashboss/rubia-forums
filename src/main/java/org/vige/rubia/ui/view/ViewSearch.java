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
package org.vige.rubia.ui.view;

import static org.vige.rubia.search.DisplayAs.POSTS;
import static org.vige.rubia.search.DisplayAs.TOPICS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.vige.rubia.ForumsModule;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;
import org.vige.rubia.search.ForumsSearchModule;
import org.vige.rubia.search.ResultPage;
import org.vige.rubia.search.SearchCriteria;
import org.vige.rubia.ui.BaseController;
import org.vige.rubia.ui.action.PreferenceController;
import org.vige.rubia.ui.action.Search;

@Named
public class ViewSearch extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3641493635888766463L;
	@Inject
	private ForumsModule forumsModule;
	@Inject
	private ForumsSearchModule forumsSearchModule;

	/**
	 * user preference related data
	 */
	@Inject
	private PreferenceController userPreferences;

	private List<Post> posts;
	private DataModel<Post> postsDataModel = new ListDataModel<Post>(posts);

	private List<Topic> topics;
	private DataModel<Topic> topicsDataModel = new ListDataModel<Topic>(topics);

	private Map<Object, Object> topicLastPosts;

	@Inject
	private Search search;

	public DataModel<Post> getPostsDataModel() {
		return postsDataModel;
	}

	public void setPostsDataModel(DataModel<Post> postsDataModel) {
		this.postsDataModel = postsDataModel;
	}

	public DataModel<Topic> getTopicsDataModel() {
		return topicsDataModel;
	}

	public void setTopicsDataModel(DataModel<Topic> topicsDataModel) {
		this.topicsDataModel = topicsDataModel;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public Map<Object, Object> getTopicLastPosts() {
		if (topicLastPosts == null) {
			topicLastPosts = new HashMap<Object, Object>();
		}
		return topicLastPosts;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
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

	public boolean isDisplayAsTopics() {
		String displayAs = search.getSearchCriteria().getDisplayAs();

		if (displayAs.equals(TOPICS.name())) {
			return true;
		}

		return false;
	}

	@PostConstruct
	public void execute() throws Exception {

		int currentPage = 0;

		SearchCriteria criteria = search.getSearchCriteria();

		if (criteria != null) {

			criteria.setPageSize(userPreferences.getPostsPerTopic());
			criteria.setPageNumber(currentPage);

			if (criteria.getDisplayAs().equals(POSTS.name())) {

				ResultPage<Post> resultPage = forumsSearchModule
						.findPosts(criteria);

				posts = resultPage.getPage();
				postsDataModel = new ListDataModel<Post>(posts);

				if (posts != null && posts.isEmpty()) {
					posts = null;
					postsDataModel = null;
				}
			} else {

				ResultPage<Topic> resultPage = forumsSearchModule
						.findTopics(criteria);

				topics = resultPage.getPage();
				topicsDataModel = new ListDataModel<Topic>(topics);

				if (topics != null && topics.isEmpty()) {
					topics = null;
					topicsDataModel = null;
				} else {
					topicLastPosts = forumsModule.findLastPostsOfTopics(topics);
				}
			}
		}
	}

}
