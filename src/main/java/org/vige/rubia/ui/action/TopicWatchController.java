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
package org.vige.rubia.ui.action;

import static java.lang.Boolean.valueOf;
import static java.lang.Integer.parseInt;
import static org.vige.rubia.ui.JSFUtil.getRequestParameter;
import static org.vige.rubia.ui.JSFUtil.handleException;
import static org.vige.rubia.ui.PortalUtil.getPoster;
import static org.vige.rubia.ui.PortalUtil.getUser;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.vige.rubia.ForumsModule;
import org.vige.rubia.auth.AuthorizationListener;
import org.vige.rubia.auth.SecureActionForum;
import org.vige.rubia.auth.UserModule;
import org.vige.rubia.model.Topic;
import org.vige.rubia.model.TopicWatch;
import org.vige.rubia.ui.BaseController;

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
     * 
     */
	public int getWatchType() {
		return watchType;
	}

	/**
     * 
     */
	public void setWatchType(int watchType) {
		this.watchType = watchType;
	}

	/**
     * 
     */
	public String dummyAction() {
		return "success";
	}

	/**
     * 
     */
	public int getTopicId() {
		return topicId;
	}

	/**
     * 
     */
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	/**
     * 
     */
	public Topic getTopic() {
		return topic;
	}

	/**
     * 
     */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
     * 
     */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
     * 
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
     * 
     *
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
     * 
     *
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
     * 
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
