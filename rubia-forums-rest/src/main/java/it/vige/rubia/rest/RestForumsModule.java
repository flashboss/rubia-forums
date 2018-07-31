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

import java.util.Collection;
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

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.User;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.ForumInstance;
import it.vige.rubia.model.ForumWatch;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.model.TopicType;
import it.vige.rubia.model.TopicWatch;
import it.vige.rubia.model.Watch;

@Path("/forums/")
public class RestForumsModule implements ForumsModule {

	@EJB
	private ForumsModule forumsModule;

	@GET
	@Path("getGuestUserName")
	@Override
	public String getGuestUserName() {
		return forumsModule.getGuestUserName();
	}

	@GET
	@Path("setGuestUserName/{guestUserName}")
	@Override
	public void setGuestUserName(@PathParam("guestUserName") String guestUserName) {
		forumsModule.setGuestUserName(guestUserName);
	}

	@GET
	@Path("getFromAddress")
	@Override
	public String getFromAddress() {
		return forumsModule.getFromAddress();
	}

	@GET
	@Path("setFromAddress/{fromAddress}")
	@Override
	public void setFromAddress(@PathParam("fromAddress") String fromAddress) {
		forumsModule.setFromAddress(fromAddress);
	}

	@POST
	@Path("findAnnouncements")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findAnnouncements(Forum forum) throws ModuleException {
		return forumsModule.findAnnouncements(forum);
	}

	@GET
	@Path("findTopics/{indexInstance}")
	@Consumes(APPLICATION_JSON)
	@Override
	public List<Topic> findTopics(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopics(indexInstance);
	}

	@POST
	@Path("findTopicsAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsAsc(Forum forum, TopicType type, int start, int perPage) throws ModuleException {
		return forumsModule.findTopicsAsc(forum, start, perPage);
	}

	@POST
	@Path("findTopicsDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsDesc(Forum forum, TopicType type, int start, int perPage) throws ModuleException {
		return forumsModule.findTopicsDesc(forum, start, perPage);
	}

	@POST
	@Path("findTopicsAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsAsc(Forum forum, int start, int perPage) throws ModuleException {
		return forumsModule.findTopicsAsc(forum, start, perPage);
	}

	@POST
	@Path("findTopicsDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsDesc(Forum forum, int start, int perPage) throws ModuleException {
		return forumsModule.findTopicsDesc(forum, start, perPage);
	}

	@POST
	@Path("findTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopics(Forum forum) throws ModuleException {
		return forumsModule.findTopics(forum);
	}

	@POST
	@Path("findTopicsBefore")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsBefore(Forum forum, TopicType type, int start, int perPage, Date date)
			throws ModuleException {
		return forumsModule.findTopicsBefore(forum, type, start, perPage, date);
	}

	@GET
	@Path("findTopicsHot/{replies}/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsHot(@PathParam("replies") int replies, @PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicsHot(replies, limit, indexInstance);
	}

	@GET
	@Path("findTopicsByLatestPosts/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsByLatestPosts(@PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicsByLatestPosts(limit, indexInstance);
	}

	@GET
	@Path("findTopicsHottest/{after}/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsHottest(@PathParam("after") Date after, @PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicsHottest(after, limit, indexInstance);
	}

	@GET
	@Path("findTopicsMostViewed/{after}/{limit}/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicsMostViewed(@PathParam("after") Date after, @PathParam("limit") int limit,
			@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicsMostViewed(after, limit, indexInstance);
	}

	@GET
	@Path("findForumById/{id}")
	@Produces(APPLICATION_JSON)
	@Override
	public Forum findForumById(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.findForumById(id);
	}

	@GET
	@Path("findForumByIdFetchTopics/{id}")
	@Produces(APPLICATION_JSON)
	@Override
	public Forum findForumByIdFetchTopics(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.findForumByIdFetchTopics(id);
	}

	@POST
	@Path("createForum")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Forum createForum(Category category, String name, String description) throws ModuleException {
		return forumsModule.createForum(category, name, description);
	}

	@GET
	@Path("findPosts/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPosts(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findPosts(indexInstance);
	}

	@GET
	@Path("findPostById/{id}")
	@Produces(APPLICATION_JSON)
	@Override
	public Post findPostById(@PathParam("id") Integer id) throws ModuleException {
		return forumsModule.findPostById(id);
	}

	@GET
	@Path("findPosterByUserId/{userId}")
	@Produces(APPLICATION_JSON)
	@Override
	public Poster findPosterByUserId(@PathParam("userId") String userID) throws ModuleException {
		return forumsModule.findPosterByUserId(userID);
	}

	@GET
	@Path("findCategories/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Category> findCategories(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findCategories(indexInstance);
	}

	@GET
	@Path("findCategoriesFetchForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Category> findCategoriesFetchForums(@PathParam("indexInstance") Integer indexInstance)
			throws ModuleException {
		return forumsModule.findCategoriesFetchForums(indexInstance);
	}

	@GET
	@Path("findForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Forum> findForums(@PathParam("indexInstance") Integer indexInstance) throws ModuleException {
		return forumsModule.findForums(indexInstance);
	}

	@POST
	@Path("findForumsByCategory")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Forum> findForumsByCategory(Category category) throws ModuleException {
		return forumsModule.findForumsByCategory(category);
	}

	@POST
	@Path("createTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post createTopic(Forum forum, Message message, Date creationDate, Poster poster, Poll poll,
			Collection<Attachment> attachments, TopicType type) throws ModuleException {
		return forumsModule.createTopic(forum, message, creationDate, poster, poll, attachments, type);
	}

	@POST
	@Path("createTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Topic createTopic(Forum forum, String userID, String subject, TopicType type) throws ModuleException {
		return forumsModule.createTopic(forum, userID, subject, type);
	}

	@POST
	@Path("createPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post createPost(Topic topic, Forum forum, Message message, Date creationTime, Poster poster,
			Collection<Attachment> attachments) throws ModuleException {
		return forumsModule.createPost(topic, forum, message, creationTime, poster, attachments);
	}

	@POST
	@Path("addPollToTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Poll addPollToTopic(Topic topic, Poll poll) throws ModuleException {
		return forumsModule.addPollToTopic(topic, poll);
	}

	@POST
	@Path("createCategory")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Category createCategory(String name, ForumInstance forumInstance) throws ModuleException {
		return forumsModule.createCategory(name, forumInstance);
	}

	@GET
	@Path("removeCategory/{categoryId}")
	@Override
	public void removeCategory(@PathParam("categoryId") int categoryId) throws ModuleException {
		forumsModule.removeCategory(categoryId);
	}

	@GET
	@Path("createForumInstance/{indexInstance}/{name}")
	@Produces(APPLICATION_JSON)
	@Override
	public ForumInstance createForumInstance(@PathParam("indexInstance") Integer indexInstance,
			@PathParam("name") String name) throws ModuleException {
		return forumsModule.createForumInstance(indexInstance, name);
	}

	@GET
	@Path("removeForumInstance/{indexInstance}")
	@Override
	public void removeForumInstance(@PathParam("indexInstance") int indexInstance) throws ModuleException {
		forumsModule.removeForumInstance(indexInstance);
	}

	@GET
	@Path("removeForum/{forumId}")
	@Override
	public void removeForum(@PathParam("forumId") int forumId) throws ModuleException {
		forumsModule.removeForum(forumId);
	}

	@GET
	@Path("removePost/{postId}/{isLastPost}")
	@Override
	public void removePost(@PathParam("postId") int postId, @PathParam("isLastPost") boolean isLastPost)
			throws ModuleException {
		forumsModule.removePost(postId, isLastPost);
	}

	@POST
	@Path("removePollInTopic")
	@Consumes(APPLICATION_JSON)
	@Override
	public void removePollInTopic(Topic topic) throws ModuleException {
		forumsModule.removePollInTopic(topic);
	}

	@GET
	@Path("removeTopic/{topicId}")
	@Override
	public void removeTopic(@PathParam("topicId") int topicId) throws ModuleException {
		forumsModule.removeTopic(topicId);
	}

	@GET
	@Path("findCategoryById/{categoryID}")
	@Produces(APPLICATION_JSON)
	@Override
	public Category findCategoryById(@PathParam("categoryID") Integer categoryID) throws ModuleException {
		return forumsModule.findCategoryById(categoryID);
	}

	@GET
	@Path("findCategoryByIdFetchForums/{categoryID}")
	@Produces(APPLICATION_JSON)
	@Override
	public Category findCategoryByIdFetchForums(@PathParam("categoryID") Integer categoryID) throws ModuleException {
		return forumsModule.findCategoryByIdFetchForums(categoryID);
	}

	@POST
	@Path("addAllForums")
	@Consumes(APPLICATION_JSON)
	@Override
	public void addAllForums(Category source, Category target) throws ModuleException {
		forumsModule.addAllForums(source, target);
	}

	@GET
	@Path("findTopicById/{topicID}")
	@Produces(APPLICATION_JSON)
	@Override
	public Topic findTopicById(@PathParam("topicID") Integer topicID) throws ModuleException {
		return forumsModule.findTopicById(topicID);
	}

	@POST
	@Path("findPostsByTopicId")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsByTopicId(Topic topic) throws ModuleException {
		return forumsModule.findPostsByTopicId(topic);
	}

	@POST
	@Path("findPostsByIdsAscFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Integer> posts) throws ModuleException {
		return forumsModule.findPostsByIdsAscFetchAttachmentsAndPosters(posts);
	}

	@POST
	@Path("findPostsByIdsDescFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Integer> posts) throws ModuleException {
		return forumsModule.findPostsByIdsDescFetchAttachmentsAndPosters(posts);
	}

	@POST
	@Path("findPostIdsAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Integer> findPostIdsAsc(Topic topic, int start, int limit) throws ModuleException {
		return forumsModule.findPostIdsAsc(topic, start, limit);
	}

	@POST
	@Path("findPostIdsDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Integer> findPostIdsDesc(Topic topic, int start, int limit) throws ModuleException {
		return forumsModule.findPostIdsDesc(topic, start, limit);
	}

	@POST
	@Path("findPostsByTopicIdAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsByTopicIdAsc(Topic topic, int start, int limit) throws ModuleException {
		return forumsModule.findPostsByTopicIdAsc(topic, start, limit);
	}

	@POST
	@Path("findPostsByTopicIdDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsByTopicIdDesc(Topic topic, int start, int limit) throws ModuleException {
		return forumsModule.findPostsByTopicIdDesc(topic, start, limit);
	}

	@POST
	@Path("findLastPostDateForUser")
	@Consumes(APPLICATION_JSON)
	@Override
	public Date findLastPostDateForUser(User user) throws ModuleException {
		return forumsModule.findLastPostDateForUser(user);
	}

	@POST
	@Path("findLastPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post findLastPost(Forum forum) throws ModuleException {
		return forumsModule.findLastPost(forum);
	}

	@POST
	@Path("findFirstPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post findFirstPost(Topic topic) throws ModuleException {
		return forumsModule.findFirstPost(topic);
	}

	@POST
	@Path("findLastPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post findLastPost(Topic topic) throws ModuleException {
		return forumsModule.findLastPost(topic);
	}

	@POST
	@Path("findLastPostsOfTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Map<Object, Object> findLastPostsOfTopics(Collection<Topic> topics) throws ModuleException {
		return forumsModule.findLastPostsOfTopics(topics);
	}

	@GET
	@Path("findLastPostsOfForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	@Override
	public Map<Object, Post> findLastPostsOfForums(@PathParam("indexInstance") Integer indexInstance)
			throws ModuleException {
		return forumsModule.findLastPostsOfForums(indexInstance);
	}

	@POST
	@Path("findForumWatchByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<ForumWatch> findForumWatchByUser(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findForumWatchByUser(user, indexInstance);
	}

	@POST
	@Path("findForumWatchedByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Forum> findForumWatchedByUser(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findForumWatchedByUser(user, indexInstance);
	}

	@POST
	@Path("findTopicWatchedByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicWatchedByUser(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicWatchedByUser(user, indexInstance);
	}

	@POST
	@Path("findTopicWatchedByUser")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Topic> findTopicWatchedByUser(User user, Date date, Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicWatchedByUser(user, date, indexInstance);
	}

	@POST
	@Path("findPostsFromForumAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsFromForumAsc(Forum forum, int limit) throws ModuleException {
		return forumsModule.findPostsFromForumAsc(forum, limit);
	}

	@POST
	@Path("findPostsFromForumDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsFromForumDesc(Forum forum, int limit) throws ModuleException {
		return forumsModule.findPostsFromForumDesc(forum, limit);
	}

	@POST
	@Path("findPostsFromCategoryAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsFromCategoryAsc(Category category, int limit) throws ModuleException {
		return forumsModule.findPostsFromCategoryAsc(category, limit);
	}

	@POST
	@Path("findPostsFromCategoryDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsFromCategoryDesc(Category category, int limit) throws ModuleException {
		return forumsModule.findPostsFromCategoryDesc(category, limit);
	}

	@POST
	@Path("findTopicWatches")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Map<Object, Object> findTopicWatches(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findTopicWatches(user, indexInstance);
	}

	@GET
	@Path("findAttachmentById/{attachID}")
	@Produces(APPLICATION_JSON)
	@Override
	public Attachment findAttachmentById(@PathParam("attachID") Integer attachID) throws ModuleException {
		return forumsModule.findAttachmentById(attachID);
	}

	@GET
	@Path("createPoster/{userID}")
	@Produces(APPLICATION_JSON)
	@Override
	public Poster createPoster(@PathParam("userID") String userID) throws ModuleException {
		return forumsModule.createPoster(userID);
	}

	@POST
	@Path("createWatch")
	@Consumes(APPLICATION_JSON)
	@Override
	public void createWatch(Poster poster, Forum forum, int i) throws ModuleException {
		forumsModule.createWatch(poster, forum, i);
	}

	@GET
	@Path("findForumWatchById/{forumWatchID}")
	@Produces(APPLICATION_JSON)
	@Override
	public ForumWatch findForumWatchById(@PathParam("forumWatchID") Integer forumWatchID) throws ModuleException {
		return forumsModule.findForumWatchById(forumWatchID);
	}

	@POST
	@Path("findForumWatches")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Map<Object, Object> findForumWatches(User user, Integer indexInstance) throws ModuleException {
		return forumsModule.findForumWatches(user, indexInstance);
	}

	@POST
	@Path("findForumWatchByUserAndForum")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public ForumWatch findForumWatchByUserAndForum(User user, int forumId) throws ModuleException {
		return forumsModule.findForumWatchByUserAndForum(user, forumId);
	}

	@POST
	@Path("findTopicWatchByUserAndTopic")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public TopicWatch findTopicWatchByUserAndTopic(User user, int topicId) throws ModuleException {
		return forumsModule.findTopicWatchByUserAndTopic(user, topicId);
	}

	@GET
	@Path("findForumInstanceById/{forumInstanceID}")
	@Produces(APPLICATION_JSON)
	@Override
	public ForumInstance findForumInstanceById(@PathParam("forumInstanceID") Integer forumInstanceID)
			throws ModuleException {
		return forumsModule.findForumInstanceById(forumInstanceID);
	}

	@POST
	@Path("createWatch")
	@Consumes(APPLICATION_JSON)
	@Override
	public void createWatch(Poster poster, Topic topic, int mode) throws ModuleException {
		forumsModule.createWatch(poster, topic, mode);
	}

	@GET
	@Path("findTopicWatchById/{topicWatchID}")
	@Produces(APPLICATION_JSON)
	@Override
	public TopicWatch findTopicWatchById(@PathParam("topicWatchID") Integer topicWatchID) throws ModuleException {
		return forumsModule.findTopicWatchById(topicWatchID);
	}

	@POST
	@Path("removeWatch")
	@Consumes(APPLICATION_JSON)
	@Override
	public void removeWatch(Watch watch) throws ModuleException {
		forumsModule.removeWatch(watch);
	}

	@GET
	@Path("processNotifications")
	@Override
	public void processNotifications(@PathParam("postId") Integer postId, @PathParam("watchType") int watchType,
			@PathParam("postUrl") String postUrl, @PathParam("replyUrl") String replyUrl) {
		forumsModule.processNotifications(postId, watchType, postUrl, replyUrl);
	}

	@GET
	@Path("findPostsDesc/{limit}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsDesc(@PathParam("limit") int limit) throws ModuleException {
		return forumsModule.findPostsDesc(limit);
	}

	@GET
	@Path("findPostsAsc/{limit}")
	@Produces(APPLICATION_JSON)
	@Override
	public List<Post> findPostsAsc(@PathParam("limit") int limit) throws ModuleException {
		return forumsModule.findPostsAsc(limit);
	}

	@POST
	@Path("addAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post addAttachments(Collection<Attachment> attachments, Post post) {
		return forumsModule.addAttachments(attachments, post);
	}

	@POST
	@Path("findAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Collection<Attachment> findAttachments(Post post) {
		return forumsModule.findAttachments(post);
	}

	@POST
	@Path("removeAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post removeAttachments(Post post) {
		return forumsModule.removeAttachments(post);
	}

	@POST
	@Path("updateAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Post updateAttachments(Collection<Attachment> attachments, Post post) {
		return forumsModule.updateAttachments(attachments, post);
	}

	@POST
	@Path("update")
	@Consumes(APPLICATION_JSON)
	@Override
	public void update(Object object) {
		forumsModule.update(object);
	}

}
