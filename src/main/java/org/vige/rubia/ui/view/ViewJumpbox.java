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

import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.vige.rubia.ui.JSFUtil.handleException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.vige.rubia.ForumsModule;
import org.vige.rubia.auth.AuthorizationListener;
import org.vige.rubia.auth.SecureActionForum;
import org.vige.rubia.model.Category;
import org.vige.rubia.ui.BaseController;
import org.vige.rubia.ui.action.PreferenceController;

/**
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("jumpbox")
@RequestScoped
public class ViewJumpbox extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2141380006506609949L;

	@Inject
	private ForumsModule forumsModule;
	// user preference controller

	@Inject
	private PreferenceController userPreferences;

	private List<Category> categories;

	private String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

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

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public List<Category> getCategories() {
		if (categories != null) {
			return categories;
		}
		try {
			// Luca Stancapiano start
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			categories = forumsModule
					.findCategoriesFetchForums(forumInstanceId);
			// Luca Stancapiano end

			return categories;
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	public void modeChanged(ValueChangeEvent event) {
		String action = "";
		String eventParameter = event.getNewValue().toString();
		if (!eventParameter.equals("0")) {
			FacesContext facesContext = getCurrentInstance();
			Map<String, List<String>> parameters = new HashMap<String, List<String>>();
			if (eventParameter.startsWith("forum-")) {
				parameters.put(p_forumId, Arrays
						.asList(new String[] { eventParameter.replace("forum-",
								"") }));
				action = "/views/forums/viewforum_body.xhtml";
			} else {
				action = "/views/category/viewcategory_body.xhtml";
				parameters.put(p_categoryId, Arrays
						.asList(new String[] { eventParameter.replace(
								"category-", "") }));
			}
			redirectUrl = facesContext.getApplication().getViewHandler()
					.getRedirectURL(facesContext, action, parameters, true);
			try {
				facesContext.getExternalContext().redirect(redirectUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}