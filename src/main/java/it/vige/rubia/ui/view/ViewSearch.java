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

import it.vige.rubia.ForumsModule;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.action.PreferenceController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class ViewSearch extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3641493635888766463L;
	@Inject
	private ForumsModule forumsModule;

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

	public void setTopicLastPosts(Map<Object, Object> topicLastPosts) {
		this.topicLastPosts = topicLastPosts;
	}

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

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public Map<Object, Object> getTopicLastPosts() {
		if (topicLastPosts == null) {
			topicLastPosts = new HashMap<Object, Object>();
		}
		return topicLastPosts;
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

	public int getLastPageNumber() {
		if (posts != null)
			return posts.size() / userPreferences.getPostsPerTopic() + 1;
		else if (topics != null)
			return topics.size() / userPreferences.getTopicsPerForum() + 1;
		else
			return 1;
	}

	public Collection<Attachment> findAttachments(Post post) {
		return forumsModule.findAttachments(post);
	}

}
