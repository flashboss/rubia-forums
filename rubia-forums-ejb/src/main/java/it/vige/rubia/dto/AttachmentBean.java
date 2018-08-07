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

/**
 * Created on 29 juil. 2004
 * 
 * @author theute
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a> $Revision: 1018 $
 */

public class AttachmentBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 871273047733790809L;

	private Integer id;

	private String comment;

	private PostBean post;

	private String contentType;

	private byte[] content;

	private long size;

	private String name;

	public AttachmentBean() {

	}

	public AttachmentBean(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}

	public AttachmentBean(String name, String comment, byte[] content) {
		this(name, comment);
		this.content = content;
	}

	public AttachmentBean(String name, String comment, byte[] content, PostBean post, String contentType, long size) {
		this(name, comment, content);
		this.post = post;
		this.contentType = contentType;
		this.size = size;
	}

	/**
	 * @return the comment of the attachment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the id of the attachment
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PostBean getPost() {
		return post;
	}

	public void setPost(PostBean post) {
		this.post = post;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}