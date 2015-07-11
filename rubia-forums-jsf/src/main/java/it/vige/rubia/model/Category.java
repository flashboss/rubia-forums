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
package it.vige.rubia.model;

import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

/**
 * Category of forums.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet </a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 916 $
 */

@NamedQueries({
		@NamedQuery(name = "findCategoryByIdFetchForums", query = "select c from Category as c join fetch c.forums "
				+ "where c.id=:categoryId"),
		@NamedQuery(name = "findCategories", query = "select c from Category as c where "
				+ "c.forumInstance.id = :forumInstanceId "
				+ "order by c.order asc"),
		@NamedQuery(name = "findCategoriesFetchForums", query = "select c from Category as c "
				+ "left outer join fetch c.forums "
				+ "where c.forumInstance.id = :forumInstanceId "
				+ "order by c.order asc"),
		@NamedQuery(name = "getLastCategoryOrder", query = "select max(c.order) from Category "
				+ "as c where c.forumInstance.id = :forumInstanceId") })
@Entity
@Table(name = "JBP_FORUMS_CATEGORIES")
@Indexed(index = "indexes/categories")
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8164247625235206934L;
	@OneToMany(mappedBy = "category", cascade = REMOVE)
	@OrderBy("order ASC")
	private List<Forum> forums;
	/*
	 * Luca Stancapiano - forumInstance field to tie category to it, so that
	 * each forum can to get different categories
	 */
	@ManyToOne
	@JoinColumn(name = "JBP_FORUM_INSTANCE_ID")
	private ForumInstance forumInstance;

	@Column(name = "JBP_ORDER")
	private int order;

	@Column(name = "JBP_TITLE")
	private String title;

	@Id
	@Column(name = "JBP_ID")
	@DocumentId
	@GeneratedValue
	private Integer id;

	/**
	 * Creates a new {@link Category} object.
	 */
	public Category() {
		setForums(new ArrayList<Forum>());
	}

	public Category(String title) {
		this();
		this.title = title;
	}

	/**
    */
	public List<Forum> getForums() {
		return forums;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void setForums(List<Forum> value) {
		forums = value;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void addForum(Forum value) {
		value.setCategory(this);
		forums.add(value);
	}

	/**
    */
	public int getOrder() {
		return order;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param order
	 *            DOCUMENT_ME
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
    */
	public String getTitle() {
		return title;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param title
	 *            DOCUMENT_ME
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
    */
	public Integer getId() {
		return id;
	}

	/*
	 * Luca Stancapiano start - I add accessors to manage ForumInstance field so
	 * Category is tied to the Forum Instance. Now each category belong to a
	 * Forum Instance so that we can to get different instances of forums with
	 * different categories and different sub objects
	 */
	public ForumInstance getForumInstance() {
		return forumInstance;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum
	 *            DOCUMENT_ME
	 */
	public void setForumInstance(ForumInstance forumInstance) {
		this.forumInstance = forumInstance;
	}
	// Luca Stancapiano end
}