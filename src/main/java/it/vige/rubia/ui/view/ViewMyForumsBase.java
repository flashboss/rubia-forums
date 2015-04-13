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

import static it.vige.rubia.ui.JSFUtil.handleException;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Topic;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.PageNavigator;
import it.vige.rubia.ui.action.PreferenceController;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.interceptor.Interceptors;

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
	protected Collection<Topic> watchedTopics;
	private Map<Integer, PageNavigator> topicNavigator = new HashMap<Integer, PageNavigator>();

	public abstract Collection<Topic> getWatchedTopics();

	public abstract void setWatchedTopics(Collection<Topic> watchedTopics);

	/**
     * 
     */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Object, Object> getTopicsLastPosts() {
		if (topicsLastPosts == null) {
			try {
				Collection<Topic> watched = getWatchedTopics();
				if (watched != null) {
					Set<Topic> temporaryContainer = new HashSet<Topic>(watched.size());
					Iterator<Topic> it = watched.iterator();
					while (it.hasNext()) {
						Topic topic = it.next();
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
     * 
     */
	public void setTopicsLastPosts(Map<Object, Object> topicsLastPosts) {
		this.topicsLastPosts = topicsLastPosts;
	}

	// ------------user
	// preferences-------------------------------------------------------------------------------------------------------------
	/**
	 * @return Returns the userPreferences.
	 */
	public abstract PreferenceController getUserPreferences();

	/**
	 * @param userPreferences
	 *            The userPreferences to set.
	 */
	public abstract void setUserPreferences(PreferenceController userPreferences);

	/**
     * 
     */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public void execute() throws Exception {

		Collection<Topic> topics = getWatchedTopics();

		// setup dummy pageNavigators for all topics being displayed for topic
		// minipaging
		if (topics != null) {
			for (Topic courTopic : topics) {
				if (courTopic.getReplies() > 0) {
					PageNavigator topicNav = new PageNavigator(courTopic.getReplies() + 1, getUserPreferences().getPostsPerTopic(), // this
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
