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
package org.rubia.forums.ui.view;

import java.util.ResourceBundle;

public enum SummaryMode {
	BLOCK_TOPICS_MODE_HOT_TOPICS, BLOCK_TOPICS_MODE_HOTTEST_TOPICS, BLOCK_TOPICS_MODE_LATEST_POSTS, BLOCK_TOPICS_MODE_MOST_VIEWED;

	/** Resources for the default locale */
	private static final ResourceBundle res = ResourceBundle.getBundle("ResourceJSF");

	/** @return the locale-dependent message */
	public String toString() {
		return res.getString(name());
	}
}
