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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.vige.rubia.auth.User;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 2066 $
 */

public class PostBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private TopicBean topic;

	private User user;

	private int count;

	private Date date;

	private Integer id;

	private Date createDate;

	private MessageBean message;

	private PosterBean poster;

	private List<AttachmentBean> attachments;

	/**
	 * Creates a new {@link PostBean} object.
	 */
	public PostBean() {
		attachments = new LinkedList<AttachmentBean>();
	}

	public PostBean(String text) {
		this();
		this.message = new MessageBean(text);
	}

	public PostBean(String text, List<AttachmentBean> attachments) {
		this(text);
		this.attachments = attachments;
	}

	public PostBean(TopicBean topic, String text, List<AttachmentBean> attachments) {
		this(text, attachments);
		this.topic = topic;
	}

	public PostBean(TopicBean topic, String text) {
		this(text);
		this.topic = topic;
	}

	/**
	 * @return the topic of the post
	 */
	public TopicBean getTopic() {
		return topic;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 */
	public void setTopic(TopicBean topic) {
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
	 * @param user DOCUMENT_ME
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the number of updates of the topic
	 */
	public int getEditCount() {
		return count;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param count DOCUMENT_ME
	 */
	public void setEditCount(int count) {
		this.count = count;
	}

	/**
	 * @return the edit date of the post
	 */
	public Date getEditDate() {
		return date;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param date DOCUMENT_ME
	 */
	public void setEditDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the id of the post
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
	 * @return the create date of the post
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param createDate DOCUMENT_ME
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the message of the post
	 */
	public MessageBean getMessage() {
		return message;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param message DOCUMENT_ME
	 */
	public void setMessage(MessageBean message) {
		this.message = message;
	}

	/**
	 * @return the poster user of the post
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

	// TODO:BD - order it by something... is name ok?
	/**
	 * @return the attachments list of the post
	 */
	public List<AttachmentBean> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentBean> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(AttachmentBean attachment) {
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
		PostBean other = (PostBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}