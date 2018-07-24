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
package it.vige.rubia.search;

import static it.vige.rubia.search.DisplayAs.POSTS;
import static it.vige.rubia.search.Searching.TITLE_MSG;
import static it.vige.rubia.search.SortOrder.ASC;

import java.io.Serializable;

public class SearchCriteria implements Serializable {

	private static final long serialVersionUID = 6813892676770463283L;

	private String keywords;

	private String author;

	private String category;

	private String forum;

	private String timePeriod;

	private String searching;

	private String sortBy;

	private String sortOrder;

	private String displayAs;

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getForum() {
		return forum;
	}

	public void setForum(String forum) {
		this.forum = forum;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getSearching() {
		if (searching == null || searching.length() == 0)
			searching = TITLE_MSG.name();

		return searching;
	}

	public void setSearching(String searching) {
		this.searching = searching;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		if (sortOrder == null || sortOrder.length() == 0)
			sortOrder = ASC.name();

		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getDisplayAs() {
		if (displayAs == null || displayAs.length() == 0)
			displayAs = POSTS.name();

		return displayAs;
	}

	public void setDisplayAs(String displayAs) {
		this.displayAs = displayAs;
	}

}
