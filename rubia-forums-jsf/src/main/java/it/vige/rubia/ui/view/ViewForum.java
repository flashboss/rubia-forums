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

import static it.vige.rubia.dto.TopicType.ADVICE;
import static it.vige.rubia.dto.TopicType.IMPORTANT;
import static it.vige.rubia.dto.TopicType.NORMAL;
import static it.vige.rubia.feeds.FeedConstants.ATOM;
import static it.vige.rubia.feeds.FeedConstants.FORUM;
import static it.vige.rubia.feeds.FeedConstants.RSS;
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.ForumUtil.truncate;
import static it.vige.rubia.ui.JSFUtil.createFeedLink;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.TopicRequestBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.PageNavigator;
import it.vige.rubia.ui.action.PreferenceController;

//jsf imports

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@Named("forum")
@RequestScoped
public class ViewForum extends BaseController {

	private static final long serialVersionUID = -1193917741044670669L;

	@EJB
	private ForumsModule forumsModule;
	@EJB
	private UserModule userModule;

	// preference related data
	@Inject
	private PreferenceController userPreferences;
	private Map<Integer, PageNavigator> topicNavigator = new HashMap<Integer, PageNavigator>();

	// business data being generated in this bean by executing ui actions
	// this is data is created such that it can be consumed by the view
	// components
	// like facelets
	private ForumBean forum;
	private List<TopicBean> normalThreads = new ArrayList<TopicBean>();
	private Map<Object, Object> topicLastPosts;
	private List<TopicBean> stickyThreads;
	private List<TopicBean> announcements;
	private DataModel<TopicBean> normalThreadsDataModel = new ListDataModel<TopicBean>(normalThreads);

	public DataModel<TopicBean> getNormalThreadsDataModel() {
		return normalThreadsDataModel;
	}

	public void setNormalThreadsDataModel(DataModel<TopicBean> normalThreadsDataModel) {
		this.normalThreadsDataModel = normalThreadsDataModel;
	}

	public int getLastPageNumber() {
		if (forum != null)
			return forum.getTopicCount() / userPreferences.getTopicsPerForum() + 1;
		else
			return 1;
	}

	// ----------------business data being generated for use by the view
	// components like
	// facelets---------------------------------------------------------------------------------------
	/**
	 * @return the current forum
	 */
	public ForumBean getForum() {
		return forum;
	}

	public void setForum(ForumBean forum) {
		this.forum = forum;
	}

	/**
	 * @return the topics of the announcement
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<TopicBean> getAnnouncements() {
		if (announcements != null) {
			return announcements;
		}
		announcements = new ArrayList<TopicBean>();

		TopicRequestBean topicRequestBean = new TopicRequestBean();
		TopicBean topicBean = new TopicBean();
		topicBean.setForum(forum);
		topicBean.setType(ADVICE);
		topicRequestBean.setTopic(topicBean);
		topicRequestBean.setPerPage(MAX_VALUE);
		try {
			announcements = forumsModule.findTopicsDesc(topicRequestBean);
		} catch (Exception e) {
			handleException(e);
		}
		return announcements;
	}

	/**
	 * 
	 * TODO: Make a special method in ForumsModule for that.
	 * 
	 * @return true if an announcement is present
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

		TopicRequestBean topicRequestBean = new TopicRequestBean();
		TopicBean topicBean = new TopicBean();
		topicBean.setForum(forum);
		topicBean.setType(ADVICE);
		topicRequestBean.setTopic(topicBean);
		topicRequestBean.setPerPage(MAX_VALUE);
		try {
			announcements = forumsModule.findTopicsDesc(topicRequestBean);
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
	 * 
	 * @return all the sticky topicsof the forum
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<TopicBean> getStickyThreads() {
		if (stickyThreads != null) {
			return stickyThreads;
		}
		stickyThreads = new ArrayList<TopicBean>();

		TopicRequestBean topicRequestBean = new TopicRequestBean();
		TopicBean topicBean = new TopicBean();
		topicBean.setForum(forum);
		topicBean.setType(IMPORTANT);
		topicRequestBean.setTopic(topicBean);
		topicRequestBean.setPerPage(MAX_VALUE);
		try {
			// ForumsModule fm = this.getForumsModule();
			stickyThreads = forumsModule.findTopicsDesc(topicRequestBean);
		} catch (Exception e) {
			handleException(e);
		}
		return stickyThreads;
	}

	/**
	 * 
	 * TODO: Make a special method in ForumsModule for that.
	 * 
	 * @return true if a sticky thread is present
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

		TopicRequestBean topicRequestBean = new TopicRequestBean();
		TopicBean topicBean = new TopicBean();
		topicBean.setForum(forum);
		topicBean.setType(IMPORTANT);
		topicRequestBean.setTopic(topicBean);
		topicRequestBean.setPerPage(MAX_VALUE);
		try {
			stickyThreads = forumsModule.findTopicsDesc(topicRequestBean);
			if (stickyThreads != null && stickyThreads.size() > 0) {
				present = true;
			}
		} catch (Exception e) {
			handleException(e);
		}
		return present;
	}

	/**
	 * @return all the normal threads of the forum
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public Collection<TopicBean> getNormalThreads() {
		return normalThreads;
	}

	/**
	 * @return true if a normal thread is present
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

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String getLastPostSubject(int id) {
		PostBean post = (PostBean) getTopicLastPosts().get(id);
		if (post != null) {
			String subject = post.getMessage().getSubject();
			return truncate(subject, 25);
		} else
			return "";
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
			try {
				forum = fm.findForumById(forumId);

				// Getting sticky topics for this page
				Collection<TopicBean> stickies = getStickyThreads();

				// Getting announcements
				Collection<TopicBean> announcements = getAnnouncements();

				TopicRequestBean topicRequestBean = new TopicRequestBean();
				TopicBean topicBean = new TopicBean();
				topicBean.setForum(forum);
				topicBean.setType(NORMAL);
				topicRequestBean.setTopic(topicBean);
				topicRequestBean.setPerPage(MAX_VALUE);
				
				normalThreads = fm.findTopicsDesc(topicRequestBean);
				normalThreadsDataModel = new ListDataModel<TopicBean>(normalThreads);

				Collection<TopicBean> listOfTopics = new LinkedList<TopicBean>();

				listOfTopics.addAll(stickies);
				listOfTopics.addAll(announcements);
				listOfTopics.addAll(normalThreads);

				// Getting sticky topics for this page
				topicLastPosts = fm.findLastPostsOfTopics(listOfTopics);

				// setup dummy pageNavigators for all topics being displayed for
				// topic minipaging
				for (TopicBean cour : listOfTopics) {
					if (cour.getReplies() > 0) {
						PageNavigator topicNav = new PageNavigator(cour.getReplies() + 1,
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
			} catch (ModuleException e) {
				handleException(e);
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
}
