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
package it.vige.rubia;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.vige.rubia.auth.User;
import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.ForumWatchBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.TopicRequestBean;
import it.vige.rubia.dto.TopicWatchBean;
import it.vige.rubia.dto.WatchBean;

@Local
public interface ForumsModule {

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	String getGuestUserName();

	/**
	 * DOCUMENT_ME
	 * 
	 * @param guestUserName the username of the guest
	 */
	void setGuestUserName(String guestUserName);

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	String getFromAddress();

	/**
	 * DOCUMENT_ME
	 * 
	 * @param fromAddress DOCUMENT_ME
	 */
	void setFromAddress(String fromAddress);

	/**
	 * Returns some topics of a forum that are not of a certain type. Need to a
	 * forumInstanceId argument to take only topics of a specific forum instance.
	 * FindTopics method need to a forumInstanceId argument to take only topics of a
	 * specific forum instance
	 * 
	 * @param indexInstance the forum instance where find the topics
	 * @return List of topics
	 * @throws ModuleException Throws an excpetion if unable to find the topics.
	 */
	List<TopicBean> findTopics(Integer indexInstance) throws ModuleException;

	/**
	 * Returns topics that are ordered by creation date from newest to oldest.
	 * 
	 * @param topicRequestBean Topic in which we want to search for topics
	 * @return List of perPage topics ordered by opposite creation date.
	 * @throws ModuleException Throws an excpetion if unable to find the topics.
	 */
	List<TopicBean> findTopicsDesc(TopicRequestBean topicRequestBean) throws ModuleException;

	/**
	 * *
	 * 
	 * @param forum Forum in which we want to search for topics
	 * @return List of perPage topics ordered by creation date.
	 * @throws ModuleException Throws an excpetion if unable to find the topics.
	 */
	List<TopicBean> findTopics(ForumBean forum) throws ModuleException;

	/**
	 * FindTopicsHot, findTopicsByLatestPosts, findTopicsHottest,
	 * findTopicsMostViewed methods need to a forumInstanceId argument to take only
	 * topics of a specific forum instance
	 * 
	 * @param replies       DOCUMENT_ME
	 * @param limit         DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the list of topics
	 * 
	 * @throws ModuleException DOCUMENT_ME
	 */

	List<TopicBean> findTopicsHot(int replies, int limit, Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param limit         DOCUMENT_ME
	 * 
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<TopicBean> findTopicsByLatestPosts(int limit, Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param after         DOCUMENT_ME
	 * @param limit         DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<TopicBean> findTopicsHottest(Date after, int limit, Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param after         DOCUMENT_ME
	 * @param limit         DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<TopicBean> findTopicsMostViewed(Date after, int limit, Integer indexInstance) throws ModuleException;

	/**
	 * Find a forum by specifying its ID
	 * 
	 * @param id ID of the forum to retrieve
	 * @return Forum with specified ID
	 * @throws ModuleException Throws an exception if the forum cannot be found
	 */
	ForumBean findForumById(Integer id) throws ModuleException;

	/**
	 * Find a forum by specifying its ID and fetch Topics of this Forum.
	 * 
	 * @param id ID of the forum to retrieve
	 * @return Forum with specified ID
	 * @throws ModuleException Throws an exception if the forum cannot be found
	 */
	ForumBean findForumByIdFetchTopics(Integer id) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	ForumBean createForum(ForumBean forum) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param id DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	PostBean findPostById(Integer id) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param userID DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	PosterBean findPosterByUserId(String userID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param id DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	PosterBean removePoster(Integer id) throws ModuleException;

	/**
	 * Get all the categories of forums.. FindCategories, findCategoriesFetchForums
	 * and findForums methods need to a forumInstanceId argument to take only
	 * categories of a specific forum instance. FindCategories,
	 * findCategoriesFetchForums, findForums methods need to a forumInstanceId
	 * argument to take only objects of a specific forum instance
	 * 
	 * @param indexInstance the forum instance where find the categories
	 * @return the list of categories
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<CategoryBean> findCategories(Integer indexInstance) throws ModuleException;

	/**
	 * Get all the categories of forums and fetch forums.
	 * 
	 * @param indexInstance the forum instance where find the categories
	 * 
	 * @return All the categories
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<CategoryBean> findCategoriesFetchForums(Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param indexInstance the forum instance where find the categories
	 * 
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<ForumBean> findForums(Integer indexInstance) throws ModuleException;

	/**
	 * Get all the forums of a category
	 * 
	 * @param category Category of forums
	 * @return Forums of one category
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<ForumBean> findForumsByCategory(CategoryBean category) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @return The new post created
	 * @throws ModuleException DOCUMENT_ME
	 */
	PostBean createTopic(TopicBean topic) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	TopicBean createTopicWithPoster(TopicBean forum) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param post DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	PostBean createPost(PostBean post) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @param poll  DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	PollBean addPollToTopic(TopicBean topic, PollBean poll) throws ModuleException;

	/**
	 * Need to a forumInstanceId argument to create only categories of a specific
	 * forum instance
	 * 
	 * @param category DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	CategoryBean createCategory(CategoryBean category) throws ModuleException;

	/**
	 * add createForumInstance and removeForumInstance methods to manage create and
	 * remove of a ForumInstance.
	 * 
	 * @param categoryId DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void removeCategory(int categoryId) throws ModuleException;

	/**
	 * 
	 * I add createForumInstance, removeForumInstance and findForumInstanceById
	 * methods to manage ForumInstance object
	 * 
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @param name          DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	ForumInstanceBean createForumInstance(Integer indexInstance, String name) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param indexInstance DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void removeForumInstance(int indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forumId DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void removeForum(int forumId) throws ModuleException;

	/**
	 * Delete a post
	 * 
	 * @param postId     Post to delete
	 * 
	 * @param isLastPost if the post is the last of the list
	 * 
	 * @throws ModuleException DOCUMENT_ME
	 */
	void removePost(int postId, boolean isLastPost) throws ModuleException;

	/**
	 * Delete a topic
	 * 
	 * @param topicId Topic to delete
	 * @throws ModuleException DOCUMENT_ME
	 */
	void removeTopic(int topicId) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param categoryID DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	CategoryBean findCategoryById(Integer categoryID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param categoryID DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	CategoryBean findCategoryByIdFetchForums(Integer categoryID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param category DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void addAllForums(CategoryBean... category) throws ModuleException;

	/**
	 * @param topicID DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	TopicBean findTopicById(Integer topicID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<PostBean> findPostsByTopicId(TopicBean topic) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param posts DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<PostBean> findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Integer> posts) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param posts DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<PostBean> findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Integer> posts) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @param start DOCUMENT_ME
	 * @param limit DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<Integer> findPostIdsAsc(TopicBean topic, int start, int limit) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @param start DOCUMENT_ME
	 * @param limit DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<Integer> findPostIdsDesc(TopicBean topic, int start, int limit) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @param start DOCUMENT_ME
	 * @param limit DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<PostBean> findPostsByTopicIdAsc(TopicBean topic, int start, int limit) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic DOCUMENT_ME
	 * @param start DOCUMENT_ME
	 * @param limit DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<PostBean> findPostsByTopicIdDesc(TopicBean topic, int start, int limit) throws ModuleException;

	/**
	 * @param topics DOCUMENT_ME
	 * @return the last posts of the topic
	 * @throws ModuleException DOCUMENT_ME
	 */
	Map<Integer, PostBean> findLastPostsOfTopics(Collection<TopicBean> topics) throws ModuleException;

	/**
	 * findLastPostsOfForums method need to a forumInstanceId argument to take only
	 * posts of a specific forum instance. FindLastPostsOfForums,
	 * findForumWatchByUser, findForumWatchedByUser, findTopicWatchedByUser and
	 * findTopicWatches methods need to a forumInstanceId argument to take only
	 * objects of a specific forum instance
	 * 
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the last posts of the forum instance
	 * @throws ModuleException DOCUMENT_ME
	 */
	Map<Integer, PostBean> findLastPostsOfForums(Integer indexInstance) throws ModuleException;

	/**
	 * findForumWatchByUser method need to a forumInstanceId argument to take only
	 * categories of a specific forum instance
	 * 
	 * @param user          DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the forum watches list
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<ForumWatchBean> findForumWatchByUser(User user, Integer indexInstance) throws ModuleException;

	/**
	 * findForumWatchedByUser method need to a forumInstanceId argument to take only
	 * forums of a specific forum instance
	 * 
	 * @param user          DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the forums watched by the user
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<ForumBean> findForumWatchedByUser(User user, Integer indexInstance) throws ModuleException;

	/**
	 * findTopicWatchedByUser method need to a forumInstanceId argument to take only
	 * topics of a specific forum instance
	 * 
	 * @param user          DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the topics watched by the user
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<TopicBean> findTopicWatchedByUser(User user, Integer indexInstance) throws ModuleException;

	/**
	 * findTopicWatchedByUser method need to a forumInstanceId argument to take only
	 * topics of a specific forum instance
	 * 
	 * @param user          DOCUMENT_ME
	 * @param date          DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the topics watched by the user
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<TopicBean> findTopicWatchedByUser(User user, Date date, Integer indexInstance) throws ModuleException;

	List<PostBean> findPostsFromForumDesc(ForumBean forum, int limit) throws ModuleException;

	List<PostBean> findPostsFromCategoryDesc(CategoryBean category, int limit) throws ModuleException;

	/**
	 * findTopicWatches method need to a forumInstanceId argument to take only
	 * topics of a specific forum instance. This method returns a map of
	 * Integer,TopicWatch pairs where Integer key is watched topic id.
	 * 
	 * @param user          DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * 
	 * @return the map of topic watches
	 * @throws ModuleException DOCUMENT_ME
	 */
	Map<Integer, TopicWatchBean> findTopicWatches(User user, Integer indexInstance) throws ModuleException;

	AttachmentBean findAttachmentById(Integer attachID) throws ModuleException;

	PosterBean createPoster(String userID) throws ModuleException;

	/**
	 * @param poster DOCUMENT_ME
	 * @param forum  DOCUMENT_ME
	 * @param i      DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void createWatch(PosterBean poster, ForumBean forum, int i) throws ModuleException;

	/**
	 * @param forumWatchID DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	ForumWatchBean findForumWatchById(Integer forumWatchID) throws ModuleException;

	/**
	 * findForumWatches method need to a forumInstanceId argument to take only
	 * forums of a specific forum instance. This method returns a map of
	 * Integer,ForumWatch pairs where Integer key is watched forum id. Need to a
	 * forumInstanceId argument to take only forums of a specific forum instance
	 * 
	 * @param user          DOCUMENT_ME
	 * @param indexInstance DOCUMENT_ME
	 * @return the map of the forum watches
	 * @throws ModuleException DOCUMENT_ME
	 */
	Map<Integer, ForumWatchBean> findForumWatches(User user, Integer indexInstance) throws ModuleException;

	/**
	 * 
	 * @param user    DOCUMENT_ME
	 * @param forumId DOCUMENT_ME
	 * @return ForumWatch
	 * @throws ModuleException DOCUMENT_ME
	 */
	ForumWatchBean findForumWatchByUserAndForum(User user, int forumId) throws ModuleException;

	/**
	 * 
	 * @param user    DOCUMENT_ME
	 * @param topicId DOCUMENT_ME
	 * @return TopicWatch
	 * @throws ModuleException DOCUMENT_ME
	 */
	TopicWatchBean findTopicWatchByUserAndTopic(User user, int topicId) throws ModuleException;

	/**
	 * Search method for forum instances
	 * 
	 * @param forumInstanceID DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	ForumInstanceBean findForumInstanceById(Integer forumInstanceID) throws ModuleException;

	/**
	 * @param poster DOCUMENT_ME
	 * @param topic  DOCUMENT_ME
	 * @param mode   DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void createWatch(PosterBean poster, TopicBean topic, int mode) throws ModuleException;

	/**
	 * @param watch DOCUMENT_ME
	 * @throws ModuleException DOCUMENT_ME
	 */
	void removeWatch(WatchBean watch) throws ModuleException;

	/**
	 * @param limit DOCUMENT_ME
	 * 
	 * @return the list of posts in the limit
	 * @throws ModuleException DOCUMENT_ME
	 */
	List<PostBean> findPostsDesc(int limit) throws ModuleException;

	/**
	 * @param attachments DOCUMENT_ME
	 * @param post        DOCUMENT_ME
	 * 
	 * @return the post where the attachment is done
	 */
	PostBean addAttachments(Collection<AttachmentBean> attachments, PostBean post);

	/**
	 * @param post DOCUMENT_ME
	 * 
	 * @return the list of attachments of the post
	 */
	Collection<AttachmentBean> findAttachments(PostBean post);

	/**
	 * @param post DOCUMENT_ME
	 * @return the post where the attachments are removed
	 */
	PostBean removeAttachments(PostBean post);

	/**
	 * @param attachments DOCUMENT_ME
	 * @param post        DOCUMENT_ME
	 * 
	 * @return the post where the attachments are updated
	 */
	PostBean updateAttachments(Collection<AttachmentBean> attachments, PostBean post);

	/**
	 * @param topic DOCUMENT_ME
	 */
	void update(TopicBean topic);

	/**
	 * @param forum DOCUMENT_ME
	 */
	void update(ForumBean forum);

	/**
	 * @param category DOCUMENT_ME
	 */
	void update(CategoryBean category);

	/**
	 * @param pollOption DOCUMENT_ME
	 */
	void update(PollOptionBean pollOption);

	/**
	 * @param post DOCUMENT_ME
	 */
	void update(PostBean post);

	/**
	 * @param topicWatch DOCUMENT_ME
	 */
	void update(TopicWatchBean topicWatch);

	/**
	 * @param forumWatch DOCUMENT_ME
	 */
	void update(ForumWatchBean forumWatch);

}