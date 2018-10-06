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
import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static it.vige.rubia.ui.JSFUtil.getUserLastLoginDate;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Integer.parseInt;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumWatchBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.WatchBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.ThemeHelper;
import it.vige.rubia.ui.action.PreferenceController;

@Named("myForumsEdit")
public class ViewMyForumsEditAllForums extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7833189733125983452L;
	@EJB
	private ForumsModule forumsModule;
	// user preference controller
	@Inject
	private PreferenceController userPreferences;
	@EJB
	private UserModule userModule;
	@EJB
	private UserProfileModule userProfileModule;
	@Inject
	private ThemeHelper themeHelper;

	// Map<ForumId,LastPost>
	private Map<Integer, PostBean> forumsLastPosts;
	private Map<Integer, String> forumImageDescriptions;
	private Map<Integer, String> forumImages;
	private Map<Integer, ForumWatchBean> forumWatches;
	private Collection<ForumBean> watchedForums;

	private WatchBean watch;
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
	 * @param userPreferences The userPreferences to set.
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
	 * @param forumImageDescriptions the map of the image descriptions
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
	 * @param forumImages the map of the images
	 */
	public void setForumImages(Map<Integer, String> forumImages) {
		this.forumImages = forumImages;
	}

	/**
	 * @return the map of the forum last posts
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
	 * @param forumsLastPosts the map of the forum last posts
	 */
	public void setForumsLastPosts(Map<Integer, PostBean> forumsLastPosts) {
		this.forumsLastPosts = forumsLastPosts;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		PostBean post = getForumsLastPosts().get(id);
		if (post != null) {
			String subject = post.getMessage().getSubject();
			return truncate(subject, 25);
		} else
			return "";
	}

	/**
	 * @return the forum watches
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, ForumWatchBean> getForumWatches() {
		return forumWatches;
	}

	/**
	 * @param forumWatches the forum watches
	 */
	public void setForumWatches(Map<Integer, ForumWatchBean> forumWatches) {
		this.forumWatches = forumWatches;
	}

	/**
	 * @return the list of watched forums
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
	 * @param watchedForums the list of watched forums
	 */
	public void setWatchedForums(Collection<ForumBean> watchedForums) {
		this.watchedForums = watchedForums;
	}

	/**
	 * @return the current watch
	 */
	public WatchBean getWatch() {
		return watch;
	}

	/**
	 * @param watch the current watch
	 */
	public void setWatch(WatchBean watch) {
		this.watch = watch;
	}

	/**
	 * @return the current forum id
	 */
	public int getForumId() {
		return forumId;
	}

	/**
	 * @param forumId the current forum id to set
	 */
	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	/**
	 * 
	 */
	@PostConstruct
	public void execute() {

		Collection<ForumBean> forums = getWatchedForums();

		try {
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();
			forumWatches = forumsModule.findForumWatches(getUser(userModule), forumInstanceId);

		} catch (Exception e) {
			handleException(e);
			setForumWatches(new HashMap<Integer, ForumWatchBean>(0));
		}

		Date userLastLogin = getUserLastLoginDate(userModule, userProfileModule);

		for (ForumBean currentForum : forums) {

			// setup folderLook based on whats specified in the theme
			String folderImage = themeHelper.getResourceForumURL();
			String folderAlt = "No_new_posts"; // bundle key
			if (forumsLastPosts != null && forumsLastPosts.containsKey(currentForum.getId())) {
				PostBean lastPost = (PostBean) forumsLastPosts.get(currentForum.getId());
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
