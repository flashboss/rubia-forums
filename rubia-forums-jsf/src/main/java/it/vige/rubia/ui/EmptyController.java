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
package it.vige.rubia.ui;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * 
 */
@Named("shared")
@ApplicationScoped
public class EmptyController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1354549818352820599L;

	private String mainPageName = MAIN_PAGE; // bean=level configuration
												// supplied by
	// the forums-config.xml
	private Map<String, String> links = new HashMap<String, String>();

	{
		links.put("category", "/views/category/viewcategory_body.xhtml");
		links.put("forum", "/views/forums/viewforum_body.xhtml");
		links.put("topic", "/views/topics/viewtopic_body.xhtml");
		links.put("index", "/views/index.xhtml");
		links.put("posting", "/views/topics/posting_new_body.xhtml");
		links.put("profile", "/views/profile/viewprofile_body.xhtml");
		links.put("message", "/views/profile/usermessage_body.xhtml");
		links.put("moderator", "/views/moderator/modcp_body.xhtml");
		links.put("topicSplit", "/views/moderator/modcp_split.xhtml");
		links.put("forumWatch", "/views/watches/forumWatch.xhtml");
		links.put("topicWatch", "/views/watches/topicWatch.xhtml");
		links.put("admin", "/views/admin/index.xhtml");
		links.put("pref", "/views/pref/index.xhtml");
		links.put("myForums", "/views/myforums/myforums_main.xhtml");
		links.put("myForumsAll", "/views/myforums/myforums_viewall.xhtml");
		links.put("myForumsEdit", "/views/myforums/myforums_editforums.xhtml");
		links.put("search", "/views/search/viewsearch_body.xhtml");
		links.put("searchResults", "/views/search/viewsearch_results.xhtml");

	}

	/**
	 * 
	 * @author sshah
	 * 
	 */
	public EmptyController() {

	}

	public String getMainPageName() {
		return mainPageName;
	}

	public Map<String, String> getLinks() {
		return links;
	}

	/**
	 * 
	 * @param mainPageName
	 *            the name of the main page
	 */
	public void setMainPageName(String mainPageName) {
		this.mainPageName = mainPageName;
	}

	/**
	 * @param links
	 *            the list of links of the application
	 */
	public void setLinks(Map<String, String> links) {
		this.links = links;
	}
}
