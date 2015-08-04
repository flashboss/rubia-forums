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
package it.vige.rubia.ui.view;

import static it.vige.rubia.ui.JSFUtil.handleException;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.action.PreferenceController;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("adminPanel")
public class ViewAdminPanel extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2921359509888139037L;

	@EJB
	private ForumsModule forumsModule;

	// user preference controller
	@Inject
	private PreferenceController userPreferences = null;

	private List<Category> categories = null;
	private List<Forum> forums = null;

	// ------------user
	// preferences-------------------------------------------------------------------------------------------------------------
	/**
	 * @return Returns the userPreferences.
	 */
	public PreferenceController getUserPreferences() {
		return userPreferences;
	}

	/**
	 * @param userPreferences
	 *            The userPreferences to set.
	 */
	public void setUserPreferences(PreferenceController userPreferences) {
		this.userPreferences = userPreferences;
	}

	/**
	 * @return the list of categories in the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public List<Category> getCategories() {
		if (categories != null) {
			return categories;
		}
		synchronized (this) {
			if (categories != null) {
				return categories;
			}
			try {
				// get the forumInstanceId where this forum should be added
				int forumInstanceId = userPreferences.getForumInstanceId();

				categories = forumsModule.findCategoriesFetchForums(forumInstanceId);

				return categories;
			} catch (Exception e) {
				handleException(e);
			}
			return null;
		}
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public List<Forum> getForums() {
		if (forums != null) {
			return forums;
		}
		try {
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			forums = forumsModule.findForums(forumInstanceId);

			return forums;
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

}
