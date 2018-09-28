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

import static it.vige.rubia.ui.ForumUtil.truncate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.action.PreferenceController;

@Named
@SessionScoped
public class ViewSearch extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3641493635888766463L;
	@EJB
	private ForumsModule forumsModule;

	/**
	 * user preference related data
	 */
	@Inject
	private PreferenceController userPreferences;

	private List<PostBean> posts;
	private DataModel<PostBean> postsDataModel = new ListDataModel<PostBean>(posts);

	private List<TopicBean> topics;
	private DataModel<TopicBean> topicsDataModel = new ListDataModel<TopicBean>(topics);

	private Map<Integer, PostBean> topicLastPosts;

	public void setTopicLastPosts(Map<Integer, PostBean> topicLastPosts) {
		this.topicLastPosts = topicLastPosts;
	}

	public DataModel<PostBean> getPostsDataModel() {
		return postsDataModel;
	}

	public void setPostsDataModel(DataModel<PostBean> postsDataModel) {
		this.postsDataModel = postsDataModel;
	}

	public DataModel<TopicBean> getTopicsDataModel() {
		return topicsDataModel;
	}

	public void setTopicsDataModel(DataModel<TopicBean> topicsDataModel) {
		this.topicsDataModel = topicsDataModel;
	}

	public List<PostBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostBean> posts) {
		this.posts = posts;
	}

	public List<TopicBean> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicBean> topics) {
		this.topics = topics;
	}

	public Map<Integer, PostBean> getTopicLastPosts() {
		if (topicLastPosts == null) {
			topicLastPosts = new HashMap<Integer, PostBean>();
		}
		return topicLastPosts;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		PostBean post = (PostBean) getTopicLastPosts().get(id);
		if (post != null) {
			String subject = post.getMessage().getSubject();
			return truncate(subject, 25);
		} else
			return "";
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

	public int getLastPageNumber() {
		if (posts != null)
			return posts.size() / userPreferences.getPostsPerTopic() + 1;
		else if (topics != null)
			return topics.size() / userPreferences.getTopicsPerForum() + 1;
		else
			return 1;
	}

	public Collection<AttachmentBean> findAttachments(PostBean post) {
		return forumsModule.findAttachments(post);
	}

}
