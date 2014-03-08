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
package org.rubia.forums.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Instance of forums.
 * 
 * @author <a href="mailto:jedim@vige.it">Luca Stancapiano </a>
 */

@Entity
@Table(name = "JBP_FORUMS_INSTANCES")
public class ForumInstance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7296482227228166251L;

	@Id
	@Column(name = "JBP_ID")
	@GeneratedValue
	private Integer id;

	@Column(name = "JBP_NAME")
	private String name;

	@OneToMany(mappedBy = "forumInstance")
	private List<Category> categories;

	/**
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name
	 *            DOCUMENT_ME
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 */
	public List<Category> getCategories() {
		return categories;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void setCategories(List<Category> value) {
		categories = value;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void addCategory(Category value) {
		value.setForumInstance(this);
		categories.add(value);
	}
}
