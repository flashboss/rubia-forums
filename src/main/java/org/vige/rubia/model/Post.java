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

import static org.hibernate.search.annotations.Resolution.MINUTE;
import static org.hibernate.search.annotations.Store.YES;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.vige.rubia.auth.User;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 2066 $
 */

@NamedQueries({
		@NamedQuery(name = "findPosts", query = "select p from Post as p "
				+ "where p.topic.forum.category.forumInstance.id = :forumInstanceId"),
		@NamedQuery(name = "findPostsByTopicIdasc", query = "select p from Post as p where p.topic=:topicId order by p.createDate asc"),
		@NamedQuery(name = "findPostsByTopicIddesc", query = "select p from Post as p where p.topic=:topicId order by p.createDate desc"),
		@NamedQuery(name = "findPostsByIdsFetchAttachmentsAndPostersasc", query = "select p from Post as p "
				+ "join fetch p.poster "
				+ "left outer join fetch p.attachments "
				+ "where p.id IN ( :postIds ) " + "order by p.createDate asc"),
		@NamedQuery(name = "findPostsByIdsFetchAttachmentsAndPostersdesc", query = "select p from Post as p "
				+ "join fetch p.poster "
				+ "left outer join fetch p.attachments "
				+ "where p.id IN ( :postIds ) " + "order by p.createDate desc"),
		@NamedQuery(name = "findPostIdsasc", query = "select p.id "
				+ "from Post as p " + "where p.topic=:topicId "
				+ "order by p.createDate asc"),
		@NamedQuery(name = "findPostIdsdesc", query = "select p.id "
				+ "from Post as p " + "where p.topic=:topicId "
				+ "order by p.createDate desc"),
		@NamedQuery(name = "findPostsByTopicIdNoOrder", query = "select p from Post as p where p.topic=:topicId"),
		@NamedQuery(name = "findLastPostDateForUser", query = "select max(p.createDate) from Post as p where p.poster.userId = :userId"),
		@NamedQuery(name = "findLastPost", query = "select p "
				+ "from Post as p " + "join fetch p.poster "
				+ "where p.createDate = ( "
				+ "select DISTINCT MAX(tc.lastPostDate) " + "from Topic as tc "
				+ "where tc.forum = :forumId " + ")"),
		@NamedQuery(name = "findFirstPost", query = "select p "
				+ "from Post as p " + "join fetch p.poster "
				+ "where p.topic = :topicId "
				+ "AND p.createDate = :lastPostDate"),
		@NamedQuery(name = "findLastPostOrder", query = "select p from Post as p where p.topic  = :topicId order by p.createDate desc"),
		@NamedQuery(name = "findLastPostsOfTopics", query = "select tc.lastPostDate as maxDate , tc.id "
				+ "from Topic as tc " + "where tc.forum.id = :forumId"),
		@NamedQuery(name = "findLastPostsOfTopicsCreateDate", query = "select pt.createDate, pt "
				+ "from Post as pt "
				+ "join fetch pt.poster "
				+ "where pt.createDate IN (:dates) " + "order by pt.createDate"),
		@NamedQuery(name = "findLastPostsOfForums", query = "select MAX(tc.lastPostDate) as maxDate , tc.forum.id "
				+ "from Topic as tc "
				+ "where tc.forum.category.forumInstance.id = :forumInstanceId "
				+ "group by tc.forum.id"),
		@NamedQuery(name = "findLastPostsOfForumsCreateDate", query = "select pt.createDate, pt "
				+ "from Post as pt "
				+ "join fetch pt.poster "
				+ "where pt.createDate IN (:dates) " + "order by pt.createDate"),
		@NamedQuery(name = "findPostsOrderasc", query = "select p from Post as p order "
				+ "by p.createDate asc"),
		@NamedQuery(name = "findPostsOrderdesc", query = "select p from Post as p order "
				+ "by p.createDate desc") })
@Indexed(index = "posts")
@Entity
@Table(name = "JBP_FORUMS_POSTS")
public class Post implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@IndexedEmbedded(targetElement = Topic.class)
	@ManyToOne
	@JoinColumn(name = "JBP_TOPIC_ID")
	private Topic topic;

	@Transient
	private User user;

	@Column(name = "JBP_EDIT_COUNT")
	private int count;

	@Column(name = "JBP_EDIT_DATE")
	private Date date;

	@DocumentId
	@Id
	@Column(name = "JBP_ID")
	@GeneratedValue
	private Integer id;

	@Field(store = YES)
	@DateBridge(resolution = MINUTE)
	@Column(name = "JBP_CREATE_DATE")
	private Date createDate;

	@IndexedEmbedded(targetElement = Message.class)
	@Embedded
	private Message message;

	@ManyToOne
	@JoinColumn(name = "JBP_POSTER_ID")
	private Poster poster;

	@OneToMany(mappedBy = "post")
	private List<Attachment> attachments;

	/**
	 * Creates a new {@link Post} object.
	 */
	public Post() {
		attachments = new LinkedList<Attachment>();
	}

	/**
    */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	public User getUser() {
		return user;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param user
	 *            DOCUMENT_ME
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
    */
	public int getEditCount() {
		return count;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param count
	 *            DOCUMENT_ME
	 */
	public void setEditCount(int count) {
		this.count = count;
	}

	/**
    */
	public Date getEditDate() {
		return date;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param date
	 *            DOCUMENT_ME
	 */
	public void setEditDate(Date date) {
		this.date = date;
	}

	/**
    */
	public Integer getId() {
		return id;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param id
	 *            DOCUMENT_ME
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
    */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param createDate
	 *            DOCUMENT_ME
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
    */
	public Message getMessage() {
		return message;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param message
	 *            DOCUMENT_ME
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
    */
	public Poster getPoster() {
		return poster;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param poster
	 *            DOCUMENT_ME
	 */
	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	// TODO:BD - order it by something... is name ok?
	/**
    */
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(Attachment attachment) {
		attachment.setPost(this);
		attachments.add(attachment);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}