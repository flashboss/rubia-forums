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

import static it.vige.rubia.util.PortalUtil.getUser;
import static it.vige.rubia.ui.ForumUtil.truncate;
import static it.vige.rubia.ui.JSFUtil.getUserLastLoginDate;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static org.jboss.logging.Logger.getLogger;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptors;

import org.jboss.logging.Logger;

import it.vige.rubia.Constants;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.ThemeHelper;
import it.vige.rubia.ui.action.PreferenceController;

@Named("myForums")
public class ViewMyForumsMain extends ViewMyForumsBase {

	private static final long serialVersionUID = -6587070080682393388L;
	private static Logger log = getLogger(ViewMyForumsMain.class);

	@EJB
	private ForumsModule forumsModule;
	@EJB
	private UserModule userModule;
	@EJB
	private UserProfileModule userProfileModule;
	@Inject
	private ThemeHelper themeHelper;
	// user preference controller
	@Inject
	private PreferenceController userPreferences;

	// Map<ForumId,LastPost>
	private Map<Integer, PostBean> forumsLastPosts;

	private Collection<ForumBean> watchedForums;

	private Map<Integer, String> forumImageDescriptions;
	private Map<Integer, String> forumImages;

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
	 * @return the watched topics
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<TopicBean> getWatchedTopics() {
		if (watchedTopics == null) {
			try {
				Date lastLoginDate = getUserLastLoginDate(userModule, userProfileModule);
				if (lastLoginDate == null) {
					return watchedTopics;
				}
				// get the forumInstanceId where this forum should be added
				int forumInstanceId = userPreferences.getForumInstanceId();
				watchedTopics = forumsModule.findTopicWatchedByUser(getUser(userModule), lastLoginDate,
						forumInstanceId);

			} catch (Exception e) {
				handleException(e);
			}
		}
		return watchedTopics;
	}

	/**
	 * @param watchedTopics the watched topics
	 */
	public void setWatchedTopics(Collection<TopicBean> watchedTopics) {
		this.watchedTopics = watchedTopics;
	}

	/**
	 * @return the watched forums
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<ForumBean> getWatchedForums() {
		if (watchedForums == null) {
			try {
				// get the forumInstanceId where this forum should be added
				int forumInstanceId = userPreferences.getForumInstanceId();
				watchedForums = forumsModule.findForumWatchedByUser(getUser(userModule), forumInstanceId);

			} catch (Exception e) {
				handleException(e);
			}
		}
		return watchedForums;
	}

	/**
	 * @param watchedForums the watched forums
	 */
	public void setWatchedForums(Collection<ForumBean> watchedForums) {
		this.watchedForums = watchedForums;
	}

	/**
	 * @return Returns the forumImageDescriptions.
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, String> getForumImageDescriptions() {
		if (forumImageDescriptions == null) {
			forumImageDescriptions = new HashMap<Integer, String>();
		}
		return forumImageDescriptions;
	}

	/**
	 * @return Returns the forumImages.
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, String> getForumImages() {
		if (forumImages == null) {
			forumImages = new HashMap<Integer, String>();
		}
		return forumImages;
	}

	/**
	 * @return the forums last posts
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, PostBean> getForumsLastPosts() {
		if (forumsLastPosts == null) {
			try {
				// get the forumInstanceId where this forum should be added
				int forumInstanceId = userPreferences.getForumInstanceId();

				forumsLastPosts = forumsModule.findLastPostsOfForums(forumInstanceId);

			} catch (Exception e) {
				handleException(e);
			}
		}
		return forumsLastPosts;
	}

	/**
	 * @param forumsLastPosts the forums last posts
	 */
	public void setForumsLastPosts(Map<Integer, PostBean> forumsLastPosts) {
		this.forumsLastPosts = forumsLastPosts;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		PostBean post = (PostBean) getForumsLastPosts().get(id);
		if (post != null) {
			String subject = post.getMessage().getSubject();
			return truncate(subject, 25);
		} else
			return "";
	}

	/**
	 * 
	 */
	@PostConstruct
	public void execute() {

		try {
			super.execute();
			Collection<ForumBean> forums = getWatchedForums();
			Date userLastLogin = getUserLastLoginDate(userModule, userProfileModule);

			for (ForumBean currentForum : forums) {

				// setup folderLook based on whats specified in the theme
				String folderImage = themeHelper.getResourceForumURL();
				String folderAlt = "No_new_posts"; // bundle key
				if (forumsLastPosts != null && forumsLastPosts.containsKey(currentForum.getId())) {
					PostBean lastPost = forumsLastPosts.get(currentForum.getId());
					Date lastPostDate = lastPost.getCreateDate();
					if (lastPostDate != null && userLastLogin != null && lastPostDate.compareTo(userLastLogin) > 0) {
						folderAlt = "New_posts"; // bundle key
						folderImage = themeHelper.getResourceForumNewURL();
					}
				}
				if (currentForum.getStatus() == Constants.FORUM_LOCKED) {
					folderImage = themeHelper.getResourceForumLockedURL();
					folderAlt = "Forum_locked"; // bundle key
				}
				getForumImages().put(currentForum.getId(), folderImage);
				getForumImageDescriptions().put(currentForum.getId(), folderAlt);
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	protected ForumsModule getMyForumsModule() throws Exception {
		return forumsModule;
	}

}
