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

import static java.util.Calendar.DATE;
import static java.util.Calendar.getInstance;
import static org.jboss.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptors;

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.action.PreferenceController;

//jsf imports

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * 
 */
@Named("summary")
@RequestScoped
public class ViewSummary extends BaseController {

	private static final long serialVersionUID = 6950361977869824L;
	private static Logger log = getLogger(ViewSummary.class);

	@EJB
	private ForumsModule forumsModule;

	// user preference controller
	@Inject
	private PreferenceController userPreferences;

	// business data being generated in this bean by executing ui actions
	// this is data is created such that it can be consumed by the view
	// components
	// like facelets
	private Collection<TopicBean> topics;

	// ----------------bean configuration supplied by the
	// forums-config.xml---------------------------------------------------------------------------------------------

	// ----------------business data being generated for use by the view
	// components like
	// facelets---------------------------------------------------------------------------------------
	/**
	 * 
	 * @return the list of topics in the summary
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<TopicBean> getTopics() {
		if (topics == null) {
			topics = new ArrayList<TopicBean>();
		}
		return topics;
	}

	/**
	 * 
	 * @return the number of topics found
	 */
	public String getNumberOfTopicsFound() {
		return String.valueOf(getTopics().size());
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
	 * @param userPreferences The userPreferences to set.
	 */
	public void setUserPreferences(PreferenceController userPreferences) {
		this.userPreferences = userPreferences;
	}

	/**
	 * 
	 *
	 */
	@PostConstruct
	public void execute() {
		// load the topics
		try {
			loadDefaultTopics();
		} catch (Exception e) {
			log.error(e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * This method returns the blockTopicType based on the summaryMode set for this
	 * application
	 * 
	 * @return the block topics type
	 */
	public String getBlockTopicsType() {
		return userPreferences.getSummaryMode().name();
	}

	/**
	 * 
	 * @throws Exception the error exception
	 */
	private void loadDefaultTopics() throws Exception {
		Calendar after = getInstance();
		after.add(DATE, -userPreferences.getSummaryTopicDays());
		Date time = after.getTime();
		int summaryTopicReplies = userPreferences.getSummaryTopicReplies();
		int summaryTopicLimit = userPreferences.getSummaryTopicLimit();

		// get the forumInstanceId where this forum should be added
		int forumInstanceId = userPreferences.getForumInstanceId();

		if (forumsModule.findForumInstanceById(forumInstanceId) == null)
			forumsModule.createForumInstance(forumInstanceId, "by_manual_preferences");

		switch (userPreferences.getSummaryMode()) {
		/*
		 * FindTopicsHot, findTopicsHottest, findTopicsByLatestPosts,
		 * findTopicsMostViewed methods need to a forumInstanceId argument to take only
		 * categories of a specific forum instance
		 */
		case BLOCK_TOPICS_MODE_HOT_TOPICS:
			topics = forumsModule.findTopicsHot(summaryTopicReplies, summaryTopicLimit, forumInstanceId);
			break;
		case BLOCK_TOPICS_MODE_HOTTEST_TOPICS:
			topics = forumsModule.findTopicsHottest(time, summaryTopicLimit, forumInstanceId);
			break;
		case BLOCK_TOPICS_MODE_LATEST_POSTS:
			topics = forumsModule.findTopicsByLatestPosts(summaryTopicLimit, forumInstanceId);
			break;
		case BLOCK_TOPICS_MODE_MOST_VIEWED:
			topics = forumsModule.findTopicsMostViewed(time, summaryTopicLimit, forumInstanceId);
			break;
		}
	}
}
