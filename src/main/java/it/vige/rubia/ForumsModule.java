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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

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
	 * @param guestUserName
	 *            the username of the guest
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
	 * @param fromAddress
	 *            DOCUMENT_ME
	 */
	void setFromAddress(String fromAddress);

	/**
	 * Returns all the announcements of the forum
	 *
	 * @param forum
	 *            Forum in which we want to search for the announcements
	 * @return List of topics
	 * @throws ModuleException
	 *             Throws an exception if unable to find the announcements.
	 */
	List<Post> findAnnouncements(Forum forum) throws ModuleException;

	/**
	 * Returns some topics of a forum that are not of a certain type. Need to a
	 * forumInstanceId argument to take only topics of a specific forum
	 * instance. FindTopics method need to a forumInstanceId argument to take
	 * only topics of a specific forum instance
	 *
	 * @param indexInstance
	 *            the forum instance where find the topics
	 * @return List of topics
	 * @throws ModuleException
	 *             Throws an excpetion if unable to find the topics.
	 */
	List<Topic> findTopics(Integer indexInstance) throws ModuleException;

	/**
	 * Returns some topics of a forum that are not of a certain type The topics
	 * are ordered by creation date from oldest to newest
	 * 
	 * @param forum
	 *            Forum in which we want to search for topics
	 * @param type
	 *            Type to avoid
	 * @param start
	 *            Index for fetching result
	 * @param perPage
	 *            Number of result to return
	 * @return List of perPage topics ordered by creation date.
	 * @throws ModuleException
	 *             Throws an excpetion if unable to find the topics.
	 */
	List<Topic> findTopicsAsc(Forum forum, TopicType type, int start, int perPage) throws ModuleException;

	/**
	 * Returns topics that are ordered by creation date from newest to oldest.
	 * 
	 * @param forum
	 *            Forum in which we want to search for topics
	 * @param type
	 *            Type to avoid
	 * @param start
	 *            Index for fetching result
	 * @param perPage
	 *            Number of result to return
	 * @return List of perPage topics ordered by opposite creation date.
	 * @throws ModuleException
	 *             Throws an excpetion if unable to find the topics.
	 */
	List<Topic> findTopicsDesc(Forum forum, TopicType type, int start, int perPage) throws ModuleException;

	/**
	 * Returns topics that are ordered by creation date from oldest to newest.
	 * 
	 * @param forum
	 *            Forum in which we want to search for topics
	 * @param start
	 *            Index for fetching result
	 * @param perPage
	 *            Number of result to return
	 * @return List of perPage topics ordered by creation date.
	 * @throws ModuleException
	 *             Throws an excpetion if unable to find the topics.
	 */
	List<Topic> findTopicsAsc(Forum forum, int start, int perPage) throws ModuleException;

	/**
	 * Returns some topics of a forum that are not of a certain type The topics
	 * are ordered by creation date from newest to oldest
	 * 
	 * @param forum
	 *            Forum in which we want to search for topics
	 * @param start
	 *            Index for fetching result
	 * @param perPage
	 *            Number of result to return
	 * @return List of perPage topics ordered by opposite creation date.
	 * @throws ModuleException
	 *             Throws an excpetion if unable to find the topics.
	 */
	List<Topic> findTopicsDesc(Forum forum, int start, int perPage) throws ModuleException;

	/**
	 * *
	 * 
	 * @param forum
	 *            Forum in which we want to search for topics
	 * @return List of perPage topics ordered by creation date.
	 * @throws ModuleException
	 *             Throws an excpetion if unable to find the topics.
	 */
	List<Topic> findTopics(Forum forum) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum
	 *            DOCUMENT_ME
	 * @param type
	 *            DOCUMENT_ME
	 * @param start
	 *            DOCUMENT_ME
	 * @param perPage
	 *            DOCUMENT_ME
	 * @param date
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Topic> findTopicsBefore(Forum forum, TopicType type, int start, int perPage, Date date) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param replies
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */

	/*
	 * Luca Stancapiano start - findTopicsHot, findTopicsByLatestPosts,
	 * findTopicsHottest, findTopicsMostViewed methods need to a forumInstanceId
	 * argument to take only objects of a specific forum instance
	 */
	List<Topic> findTopicsHot(int replies, int limit, Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param limit
	 *            DOCUMENT_ME
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Topic> findTopicsByLatestPosts(int limit, Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param after
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Topic> findTopicsHottest(Date after, int limit, Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param after
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Topic> findTopicsMostViewed(Date after, int limit, Integer indexInstance) throws ModuleException;

	// Luca Stancapiano end

	/**
	 * Find a forum by specifying its ID
	 * 
	 * @param id
	 *            ID of the forum to retrieve
	 * @return Forum with specified ID
	 * @throws ModuleException
	 *             Throws an exception if the forum cannot be found
	 */
	Forum findForumById(Integer id) throws ModuleException;

	/**
	 * Find a forum by specifying its ID and fetch Topics of this Forum.
	 * 
	 * @param id
	 *            ID of the forum to retrieve
	 * @return Forum with specified ID
	 * @throws ModuleException
	 *             Throws an exception if the forum cannot be found
	 */
	Forum findForumByIdFetchTopics(Integer id) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param category
	 *            DOCUMENT_ME
	 * @param name
	 *            DOCUMENT_ME
	 * @param description
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Forum createForum(Category category, String name, String description) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */

	/*
	 * Luca Stancapiano - findPosts method need to a forumInstanceId argument to
	 * take only posts of a specific forum instance
	 */
	List<Post> findPosts(Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param id
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Post findPostById(Integer id) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param userID
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Poster findPosterByUserId(String userID) throws ModuleException;

	/**
	 * Get all the categories of forums.
	 * 
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * 
	 * @return All the categories
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */

	/*
	 * Luca Stancapiano start - findCategories, findCategoriesFetchForums and
	 * findForums methods need to a forumInstanceId argument to take only
	 * categories of a specific forum instance
	 */
	List<Category> findCategories(Integer indexInstance) throws ModuleException;

	/**
	 * Get all the categories of forums and fetch forums.
	 * 
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return All the categories
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Category> findCategoriesFetchForums(Integer indexInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Forum> findForums(Integer indexInstance) throws ModuleException;

	// Luca Stancapiano end

	/**
	 * Get all the forums of a category
	 * 
	 * @param category
	 *            Category of forums
	 * @return Forums of one category
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Forum> findForumsByCategory(Category category) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum
	 *            DOCUMENT_ME
	 * @param message
	 *            DOCUMENT_ME
	 * @param creationDate
	 *            DOCUMENT_ME
	 * @param poster
	 *            DOCUMENT_ME
	 * @param poll
	 *            DOCUMENT_ME
	 * @param attachments
	 *            DOCUMENT_ME
	 * @param type
	 *            DOCUMENT_ME
	 * @return The new post created
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Post createTopic(Forum forum, Message message, Date creationDate, Poster poster, Poll poll,
			Collection<Attachment> attachments, TopicType type) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum
	 *            DOCUMENT_ME
	 * @param userID
	 *            DOCUMENT_ME
	 * @param subject
	 *            DOCUMENT_ME
	 * @param type
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Topic createTopic(Forum forum, String userID, String subject, TopicType type) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @param forum
	 *            DOCUMENT_ME
	 * @param message
	 *            DOCUMENT_ME
	 * @param creationTime
	 *            DOCUMENT_ME
	 * @param poster
	 *            DOCUMENT_ME
	 * @param attachments
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Post createPost(Topic topic, Forum forum, Message message, Date creationTime, Poster poster,
			Collection<Attachment> attachments) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @param poll
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Poll addPollToTopic(Topic topic, Poll poll) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name
	 *            DOCUMENT_ME
	 * @param forumInstance
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	/*
	 * Luca Stancapiano - createCategory method need to a forumInstanceId
	 * argument to create only categories of a specific forum instance
	 */
	Category createCategory(String name, ForumInstance forumInstance) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param categoryId
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removeCategory(int categoryId) throws ModuleException;

	/*
	 * Luca Stancapiano start - I add createForumInstance and
	 * removeForumInstance methods to manage create and remove of a
	 * ForumInstance.
	 */
	/**
	 * DOCUMENT_ME
	 * 
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @param name
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	ForumInstance createForumInstance(Integer indexInstance, String name) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removeForumInstance(int indexInstance) throws ModuleException;

	// Luca Stancapiano end

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forumId
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removeForum(int forumId) throws ModuleException;

	/**
	 * Delete a post
	 *
	 * @param postId
	 *            Post to delete
	 *
	 * @param isLastPost
	 *            if the post is the last of the list
	 *
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removePost(int postId, boolean isLastPost) throws ModuleException;

	/**
	 * Delete a post
	 * 
	 * @param topic
	 *            Post to delete
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removePollInTopic(Topic topic) throws ModuleException;

	/**
	 * Delete a topic
	 * 
	 * @param topicId
	 *            Topic to delete
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removeTopic(int topicId) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param categoryID
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Category findCategoryById(Integer categoryID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param categoryID
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Category findCategoryByIdFetchForums(Integer categoryID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param source
	 *            DOCUMENT_ME
	 * @param target
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void addAllForums(Category source, Category target) throws ModuleException;

    /**
     * @param topicID
     *            DOCUMENT_ME
     * @return DOCUMENT_ME
     * @throws ModuleException
     *             DOCUMENT_ME
     */
	Topic findTopicById(Integer topicID) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsByTopicId(Topic topic) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 *
	 * @param posts
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Post> posts) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 *
	 * @param posts
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Post> posts) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @param start
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostIdsAsc(Topic topic, int start, int limit) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @param start
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostIdsDesc(Topic topic, int start, int limit) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @param start
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsByTopicIdAsc(Topic topic, int start, int limit) throws ModuleException;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param topic
	 *            DOCUMENT_ME
	 * @param start
	 *            DOCUMENT_ME
	 * @param limit
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsByTopicIdDesc(Topic topic, int start, int limit) throws ModuleException;

	/**
	 * @param user
	 *            DOCUMENT_ME
	 * @return the last post date of the user
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Date findLastPostDateForUser(User user) throws ModuleException;

	Post findLastPost(Forum forum) throws ModuleException;

	Post findFirstPost(Topic topic) throws ModuleException;

	Post findLastPost(Topic topic) throws ModuleException;

	Map<Object, Object> findLastPostsOfTopics(Collection<Topic> topics) throws ModuleException;

	/*
	 * Luca Stancapiano - findLastPostsOfForums method need to a forumInstanceId
	 * argument to take only posts of a specific forum instance
	 */
	Map<Object, Post> findLastPostsOfForums(Integer indexInstance) throws ModuleException;

	/*
	 * Luca Stancapiano - findForumWatchByUser method need to a forumInstanceId
	 * argument to take only categories of a specific forum instance
	 */
	List<ForumWatch> findForumWatchByUser(User user, Integer indexInstance) throws ModuleException;

	/*
	 * Luca Stancapiano - findForumWatchedByUser method need to a
	 * forumInstanceId argument to take only forums of a specific forum instance
	 */
	List<Forum> findForumWatchedByUser(User user, Integer indexInstance) throws ModuleException;

	/*
	 * Luca Stancapiano - findTopicWatchedByUser method need to a
	 * forumInstanceId argument to take only topics of a specific forum instance
	 */
	List<Topic> findTopicWatchedByUser(User user, Integer indexInstance) throws ModuleException;

	/*
	 * Luca Stancapiano - findTopicWatchedByUser method need to a
	 * forumInstanceId argument to take only topics of a specific forum instance
	 */
	List<Topic> findTopicWatchedByUser(User user, Date date, Integer indexInstance) throws ModuleException;

	List<Post> findPostsFromForumAsc(Forum forum, int limit) throws ModuleException;

	List<Post> findPostsFromForumDesc(Forum forum, int limit) throws ModuleException;

	List<Post> findPostsFromCategoryAsc(Category category, int limit) throws ModuleException;

	List<Post> findPostsFromCategoryDesc(Category category, int limit) throws ModuleException;

	/*
	 * Luca Stancapiano - findTopicWatches method need to a forumInstanceId
	 * argument to take only topics of a specific forum instance
	 */
	Map<Object, Object> findTopicWatches(User user, Integer indexInstance) throws ModuleException;

	Attachment findFindAttachmentById(Integer attachID) throws ModuleException;

	Poster createPoster(String userID) throws ModuleException;

	/**
	 * @param poster
	 *            DOCUMENT_ME
	 * @param forum
	 *            DOCUMENT_ME
	 * @param i
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void createWatch(Poster poster, Forum forum, int i) throws ModuleException;

	/**
	 * @param forumWatchID
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	ForumWatch findForumWatchById(Integer forumWatchID) throws ModuleException;

	/**
	 * findForumWatches method need to a forumInstanceId argument to take only
	 * forums of a specific forum instance. This method returns a map of
	 * Integer,ForumWatch pairs where Integer key is watched forum id. Need to a
	 * forumInstanceId argument to take only forums of a specific forum instance
	 *
	 * @param user
	 *            DOCUMENT_ME
	 * @param indexInstance
	 *            DOCUMENT_ME
	 * @return the map of the forum watches
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	Map<Object, Object> findForumWatches(User user, Integer indexInstance) throws ModuleException;

	/**
	 *
	 * @param user
	 *            DOCUMENT_ME
	 * @param forumId
	 *            DOCUMENT_ME
	 * @return ForumWatch
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	ForumWatch findForumWatchByUserAndForum(User user, int forumId) throws ModuleException;

	/**
	 *
	 * @param user
	 *            DOCUMENT_ME
	 * @param topicId
	 *            DOCUMENT_ME
	 * @return TopicWatch
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	TopicWatch findTopicWatchByUserAndTopic(User user, int topicId) throws ModuleException;

	/**
	 * Search method for forum instances
	 *
	 * @param forumInstanceID
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	ForumInstance findForumInstanceById(Integer forumInstanceID) throws ModuleException;

	// Luca Stancapiano end

	/**
	 * @param poster
	 *            DOCUMENT_ME
	 * @param topic
	 *            DOCUMENT_ME
	 * @param mode
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void createWatch(Poster poster, Topic topic, int mode) throws ModuleException;

	/**
	 * @param topicWatchID
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	TopicWatch findTopicWatchById(Integer topicWatchID) throws ModuleException;

	/**
	 * @param watch
	 *            DOCUMENT_ME
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	void removeWatch(Watch watch) throws ModuleException;

	/**
	 * @param postId
	 *            DOCUMENT_ME
	 * @param watchType
	 *            DOCUMENT_ME
	 * @param postUrl
	 *            DOCUMENT_ME
	 * @param replyUrl
	 *            DOCUMENT_ME
	 *
	 */
	void processNotifications(Integer postId, int watchType, String postUrl, String replyUrl);

	/**
	 * @param limit
	 *            DOCUMENT_ME
	 *
	 * @return the list of posts in the limit
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsDesc(int limit) throws ModuleException;

	/**
	 * @param limit
	 *            DOCUMENT_ME
	 *
	 * @return the list of ascendent posts in the limit
	 * @throws ModuleException
	 *             DOCUMENT_ME
	 */
	List<Post> findPostsAsc(int limit) throws ModuleException;

	/**
	 * @param attachments
	 *            DOCUMENT_ME
	 * @param post
	 *            DOCUMENT_ME
	 *
	 * @return the post where the attachment is done
	 */
	Post addAttachments(Collection<Attachment> attachments, Post post);

	/**
	 * @param post
	 *            DOCUMENT_ME
	 *
	 * @return the list of attachments of the post
	 */
	Collection<Attachment> findAttachments(Post post);

	/**
	 * @param post
	 *            DOCUMENT_ME
	 * @return the post where the attachments are removed
	 */
	Post removeAttachments(Post post);

	/**
	 * @param attachments
	 *            DOCUMENT_ME
	 * @param post
	 *            DOCUMENT_ME
	 *
	 * @return the post where the attachments are updated
	 */
	Post updateAttachments(Collection<Attachment> attachments, Post post);

	/**
	 * @param object
	 *            DOCUMENT_ME
	 */
	void update(Object object);

}