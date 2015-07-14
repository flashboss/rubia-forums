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

import static it.vige.rubia.Constants.THEMENAME;
import static it.vige.rubia.ui.JSFUtil.getDefaultLocale;
import static it.vige.rubia.ui.JSFUtil.getSelectedLocale;
import static it.vige.rubia.ui.JSFUtil.getSupportedLocales;
import static it.vige.rubia.ui.JSFUtil.getUserLastLoginDate;
import static java.lang.Thread.currentThread;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Singleton;

import it.vige.rubia.Constants;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.model.Topic;
import it.vige.rubia.properties.TCCLXProperties;
import it.vige.rubia.theme.FolderType;
import it.vige.rubia.theme.ForumsTheme;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@Singleton
@Named
public class ThemeHelper {

	@EJB
	private UserModule userModule;

	@EJB
	private UserProfileModule userProfileModule;

	/**
	 * If number of posts in topic exceeds this value it means that this topic
	 * is hot.
	 */
	public static final int hotThreshold = 10;

	/**
	 * 
	 */
	private Set<String> supportedLanguages;

	/**
	 * 
	 */
	private ForumsTheme theme;

	/**
	 * 
	 *
	 */
	public ThemeHelper() throws Exception {
		// Start the theme
		theme = new ForumsTheme();
		// start theme
		theme.setExtendedProperties(new TCCLXProperties(THEMENAME, "theme.properties"));
		theme.start();
		SortedSet<String> languages = new TreeSet<String>();
		Iterator<Locale> it = getSupportedLocales();
		while (it.hasNext()) {
			languages.add(it.next().getLanguage());
		}
		supportedLanguages = java.util.Collections.unmodifiableSortedSet(languages);
	}

	/**
	 * 
	 */
	private boolean isSupportedLanguage(String language) {
		return supportedLanguages.contains(language);
	}

	// method linked to facelet
	// functions---------------------------------------------------------------------------------------------------------
	/**
	 * 
	 *
	 */
	public String getURL(String urlKey) {
		try {
			String url = null;

			url = (String) ForumsTheme.class.getField(urlKey).get(theme);
			int lastIndexOfSlash = url.lastIndexOf("/");
			String beginning = url.substring(0, lastIndexOfSlash);

			// We don't want to I18N images contained in common directory.
			if (!beginning.endsWith("common")) {
				String language = getSelectedLocale().getLanguage();
				if (language.compareTo("") == 0 || !isSupportedLanguage(language)) {
					language = getDefaultLocale().getLanguage();
				}
				String end = url.substring(lastIndexOfSlash, url.length());
				url = beginning + "/" + language + end;
			}
			return url;
		} catch (Exception e) {
			return null;
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public String getResourceForumURL() {
		return theme.resourceForumURL;
	}

	/**
	 * 
	 * @return
	 */
	public String getResourceForumNewURL() {
		return theme.resourceForumNewURL;
	}

	/**
	 * 
	 * @return
	 */
	public String getResourceForumLockedURL() {
		return theme.resourceForumLockedURL;
	}

	/**
	 * 
	 *
	 */
	public String getFolderTypeURL(Topic topic, boolean isAnonymous) {
		String folderTypeURL = getURL("resourceFolderURL");

		FolderType folderType = theme.getFolderType(topic.getType(), topic.getStatus(),
				topic.getReplies() >= hotThreshold);

		if (!isAnonymous) {
			Date lastPostDate = topic.getLastPostDate();
			Date lastLoginDate = getUserLastLoginDate(userModule, userProfileModule);

			if (lastPostDate == null || lastLoginDate == null || lastPostDate.compareTo(lastLoginDate) <= 0) {
				folderTypeURL = folderType.getFolder();
			} else {
				folderTypeURL = folderType.getFolderNew();
			}
		} else {
			folderTypeURL = folderType.getFolder();
		}

		return folderTypeURL;
	}

	/**
	 *
	 *
	 */
	public String getFolderType(Topic topic) {

		// Getting ResourceBundle with current Locale
		FacesContext ctx = getCurrentInstance();
		UIViewRoot uiRoot = ctx.getViewRoot();
		Locale locale = uiRoot.getLocale();
		ClassLoader ldr = currentThread().getContextClassLoader();
		ResourceBundle bundle = getBundle("ResourceJSF", locale, ldr);

		String topicType = null;

		int topicStatus = topic.getStatus();
		FolderType folderType = theme.getFolderType(topic.getType(), topicStatus, topic.getReplies() >= hotThreshold);

		try {
			if (topicStatus != Constants.TOPIC_MOVED) {
				try {
					topicType = bundle.getString(folderType.type);
				} catch (MissingResourceException e) {
					topicType = "";
				}
			} else {
				topicType = bundle.getString("Topic_Moved");
			}
		} catch (Exception e) {
			return "";
		}
		return topicType;
	}
}
