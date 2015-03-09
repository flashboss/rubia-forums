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
package org.vige.rubia.ui.view;

import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public enum SummaryMode {
	BLOCK_TOPICS_MODE_HOT_TOPICS, BLOCK_TOPICS_MODE_HOTTEST_TOPICS, BLOCK_TOPICS_MODE_LATEST_POSTS, BLOCK_TOPICS_MODE_MOST_VIEWED;
	/** @return the locale-dependent message */
	public String toString() {
		FacesContext context = getCurrentInstance();
		ResourceBundle res;
		if (context != null) {
			Locale locale = context.getViewRoot().getLocale();
			res = getBundle("ResourceJSF", locale);
		} else
			res = getBundle("ResourceJSF");
		return res.getString(name());
	}
}
