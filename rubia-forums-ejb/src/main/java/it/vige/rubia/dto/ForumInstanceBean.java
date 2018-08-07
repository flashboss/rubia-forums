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
package it.vige.rubia.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Instance of forums.
 * 
 * @author <a href="mailto:jedim@vige.it">Luca Stancapiano </a>
 */

public class ForumInstanceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7296482227228166251L;

	private Integer id;

	private String name;

	private List<CategoryBean> categories;

	/**
	 * @return the id of the forum instance
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name of the forum instance
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name DOCUMENT_ME
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the categories of the forum instance
	 */
	public List<CategoryBean> getCategories() {
		return categories;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value DOCUMENT_ME
	 */
	public void setCategories(List<CategoryBean> value) {
		categories = value;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value DOCUMENT_ME
	 */
	public void addCategory(CategoryBean value) {
		value.setForumInstance(this);
		categories.add(value);
	}
}
