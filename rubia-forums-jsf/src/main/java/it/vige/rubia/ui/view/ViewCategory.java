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
import static it.vige.rubia.feeds.FeedConstants.CATEGORY;
import static it.vige.rubia.feeds.FeedConstants.GLOBAL;
import static it.vige.rubia.feeds.FeedConstants.RSS;
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.ForumUtil.truncate;
import static it.vige.rubia.ui.JSFUtil.createFeedLink;
import static it.vige.rubia.ui.JSFUtil.getUserLastLoginDate;
import static it.vige.rubia.ui.JSFUtil.handleException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.ThemeHelper;
import it.vige.rubia.ui.action.PreferenceController;

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
	@EJB
	private ForumsModule forumsModule;
	@Inject
	private ThemeHelper themeHelper;
	@EJB
	private UserModule userModule;
	@EJB
	private UserProfileModule userProfileModule;
	// user preference controller
	@Inject
	private PreferenceController userPreferences;

	// business data being generated in this bean by executing ui actions
	// this is data is created such that it can be consumed by the view
	// components
	// like facelets
	private Collection<CategoryBean> categories = null;
	private Map<Integer, Collection<ForumBean>> forums = null;
	private Map<Integer, String> forumImages = null;
	private Map<Integer, String> forumImageDescriptions = null;
	private Map<Integer, PostBean> forumLastPosts = null;
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
	 * @param userPreferences The userPreferences to set.
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
	 * @return the list of all the categories
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<CategoryBean> getCategories() {
		if (categories == null) {
			categories = new ArrayList<CategoryBean>();
		}
		return categories;
	}

	/**
	 * 
	 * @return the map of forums in the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Integer, Collection<ForumBean>> getForums() {
		if (forums == null) {
			forums = new HashMap<Integer, Collection<ForumBean>>();
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
	public Map<Integer, PostBean> getForumLastPosts() {
		if (forumLastPosts == null) {
			forumLastPosts = new HashMap<Integer, PostBean>();
		}
		return forumLastPosts;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		PostBean post = getForumLastPosts().get(id);
		if (post != null) {
			String subject = post.getMessage().getSubject();
			return truncate(subject, 25);
		} else
			return "";
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
			CategoryBean category = categories.iterator().next();
			return createFeedLink(RSS, CATEGORY, category.getId());
		} else {
			return createFeedLink(RSS, GLOBAL, null);
		}
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getAtomFeed() {
		if (categorySelected && categories != null && !categories.isEmpty()) {
			CategoryBean category = categories.iterator().next();
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
				Collection<CategoryBean> cour = forumsModule.findCategoriesFetchForums(forumInstanceId);
				if (cour != null) {
					for (CategoryBean currentCategory : cour)
						processCategory(currentCategory);
				}
			} else {
				// process the specifed category
				CategoryBean currentCategory = forumsModule.findCategoryById(categoryId);
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
	private void processCategory(CategoryBean category) throws Exception {
		Date userLastLogin = getUserLastLoginDate(userModule, userProfileModule);
		if (category != null) {
			getCategories().add(category);

			// process the forums associated with this category
			Collection<ForumBean> forums = forumsModule.findForumsByCategory(category);
			Collection<ForumBean> categoryForums = new ArrayList<ForumBean>();
			for (ForumBean currentForum : forums) {
				categoryForums.add(currentForum);

				// setup folderLook based on whats specified in the theme
				String folderImage = themeHelper.getResourceForumURL();
				String folderAlt = "No_new_posts"; // bundle key
				if (this.forumLastPosts != null && forumLastPosts.containsKey(currentForum.getId())) {
					PostBean lastPost = forumLastPosts.get(currentForum.getId());
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
