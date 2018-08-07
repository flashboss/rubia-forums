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
import java.util.ArrayList;
import java.util.List;

/**
 * Category of forums.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet </a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 916 $
 */

public class CategoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8164247625235206934L;

	private List<ForumBean> forums;
	/**
	 * forumInstance field to tie category to it, so that each forum can to get
	 * different categories
	 */
	private ForumInstanceBean forumInstance;

	private int order;

	private String title;

	private Integer id;

	/**
	 * Creates a new {@link CategoryBean} object.
	 */
	public CategoryBean() {
		setForums(new ArrayList<ForumBean>());
	}

	public CategoryBean(String title) {
		this();
		this.title = title;
	}

	/**
	 * @return the list of forums of the category
	 */
	public List<ForumBean> getForums() {
		return forums;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value DOCUMENT_ME
	 */
	public void setForums(List<ForumBean> value) {
		forums = value;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value DOCUMENT_ME
	 */
	public void addForum(ForumBean value) {
		value.setCategory(this);
		forums.add(value);
	}

	/**
	 * @return the order number of the category
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param order DOCUMENT_ME
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the title of the category
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param title DOCUMENT_ME
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the id of teh category
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param id DOCUMENT_ME
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Accessors to manage ForumInstance field so Category is tied to the Forum
	 * Instance. Now each category belong to a Forum Instance so that we can to get
	 * different instances of forums with different categories and different sub
	 * objects
	 * 
	 * @return the forum instance of the category
	 */
	public ForumInstanceBean getForumInstance() {
		return forumInstance;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forumInstance DOCUMENT_ME
	 */
	public void setForumInstance(ForumInstanceBean forumInstance) {
		this.forumInstance = forumInstance;
	}
}