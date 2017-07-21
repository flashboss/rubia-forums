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
import static org.jboss.logging.Logger.getLogger;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;
import it.vige.rubia.ui.BaseController;

/*
 * Created on May 3, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
@Named
@RequestScoped
public class DeletePost extends BaseController {

	private static final long serialVersionUID = 8446392847381813917L;
	private static Logger log = getLogger(DeletePost.class);

	@EJB
	private ForumsModule forumsModule;

	private int postId;
	private Post post;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	/**
	 * 
	 */
	@PostConstruct
	public void execute() {
		// get the post id
		int postId = -1;
		String p = getParameter(p_postId);
		if (p != null && p.trim().length() > 0) {
			postId = Integer.parseInt(p);
		}
		this.postId = postId;
		if (postId != -1) {
			try {
				post = (Post) forumsModule.findPostById(postId);
			} catch (ModuleException e) {
				log.error(e);
			}
		}
	}

	// actions---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * @return the navigation state of the application
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String confirmDelete() {
		String navState = null;
		try {

			// setup the business objects/data of interest
			Post post = forumsModule.findPostById(postId);
			Topic topic = post.getTopic();

			// make sure this topic is not locked
			if (topic.getStatus() == TOPIC_LOCKED) {
				// should not allow posting a reply since the topic is locked
				throw new Exception(getBundleMessage(BUNDLE_NAME, TOPIC_LOCKED_ERR_KEY));
			}

			boolean isFirstPost = false;
			boolean isLastPost = false;
			List<Post> posts = forumsModule.findPostsByTopicId(topic);
			if (posts.get(0).getId() == post.getId().intValue()) {
				isFirstPost = true;
			}
			if (posts.get(posts.size() - 1).getId().intValue() == post.getId().intValue()) {
				isLastPost = true;
			}

			// now perform the actual delete operation.........................
			if (isLastPost && isFirstPost) {
				// cascade delete will take care of removing
				// the post
				// the watches
				// the poll
				// the links
				forumsModule.removeTopic(post.getTopic().getId());

				// set the proper navigation state
				navState = TOPIC_DELETED;
			} else {
				forumsModule.removePost(post.getId(), isLastPost);

				// set the proper navigation state
				navState = SUCCESS;
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}
}
