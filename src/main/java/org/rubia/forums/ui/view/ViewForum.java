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

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static org.rubia.forums.feeds.FeedConstants.ATOM;
import static org.rubia.forums.feeds.FeedConstants.FORUM;
import static org.rubia.forums.feeds.FeedConstants.RSS;
import static org.rubia.forums.ui.ForumUtil.getParameter;
import static org.rubia.forums.ui.JSFUtil.handleException;
import static org.rubia.forums.ui.PortalUtil.createFeedLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.rubia.forums.ForumsModule;
import org.rubia.forums.auth.AuthorizationListener;
import org.rubia.forums.auth.SecureActionForum;
import org.rubia.forums.model.Forum;
import org.rubia.forums.model.Topic;
import org.rubia.forums.ui.BaseController;
import org.rubia.forums.ui.PageNavigator;
import org.rubia.forums.ui.action.PreferenceController;

//jsf imports

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@Named("forum")
@RequestScoped
public class ViewForum extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1193917741044670669L;

	@Inject
	private ForumsModule forumsModule;

	// preference related data
	@Inject
	private PreferenceController userPreferences;
	private Map<Integer, PageNavigator> topicNavigator = new HashMap<Integer, PageNavigator>();

	// business data being generated in this bean by executing ui actions
	// this is data is created such that it can be consumed by the view
	// components
	// like facelets
	private Forum forum;
	private List<Topic> normalThreads = new ArrayList<Topic>();
	private Map<Object, Object> topicLastPosts;
	private List<Topic> stickyThreads;
	private List<Topic> announcements;
	private DataModel<Topic> normalThreadsDataModel = new ListDataModel<Topic>(
			normalThreads);

	public DataModel<Topic> getNormalThreadsDataModel() {
		return normalThreadsDataModel;
	}

	public void setNormalThreadsDataModel(
			DataModel<Topic> normalThreadsDataModel) {
		this.normalThreadsDataModel = normalThreadsDataModel;
	}

	public int getLastPageNumber() {
		if (forum != null)
			return forum.getTopicCount() / userPreferences.getTopicsPerForum()
					+ 1;
		else
			return 1;
	}

	// ----------------business data being generated for use by the view
	// components like
	// facelets---------------------------------------------------------------------------------------
	/**
     * 
     */
	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}

	// --------------------------------------------------------------------------------------------
	/**
      * 
      *
      */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Topic> getAnnouncements() {
		if (announcements != null) {
			return announcements;
		}
		announcements = new ArrayList<Topic>();
		try {
			announcements = forumsModule.findTopicsDesc(forum, POST_ANNOUNCE,
					0, MAX_VALUE);
		} catch (Exception e) {
			handleException(e);
		}
		return announcements;
	}

	/**
	 * 
	 * TODO: Make a special method in ForumsModule for that.
	 */
	public boolean isAnnouncementsPresent() {
		if (announcements != null) {
			if (announcements.size() > 0) {
				return true;
			} else {
				return false;
			}
		}
		boolean present = false;
		try {
			announcements = forumsModule.findTopicsDesc(forum, POST_ANNOUNCE,
					0, MAX_VALUE);
			if (announcements != null && announcements.size() > 0) {
				present = true;
			}
		} catch (Exception e) {
			handleException(e);
		}
		return present;
	}

	/**
	 * 
	 * TODO: Make a special method in ForumsModule for that.
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Topic> getStickyThreads() {
		if (stickyThreads != null) {
			return stickyThreads;
		}
		stickyThreads = new ArrayList<Topic>();
		try {
			// ForumsModule fm = this.getForumsModule();
			stickyThreads = forumsModule.findTopicsDesc(forum, POST_STICKY, 0,
					MAX_VALUE);
		} catch (Exception e) {
			handleException(e);
		}
		return stickyThreads;
	}

	/**
	 * 
	 * TODO: Make a special method in ForumsModule for that.
	 */
	public boolean isStickyThreadsPresent() {
		if (stickyThreads != null) {
			if (stickyThreads.size() > 0) {
				return true;
			} else {
				return false;
			}
		}
		boolean present = false;
		try {
			stickyThreads = forumsModule.findTopicsDesc(forum, POST_STICKY, 0,
					MAX_VALUE);
			if (stickyThreads != null && stickyThreads.size() > 0) {
				present = true;
			}
		} catch (Exception e) {
			handleException(e);
		}
		return present;
	}

	/**
      * 
      */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<Topic> getNormalThreads() {
		return normalThreads;
	}

	/**
      * 
      */
	public boolean isNormalThreadsPresent() {
		return normalThreads.size() > 0;
	}

	/**
	 * @return Returns the a Map which contains TopicId:LastPost pairs.
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Map<Object, Object> getTopicLastPosts() {
		if (topicLastPosts == null) {
			topicLastPosts = new HashMap<Object, Object>();
		}
		return topicLastPosts;
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

	// -------------------------------------------------------------------------------------------------------------------------------------

	/**
      * 
      *
      */
	@PostConstruct
	private void execute() throws Exception {
		// parse the input parameters
		int forumId = -1;
		String f = getParameter(p_forumId);
		if (f != null && f.trim().length() > 0) {
			forumId = parseInt(f);
		}

		// ForumsModule is stored as a final variable so that anonymous
		// class
		// could use it.
		final ForumsModule fm = forumsModule;

		// grab the data to be displayed for this page
		if (forumId != -1) {
			// setup the business objects like the forum, topics etc that
			// will
			// be displayed
			forum = fm.findForumById(forumId);

			// Getting sticky topics for this page
			Collection<Topic> stickies = getStickyThreads();

			// Getting announcements
			Collection<Topic> announcements = getAnnouncements();

			normalThreads = fm.findTopicsDesc(forum, POST_NORMAL, 0, MAX_VALUE);
			normalThreadsDataModel = new ListDataModel<Topic>(normalThreads);

			Collection<Topic> listOfTopics = new LinkedList<Topic>();

			listOfTopics.addAll(stickies);
			listOfTopics.addAll(announcements);
			listOfTopics.addAll(normalThreads);

			// Getting sticky topics for this page
			topicLastPosts = fm.findLastPostsOfTopics(listOfTopics);

			// setup dummy pageNavigators for all topics being displayed for
			// topic minipaging
			for (Topic cour : listOfTopics) {
				if (cour.getReplies() > 0) {
					PageNavigator topicNav = new PageNavigator(
							cour.getReplies() + 1,
							userPreferences.getPostsPerTopic(), // this
																// is
																// user's
																// posts
																// per
																// page
																// preference
							0 // current page of the navigator
					) {

						/**
						 * 
						 */
						private static final long serialVersionUID = 6277599446838264687L;

						protected Collection<Integer> initializePage() {
							return null;
						}

					};
					topicNavigator.put(cour.getId(), topicNav);
				}
			}
		}
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getRssFeed() {
		return createFeedLink(RSS, FORUM, forum.getId());
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getAtomFeed() {
		return createFeedLink(ATOM, FORUM, forum.getId());
	}

	public boolean isLocked() {
		return forum.getStatus() == FORUM_LOCKED;
	}

	public Map<Integer, PageNavigator> getTopicNavigator() {
		return topicNavigator;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------
}
