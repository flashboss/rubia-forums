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
import java.util.Date;
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

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 916 $
 */

@NamedQueries({
		@NamedQuery(name = "findAnnouncements", query = "select t from Topic as t where t.forum=:forumid and "
				+ "t.type = :type order by t.lastPostDate"),
		@NamedQuery(name = "findPoll", query = "select t.poll from Topic as t where t.id=:topicid"),
		@NamedQuery(name = "findTopics", query = "select t from Topic as t "
				+ "where t.forum.category.forumInstance.id = :forumInstanceId"),
		@NamedQuery(name = "findTopicsTypeasc", query = "select t from Topic as t "
				+ "join fetch t.poster "
				+ "where t.forum = :forumid "
				+ "and t.type = :type " + "order by t.lastPostDate asc"),
		@NamedQuery(name = "findTopicsTypedesc", query = "select t from Topic as t "
				+ "join fetch t.poster "
				+ "where t.forum = :forumid "
				+ "and t.type = :type " + "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsForumasc", query = "select t from Topic as t "
				+ "join fetch t.poster "
				+ "where t.forum = :forumid "
				+ "order by t.lastPostDate asc"),
		@NamedQuery(name = "findTopicsForumdesc", query = "select t from Topic as t "
				+ "join fetch t.poster "
				+ "where t.forum = :forumid "
				+ "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsHot", query = "select t from Topic as t where t.replies > :replies "
				+ "and t.forum.category.forumInstance.id = :forumInstanceId "
				+ "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsByLatestPosts", query = "select t from Topic as t where t.forum.category.forumInstance.id = :forumInstanceId "
				+ "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsHottest", query = "select t from Topic as t where t.lastPostDate > :after "
				+ "and t.forum.category.forumInstance.id = :forumInstanceId "
				+ "order by t.replies desc"),
		@NamedQuery(name = "findTopicsMostViewed", query = "select t from Topic as t where t.lastPostDate > :after "
				+ "and t.forum.category.forumInstance.id = :forumInstanceId "
				+ "order by t.viewCount desc"),
		@NamedQuery(name = "findTopicsForumNoOrder", query = "select t from Topic as t where t.forum = :forumid"),
		@NamedQuery(name = "findPostsFromForumasc", query = "select p from Topic as t join t.posts as p where "
				+ "t.forum.id = :forumId order by p.createDate asc"),
		@NamedQuery(name = "findPostsFromForumdesc", query = "select p from Topic as t join t.posts as p where "
				+ "t.forum.id = :forumId order by p.createDate desc") })
@Entity
@Table(name = "JBP_FORUMS_TOPICS")
@Indexed(index = "topics")
public class Topic implements Serializable, Comparable<Topic> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3426875789016150344L;

	@Id
	@Column(name = "JBP_ID")
	@DocumentId
	@GeneratedValue
	private Integer id;

	@OneToMany(mappedBy = "topic", cascade = REMOVE)
	private List<Post> posts;

	@ManyToOne
	@JoinColumn(name = "JBP_FORUM_ID")
	@IndexedEmbedded(targetElement = Forum.class)
	private Forum forum;

	@Column(name = "JBP_VIEW_COUNT")
	private int viewCount;

	@Column(name = "JBP_REPLIES")
	private int replies;
	// this field is cached but was left as it's easier to sort topics with HQL
	// having such column

	@Column(name = "JBP_LAST_POST_DATE")
	private Date lastPostDate;

	@ManyToOne
	@JoinColumn(name = "JBP_POSTER")
	private Poster poster;

	@Column(name = "JBP_TYPE")
	private int type;

	@Column(name = "JBP_STATUS")
	private int status;

	@Column(name = "JBP_SUBJECT")
	private String subject;

	@ManyToMany
	@JoinTable(name = "JBP_FORUMS_TOPICSWATCH", joinColumns = @JoinColumn(name = "JBP_TOPIC_ID"), inverseJoinColumns = @JoinColumn(name = "JBP_ID"))
	private List<Watch> watches;

	@ManyToOne(cascade = REMOVE)
	@JoinColumn(name = "JBP_POLL")
	private Poll poll;

	/**
	 * Creates a new {@link Topic} object.
	 */
	public Topic() {
		setPosts(new ArrayList<Post>());
	}

	// we are implementing comparable to be able to sort topics by last post
	// date without to have
	// a column in db.
	public int compareTo(Topic comp) {
		Date thisDate = getLastPostDate();
		Date thatDate = comp.getLastPostDate();
		if (thisDate != null && (thatDate != null)) {
			return thisDate.compareTo(thatDate);
		} else if (thisDate == null && (thatDate != null)) {
			return -1;
		} else if (thisDate != null && (thatDate == null)) {
			return 1;
		} else {
			return 0;
		}
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
	public List<Post> getPosts() {
		return posts;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void setPosts(List<Post> value) {
		posts = value;
	}

	/**
    */
	public Forum getForum() {
		return forum;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum
	 *            DOCUMENT_ME
	 */
	public void setForum(Forum forum) {
		this.forum = forum;
	}

	/**
    */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param viewCount
	 *            DOCUMENT_ME
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
    */
	public int getReplies() {
		return replies;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param replies
	 *            DOCUMENT_ME
	 */
	public void setReplies(int replies) {
		this.replies = replies;
	}

	/**
    */
	public Date getLastPostDate() {
		return lastPostDate;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param lastPostDate
	 *            DOCUMENT_ME
	 */
	public void setLastPostDate(Date lastPostDate) {
		this.lastPostDate = lastPostDate;
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

	/**
    */
	public int getType() {
		return type;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param type
	 *            DOCUMENT_ME
	 */
	public void setType(int type) {
		this.type = type;
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
	public String getSubject() {
		return subject;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param subject
	 *            DOCUMENT_ME
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
    */
	public List<Watch> getWatches() {
		return watches;
	}

	public void setWatches(List<Watch> watches) {
		this.watches = watches;
	}

	/**
    */
	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
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
		Topic other = (Topic) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}