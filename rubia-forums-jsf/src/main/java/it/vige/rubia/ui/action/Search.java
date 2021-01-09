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
package it.vige.rubia.ui.action;

import static it.vige.rubia.search.DisplayAs.POSTS;
import static it.vige.rubia.search.DisplayAs.TOPICS;
import static it.vige.rubia.ui.JSFUtil.handleException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.model.ListDataModel;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.search.ForumsSearchModule;
import it.vige.rubia.search.ResultPage;
import it.vige.rubia.search.SearchCriteria;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.view.ViewSearch;

@Named
@RequestScoped
public class Search extends BaseController {

	private static final long serialVersionUID = -7028498560394081079L;
	@EJB
	private ForumsModule forumsModule;
	@EJB
	private ForumsSearchModule forumsSearchModule;
	@Inject
	private ViewSearch viewSearch;
	// user preference controller
	@Inject
	private PreferenceController userPreferences;

	private SearchCriteria searchCriteria = new SearchCriteria();

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

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public Collection<SelectItem> getCategoriesItems() {
		Collection<SelectItem> categories = new ArrayList<SelectItem>();

		categories.add(new SelectItem("", "Search All Categories"));

		try {

			// Luca Stancapiano start
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			List<CategoryBean> c = forumsModule.findCategories(forumInstanceId);

			if (c != null) {
				for (CategoryBean category : c) {
					categories.add(new SelectItem(category.getId().toString(), category.getTitle()));
				}
			}

		} catch (Exception e) {
			handleException(e);
		}

		return categories;
	}

	public Collection<SelectItem> getForumsItems() {
		Collection<SelectItem> forums = new ArrayList<SelectItem>();

		forums.add(new SelectItem("", "Search All Forums"));

		try {
			// Luca Stancapiano start
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			List<ForumBean> f = forumsModule.findForums(forumInstanceId);

			if (f != null) {
				for (ForumBean forum : f) {
					forums.add(new SelectItem(forum.getId().toString(), forum.getName()));
				}
			}

		} catch (Exception e) {
			handleException(e);
		}

		return forums;
	}

	public String search() throws Exception {

		SearchCriteria criteria = getSearchCriteria();

		if (criteria.getDisplayAs().equals(POSTS.name())) {

			ResultPage<PostBean> resultPage = forumsSearchModule.findPosts(criteria);

			viewSearch.setPosts(resultPage.getPage());
			viewSearch.setPostsDataModel(new ListDataModel<PostBean>(viewSearch.getPosts()));

			if (viewSearch.getPosts() != null && viewSearch.getPosts().isEmpty()) {
				viewSearch.setPosts(null);
				viewSearch.setPostsDataModel(null);
			}
			return "posts";
		} else {

			ResultPage<TopicBean> resultPage = forumsSearchModule.findTopics(criteria);

			viewSearch.setTopics(resultPage.getPage());
			viewSearch.setTopicsDataModel(new ListDataModel<TopicBean>(viewSearch.getTopics()));

			if (viewSearch.getTopics() == null || viewSearch.getTopics().isEmpty()) {
				viewSearch.setTopics(null);
				viewSearch.setTopicsDataModel(null);
			} else {
				viewSearch.setTopicLastPosts(forumsModule.findLastPostsOfTopics(viewSearch.getTopics()));
			}
			return "topics";
		}
	}

	@PostConstruct
	public void execute() {
		searchCriteria = new SearchCriteria();
	}

	public boolean isDisplayAsTopics() {
		String displayAs = getSearchCriteria().getDisplayAs();

		if (displayAs.equals(TOPICS.name())) {
			return true;
		}

		return false;
	}

}
