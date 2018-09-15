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
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.JSFUtil.getBundleMessage;
import static java.lang.Integer.parseInt;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.jboss.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.ui.BaseController;

/**
 * SplitTopic is a bean which keeps data and has actions needed to achieve
 * spltting topic into two separate topics.
 * 
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named
@RequestScoped
public class SplitTopic extends BaseController {

	private static final long serialVersionUID = -1573920430742051428L;
	private static Logger log = getLogger(SplitTopic.class);

	@EJB
	private ForumsModule forumsModule;

	@EJB
	private UserModule userModule;

	/** Title for newly created topic */
	private String newTopicTitle;

	/** Map containing Integer:Boolean pairs with TopicId:IsSelected states */
	private Map<Integer, Boolean> checkboxes;

	/** Topic to split */
	private TopicBean topic;

	private List<PostBean> posts;

	// ---------- Getters And Setters for bean's attributes --------------------

	public List<PostBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostBean> posts) {
		this.posts = posts;
	}

	public void setNewTopicTitle(String newTopicTitle) {
		this.newTopicTitle = newTopicTitle;
	}

	public String getNewTopicTitle() {
		return newTopicTitle;
	}

	public void setCheckboxes(Map<Integer, Boolean> checkboxes) {
		this.checkboxes = checkboxes;
	}

	public Map<Integer, Boolean> getCheckboxes() {
		return checkboxes;
	}

	public TopicBean getTopic() {
		return topic;
	}

	// ---------- UI Actions supported by this bean ----------------------------

	/**
	 * This user interface action is spliting topic after post selected by user.
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String splitAfter() {

		// Checking whether topic has only one post, so it can't be splitted
		if (posts.size() == 1) {
			setWarnBundleMessage("ERR_SPLIT_ONE_POST_TOPIC");
			return "";
		}

		// Removing all not slected posts
		Iterator<Integer> selectIt = checkboxes.keySet().iterator();
		while (selectIt.hasNext()) {
			Boolean postFlag = checkboxes.get(selectIt.next());
			if (!postFlag.booleanValue()) {
				selectIt.remove();
			}
		}

		// Checking if user selected anything.
		if (checkboxes.size() == 0) {
			setWarnBundleMessage("ERR_NO_POST_SELECTED");
			return "";
		}

		// User can't select more than one post for this action.
		if (checkboxes.size() != 1) {
			setWarnBundleMessage("Too_many_error");
			return "";
		}

		// check if user selected first post
		try {
			posts = forumsModule.findPostsByTopicId(topic);
			if (posts.get(0).getId().equals(checkboxes.keySet().iterator().next())) {
				setWarnBundleMessage("ERR_SPLIT_ALL");
				return "";
			}
		} catch (ModuleException e1) {
			setWarnBundleMessage("ERR_SPLIT_ALL");
			return "";
		} catch (Exception e1) {
			setWarnBundleMessage("ERR_SPLIT_ALL");
			return "";
		}

		// Trying to get destination forum for new topic.
		String toForumId = getParameter(p_forum_to_id);
		if (toForumId == null || toForumId.trim().compareToIgnoreCase("-1") == 0 || toForumId.trim().length() == 0) {
			setWarnBundleMessage("ERR_DEST_FORUM");
			return "";
		}

		// Checking if user gave subject for new topic.
		if (newTopicTitle == null || newTopicTitle.trim().compareToIgnoreCase("-1") == 0
				|| newTopicTitle.trim().length() == 0) {
			setWarnBundleMessage("ERR_NO_SUBJECT_GIVEN");
			return "";
		}

		try {

			ForumBean destForum = forumsModule.findForumById(parseInt(toForumId));

			// Creating new topic in destination forum.
			TopicBean oldTopic = new TopicBean(destForum, newTopicTitle, null, topic.getType(), null);
			oldTopic.setPoster(new PosterBean(getUser(userModule).getId().toString()));
			TopicBean newTopic = forumsModule.createTopicWithPoster(oldTopic);

			// Getting post id after which the topic must be splitted.
			Integer selectedPostId = (Integer) checkboxes.keySet().iterator().next();

			// Searching for the split pointing post in topic.
			Iterator<PostBean> it = posts.iterator();
			PostBean tempPost = null;
			while (it.hasNext()) {
				tempPost = it.next();
				// searching for post to split after
				if (tempPost.getId().equals(selectedPostId)) {
					break;
				}
			}
			List<PostBean> postsToRemove = new ArrayList<PostBean>();

			// Adding splitting post and all which are after him to new topic.
			if (tempPost != null) {
				tempPost.setTopic(newTopic);
				forumsModule.update(tempPost);
				postsToRemove.add(tempPost);
			}
			while (it.hasNext()) {
				PostBean post = it.next();
				post.setTopic(newTopic);
				forumsModule.update(post);
				postsToRemove.add(post);
			}
			newTopic = forumsModule.findTopicById(newTopic.getId());
			List<PostBean> postsNewTopic = forumsModule.findPostsByTopicId(newTopic);
			newTopic.setReplies(postsNewTopic.size() - 1);
			newTopic.setLastPostDate(postsNewTopic.get(postsNewTopic.size() - 1).getCreateDate());

			ForumBean fromForum = topic.getForum();
			topic.setReplies(topic.getReplies() - newTopic.getReplies() - 1);
			fromForum.setPostCount(fromForum.getPostCount() - newTopic.getReplies() - 1);
			posts.removeAll(postsToRemove);
			topic.setLastPostDate(posts.get(posts.size() - 1).getCreateDate());

			destForum.addTopicSize();
			destForum.setPostCount(destForum.getPostCount() + newTopic.getReplies() + 1);
			forumsModule.update(newTopic);
			forumsModule.update(topic);
			forumsModule.update(fromForum);
			forumsModule.update(destForum);

		} catch (Exception e) {
			log.error(e);
			setWarnBundleMessage("ERR_INTERNAL");
			return "";
		}

		// Setting message that everything went smooth.
		setInfoBundleMessage("SUCC_TOPIC_SPLITTED");
		return "";
	}

	/**
	 * This user interface action is spliting topic bh=y moving all selected by user
	 * posts into newly created topic.
	 * 
	 * @return the name of the operation
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String splitPosts() {

		// Checking whether topic has only one post, so it can't be splitted
		if (posts.size() == 1) {
			setWarnBundleMessage("ERR_SPLIT_ONE_POST_TOPIC");
			return "";
		}

		// Removing all not slected posts
		Iterator<Integer> selectIt = checkboxes.keySet().iterator();
		while (selectIt.hasNext()) {
			Boolean postFlag = checkboxes.get(selectIt.next());
			if (!postFlag.booleanValue()) {
				selectIt.remove();
			}
		}

		// Checking if user selected anything.
		if (checkboxes.size() == 0) {
			setWarnBundleMessage("ERR_NO_POST_SELECTED");
			return "";
		}

		// Checking if user didn't select all posts.
		if (checkboxes.size() == posts.size()) {
			setWarnBundleMessage("ERR_SPLIT_ALL");
			return "";
		}

		// Trying to get destination forum for new topic.
		String toForumId = getParameter(p_forum_to_id);
		if (toForumId == null || toForumId.trim().compareToIgnoreCase("-1") == 0 || toForumId.trim().length() == 0) {
			setWarnBundleMessage("ERR_DEST_FORUM");
			return "";
		}

		// Checking if user gave subject for new topic.
		if (newTopicTitle == null || newTopicTitle.trim().compareToIgnoreCase("-1") == 0
				|| newTopicTitle.trim().length() == 0) {
			setWarnBundleMessage("ERR_NO_SUBJECT_GIVEN");
			return "";
		}
		try {

			ForumBean destForum = forumsModule.findForumById(parseInt(toForumId));

			// Creating new topic in selected destination forum.
			TopicBean oldTopic = new TopicBean(destForum, newTopicTitle, null, topic.getType(), null);
			oldTopic.setPoster(new PosterBean(getUser(userModule).getId().toString()));
			TopicBean newTopic = forumsModule.createTopicWithPoster(oldTopic);

			// Moving all selected posts to new topic.
			selectIt = checkboxes.keySet().iterator();
			PostBean movedPost = null;
			List<PostBean> postsToRemove = new ArrayList<PostBean>();
			while (selectIt.hasNext()) {
				movedPost = forumsModule.findPostById(selectIt.next());
				movedPost.setTopic(newTopic);
				forumsModule.update(movedPost);
				postsToRemove.add(movedPost);
			}

			ForumBean fromForum = topic.getForum();
			topic.setReplies(topic.getReplies() - checkboxes.size());
			fromForum.setPostCount(fromForum.getPostCount() - checkboxes.size());
			posts.removeAll(postsToRemove);
			topic.setLastPostDate(posts.get(posts.size() - 1).getCreateDate());

			newTopic.setReplies(checkboxes.size() - 1);
			List<PostBean> postsNewTopic = forumsModule.findPostsByTopicId(newTopic);
			newTopic.setLastPostDate(postsNewTopic.get(postsNewTopic.size() - 1).getCreateDate());

			destForum.addTopicSize();
			destForum.setPostCount(destForum.getPostCount() + newTopic.getReplies() + 1);
			forumsModule.update(topic);
			forumsModule.update(newTopic);
			forumsModule.update(fromForum);
			forumsModule.update(destForum);
		} catch (Exception e) {
			log.error(e);
			setWarnBundleMessage("ERR_INTERNAL");
			return "";
		}

		// Setting message that everything went smooth.
		setInfoBundleMessage("SUCC_TOPIC_SPLITTED");
		return "";
	}

	/**
	 * Bean's attributes initialization.
	 */
	@PostConstruct
	public void execute() {
		// parse input data
		int topicId = -1;
		String t = getParameter(p_topicId);
		if (t != null && t.trim().length() > 0) {
			topicId = parseInt(t);
		}

		// process the topic information
		try {
			if (topicId != -1) {
				topic = (TopicBean) forumsModule.findTopicById(topicId);
				posts = forumsModule.findPostsByTopicId(topic);
			}
			List<PostBean> posts = forumsModule.findPostsByTopicId(topic);
			if (checkboxes == null || checkboxes.size() != posts.size()) {
				checkboxes = new HashMap<Integer, Boolean>();
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

}
