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
import static it.vige.rubia.ui.JSFUtil.setMessage;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.Iterator;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.view.ViewForum;

/**
 * 
 * Created on May 16, 2006
 * 
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named
@RequestScoped
public class AdminController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1977660655542809904L;
	@EJB
	private ForumsModule forumsModule;
	/**
	 * user preference controller
	 */
	@Inject
	private PreferenceController userPreferences;

	@Inject
	private ViewForum viewForum;

	private static final int up = -15;
	private static final int down = 15;

	/**
	 * ui data associated with "Category" information
	 * 
	 */
	private String categoryName; // this is used by the create new
									// category usecase

	/**
	 * ui data associated with "Forum" information
	 * 
	 */
	private String forumName;
	private String forumDescription;

	/**
	 * ui data associated with selecting a category to perform a certain operation
	 */
	private int selectedCategory = -1;
	private int selectedForum = -1;

	/**
	 * flags telling in what mode should the view be displayed
	 */
	private boolean editCategoryMode;
	private boolean editForumMode;
	private boolean addCategoryMode;
	private boolean addForumMode;

	/**
	 * User Preferences
	 * 
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
	 * @return Returns the categoryName.
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName The categoryName to set.
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return Returns the forumDescription.
	 */
	public String getForumDescription() {
		return forumDescription;
	}

	/**
	 * @param forumDescription The forumDescription to set.
	 */
	public void setForumDescription(String forumDescription) {
		this.forumDescription = forumDescription;
	}

	/**
	 * @return Returns the forumName.
	 */
	public String getForumName() {
		return forumName;
	}

	/**
	 * @param forumName The forumName to set.
	 */
	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	/**
	 * @return the selected category
	 */
	public int getSelectedCategory() {
		return selectedCategory;
	}

	/**
	 * @param selectedCategory the selected category
	 */
	public void setSelectedCategory(int selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	/**
	 * @return the selected forum
	 */
	public int getSelectedForum() {
		return selectedForum;
	}

	/**
	 * @param selectedForum the selected forum
	 */
	public void setSelectedForum(int selectedForum) {
		this.selectedForum = selectedForum;
	}

	/**
	 * @return true if the category mode is edit
	 */
	public boolean isEditCategoryMode() {
		return editCategoryMode;
	}

	/**
	 * @return true if the forum mode is edit
	 */
	public boolean isEditForumMode() {
		return editForumMode;
	}

	/**
	 * @return true if the category mode is add
	 */
	public boolean isAddCategoryMode() {
		return addCategoryMode;
	}

	/**
	 * @return true if the forum mode is add
	 */
	public boolean isAddForumMode() {
		return addForumMode;
	}

	/**
	 * Cleanup related
	 */
	public void cleanup() {
		categoryName = null;
		forumName = null;
		forumDescription = null;
		selectedCategory = -1;
		selectedForum = -1;
		editCategoryMode = false;
		editForumMode = false;
		addCategoryMode = false;
		addForumMode = false;
	}

	/**
	 * Start the admin controller as service
	 */
	@PostConstruct
	public void startService() {
		try {
			// load the selected category if a categoryid is found
			// fetch the category to be edited/deleted
			int categoryId = -1;
			String cour = getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = parseInt(cour);
			}
			if (categoryId != -1) {
				CategoryBean category = null;
				try {
					category = forumsModule.findCategoryById(categoryId);
				} catch (ModuleException e) {
					// Category was deleted
				}
				if (category != null) {
					categoryName = category.getTitle();
					selectedCategory = category.getId().intValue();
				}
			}

			// load the selected forum is a forumid is found
			// fetch the forum to be edited/deleted
			int forumId = -1;
			String forumIdStr = getParameter(p_forumId);
			if (forumIdStr != null && forumIdStr.trim().length() > 0) {
				forumId = parseInt(forumIdStr);
			}
			if (forumId != -1) {
				ForumBean forum = null;
				try {
					forum = forumsModule.findForumById(forumId);
				} catch (ModuleException e) {
					// Forum was deleted
				}
				if (forum != null) {
					forumName = forum.getName();
					forumDescription = forum.getDescription();
					selectedCategory = forum.getCategory().getId().intValue();
					selectedForum = forum.getId().intValue();
				}
			}

			// Checking for editModes flags
			String editCatStr = getParameter(EDIT_CATEGORY);
			if (editCatStr != null && editCatStr.trim().length() > 0) {
				editCategoryMode = Boolean.valueOf(editCatStr).booleanValue();
			}

			String editForStr = getParameter(EDIT_FORUM);
			if (editForStr != null && editForStr.trim().length() > 0) {
				editForumMode = Boolean.valueOf(editForStr).booleanValue();
			}

			// Checking for addModes flags
			String addCatStr = getParameter(ADD_CATEGORY);
			if (addCatStr != null && addCatStr.trim().length() > 0) {
				addCategoryMode = Boolean.valueOf(addCatStr).booleanValue();
			}

			String addForStr = getParameter(ADD_FORUM);
			if (addForStr != null && addForStr.trim().length() > 0) {
				addForumMode = Boolean.valueOf(addForStr).booleanValue();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	// ----actions---------------------------------------------------------------------------------------------------------------------------
	/**
	 * adds a category
	 * 
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String addCategory() {
		String navState = null;
		boolean success = false;
		try {
			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			// add this new category to the forum instance
			ForumInstanceBean forumInstance = forumsModule.findForumInstanceById(forumInstanceId);
			CategoryBean categoryBean = new CategoryBean(categoryName);
			categoryBean.setForumInstance(forumInstance);
			forumsModule.createCategory(categoryBean);

			String start = getBundleMessage("ResourceJSF", "Category_created_0");
			String end = getBundleMessage("ResourceJSF", "Category_created_1");
			setMessage(FEEDBACK, start + " \"" + categoryName + "\" " + end);
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				// cleanup the state
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * edit category
	 * 
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String editCategory() {
		String navState = null;
		boolean success = false;
		try {
			int categoryId = -1;
			String cour = getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = parseInt(cour);
			}

			// grab the category from the module and set the title
			CategoryBean category = forumsModule.findCategoryById(categoryId);
			category.setTitle(categoryName);
			forumsModule.update(category);

			String start = getBundleMessage("ResourceJSF", "Category_updated_0");
			String end = getBundleMessage("ResourceJSF", "Category_updated_1");
			setMessage(FEEDBACK, start + " \"" + categoryName + "\" " + end);

			navState = "";
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				// cleanup the state
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String deleteCategory() {
		String navState = null;
		boolean success = false;
		try {
			int categoryId = -1;
			String cour = getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = parseInt(cour);
			}

			// grab the category from the module and set the title
			CategoryBean source = forumsModule.findCategoryById(categoryId);

			if (selectedCategory != -1) {

				CategoryBean target = forumsModule.findCategoryById(selectedCategory);

				// move all the forums from source category to the selected
				// target category
				forumsModule.addAllForums(source, target);

			}

			// remove the source category
			forumsModule.removeCategory(source.getId());

			String start = getBundleMessage("ResourceJSF", "Category_deleted_0");
			String end = getBundleMessage("ResourceJSF", "Category_deleted_1");
			setMessage(FEEDBACK, start + " \"" + categoryName + "\" " + end);

			navState = DELETE_CATEGORY;
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				// cleanup the state
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * adds a new forum
	 * 
	 * @return the navigation state of the application
	 * 
	 */
	public String addForum() {
		String navState = null;
		boolean success = false;
		try {
			// add this new forum to the category
			CategoryBean category = forumsModule.findCategoryById(selectedCategory);
			ForumBean forum = new ForumBean(forumName, forumDescription, category);
			forumsModule.createForum(forum);

			String start = getBundleMessage("ResourceJSF", "Forum_created_0");
			String end = getBundleMessage("ResourceJSF", "Forum_created_1");
			setMessage(FEEDBACK, start + " \"" + forumName + "\" " + end);

			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				// cleanup the state
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String editForum() {
		String navState = null;
		boolean success = false;
		try {
			int forumId = -1;
			String cour = getParameter(p_forumId);
			if (cour != null && cour.trim().length() > 0) {
				forumId = parseInt(cour);
			}

			// grab the forum from the module and set the proper information
			ForumBean forum = forumsModule.findForumById(forumId);
			CategoryBean selectedCategory = forumsModule.findCategoryById(this.selectedCategory);
			forum.setCategory(selectedCategory);
			forum.setName(forumName);
			forum.setDescription(forumDescription);
			forumsModule.update(forum);

			String start = getBundleMessage("ResourceJSF", "Forum_updated_0");
			String end = getBundleMessage("ResourceJSF", "Forum_updated_1");
			setMessage(FEEDBACK, start + " \"" + forumName + "\" " + end);

			navState = "";
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				// cleanup the state
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String deleteForum() {
		String navState = null;
		boolean success = false;
		try {
			int forumId = -1;
			String cour = getParameter(p_forumId);
			if (cour != null && cour.trim().length() > 0) {
				forumId = parseInt(cour);
			}
			ForumBean source = null;
			// move all the topics/posts of this forum to the specified target
			// forum
			if (selectedForum != -1) {
				source = forumsModule.findForumByIdFetchTopics(forumId);
				ForumBean target = forumsModule.findForumByIdFetchTopics(selectedForum);
				target.getTopics().addAll(source.getTopics());
				target.setPostCount(target.getPostCount() + source.getPostCount());
				target.setTopicCount(target.getTopicCount() + source.getTopicCount());
				forumsModule.update(target);
				for (TopicBean tp : target.getTopics()) {
					tp.setForum(target);
					forumsModule.update(tp);
				}

				// clear the source out before delete
				source.setTopics(new ArrayList<TopicBean>());
				forumsModule.update(source);
			} else {
				source = forumsModule.findForumById(forumId);
			}

			// means delete all topic/posts on this forum
			forumsModule.removeForum(source.getId());

			String start = getBundleMessage("ResourceJSF", "Forum_deleted_0");
			String end = getBundleMessage("ResourceJSF", "Forum_deleted_1");
			setMessage(FEEDBACK, start + " \"" + forumName + "\" " + end);

			navState = DELETE_FORUM;
			success = true;
		} catch (Exception e) {
			handleException(e);
		} finally {
			if (success) {
				// cleanup the state
				cleanup();
			}
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String moveCategoryUp() {
		String navState = null;
		try {
			// get the categoryId where this forum should be added
			int categoryId = -1;
			String cour = getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = parseInt(cour);
			}

			CategoryBean category = forumsModule.findCategoryById(categoryId);
			category.setOrder(category.getOrder() + up);
			forumsModule.update(category);

			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			Iterator<CategoryBean> categories = forumsModule.findCategories(forumInstanceId).iterator();

			for (int index = 10; categories.hasNext(); index += 10) {
				category = categories.next();
				category.setOrder(index);
				forumsModule.update(category);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String moveCategoryDown() {
		String navState = null;
		try {
			// get the categoryId where this forum should be added
			int categoryId = -1;
			String cour = getParameter(p_categoryId);
			if (cour != null && cour.trim().length() > 0) {
				categoryId = parseInt(cour);
			}

			CategoryBean category = forumsModule.findCategoryById(categoryId);
			category.setOrder(category.getOrder() + down);
			forumsModule.update(category);

			// get the forumInstanceId where this forum should be added
			int forumInstanceId = userPreferences.getForumInstanceId();

			Iterator<CategoryBean> categories = forumsModule.findCategories(forumInstanceId).iterator();

			for (int index = 10; categories.hasNext(); index += 10) {
				category = categories.next();
				category.setOrder(index);
				forumsModule.update(category);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String moveForumUp() {
		String navState = null;
		try {
			// get the categoryId where this forum should be added
			int forumId = -1;
			String cour = getParameter(p_forumId);
			if (cour != null && cour.trim().length() > 0) {
				forumId = parseInt(cour);
			}

			ForumBean forum = forumsModule.findForumById(forumId);
			forum.setOrder(forum.getOrder() + up);
			forumsModule.update(forum);
			Iterator<ForumBean> forums = forumsModule.findForumsByCategory(forum.getCategory()).iterator();
			for (int index = 10; forums.hasNext(); index += 10) {
				forum = forums.next();
				forum.setOrder(index);
				forumsModule.update(forum);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 *
	 */
	public String moveForumDown() {
		String navState = null;
		try {
			// get the categoryId where this forum should be added
			int forumId = -1;
			String cour = getParameter(p_forumId);
			if (cour != null && cour.trim().length() > 0) {
				forumId = parseInt(cour);
			}

			ForumBean forum = forumsModule.findForumById(forumId);
			forum.setOrder(forum.getOrder() + down);
			forumsModule.update(forum);
			Iterator<ForumBean> forums = forumsModule.findForumsByCategory(forum.getCategory()).iterator();
			for (int index = 10; forums.hasNext(); index += 10) {
				forum = forums.next();
				forum.setOrder(index);
				forumsModule.update(forum);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * @return the navigation state of the application
	 */
	public String lockForum() {
		try {
			// get the forumId where this forum should be added
			int forumId = -1;
			String cour = getParameter(p_forumId);
			if (cour != null && cour.trim().length() > 0) {
				forumId = parseInt(cour);
			}

			ForumBean forum = forumsModule.findForumById(forumId);
			forum.setStatus(FORUM_LOCKED);
			forumsModule.update(forum);
			String message = getBundleMessage("ResourceJSF", "Forum_locked");
			setMessage(FEEDBACK, message);
			viewForum.setForum(forum);
		} catch (Exception e) {
			handleException(e);
		} finally {
			// cleanup the state
			cleanup();
		}
		return null;
	}

	/**
	 * @return the navigation state of the application
	 */
	public String unlockForum() {
		try {
			// get the forumId where this forum should be added
			int forumId = -1;
			String cour = getParameter(p_forumId);
			if (cour != null && cour.trim().length() > 0) {
				forumId = parseInt(cour);
			}

			ForumBean forum = forumsModule.findForumById(forumId);
			forum.setStatus(FORUM_UNLOCKED);
			forumsModule.update(forum);
			String message = getBundleMessage("ResourceJSF", "Forum_unlocked");
			setMessage(FEEDBACK, message);
			viewForum.setForum(forum);
		} catch (Exception e) {
			handleException(e);
		} finally {
			// cleanup the state
			cleanup();
		}
		return null;
	}

	/**
	 * @return the action name of the operation
	 */
	public String cancel() {
		cleanup();
		return "cancel";
	}

	public boolean isLocked(int status) {
		return status == FORUM_LOCKED;
	}
}
