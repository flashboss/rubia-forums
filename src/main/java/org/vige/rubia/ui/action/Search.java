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
package org.vige.rubia.ui.action;

import static org.vige.rubia.ui.JSFUtil.handleException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.vige.rubia.ForumsModule;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.search.SearchCriteria;
import org.vige.rubia.ui.BaseController;

@Named
public class Search extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7028498560394081079L;
	@Inject
	private ForumsModule forumsModule;
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
	 * @param userPreferences
	 *            The userPreferences to set.
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

			List<Category> c = forumsModule.findCategories(forumInstanceId);

			if (c != null) {
				for (Category category : c) {
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

			List<Forum> f = forumsModule.findForums(forumInstanceId);

			if (f != null) {
				for (Forum forum : f) {
					forums.add(new SelectItem(forum.getId().toString(), forum.getName()));
				}
			}

		} catch (Exception e) {
			handleException(e);
		}

		return forums;
	}

	public String search() {
		return "success";
	}

	@PostConstruct
	public void execute() throws Exception {
		searchCriteria = new SearchCriteria();
	}

}