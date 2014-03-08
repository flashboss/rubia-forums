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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created on 29 juil. 2004
 * 
 * @author theute
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a> $Revision: 1018 $
 */

@Entity
@Table(name = "JBP_FORUMS_ATTACHMENTS")
public class Attachment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 871273047733790809L;

	@Id
	@Column(name = "JBP_ATTACHMENT_ID")
	@GeneratedValue
	private Integer id;

	@Transient
	private UploadedFile file;

	@Column(name = "JBP_COMMENT")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "JBP_POST_ID")
	private Post post;

	public Attachment() {

	}

	/**
    */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
    */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * @param file
	 *            The file to set.
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

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
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}