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
package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import it.vige.rubia.Constants;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.User;
import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.CategoryRequestBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.ForumRequestBean;
import it.vige.rubia.dto.ForumWatchBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.TopicRequestBean;
import it.vige.rubia.dto.TopicWatchBean;
import it.vige.rubia.dto.WatchBean;

@Path("/forums/")
public class RestForumsModule implements Constants {

	@EJB
	private ForumsModule forumsModule;

	@GET
	@Path("getGuestUserName")
	public String getGuestUserName() {
		return forumsModule.getGuestUserName();
	}

	@GET
	@Path("setGuestUserName/{guestUserName}")
	public void setGuestUserName(@PathParam("guestUserName") String guestUserName) {
		forumsModule.setGuestUserName(guestUserName);
	}

	@GET
	@Path("getFromAddress")
	public String getFromAddress() {
		return forumsModule.getFromAddress();
	}

	@GET
	@Path("setFromAddress/{fromAddress}")
	public void setFromAddress(@PathParam("fromAddress") String fromAddress) {
		forumsModule.setFromAddress(fromAddress);
	}

	@GET
	@Path("findTopics/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopics(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopics(indexInstance);
	}

	@POST
	@Path("findTopicsDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicsDesc(TopicRequestBean topicRequestBean) throws ModuleException {
		return forumsModule.findTopicsDesc(topicRequestBean);
	}

	@POST
	@Path("findTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopics(ForumBean forum) throws ModuleException {
		return forumsModule.findTopics(forum);
	}

	@GET
	@Path("findTopicsHot/{replies}/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicsHot(@PathParam("replies") int replies, @PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicsHot(replies, limit, indexInstance);
	}

	@GET
	@Path("findTopicsByLatestPosts/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicsByLatestPosts(@PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicsByLatestPosts(limit, indexInstance);
	}

	@GET
	@Path("findTopicsHottest/{after}/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicsHottest(@PathParam("after") String after, @PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException, ParseException {
		Date date = restDateFormat.parse(after);
		return forumsModule.findTopicsHottest(date, limit, indexInstance);
	}

	@GET
	@Path("findTopicsMostViewed/{after}/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicsMostViewed(@PathParam("after") String after, @PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException, ParseException {
		Date date = restDateFormat.parse(after);
		return forumsModule.findTopicsMostViewed(date, limit, indexInstance);
	}

	@GET
	@Path("findForumById/{id}")
	@Produces(APPLICATION_JSON)
	public ForumBean findForumById(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.findForumById(id);
	}

	@GET
	@Path("findForumByIdFetchTopics/{id}")
	@Produces(APPLICATION_JSON)
	public ForumBean findForumByIdFetchTopics(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.findForumByIdFetchTopics(id);
	}

	@POST
	@Path("createForum")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public ForumBean createForum(ForumBean forum) throws ModuleException {
		return forumsModule.createForum(forum);
	}

	@GET
	@Path("findPostById/{id}")
	@Produces(APPLICATION_JSON)
	public PostBean findPostById(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.findPostById(id);
	}

	@GET
	@Path("findPosterByUserId/{userId}")
	@Produces(APPLICATION_JSON)
	public PosterBean findPosterByUserId(@PathParam("userId") String userID) throws ModuleException {
		return forumsModule.findPosterByUserId(userID);
	}

	@GET
	@Path("removePoster/{id}")
	@Produces(APPLICATION_JSON)
	public PosterBean removePoster(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.removePoster(id);
	}

	@GET
	@Path("findCategories/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<CategoryBean> findCategories(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findCategories(indexInstance);
	}

	@GET
	@Path("findCategoriesFetchForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<CategoryBean> findCategoriesFetchForums(@PathParam("indexInstance") Integer indexInstance)
			throws ModuleException {
		return forumsModule.findCategoriesFetchForums(indexInstance);
	}

	@GET
	@Path("findForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<ForumBean> findForums(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findForums(indexInstance);
	}

	@POST
	@Path("findForumsByCategory")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<ForumBean> findForumsByCategory(CategoryBean category) throws ModuleException {
		return forumsModule.findForumsByCategory(category);
	}

	@POST
	@Path("createTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean createTopic(TopicBean topic) throws ModuleException {
		return forumsModule.createTopic(topic);
	}

	@POST
	@Path("createTopicWithPoster")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public TopicBean createTopicWithPoster(TopicBean topic) throws ModuleException {
		return forumsModule.createTopicWithPoster(topic);
	}

	@POST
	@Path("createPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean createPost(PostBean post) throws ModuleException {
		return forumsModule.createPost(post);
	}

	@POST
	@Path("addPollToTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PollBean addPollToTopic(TopicBean topic, PollBean poll) throws ModuleException {
		return forumsModule.addPollToTopic(topic, poll);
	}

	@POST
	@Path("createCategory")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public CategoryBean createCategory(CategoryBean category) throws ModuleException {
		return forumsModule.createCategory(category);
	}

	@GET
	@Path("removeCategory/{categoryId}")
	public void removeCategory(@PathParam("categoryId") int categoryId) throws ModuleException {
		forumsModule.removeCategory(categoryId);
	}

	@GET
	@Path("createForumInstance/{indexInstance}/{name}")
	@Produces(APPLICATION_JSON)
	public ForumInstanceBean createForumInstance(@PathParam("indexInstance") Integer indexInstance,
			@PathParam("name") String name) throws ModuleException {
		return forumsModule.createForumInstance(indexInstance, name);
	}

	@GET
	@Path("removeForumInstance/{indexInstance}")
	public void removeForumInstance(@PathParam("indexInstance") int indexInstance) throws ModuleException {
		forumsModule.removeForumInstance(indexInstance);
	}

	@GET
	@Path("removeForum/{forumId}")
	public void removeForum(@PathParam("forumId") int forumId) throws ModuleException {
		forumsModule.removeForum(forumId);
	}

	@GET
	@Path("removePost/{postId}/{isLastPost}")
	public void removePost(@PathParam("postId") int postId, @PathParam("isLastPost") boolean isLastPost)
			throws ModuleException {
		forumsModule.removePost(postId, isLastPost);
	}

	@POST
	@Path("removePollInTopic")
	@Consumes(APPLICATION_JSON)
	public void removePollInTopic(TopicBean topic) throws ModuleException {
		forumsModule.removePollInTopic(topic);
	}

	@GET
	@Path("removeTopic/{topicId}")
	public void removeTopic(@PathParam("topicId") int topicId) throws ModuleException {
		forumsModule.removeTopic(topicId);
	}

	@GET
	@Path("findCategoryById/{categoryID}")
	@Produces(APPLICATION_JSON)
	public CategoryBean findCategoryById(@PathParam("categoryID") Integer categoryID) throws ModuleException {
		return forumsModule.findCategoryById(categoryID);
	}

	@GET
	@Path("findCategoryByIdFetchForums/{categoryID}")
	@Produces(APPLICATION_JSON)
	public CategoryBean findCategoryByIdFetchForums(@PathParam("categoryID") Integer categoryID)
			throws ModuleException {
		return forumsModule.findCategoryByIdFetchForums(categoryID);
	}

	@POST
	@Path("addAllForums")
	@Consumes(APPLICATION_JSON)
	public void addAllForums(CategoryBean... category) throws ModuleException {
		forumsModule.addAllForums(category);
	}

	@GET
	@Path("findTopicById/{topicID}")
	@Produces(APPLICATION_JSON)
	public TopicBean findTopicById(@PathParam("topicID") Integer topicID) throws ModuleException {
		return forumsModule.findTopicById(topicID);
	}

	@POST
	@Path("findPostsByTopicId")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByTopicId(TopicBean topic) throws ModuleException {
		return forumsModule.findPostsByTopicId(topic);
	}

	@POST
	@Path("findPostsByIdsAscFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByIdsAscFetchAttachmentsAndPosters(List<Integer> posts) throws ModuleException {
		return forumsModule.findPostsByIdsAscFetchAttachmentsAndPosters(posts);
	}

	@POST
	@Path("findPostsByIdsDescFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByIdsDescFetchAttachmentsAndPosters(List<Integer> posts) throws ModuleException {
		return forumsModule.findPostsByIdsDescFetchAttachmentsAndPosters(posts);
	}

	@POST
	@Path("findPostIdsAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<Integer> findPostIdsAsc(TopicRequestBean topicRequestBean) throws ModuleException {
		return forumsModule.findPostIdsAsc(topicRequestBean.getTopic(), topicRequestBean.getStart(),
				topicRequestBean.getPerPage());
	}

	@POST
	@Path("findPostIdsDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<Integer> findPostIdsDesc(TopicRequestBean topicRequestBean) throws ModuleException {
		return forumsModule.findPostIdsDesc(topicRequestBean.getTopic(), topicRequestBean.getStart(),
				topicRequestBean.getPerPage());
	}

	@POST
	@Path("findPostsByTopicIdAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByTopicIdAsc(TopicRequestBean topicRequestBean) throws ModuleException {
		return forumsModule.findPostsByTopicIdAsc(topicRequestBean.getTopic(), topicRequestBean.getStart(),
				topicRequestBean.getPerPage());
	}

	@POST
	@Path("findPostsByTopicIdDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByTopicIdDesc(TopicRequestBean topicRequestBean) throws ModuleException {
		return forumsModule.findPostsByTopicIdDesc(topicRequestBean.getTopic(), topicRequestBean.getStart(),
				topicRequestBean.getPerPage());
	}

	@POST
	@Path("findLastPostsOfTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Map<Integer, PostBean> findLastPostsOfTopics(List<TopicBean> topics) throws ModuleException {
		return forumsModule.findLastPostsOfTopics(topics);
	}

	@GET
	@Path("findLastPostsOfForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public Map<Integer, PostBean> findLastPostsOfForums(@PathParam("indexInstance") Integer indexInstance)
			throws ModuleException {
		return forumsModule.findLastPostsOfForums(indexInstance);
	}

	@POST
	@Path("findForumWatchByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<ForumWatchBean> findForumWatchByUser(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findForumWatchByUser(user, indexInstance);
	}

	@POST
	@Path("findForumWatchedByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<ForumBean> findForumWatchedByUser(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findForumWatchedByUser(user, indexInstance);
	}

	@POST
	@Path("findTopicWatchedByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicWatchedByUser(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicWatchedByUser(user, indexInstance);
	}

	@POST
	@Path("findTopicWatchedByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopicWatchedByUser(User user, Date date, Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicWatchedByUser(user, date, indexInstance);
	}

	@POST
	@Path("findPostsFromForumDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsFromForumDesc(ForumRequestBean forumRequestBean) throws ModuleException {
		return forumsModule.findPostsFromForumDesc(forumRequestBean.getForum(), forumRequestBean.getLimit());
	}

	@POST
	@Path("findPostsFromCategoryDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsFromCategoryDesc(CategoryRequestBean categoryRequestBean) throws ModuleException {
		return forumsModule.findPostsFromCategoryDesc(categoryRequestBean.getCategory(),
				categoryRequestBean.getLimit());
	}

	@POST
	@Path("findTopicWatches")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Map<Object, Object> findTopicWatches(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicWatches(user, indexInstance);
	}

	@GET
	@Path("findAttachmentById/{attachID}")
	@Produces(APPLICATION_JSON)
	public AttachmentBean findAttachmentById(@PathParam("attachID") Integer attachID) throws ModuleException {
		return forumsModule.findAttachmentById(attachID);
	}

	@GET
	@Path("createPoster/{userID}")
	@Produces(APPLICATION_JSON)
	public PosterBean createPoster(@PathParam("userID") String userID) throws ModuleException {
		return forumsModule.createPoster(userID);
	}

	@POST
	@Path("createWatch")
	@Consumes(APPLICATION_JSON)
	public void createWatch(PosterBean poster, ForumBean forum, int i) throws ModuleException {
		forumsModule.createWatch(poster, forum, i);
	}

	@GET
	@Path("findForumWatchById/{forumWatchID}")
	@Produces(APPLICATION_JSON)
	public ForumWatchBean findForumWatchById(@PathParam("forumWatchID") Integer forumWatchID) throws ModuleException {
		return forumsModule.findForumWatchById(forumWatchID);
	}

	@POST
	@Path("findForumWatches")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Map<Object, Object> findForumWatches(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findForumWatches(user, indexInstance);
	}

	@POST
	@Path("findForumWatchByUserAndForum")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public ForumWatchBean findForumWatchByUserAndForum(User user, int forumId) throws ModuleException {
		return forumsModule.findForumWatchByUserAndForum(user, forumId);
	}

	@POST
	@Path("findTopicWatchByUserAndTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public TopicWatchBean findTopicWatchByUserAndTopic(User user, int topicId) throws ModuleException {
		return forumsModule.findTopicWatchByUserAndTopic(user, topicId);
	}

	@GET
	@Path("findForumInstanceById/{forumInstanceID}")
	@Produces(APPLICATION_JSON)
	public ForumInstanceBean findForumInstanceById(@PathParam("forumInstanceID") Integer forumInstanceID)
			throws ModuleException {
		return forumsModule.findForumInstanceById(forumInstanceID);
	}

	@POST
	@Path("createWatch")
	@Consumes(APPLICATION_JSON)
	public void createWatch(PosterBean poster, TopicBean topic, int mode) throws ModuleException {
		forumsModule.createWatch(poster, topic, mode);
	}

	@GET
	@Path("findTopicWatchById/{topicWatchID}")
	@Produces(APPLICATION_JSON)
	public TopicWatchBean findTopicWatchById(@PathParam("topicWatchID") Integer topicWatchID) throws ModuleException {
		return forumsModule.findTopicWatchById(topicWatchID);
	}

	@POST
	@Path("removeWatch")
	@Consumes(APPLICATION_JSON)
	public void removeWatch(WatchBean watch) throws ModuleException {
		forumsModule.removeWatch(watch);
	}

	@GET
	@Path("processNotifications")
	public void processNotifications(@PathParam("postId") Integer postId, @PathParam("watchType") int watchType,
			@PathParam("postUrl") String postUrl, @PathParam("replyUrl") String replyUrl) {
		forumsModule.processNotifications(postId, watchType, postUrl, replyUrl);
	}

	@GET
	@Path("findPostsDesc/{limit}")
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsDesc(@PathParam("limit") int limit) throws ModuleException {
		return forumsModule.findPostsDesc(limit);
	}

	@POST
	@Path("addAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean addAttachments(List<AttachmentBean> attachments, PostBean post) {
		return forumsModule.addAttachments(attachments, post);
	}

	@POST
	@Path("findAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<AttachmentBean> findAttachments(PostBean post) {
		return (List<AttachmentBean>) forumsModule.findAttachments(post);
	}

	@POST
	@Path("removeAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean removeAttachments(PostBean post) {
		return forumsModule.removeAttachments(post);
	}

	@POST
	@Path("updateAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean updateAttachments(List<AttachmentBean> attachments, PostBean post) {
		return forumsModule.updateAttachments(attachments, post);
	}

	@POST
	@Path("updateForum")
	@Consumes(APPLICATION_JSON)
	public void update(ForumBean forum) {
		forumsModule.update(forum);
	}

	@POST
	@Path("updateTopic")
	@Consumes(APPLICATION_JSON)
	public void update(TopicBean topic) {
		forumsModule.update(topic);
	}

	@POST
	@Path("updateCategory")
	@Consumes(APPLICATION_JSON)
	public void update(CategoryBean category) {
		forumsModule.update(category);
	}

	@POST
	@Path("updateTopicWatch")
	@Consumes(APPLICATION_JSON)
	public void update(TopicWatchBean topicWatch) {
		forumsModule.update(topicWatch);
	}

	@POST
	@Path("updateForumWatch")
	@Consumes(APPLICATION_JSON)
	public void update(ForumWatchBean forumWatch) {
		forumsModule.update(forumWatch);
	}

	@POST
	@Path("updatePost")
	@Consumes(APPLICATION_JSON)
	public void update(PostBean post) {
		forumsModule.update(post);
	}

	@POST
	@Path("updatePollOption")
	@Consumes(APPLICATION_JSON)
	public void update(PollOptionBean pollOption) {
		forumsModule.update(pollOption);
	}

}
