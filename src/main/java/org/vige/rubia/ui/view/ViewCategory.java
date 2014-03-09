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

import static org.vige.rubia.feeds.FeedConstants.ATOM;
import static org.vige.rubia.feeds.FeedConstants.CATEGORY;
import static org.vige.rubia.feeds.FeedConstants.GLOBAL;
import static org.vige.rubia.feeds.FeedConstants.RSS;
import static org.vige.rubia.ui.ForumUtil.getParameter;
import static org.vige.rubia.ui.JSFUtil.handleException;
import static org.vige.rubia.ui.PortalUtil.createFeedLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.vige.rubia.ForumsModule;
import org.vige.rubia.auth.AuthorizationListener;
import org.vige.rubia.auth.SecureActionForum;
import org.vige.rubia.auth.UserModule;
import org.vige.rubia.auth.UserProfileModule;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Post;
import org.vige.rubia.ui.BaseController;
import org.vige.rubia.ui.PortalUtil;
import org.vige.rubia.ui.ThemeHelper;
import org.vige.rubia.ui.action.PreferenceController;

//jsf imports

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("category")
@RequestScoped
public class ViewCategory extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5349549910762005145L;
	@Inject
	private ForumsModule forumsModule;
	@Inject
	private ThemeHelper themeHelper;
	@Inject
	private UserModule userModule;
	@Inject
	private UserProfileModule userProfileModule;
	// user preference controller
	@Inject
	private PreferenceController userPreferences;

	// business data being generated in this bean by executing ui actions
	// this is data is created such that it can be consumed by the view
	// components
	// like facelets
	private Collection<Category> categories = null;
	private Map<Integer, Collection<Forum>> forums = null;
	private Map<Integer, String> forumImages = null;
	private Map<Integer, String> forumImageDescriptions = null;
	private Map<Object, Post> forumLastPosts = null;
	private boolean categorySelected = false;

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

	// ----------------bean configuration supplied by the
	// forums-config.xml---------------------------------------------------------------------------------------------

	// ----------------business data being generated for use by the view
	// components like
	// facelets---------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Category> getCategories() {
		if (categories == null) {
			categories = new ArrayList<Category>();
		}
		return categories;
	}

	/**
	 * 
	 * @return
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, Collection<Forum>> getForums() {
		if (forums == null) {
			forums = new HashMap<Integer, Collection<Forum>>();
		}
		return forums;
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
	 * @return Returns the a Map which contains ForumId:LastPost pairs.
	 */
	public Map<Object, Post> getForumLastPosts() {
		if (forumLastPosts == null) {
			forumLastPosts = new HashMap<Object, Post>();
		}
		return forumLastPosts;
	}

	/**
	 * @return Returns true if category has been selected.
	 */
	public boolean isCategorySelected() {
		return categorySelected;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getRssFeed() {
		if (categorySelected && categories != null && !categories.isEmpty()) {
			Category category = categories.iterator().next();
			return createFeedLink(RSS, CATEGORY, category.getId());
		} else {
			return createFeedLink(RSS, GLOBAL, null);
		}
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getAtomFeed() {
		if (categorySelected && categories != null && !categories.isEmpty()) {
			Category category = categories.iterator().next();
			return createFeedLink(ATOM, CATEGORY, category.getId());
		} else {
			return createFeedLink(ATOM, GLOBAL, null);
		}

	}

	// ui actions supported by this
	// bean----------------------------------------------------------------------------------------------------
	/**
	 * this generates the category and its corresponding forums
	 */
	@PostConstruct
	public void execute() {
		try {
			// try to extract categoryId
			int categoryId = -1;
			String c = getParameter(p_categoryId);
			if (c != null && c.trim().length() > 0) {
				categoryId = Integer.parseInt(c);

				// Setting flag that category has been selected.
				categorySelected = true;
			}

			// Luca Stancapiano start
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			this.forumLastPosts = forumsModule.findLastPostsOfForums(forumInstanceId);
			// Luca Stancapiano end

			// setup category related data to be displayed
			if (categoryId == -1) {
				// process a default level category
				// Luca Stancapiano
				Collection<Category> cour = forumsModule.findCategoriesFetchForums(forumInstanceId);
				if (cour != null) {
					for (Category currentCategory : cour)
						processCategory(currentCategory);
				}
			} else {
				// process the specifed category
				Category currentCategory = forumsModule.findCategoryById(categoryId);
				if (currentCategory != null) {
					processCategory(currentCategory);
				}
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private void processCategory(Category category) throws Exception {
		Date userLastLogin = PortalUtil.getUserLastLoginDate(userModule, userProfileModule);
		if (category != null) {
			getCategories().add(category);

			// process the forums associated with this category
			Collection<Forum> forums = forumsModule.findForumsByCategory(category);
			Collection<Forum> categoryForums = new ArrayList<Forum>();
			for (Forum currentForum : forums) {
				categoryForums.add(currentForum);

				// setup folderLook based on whats specified in the theme
				String folderImage = themeHelper.getResourceForumURL();
				String folderAlt = "No_new_posts"; // bundle key
				if (this.forumLastPosts != null && forumLastPosts.containsKey(currentForum.getId())) {
					Post lastPost = forumLastPosts.get(currentForum.getId());
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
			getForums().put(category.getId(), categoryForums);
		}
	}
	// -------------------------------------------------------------------------------------------------------------------------------------
}
