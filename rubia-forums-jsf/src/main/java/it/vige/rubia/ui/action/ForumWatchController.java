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

import static it.vige.rubia.PortalUtil.getUser;
import static it.vige.rubia.ui.JSFUtil.getPoster;
import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Boolean.valueOf;
import static java.lang.Integer.parseInt;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.ForumWatch;
import it.vige.rubia.model.Watch;
import it.vige.rubia.ui.BaseController;

/**
 * This controller is used for activating/deactivating/managing forum watches
 * 
 * Created on Jul 7, 2006
 * 
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("forumWatch")
public class ForumWatchController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 701065663010513552L;

	@EJB
	private ForumsModule forumsModule;

	@EJB
	private UserModule userModule;

	// ui data supporting the AddForumWatch widget
	private int selectedForum = -1;
	private int watchMode = -1;
	private Forum forum = null;

	// flag informing UI that notification edit mode is turned on
	private boolean editMode;

	/**
	 * @return Returns the selectedForum.
	 */
	public int getSelectedForum() {
		return selectedForum;
	}

	/**
	 * @param selectedForum
	 *            The selectedForum to set.
	 */
	public void setSelectedForum(int selectedForum) {
		this.selectedForum = selectedForum;
	}

	/**
	 * 
	 */
	public Forum getForum() {
		return forum;
	}

	/**
	 * 
	 */
	public void setForum(Forum forum) {
		this.forum = forum;
	}

	/**
	 * @return Returns the watchMode.
	 */
	public int getWatchMode() {
		return watchMode;
	}

	/**
	 * @param watchMode
	 *            The watchMode to set.
	 */
	public void setWatchMode(int watchMode) {
		this.watchMode = watchMode;
	}

	/**
	 * 
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * 
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 *
	 */
	@PostConstruct
	public void execute() {
		try {
			// populate the already setup form watches
			// this.reloadWatches();

			try {
				String f = getRequestParameter(p_forumId);
				if (f != null && f.trim().length() > 0) {
					selectedForum = parseInt(f);
				}
			} catch (NumberFormatException e) {
				handleException(e);
			}

			if (selectedForum != -1) {
				try {
					forum = forumsModule.findForumById(selectedForum);
				} catch (Exception e) {
					handleException(e);
					forum = null;
				}
			}

			if (selectedForum != -1) {
				try {
					forum = forumsModule.findForumById(selectedForum);
				} catch (Exception e) {
					handleException(e);
				}
			}

			String edit = getRequestParameter(EDIT_WATCH);
			if (edit != null && edit.trim().length() > 0) {
				editMode = valueOf(edit).booleanValue();
			} else {
				editMode = false;
			}

		} catch (Exception e) {
			handleException(e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 *
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String activateWatch() {
		String navState = null;
		try {
			int forumId = selectedForum;

			try {
				String wt = getRequestParameter(p_notified_watch_type);
				if (wt != null && wt.trim().length() > 0) {
					watchMode = parseInt(wt);
				} else {
					watchMode = -1;
				}
			} catch (NumberFormatException e) {
				handleException(e);
				watchMode = -1;
			}

			if (forumId == -1 || watchMode == -1) {
				return null;
			}

			// make sure a watch for this forum is not already issued for this
			// user
			boolean isDuplicate = isDuplicateWatch(forumId);
			if (isDuplicate) {
				return "success";
			}

			// get the forum that must be activated for watching
			Forum forum = forumsModule.findForumById(forumId);

			// activate the watch for the selected forum
			forumsModule.createWatch(getPoster(userModule, forumsModule), forum, watchMode);
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
			int watchId = -1;

			String w = getRequestParameter(p_watchId);
			if (w != null && w.trim().length() > 0) {
				watchId = parseInt(w);
			}

			if (watchId == -1) {
				String f = getRequestParameter(p_forumId);
				if (f != null && f.trim().length() > 0) {
					int forumId = parseInt(f);
					ForumWatch forumWatch = forumsModule.findForumWatchByUserAndForum(getUser(userModule), forumId);
					watchId = forumWatch != null ? forumWatch.getId().intValue() : -1;
				}
			}

			if (watchId != -1) {
				forumsModule.removeWatch(forumsModule.findForumWatchById(watchId));
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * 
	 */
	public String updateNotificationType() {

		try {
			String wt = getRequestParameter(p_notified_watch_type);
			if (wt != null && wt.trim().length() > 0) {
				watchMode = parseInt(wt);
			} else {
				watchMode = -1;
			}
		} catch (NumberFormatException e) {
			handleException(e);
			watchMode = -1;
		}

		try {
			ForumWatch forumWatch = forumsModule.findForumWatchByUserAndForum(getUser(userModule), selectedForum);
			forumWatch.setMode(watchMode);
			forumsModule.update(forumWatch);
		} catch (Exception e) {
			handleException(e);
		}
		selectedForum = -1;
		return "";
	}

	/**
	 * When user cancels creating forum notification then this action is
	 * executed.
	 */
	public String cancel() {
		return "cancel";
	}

	/**
	 * 
	 *
	 */
	public String updateWatch() {
		return null;
	}

	// --------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 *
	 */
	private boolean isDuplicateWatch(int forumId) {
		try {
			ForumWatch watch = forumsModule.findForumWatchByUserAndForum(getUser(userModule), selectedForum);
			return watch != null;
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	/**
	 * 
	 */
	public String dummyAction() {
		return null;
	}

	/**
	 * 
	 * 
	 */
	public String getWatchingForum() {
		String watchId = null;

		try {
			User user = getUser(userModule);
			if (user == null) {
				return null;
			}

			Watch watch = forumsModule.findForumWatchByUserAndForum(user, forum.getId());

			if (watch != null) {
				return watch.getId().toString();
			}

		} catch (Exception e) {
			handleException(e);
		}

		return watchId;
	}
}
