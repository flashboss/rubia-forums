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

import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static it.vige.rubia.ui.PortalUtil.getUser;
import static it.vige.rubia.ui.PortalUtil.getUserLastLoginDate;
import static java.lang.Integer.parseInt;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Watch;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.ThemeHelper;
import it.vige.rubia.ui.action.PreferenceController;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

@Named("myForumsEdit")
public class ViewMyForumsEditAllForums extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7833189733125983452L;
	@Inject
	private ForumsModule forumsModule;
	// user preference controller
	@Inject
	private PreferenceController userPreferences;
	@Inject
	private UserModule userModule;
	@Inject
	private UserProfileModule userProfileModule;
	@Inject
	private ThemeHelper themeHelper;

	// Map<ForumId,LastPost>
	private Map<Object, Post> forumsLastPosts;
	private Map<Integer, String> forumImageDescriptions;
	private Map<Integer, String> forumImages;
	private Map<Object, Object> forumWatches;
	private Collection<Forum> watchedForums;

	private Watch watch;
	private int forumId;

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
	 * @return the map of the image descriptions
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
	 * @param forumImageDescriptions
	 *            the map of the image descriptions
	 */
	public void setForumImageDescriptions(Map<Integer, String> forumImageDescriptions) {
		this.forumImageDescriptions = forumImageDescriptions;
	}

	/**
	 * @return the map of the images
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
	 * @param forumImages
	 *            the map of the images
	 */

	public void setForumImages(Map<Integer, String> forumImages) {
		this.forumImages = forumImages;
	}

	/**
	 * @return the map of the forum last posts
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Object, Post> getForumsLastPosts() {
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
	 * @param forumsLastPosts
	 *            the map of the forum last posts
	 */
	public void setForumsLastPosts(Map<Object, Post> forumsLastPosts) {
		this.forumsLastPosts = forumsLastPosts;
	}

	/**
	 * @return the forum watches
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Object, Object> getForumWatches() {
		return forumWatches;
	}

	/**
	 * @param forumWatches
	 *            the forum watches
	 */
	public void setForumWatches(Map<Object, Object> forumWatches) {
		this.forumWatches = forumWatches;
	}

	/**
	 * @return the list of watched forums
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Forum> getWatchedForums() {
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
	 * @param watchedForums
	 *            the list of watched forums
	 */
	public void setWatchedForums(Collection<Forum> watchedForums) {
		this.watchedForums = watchedForums;
	}

	/**
	 * @return the current watch
	 */
	public Watch getWatch() {
		return watch;
	}

	/**
	 * @param watch
	 *            the current watch
	 */
	public void setWatch(Watch watch) {
		this.watch = watch;
	}

	/**
	 * @return the current forum id
	 */
	public int getForumId() {
		return forumId;
	}

	/**
	 * @param forumId
	 *            the current forum id to set
	 */
	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	/**
	 * @throws Exception
	 *             the error exception
	 */
	@PostConstruct
	public void execute() throws Exception {

		Collection<Forum> forums = getWatchedForums();

		try {
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();
			forumWatches = forumsModule.findForumWatches(getUser(userModule), forumInstanceId);

		} catch (Exception e) {
			handleException(e);
			setForumWatches(new HashMap<Object, Object>(0));
		}

		Date userLastLogin = getUserLastLoginDate(userModule, userProfileModule);

		for (Forum currentForum : forums) {

			// setup folderLook based on whats specified in the theme
			String folderImage = themeHelper.getResourceForumURL();
			String folderAlt = "No_new_posts"; // bundle key
			if (forumsLastPosts != null && forumsLastPosts.containsKey(currentForum.getId())) {
				Post lastPost = (Post) forumsLastPosts.get(currentForum.getId());
				Date lastPostDate = lastPost.getCreateDate();
				if (lastPostDate != null && userLastLogin != null && lastPostDate.compareTo(userLastLogin) > 0) {
					folderAlt = "New_posts"; // bundle key
					folderImage = themeHelper.getResourceForumNewURL();
				}
			}
			if (currentForum.getStatus() == FORUM_LOCKED) {
				folderImage = themeHelper.getResourceForumLockedURL();
				folderAlt = "Forum_locked"; // bundle key
			}
			getForumImages().put(currentForum.getId(), folderImage);
			getForumImageDescriptions().put(currentForum.getId(), folderAlt);
		}

		try {
			String t = getRequestParameter(p_forumId);
			if (t != null && t.trim().length() > 0) {
				forumId = parseInt(t);
			} else {
				forumId = -1;
			}
			if (forumId != -1) {
				watch = forumsModule.findForumWatchByUserAndForum(getUser(userModule), forumId);
			}
		} catch (Exception e) {
			handleException(e);
			forumId = -1;
			watch = null;
		}
	}

}
