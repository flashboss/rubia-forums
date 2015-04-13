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
package it.vige.rubia.theme;

import it.vige.rubia.theme.FolderType;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 878 $
 */
public class FolderType {
	/**
	 * DOCUMENT_ME
	 */
	public final String folder;

	/**
	 * DOCUMENT_ME
	 */
	public final String folderNew;

	/**
	 * DOCUMENT_ME
	 */
	public final String type;

	/**
	 * Creates a new {@link FolderType} object.
	 * 
	 * @param folder
	 *            DOCUMENT_ME
	 * @param folderNew
	 *            DOCUMENT_ME
	 * @param type
	 *            DOCUMENT_ME
	 */
	public FolderType(String folder, String folderNew, String type) {
		this.folder = folder;
		this.folderNew = folderNew;
		this.type = type;
	}

	public String getFolder() {
		return folder;
	}

	public String getFolderNew() {
		return folderNew;
	}

	public String getType() {
		return type;
	}
}