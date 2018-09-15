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
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 916 $
 */

public class TopicBean implements Serializable, Comparable<TopicBean> {

	private static final long serialVersionUID = 3426875789016150344L;

	private Integer id;

	private List<PostBean> posts;

	private ForumBean forum;

	private int viewCount;

	private int replies;

	/**
	 * this field is cached but was left as it's easier to sort topics with HQL
	 * having such column
	 * 
	 */
	private Date lastPostDate;

	private PosterBean poster;

	private TopicType type;

	private int status;

	private String subject;

	private List<WatchBean> watches;

	private PollBean poll;

	/**
	 * Creates a new {@link TopicBean} object.
	 */
	public TopicBean() {
		setPosts(new ArrayList<PostBean>());
	}

	public TopicBean(String subject) {
		this();
		this.subject = subject;
	}

	public TopicBean(ForumBean forum, String subject) {
		this(subject);
		this.forum = forum;
	}

	public TopicBean(ForumBean forum, String subject, List<PostBean> posts) {
		this(forum, subject);
		this.posts = posts;
	}

	public TopicBean(ForumBean forum, String subject, List<PostBean> posts, TopicType type, PollBean poll) {
		this(forum, subject, posts);
		this.type = type;
		this.poll = poll;
	}

	/**
	 * we are implementing comparable to be able to sort topics by last post date
	 * without to have a column in db.
	 * 
	 * @param comp the topic to compare
	 * @return the result of the comparation. -1 minor, 0 equal, 1 major
	 */
	public int compareTo(TopicBean comp) {
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
	 * @return the id of the topic
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
	 * @return the posts of the topic
	 */
	public List<PostBean> getPosts() {
		return posts;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value DOCUMENT_ME
	 */
	public void setPosts(List<PostBean> value) {
		posts = value;
	}

	/**
	 * @return the forum of the topic
	 */
	public ForumBean getForum() {
		return forum;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum DOCUMENT_ME
	 */
	public void setForum(ForumBean forum) {
		this.forum = forum;
	}

	/**
	 * @return the count of visitors
	 */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param viewCount DOCUMENT_ME
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @return the number of replies
	 */
	public int getReplies() {
		return replies;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param replies DOCUMENT_ME
	 */
	public void setReplies(int replies) {
		this.replies = replies;
	}

	/**
	 * @return the last post date
	 */
	public Date getLastPostDate() {
		return lastPostDate;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param lastPostDate DOCUMENT_ME
	 */
	public void setLastPostDate(Date lastPostDate) {
		this.lastPostDate = lastPostDate;
	}

	/**
	 * @return the poster
	 */
	public PosterBean getPoster() {
		return poster;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param poster DOCUMENT_ME
	 */
	public void setPoster(PosterBean poster) {
		this.poster = poster;
	}

	/**
	 * @return the topic type. It can be NORMAL, IMPORTANT or ADVICE
	 */
	public TopicType getType() {
		return type;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param type DOCUMENT_ME
	 */
	public void setType(TopicType type) {
		this.type = type;
	}

	/**
	 * @return the status of the topic
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param status DOCUMENT_ME
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the subject of the topic
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param subject DOCUMENT_ME
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the watches list of the topic
	 */
	public List<WatchBean> getWatches() {
		return watches;
	}

	public void setWatches(List<WatchBean> watches) {
		this.watches = watches;
	}

	/**
	 * @return the poll of the topic
	 */
	public PollBean getPoll() {
		return poll;
	}

	public void setPoll(PollBean poll) {
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
		TopicBean other = (TopicBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}