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
import java.sql.Blob;

import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 * An uploaded file.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 1018 $
 */
public class UploadedFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4633794450124309928L;
	private String contentType;
	private byte[] byteContent;
	private Blob content;
	private long size;
	private String name;

	/**
	 * @param contentType
	 *            the file content type
	 * @param content
	 *            the file chunk of bytes
	 * @param name
	 *            the file name
	 * @param size
	 *            the size of the file
	 */
	public UploadedFile(String contentType, byte[] content, String name, long size, Session session) {
		if (contentType == null) {
			throw new NullPointerException("Content type cannot be null");
		}
		if (content == null) {
			throw new NullPointerException("Content cannot be null");
		}
		this.contentType = contentType;
		this.setByteContent(content);
		this.content = Hibernate.getLobCreator(session).createBlob(content);
		this.size = size;
		this.name = name;
	}

	public UploadedFile() {

	}

	/**
    */
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String type) {
		this.contentType = type;
	}

	/**
    */
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	/**
    */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getByteContent() {
		return byteContent;
	}

	public void setByteContent(byte[] byteContent) {
		this.byteContent = byteContent;
	}

	/**
    */
	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}
}
