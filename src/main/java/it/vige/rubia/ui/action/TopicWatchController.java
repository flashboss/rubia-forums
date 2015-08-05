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

import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static it.vige.rubia.ui.PortalUtil.getPoster;
import static it.vige.rubia.ui.PortalUtil.getUser;
import static java.lang.Boolean.valueOf;
import static java.lang.Integer.parseInt;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.model.Topic;
import it.vige.rubia.model.TopicWatch;
import it.vige.rubia.ui.BaseController;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

/**
 * This controller is used for activating/deactivating topic watches
 * 
 * Created on Jul 7, 2006
 * 
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("topicWatch")
public class TopicWatchController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1444693287363025185L;

	@Inject
	private ForumsModule forumsModule;

	@Inject
	private UserModule userModule;

	private int topicId;
	private int watchType;
	private boolean editMode;
	private Topic topic;

	/**
	 * @return the current watch type
	 */
	public int getWatchType() {
		return watchType;
	}

	/**
	 * @param watchType
	 *            the current watch type
	 */
	public void setWatchType(int watchType) {
		this.watchType = watchType;
	}

	/**
	 * @return the name of the operation
	 */
	public String dummyAction() {
		return "success";
	}

	/**
	 * @return the current topic id
	 */
	public int getTopicId() {
		return topicId;
	}

	/**
	 * @param topicId
	 *            the current topic id
	 */
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	/**
	 * @return the current topic
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * @param topic
	 *            the current topic
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * @param editMode
	 *            the current edit mode
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * @return the current edit mode
	 */
	public boolean getEditMode() {
		return editMode;
	}

	/**
	 * 
	 *
	 */
	@PostConstruct
	public void startService() {
		try {
			String t = getRequestParameter(p_topicId);
			if (t != null && t.trim().length() > 0) {
				topicId = parseInt(t);
			} else {
				topicId = -1;
			}
		} catch (NumberFormatException e) {
			handleException(e);
			topicId = -1;
		}

		if (topicId != -1) {
			try {
				topic = forumsModule.findTopicById(topicId);
			} catch (Exception e) {
				handleException(e);
				topic = null;
			}
		}

		try {
			String wt = getRequestParameter(p_notified_watch_type);
			if (wt != null && wt.trim().length() > 0) {
				watchType = parseInt(wt);
			} else {
				watchType = -1;
			}
		} catch (NumberFormatException e) {
			handleException(e);
			watchType = -1;
		}

		String edit = getRequestParameter(EDIT_WATCH);
		if (edit != null && edit.trim().length() > 0) {
			editMode = valueOf(edit).booleanValue();
		} else {
			editMode = false;
		}

	}

	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String activateWatch() {
		String navState = null;

		try {

			// checking for needed parameters
			if (topicId == -1 || watchType == -1) {
				return navState;
			}

			// make sure a watch for this topic is not already issued for this
			// user
			boolean isDuplicate = isWatched();
			if (isDuplicate) {
				return navState;
			}

			// get the topic that must be activated for watching
			Topic topic = forumsModule.findTopicById(topicId);

			// activate the watch for the selected topic
			forumsModule.createWatch(getPoster(userModule, forumsModule), topic, watchType);
			navState = "success";
		} catch (Exception e) {
			handleException(e);
		}

		return navState;
	}

	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String deActivateWatch() {
		String navState = null;

		try {

			TopicWatch watch = forumsModule.findTopicWatchByUserAndTopic(getUser(userModule), topicId);
			if (watch != null)
				forumsModule.removeWatch(watch);

		} catch (Exception e) {
			handleException(e);
		}

		return navState;
	}

	/**
	 * @return the navigation state of the application
	 */
	public String updateNotificationType() {
		String navState = null;
		if (watchType == -1 || topicId == -1) {
			return navState;
		}

		try {
			TopicWatch topicWatch = forumsModule.findTopicWatchByUserAndTopic(getUser(userModule), topicId);
			topicWatch.setMode(watchType);
			forumsModule.update(topicWatch);
			navState = "success";
		} catch (Exception e) {
			handleException(e);
		}
		topicId = -1;
		return navState;
	}

	/**
	 * When user cancels creating topic notification then this action is
	 * executed.
	 *
	 * @return the name of the operation
	 */
	public String cancel() {
		return "cancel";
	}

	public boolean isWatched() {
		TopicWatch topicWatch = null;

		try {

			topicWatch = forumsModule.findTopicWatchByUserAndTopic(getUser(userModule), topicId);

		} catch (Exception e) {
			handleException(e);
		}

		return topicWatch != null;
	}

}
