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
package org.vige.rubia.model;

import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

/**
 * Category of forums.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet </a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * @version $Revision: 1124 $
 */

@NamedQueries({ @NamedQuery(name = "findForumByIdFetchTopics", query = "select f from Forum f left outer " + "join fetch f.topics where f.id = :forumId"),
		@NamedQuery(name = "findForums", query = "select f from Forum as f " + "where f.category.forumInstance.id = :forumInstanceId " + "order by f.order asc"),
		@NamedQuery(name = "findForumsByCategoryId", query = "select f from Forum as f where f.category=:categoryId " + "order by f.order asc"), @NamedQuery(name = "getLastForumOrder", query = "select max(f.order) from Forum as " + "f where f.category.id = :categoryId"),
		@NamedQuery(name = "findPostsFromCategoryasc", query = "select p from Forum as f join f.topics " + "as t join t.posts as p where f.category.id = :categoryId order by p.createDate " + "asc"),
		@NamedQuery(name = "findPostsFromCategorydesc", query = "select p from Forum as f join f.topics as " + "t join t.posts as p where f.category.id = :categoryId order by p.createDate desc") })
@Entity
@Table(name = "JBP_FORUMS_FORUMS")
@Indexed(index = "forums")
public class Forum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5226669076550683642L;

	/**
	 * Creates a new {@link Forum} object.
	 */
	public Forum() {
		setTopics(new ArrayList<Topic>());
	}

	@ManyToOne
	@JoinColumn(name = "JBP_CATEGORY_ID")
	@IndexedEmbedded(targetElement = Category.class)
	private Category category;

	@Column(name = "JBP_DESCRIPTION")
	private String description;

	@Id
	@Column(name = "JBP_ID")
	@DocumentId
	@GeneratedValue
	private Integer id;

	@Column(name = "JBP_NAME")
	@Field(store = Store.YES)
	private String name;

	@Column(name = "JBP_ORDER")
	private int order;

	@Column(name = "JBP_TOPIC_COUNT")
	private int topicCount;

	@Column(name = "JBP_POST_COUNT")
	private int postCount;

	@Column(name = "JBP_STATUS")
	private int status;

	@OneToMany(mappedBy = "forum", cascade = REMOVE)
	private List<Topic> topics;

	@Transient
	private Collection<Watch> watch;

	@ManyToMany
	@JoinTable(name = "JBP_FORUMS_FORUMSWATCH", joinColumns = @JoinColumn(name = "JBP_FORUM_ID"), inverseJoinColumns = @JoinColumn(name = "JBP_ID"))
	private List<Watch> watches;

	/**
    */
	public Category getCategory() {
		return category;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param category
	 *            DOCUMENT_ME
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
    */
	public String getDescription() {
		return description;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param description
	 *            DOCUMENT_ME
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
    */
	public Integer getId() {
		return id;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param lastPost
	 *            DOCUMENT_ME
	 */
	public void setLastPost(Post lastPost) {
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
	public int getTopicCount() {
		return topicCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param size
	 *            DOCUMENT_ME
	 */
	public void setTopicCount(int size) {
		this.topicCount = size;
	}

	/**
	 * DOCUMENT_ME
	 */
	public void addTopicSize() {
		setTopicCount(topicCount + 1);
	}

	/**
    */
	public int getPostCount() {
		return postCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param size
	 *            DOCUMENT_ME
	 */
	public void setPostCount(int size) {
		this.postCount = size;
	}

	/**
	 * DOCUMENT_ME
	 */
	public void addPostSize() {
		setPostCount(postCount + 1);
	}

	/**
	 * _@___ hibernate.property column="jbp_prune_enable" unique="false"
	 * update="true"
	 */
	public boolean getPruneEnable() {
		// return pruneEnable;
		return false;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param enable
	 *            DOCUMENT_ME
	 */
	public void setPruneEnable(boolean enable) {
		// this.pruneEnable = enable;
	}

	/**
	 * _@___ hibernate.property column="jbp_prune_next" unique="false"
	 * update="true"
	 */
	public int getPruneNext() {
		// return pruneNext;
		return 0;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param next
	 *            DOCUMENT_ME
	 */
	public void setPruneNext(int next) {
		// this.pruneNext = next;
	}

	/**
    */
	public int getStatus() {
		return status;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param status
	 *            DOCUMENT_ME
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
    */
	public List<Topic> getTopics() {
		return topics;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void setTopics(List<Topic> value) {
		topics = value;
	}

	/**
    */
	public Collection<Watch> getForumWatch() {
		return watch;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param watch
	 *            DOCUMENT_ME
	 */
	public void setForumWatch(Collection<Watch> watch) {
		this.watch = watch;
	}

	/**
    */
	public List<Watch> getWatches() {
		return watches;
	}

	public void setWatches(List<Watch> watches) {
		this.watches = watches;
	}
}