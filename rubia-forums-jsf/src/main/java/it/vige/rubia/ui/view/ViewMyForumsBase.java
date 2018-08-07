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
import static it.vige.rubia.ui.JSFUtil.handleException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.PageNavigator;
import it.vige.rubia.ui.action.PreferenceController;

/**
 * 
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
public abstract class ViewMyForumsBase extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2284310224100123687L;

	private Map<Object, Object> topicsLastPosts;
	protected Collection<TopicBean> watchedTopics;
	private Map<Integer, PageNavigator> topicNavigator = new HashMap<Integer, PageNavigator>();

	public abstract Collection<TopicBean> getWatchedTopics();

	public abstract void setWatchedTopics(Collection<TopicBean> watchedTopics);

	/**
	 * @return a map of the last posts and respective topics
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Object, Object> getTopicsLastPosts() {
		if (topicsLastPosts == null) {
			try {
				Collection<TopicBean> watched = getWatchedTopics();
				if (watched != null) {
					Set<TopicBean> temporaryContainer = new HashSet<TopicBean>(watched.size());
					Iterator<TopicBean> it = watched.iterator();
					while (it.hasNext()) {
						TopicBean topic = it.next();
						temporaryContainer.add(topic);
					}
					topicsLastPosts = getMyForumsModule().findLastPostsOfTopics(temporaryContainer);
				}
			} catch (Exception e) {
				handleException(e);
			}
		}
		return topicsLastPosts;
	}

	/**
	 * @param topicsLastPosts the last posts of the topics
	 */
	public void setTopicsLastPosts(Map<Object, Object> topicsLastPosts) {
		this.topicsLastPosts = topicsLastPosts;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		PostBean post = (PostBean) getTopicsLastPosts().get(id);
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
	public abstract PreferenceController getUserPreferences();

	/**
	 * @param userPreferences The userPreferences to set.
	 */
	public abstract void setUserPreferences(PreferenceController userPreferences);

	/**
	 * @throws Exception an error exception is launched
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public void execute() throws Exception {

		Collection<TopicBean> topics = getWatchedTopics();

		// setup dummy pageNavigators for all topics being displayed for topic
		// minipaging
		if (topics != null) {
			for (TopicBean courTopic : topics) {
				if (courTopic.getReplies() > 0) {
					PageNavigator topicNav = new PageNavigator(courTopic.getReplies() + 1,
							getUserPreferences().getPostsPerTopic(), // this
																		// is
																		// user's
																		// posts
																		// per
																		// page
																		// preference
							0 // current page of the navigator
					) {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						protected Collection<Integer> initializePage() {
							return null;
						}

					};
					topicNavigator.put(courTopic.getId(), topicNav);
				}
			}
		}
	}

	public Map<Integer, PageNavigator> getTopicNavigator() {
		return topicNavigator;
	}

	protected abstract ForumsModule getMyForumsModule() throws Exception;

}
