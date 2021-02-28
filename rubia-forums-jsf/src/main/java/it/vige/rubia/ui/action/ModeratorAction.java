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
package it.vige.rubia.ui.action;

import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.JSFUtil.getBundleMessage;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Boolean.TRUE;
import static java.lang.Integer.parseInt;
import static jakarta.faces.application.FacesMessage.SEVERITY_INFO;
import static jakarta.faces.application.FacesMessage.SEVERITY_WARN;
import static jakarta.faces.context.FacesContext.getCurrentInstance;
import static org.jboss.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptors;

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.view.ViewTopic;

/**
 * ModeratorAction class is a managed bean used for getting, keeping,
 * transforming all data and invoking moderator actions for Moderator's Control
 * Panel.
 * 
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("moderator")
@ConversationScoped
public class ModeratorAction extends BaseController {

	private static final long serialVersionUID = 557502988546166382L;
	private static Logger log = getLogger(ModeratorAction.class);

	@EJB
	private ForumsModule forumsModule;

	// preference related data
	@Inject
	private PreferenceController userPreferences;

	@Inject
	private ViewTopic viewTopic;

	@Inject
	private Conversation conversation;

	// business data being generated in this bean by executing ui actions
	// this is data is created such that it can be consumed by the view
	// components
	// like facelets
	private ForumBean forum;
	private Map<Integer, Boolean> checkboxes;
	private List<TopicBean> topics = new ArrayList<TopicBean>();
	private DataModel<TopicBean> topicsDataModel = new ListDataModel<TopicBean>(topics);

	public DataModel<TopicBean> getTopicsDataModel() {
		return topicsDataModel;
	}

	public void setTopicsDataModel(DataModel<TopicBean> topicsDataModel) {
		this.topicsDataModel = topicsDataModel;
	}

	public Collection<TopicBean> getTopics() {
		return topics;
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

	// -------------------------------------------------------------------------------------------------------------------------------------

	// ----------------business data being generated for use by the view
	// components like
	// facelets---------------------------------------------------------------------------------------
	public ForumBean getForum() {
		return forum;
	}

	public void setCheckboxes(Map<Integer, Boolean> checkboxes) {
		this.checkboxes = checkboxes;
	}

	public Map<Integer, Boolean> getCheckboxes() {
		return checkboxes;
	}

	// ui actions supported by this
	// bean----------------------------------------------------------------------------------------------------

	/**
	 * UI Action for deleting topic(s) from the forum.
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String deleteTopic() {
		try {
			for (int topicId : checkboxes.keySet()) {
				boolean value = checkboxes.get(topicId);

				try {
					if (value) {
						TopicBean topic = forumsModule.findTopicById(topicId);
						forumsModule.removeTopic(topicId);
						topics.remove(topic);
					}
				} catch (Exception e) {
					setWarnBundleMessage("ERR_CANNOT_REMOVE_TOPIC");
					return "success";
				}
			}
			setInfoBundleMessage("SUCC_TOPIC_REMOVED");

		} catch (Exception e) {
			handleException(e);
		}
		if (!conversation.isTransient())
			conversation.end();
		return "success";
	}

	/**
	 * UI Action for moveing topic(s) from one forum to other.
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String moveTopic() {
		String forum_to_id = getParameter(p_forum_to_id);
		if (forum_to_id == null || forum_to_id.trim().length() == 0
				|| forum_to_id.trim().compareToIgnoreCase("-1") == 0) {
			setWarnBundleMessage("ERR_NO_DEST_FORUM");
			return "success";
		}
		ForumBean forumDest = null;
		try {
			forumDest = forumsModule.findForumById(parseInt(forum_to_id));
		} catch (Exception e) {
			setWarnBundleMessage("ERR_INTERNAL");
			return "success";
		}

		try {

			for (int topicId : checkboxes.keySet()) {
				boolean value = checkboxes.get(topicId);
				if (value) {
					TopicBean topic = null;
					try {
						topic = forumsModule.findTopicById(topicId);
					} catch (Exception e) {
						setWarnBundleMessage("ERR_INTERNAL");
						return "success";
					}

					topic.setForum(forumDest);
					forumDest.setPostCount(forumDest.getPostCount() + topic.getReplies() + 1);
					forumDest.setTopicCount(forumDest.getTopicCount() + 1);

					forum.setPostCount(forum.getPostCount() - topic.getReplies() - 1);
					forum.setTopicCount(forum.getTopicCount() - 1);
					forumsModule.update(forum);
					forumsModule.update(forumDest);
					forumsModule.update(topic);
					topics.remove(topic);
				}
			}
			setInfoBundleMessage("SUCC_TOPIC_MOVED");

		} catch (Exception e) {
			handleException(e);
		}
		if (!conversation.isTransient())
			conversation.end();
		return "success";
	}

	/**
	 * UI Action for locking selected topic(s).
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String lockTopic() {
		if (isAnyCheckboxSelected()) {
			try {
				for (int topicId : checkboxes.keySet()) {
					boolean value = checkboxes.get(topicId);

					try {
						if (value) {
							TopicBean topic = forumsModule.findTopicById(topicId);
							updateStatus(topic, TOPIC_LOCKED);
						}
					} catch (Exception e) {
						setWarnBundleMessage("ERR_INTERNAL");
						return "";
					}
				}
				setInfoBundleMessage("SUCC_TOPIC_LOCKED");

			} catch (Exception e) {
				handleException(e);
			}
			return "";
		} else {
			setWarnBundleMessage("None_selected");
			return "";
		}

	}

	/**
	 * UI Action for unlocking selected topic(s).
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String unlockTopic() {
		if (isAnyCheckboxSelected()) {
			try {
				for (int topicId : checkboxes.keySet()) {
					boolean value = checkboxes.get(topicId);

					try {
						if (value) {
							TopicBean topic = forumsModule.findTopicById(topicId);
							updateStatus(topic, TOPIC_UNLOCKED);
						}
					} catch (Exception e) {
						setWarnBundleMessage("ERR_INTERNAL");
						return "";
					}
				}
				setInfoBundleMessage("SUCC_TOPIC_UNLOCKED");

			} catch (Exception e) {
				handleException(e);
			}
			return "";
		} else {
			setWarnBundleMessage("None_selected");
			return "";
		}
	}

	/**
	 * Action checking if user selected at least one topic and forwards him to
	 * delete confirmation view.
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String deleteConfirm() {
		if (conversation.isTransient())
			conversation.begin();
		if (isAnyCheckboxSelected()) {
			return "confirmDelete";
		} else {
			setWarnBundleMessage("None_selected");
			return "";
		}
	}

	/**
	 * Action checking if user selected at least one topic and forwards him to move
	 * topic view.
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String moveConfirm() {
		if (conversation.isTransient())
			conversation.begin();
		if (isAnyCheckboxSelected()) {
			return "confirmMove";
		} else {
			setWarnBundleMessage("None_selected");
			return "";
		}
	}

	/**
	 * Method checks if user selected at least one topic from checkboxes or there is
	 * topic id sent in request.
	 * 
	 * @return true if almost one checkbox is selected
	 */
	public boolean isAnyCheckboxSelected() {
		// Looking for selected topicId's in checkboxes Map
		if (checkboxes != null && checkboxes.size() != 0) {
			Iterator<Integer> it = checkboxes.keySet().iterator();
			while (it.hasNext()) {
				if (checkboxes.get(it.next())) {
					return true;
				}
			}
		}

		// Looking for topicId sent in parameter
		String topicId = getParameter(p_topicId);

		if (topicId != null && topicId.trim().length() > 0) {
			checkboxes = new HashMap<Integer, Boolean>(1);
			checkboxes.put(parseInt(topicId), TRUE);
			return true;
		}
		return false;
	}

	public List<TopicBean> getAllSelectedTopics() {
		List<TopicBean> list = new LinkedList<TopicBean>();
		for (int topicId : checkboxes.keySet()) {
			if (checkboxes.get(topicId)) {
				try {
					list.add(forumsModule.findTopicById(topicId));
				} catch (Exception e) {
					handleException(e);
				}
			}
		}
		return list;
	}

	@PostConstruct
	public void execute() {

		try {
			// trying to get forumId from request parameter
			int forumId = -1;
			String f = getParameter(p_forumId);

			if (f != null && f.trim().length() > 0) {
				forumId = parseInt(f);
			}

			checkboxes = new HashMap<Integer, Boolean>();

			// grab the data to be displayed for this page
			if (forumId != -1) {

				// setup the business objects like the forum, topics etc that
				// will
				// be displayed
				forum = forumsModule.findForumById(forumId);

			} else {

				// trying to get forumId from topicId read from request
				String t = getParameter(p_topicId);

				if (t != null && t.trim().length() > 0) {

					TopicBean topic = forumsModule.findTopicById(parseInt(t));
					forum = topic.getForum();

				} else {
					String p = getParameter(p_postId);

					if (p != null && p.trim().length() > 0) {

						PostBean post = forumsModule.findPostById(parseInt(p));
						TopicBean topic = post.getTopic();
						forum = topic.getForum();
					}
				}
			}
			if (forum != null) {
				topics = forumsModule.findTopics(forum);
				topicsDataModel = new ListDataModel<TopicBean>(topics);
			}
		} catch (ModuleException e) {
			log.error(e);
		}
	}

	private void setWarnBundleMessage(String bundleKey) {
		String message = getBundleMessage("ResourceJSF", bundleKey);
		getCurrentInstance().addMessage("message", new FacesMessage(SEVERITY_WARN, message, "moderate"));
	}

	private void setInfoBundleMessage(String bundleKey) {
		String message = getBundleMessage("ResourceJSF", bundleKey);
		getCurrentInstance().addMessage("message", new FacesMessage(SEVERITY_INFO, message, "moderate"));
	}

	private void updateStatus(TopicBean topic, int status) {
		topic.setStatus(status);
		for (TopicBean topicIt : topics)
			if (topicIt.getId() == topic.getId())
				topicIt.setStatus(status);
		forumsModule.update(topic);
		viewTopic.setTopic(topic);
	}

	public int getLastPageNumber() {
		if (forum != null)
			return forum.getTopicCount() / userPreferences.getTopicsPerForum() + 1;
		else
			return 1;
	}
}
