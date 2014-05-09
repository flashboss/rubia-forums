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
package org.vige.rubia;

import static org.vige.rubia.ui.Constants.POST_ANNOUNCE;
import static org.vige.rubia.ui.Constants.TOPIC_UNLOCKED;
import static org.vige.rubia.util.NotificationEngine.MODE_POST;
import static org.vige.rubia.util.NotificationEngine.MODE_REPLY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.jboss.solder.logging.TypedCategory;
import org.vige.rubia.auth.User;
import org.vige.rubia.log.ForumsModuleImplLog;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.ForumInstance;
import org.vige.rubia.model.ForumWatch;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import org.vige.rubia.model.Topic;
import org.vige.rubia.model.TopicWatch;
import org.vige.rubia.model.Watch;
import org.vige.rubia.util.NotificationEngine;

/**
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * @version $Revision: 3217 $
 * @ejb3
 */

@Stateless
public class ForumsModuleImpl implements ForumsModule {

	@Inject
	@TypedCategory(ForumsModuleImpl.class)
	private ForumsModuleImplLog logger;

	@PersistenceContext(unitName = "forums")
	private EntityManager em;

	@Inject
	private NotificationEngine notificationEngine;

	private String guestUserName = "guest";

	private String fromAddress = "portal@example.com";

	@PostConstruct
	public void startService() {
		notificationEngine.setFrom(fromAddress);
	}

	/**
	 * @jmx.managed-attribute
	 */
	@Override
	public String getGuestUserName() {
		return guestUserName;
	}

	/**
	 * @jmx.managed-attribute
	 */
	@Override
	public void setGuestUserName(String guestUserName) {
		this.guestUserName = guestUserName;
	}

	/**
	 * @jmx.managed-attribute
	 */
	@Override
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * @jmx.managed-attribute
	 */
	@Override
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	@Override
	public Forum findForumById(Integer id) throws ModuleException {
		if (id != null) {
			try {
				Forum forum = em.find(Forum.class, id);
				if (forum == null) {
					throw new ModuleException("No forum found for " + id);
				}

				return forum;
			} catch (Exception e) {
				String message = "Cannot find forum by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Forum findForumByIdFetchTopics(Integer id) throws ModuleException {
		if (id != null) {
			try {
				Query query = em.createNamedQuery("findForumByIdFetchTopics");
				query.setParameter("forumId", id);
				List<Forum> forumList = query.getResultList();
				if (forumList == null) {
					throw new ModuleException("No forum found for " + id);
				}

				if (forumList.size() > 0) {
					return (Forum) forumList.get(0);
				} else {
					return null;
				}
			} catch (Exception e) {
				String message = "Cannot find forum by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Category findCategoryById(Integer id) throws ModuleException {
		if (id != null) {
			try {
				Category category = em.find(Category.class, id);
				if (category == null) {
					throw new ModuleException("No category found for " + id);
				}

				return category;
			} catch (Exception e) {
				String message = "Cannot find category by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Category findCategoryByIdFetchForums(Integer id)
			throws ModuleException {
		if (id != null) {
			try {
				Query query = em
						.createNamedQuery("findCategoryByIdFetchForums");
				query.setParameter("categoryId", id);
				Category category = (Category) uniqueElement(query
						.getResultList());
				if (category == null) {
					throw new ModuleException("No category found for " + id);
				}

				return category;
			} catch (Exception e) {
				String message = "Cannot find category by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Poster findPosterByUserId(String userId) throws ModuleException {
		if (userId != null) {
			try {
				Query query = em.createNamedQuery("findPosterByUserId");
				query.setParameter("userId", userId);
				Poster user = (Poster) uniqueElement(query.getResultList());
				return user;
			} catch (Exception e) {
				String message = "Cannot find poster by name " + userId;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("user name cannot be null");
		}
	}

	@Override
	public Poster createPoster(String userId) throws ModuleException {
		if (userId != null) {
			try {
				Poster user = new Poster();
				user.setUserId(userId);
				em.persist(user);
				em.flush();
				return user;
			} catch (Exception e) {
				String message = "Cannot create Poster";
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("user name cannot be null");
		}
	}

	/*
	 * findCategories, findCategoriesFetchForums, findForums methods need to a
	 * forumInstanceId argument to take only objects of a specific forum
	 * instance
	 */

	@Override
	public List<Category> findCategories(Integer indexInstance)
			throws ModuleException {
		try {
			Query query = em.createNamedQuery("findCategories");
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find categories";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Category> findCategoriesFetchForums(Integer indexInstance)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findCategoriesFetchForums");
			query.setParameter("forumInstanceId", indexInstance);
			List<Category> categoriesWithDuplicates = query.getResultList();
			List<Category> categories = new LinkedList<Category>();
			for (Category category : categoriesWithDuplicates) {
				if (!categories.contains(category)) {
					categories.add(category);
				}
			}
			return categories;
		} catch (Exception e) {
			String message = "Cannot find categories";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Forum> findForums(Integer indexInstance) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForums");
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forums";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Forum> findForumsByCategory(Category category)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForumsByCategoryId");
			query.setParameter("categoryId", category);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forums";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findAnnouncements(Forum forum) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findAnnouncements");
			query.setParameter("forumid", "" + forum.getId());
			query.setParameter("type", "" + POST_ANNOUNCE);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forums";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	/*
	 * findTopics method need to a forumInstanceId argument to take only topics
	 * of a specific forum instance
	 */
	@Override
	public List<Topic> findTopics(Integer indexInstance) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopics");
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}

	}

	/**
	 * 
	 * @param forum
	 * @param type
	 * @param start
	 * @param perPage
	 * @param order
	 * @return
	 * @throws ModuleException
	 */
	private List<Topic> findTopics(Forum forum, int type, int start,
			int perPage, String order) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopicsType" + order);
			query.setFirstResult(start);
			query.setMaxResults(perPage);
			query.setParameter("forumid", forum);
			query.setParameter("type", type);
			List<Topic> list = query.getResultList();
			return list;
		} catch (Exception e) {
			String message = "Cannot find topics";
			throw new ModuleException(message, e);
		}
	}

	/**
	 * 
	 * @param forum
	 * @param start
	 * @param perPage
	 * @param order
	 * @return
	 * @throws ModuleException
	 */
	private List<Topic> findTopics(Forum forum, int start, int perPage,
			String order) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopicsForum" + order);
			query.setFirstResult(start);
			query.setMaxResults(perPage);
			query.setParameter("forumid", forum);
			List<Topic> list = query.getResultList();
			return list;
		} catch (Exception e) {
			String message = "Cannot find topics";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsAsc(Forum forum, int type, int start,
			int perPage) throws ModuleException {
		return findTopics(forum, type, start, perPage, "asc");
	}

	@Override
	public List<Topic> findTopicsDesc(Forum forum, int type, int start,
			int perPage) throws ModuleException {
		return findTopics(forum, type, start, perPage, "desc");
	}

	@Override
	public List<Topic> findTopicsAsc(Forum forum, int start, int perPage)
			throws ModuleException {
		return findTopics(forum, start, perPage, "asc");
	}

	@Override
	public List<Topic> findTopicsDesc(Forum forum, int start, int perPage)
			throws ModuleException {
		return findTopics(forum, start, perPage, "desc");
	}

	@Override
	public List<Topic> findTopicsBefore(Forum forum, int type, int start,
			int perPage, Date date) throws ModuleException {
		return null;
	}

	/*
	 * findTopicsHot, findTopicsByLatestPosts, findTopicsHottest,
	 * findTopicsMostViewed methods need to a forumInstanceId argument to take
	 * only topics of a specific forum instance
	 */

	@Override
	public List<Topic> findTopicsHot(int replies, int limit,
			Integer indexInstance) throws ModuleException {
		try {
			Query query = em.createNamedQuery("findTopicsHot");
			query.setMaxResults(limit);
			query.setParameter("replies", replies);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsByLatestPosts(int limit, Integer indexInstance)
			throws ModuleException {
		try {
			Query query = em.createNamedQuery("findTopicsByLatestPosts");
			query.setMaxResults(limit);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsHottest(Date after, int limit,
			Integer indexInstance) throws ModuleException {
		try {
			Query query = em.createNamedQuery("findTopicsHottest");
			query.setMaxResults(limit);
			query.setParameter("after", after, TemporalType.DATE);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsMostViewed(Date after, int limit,
			Integer indexInstance) throws ModuleException {
		try {
			Query query = em.createNamedQuery("findTopicsMostViewed");
			query.setMaxResults(limit);
			query.setParameter("after", after, TemporalType.DATE);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public Post createTopic(Forum forum, Message message, Date creationDate,
			Poster poster, Poll poll, Collection<Attachment> attachments,
			int type) throws ModuleException {
		try {

			if (poster.getId() == null
					|| em.find(Poster.class, poster.getId()) == null)
				em.persist(poster);
			em.merge(poster);
			em.persist(poll);
			for (PollOption pollOption : poll.getOptions())
				em.persist(pollOption);
			em.flush();

			Post post = new Post();
			post.setMessage(message);
			post.setCreateDate(creationDate);
			post.setPoster(poster);
			if (attachments != null)
				for (Attachment attachment : attachments) {
					em.persist(attachment);
					post.addAttachment(attachment);
				}

			Topic topic = new Topic();
			topic.setSubject(message.getSubject());
			topic.setForum(forum);
			topic.setPoster(poster);
			post.setTopic(topic);
			topic.setLastPostDate(creationDate);
			topic.setType(type);
			topic.setStatus(TOPIC_UNLOCKED);
			topic.setPoll(poll);

			em.persist(topic);
			em.persist(post);
			em.flush();

			forum.addTopicSize();
			forum.addPostSize();
			em.merge(forum);
			post.setTopic(topic);
			em.persist(post);

			notificationEngine.scheduleForNotification(post.getId(), MODE_POST);
			em.flush();
			return post;
		} catch (Exception e) {
			String errorMessage = "Cannot create topic";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Topic createTopic(Forum forum, String userId, String subject,
			int type) throws ModuleException {
		try {

			Poster poster = findPosterByUserId(userId);

			if (poster == null) {
				poster = createPoster(userId);
			}
			em.persist(poster);

			Topic topic = new Topic();
			topic.setSubject(subject);
			topic.setForum(forum);
			topic.setPoster(poster);
			topic.setType(type);

			topic.setPoll(null);

			em.persist(topic);
			em.flush();

			return topic;
		} catch (Exception e) {
			String errorMessage = "Cannot create topic";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Post createPost(Topic topic, Forum forum, Message message,
			Date creationDate, Poster poster, Collection<Attachment> attachments)
			throws ModuleException {
		try {

			Poster posterOld = findPosterByUserId(poster.getUserId());
			if (posterOld == null) {
				em.persist(poster);
			}
			em.merge(poster);
			Post post = new Post();
			post.setMessage(message);
			post.setCreateDate(creationDate);
			post.setPoster(poster);
			if (attachments != null)
				for (Attachment attachment : attachments) {
					em.persist(attachment);
					post.addAttachment(attachment);
				}

			em.persist(post);
			em.flush();

			post.setTopic(topic);
			topic.setLastPostDate(post.getCreateDate());
			topic.setReplies(topic.getReplies() + 1);
			em.merge(topic);
			forum.addPostSize();
			em.merge(forum);
			notificationEngine
					.scheduleForNotification(post.getId(), MODE_REPLY);
			em.flush();
			return post;
		} catch (Exception e) {
			String errorMessage = "Cannot create topic";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Poll addPollToTopic(Topic topic, Poll poll) throws ModuleException {
		try {
			Query query = em.createNamedQuery("findPoll");
			query.setParameter("topicid", topic.getId());
			Poll oldpoll = null;
			try {
				oldpoll = (Poll) query.getSingleResult();
			} catch (NoResultException ex) {
				oldpoll = null;
			}
			if (oldpoll != null) {
				em.remove(oldpoll);
			}
			em.persist(poll);
			topic.setPoll(poll);
			for (PollOption pollOption : poll.getOptions()) {
				pollOption.setPoll(poll);
				em.persist(pollOption);
			}
			update(topic);
			em.flush();
			return poll;
		} catch (Exception e) {
			String errorMessage = "Cannot add poll to topic";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	/*
	 * getLastCategoryOrder and createCategory methods need to a forumInstanceId
	 * argument to take only categories of a specific forum instance
	 */
	private int getLastCategoryOrder(Integer indexInstance) {
		try {

			Query query = em.createNamedQuery("getLastCategoryOrder");
			query.setParameter("forumInstanceId", indexInstance);
			Integer lastCategoryOrder = (Integer) uniqueElement(query
					.getResultList());
			return (lastCategoryOrder != null) ? lastCategoryOrder.intValue()
					: 0;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Category createCategory(String name, ForumInstance forumInstance)
			throws ModuleException {
		try {

			Category category = new Category();
			category.setTitle(name);
			category.setOrder(getLastCategoryOrder(forumInstance.getId()) + 10);
			category.setForumInstance(forumInstance);
			em.persist(category);
			em.flush();
			return category;
		} catch (Exception e) {
			String errorMessage = "Cannot create topic";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removeCategory(int categoryId) throws ModuleException {

		try {
			em.remove(em.find(Category.class, categoryId));
			em.flush();
		} catch (Exception e) {
			String errorMessage = "Cannot delete category";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removeForum(int forumId) throws ModuleException {

		try {
			em.remove(em.find(Forum.class, forumId));
			em.flush();
		} catch (Exception e) {
			String errorMessage = "Cannot delete forum";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removePost(int postId, boolean isLastPost)
			throws ModuleException {

		try {
			Post post = em.find(Post.class, postId);
			Topic topic = post.getTopic();
			Forum forum = topic.getForum();
			em.remove(post);
			topic.setReplies(topic.getReplies() - 1);
			forum.setPostCount(forum.getPostCount() - 1);
			List<Post> posts = findPostsByTopicId(topic);
			Post lastPost = posts.get(posts.size() - 1);
			if (isLastPost) {
				topic.setLastPostDate(lastPost.getCreateDate());
			}
			em.merge(topic);
			em.merge(forum);
			em.flush();
		} catch (Exception e) {
			String errorMessage = "Cannot delete post";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removePollInTopic(Topic topic) throws ModuleException {

		try {
			if (topic != null && topic.getPoll() != null) {
				Poll poll = em.find(Poll.class, topic.getPoll().getId());
				topic.setPoll(null);
				em.remove(poll);
				em.flush();
			}

		} catch (Exception e) {
			String errorMessage = "Cannot delete poll";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removeTopic(int topicId) throws ModuleException {

		try {
			Topic topic = em.find(Topic.class, topicId);
			Forum forum = topic.getForum();
			if (forum != null) {
				topic.setForum(null);
				forum.setPostCount(forum.getPostCount() - topic.getReplies()
						- 1);
				forum.setTopicCount(forum.getTopicCount() - 1);
				em.merge(forum);
				em.remove(em.merge(topic));
			} else {
				em.remove(topic);
			}
			em.flush();
		} catch (Exception e) {
			String errorMessage = "Cannot delete topic";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	private int getLastForumOrder(Category category) {
		try {

			Query query = em.createNamedQuery("getLastForumOrder");
			query.setParameter("categoryId", "" + category.getId());
			Integer lastForumOrder = (Integer) uniqueElement(query
					.getResultList());
			return (lastForumOrder != null) ? lastForumOrder.intValue() : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Forum createForum(Category category, String name, String description)
			throws ModuleException {
		try {

			Forum forum = new Forum();
			forum.setCategory(category);
			forum.setName(name);
			forum.setDescription(description);
			forum.setOrder(getLastForumOrder(category) + 10);
			em.persist(forum);
			em.flush();
			return forum;
		} catch (Exception e) {
			String errorMessage = "Cannot create forum";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Topic findTopicById(Integer id) throws ModuleException {
		if (id != null) {
			try {

				Topic topic = (Topic) em.find(Topic.class, id);
				if (topic == null) {
					throw new ModuleException("No topic found for " + id);
				}

				return topic;
			} catch (Exception e) {
				String message = "Cannot find forum by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public List<Topic> findTopics(Forum forum) throws ModuleException {
		if (forum != null) {
			try {

				Query query = em.createNamedQuery("findTopicsForumNoOrder");
				query.setParameter("forumid", forum);
				return query.getResultList();
			} catch (Exception e) {
				String message = "Cannot find topics";
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("forum cannot be null");
		}
	}

	/*
	 * findPosts method need to a forumInstanceId argument to take only posts of
	 * a specific forum instance
	 */
	@Override
	public List<Post> findPosts(Integer indexInstance) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPosts");
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public Post findPostById(Integer id) throws ModuleException {
		if (id != null) {
			try {

				Post post = em.find(Post.class, id);
				if (post == null) {
					throw new ModuleException("No post found for " + id);
				}

				return post;
			} catch (Exception e) {
				String message = "Cannot find post by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Attachment findFindAttachmentById(Integer attachID)
			throws ModuleException {
		if (attachID != null) {
			try {

				Attachment attach = em.find(Attachment.class, attachID);
				if (attach == null) {
					throw new ModuleException("No attachment found for "
							+ attachID);
				}

				return attach;
			} catch (Exception e) {
				String message = "Cannot find attachment by id " + attachID;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	private List<Post> findPostsByTopicId(Topic topic, int start, int limit,
			String order) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPostsByTopicId" + order);
			query.setParameter("topicId", topic);
			query.setFirstResult(start);
			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	private List<Post> findPostsByIdsFetchAttachmentsAndPosters(
			Collection<Post> posts, String order) throws ModuleException {

		if (posts == null || posts.size() == 0) {
			return new LinkedList<Post>();
		}

		try {

			Query query = em
					.createNamedQuery("findPostsByIdsFetchAttachmentsAndPosters"
							+ order);
			query.setParameter("postIds", posts);
			List<Post> it = query.getResultList();
			List<Post> list = new LinkedList<Post>();
			for (Post post : it) {
				if (!list.contains(post)) {
					list.add(post);
				}
			}
			return list;
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsByIdsAscFetchAttachmentsAndPosters(
			Collection<Post> posts) throws ModuleException {
		return findPostsByIdsFetchAttachmentsAndPosters(posts, "asc");
	}

	@Override
	public List<Post> findPostsByIdsDescFetchAttachmentsAndPosters(
			Collection<Post> posts) throws ModuleException {
		return findPostsByIdsFetchAttachmentsAndPosters(posts, "desc");
	}

	private List<Post> findPostIds(Topic topic, int start, int limit,
			String order) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPostIds" + order);
			query.setParameter("topicId", topic);
			query.setFirstResult(start);
			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find post ids";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostIdsAsc(Topic topic, int start, int limit)
			throws ModuleException {
		return findPostIds(topic, start, limit, "asc");
	}

	@Override
	public List<Post> findPostIdsDesc(Topic topic, int start, int limit)
			throws ModuleException {
		return findPostIds(topic, start, limit, "desc");
	}

	@Override
	public List<Post> findPostsByTopicId(Topic topic) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPostsByTopicIdNoOrder");
			query.setParameter("topicId", topic);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public void addAllForums(Category source, Category target)
			throws ModuleException {
		List<Forum> sourceForums = findForumsByCategory(source);
		List<Forum> targetForums = findForumsByCategory(target);
		targetForums.addAll(sourceForums);
		for (Forum forum : targetForums) {
			forum.setCategory(target);
		}
		source.setForums(new ArrayList<Forum>());
	}

	@Override
	public List<Post> findPostsByTopicIdAsc(Topic topic, int start, int limit)
			throws ModuleException {
		return findPostsByTopicId(topic, start, limit, "asc");
	}

	@Override
	public List<Post> findPostsByTopicIdDesc(Topic topic, int start, int limit)
			throws ModuleException {
		return findPostsByTopicId(topic, start, limit, "desc");
	}

	public Date findLastPostDateForUser(User user) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findLastPostDateForUser");
			query.setParameter("userId", "" + user.getId().toString());
			Date lastPostDate = (Date) uniqueElement(query.getResultList());
			return lastPostDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Post findLastPost(Forum forum) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findLastPost");
			query.setParameter("forumId", "" + forum.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			Post lastPost = (Post) uniqueElement(query.getResultList());
			return lastPost;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Post findFirstPost(Topic topic) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findFirstPost");
			query.setParameter("lastPostDate", topic.getLastPostDate(),
					TemporalType.DATE);
			query.setParameter("topicId", "" + topic.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			Post firstPost = (Post) uniqueElement(query.getResultList());
			return firstPost;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Post findLastPost(Topic topic) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findLastPostOrder");
			query.setParameter("topicId", "" + topic.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			Post lastPost = (Post) uniqueElement(query.getResultList());
			return lastPost;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<Object, Object> findLastPostsOfTopics(Collection<Topic> topics)
			throws ModuleException {
		try {

			em.createNamedQuery("findLastPostsOfTopics");
			List<Object[]> lastPostDates = new ArrayList<Object[]>(
					topics.size());
			List<Date> dates = new LinkedList<Date>();
			for (Topic tmpTopic : topics) {
				dates.add(tmpTopic.getLastPostDate());
				lastPostDates.add(new Object[] { tmpTopic.getLastPostDate(),
						tmpTopic.getId() });
			}

			// if there are no createDates then we return an empty map
			if (dates.size() == 0) {
				return new HashMap<Object, Object>(0);
			}

			Query query = em
					.createNamedQuery("findLastPostsOfTopicsCreateDate");
			query.setParameter("dates", dates);
			List<Object[]> posts = query.getResultList();
			Map<Object, Object> forumPostMap = new HashMap<Object, Object>(
					dates.size());
			for (Object[] dateTopic : lastPostDates) {
				int index = Collections.binarySearch(posts, dateTopic,
						new Comparator<Object>() {
							public int compare(Object o1, Object o2) {
								Object[] datePostPair1 = (Object[]) o1;
								Object[] datePostPair2 = (Object[]) o2;
								Date postDate1 = (Date) datePostPair1[0];
								Date postDate2 = (Date) datePostPair2[0];
								return postDate1.compareTo(postDate2);
							}
						});
				if (index < 0) {
					continue;
				}
				Object[] datePostPair = (Object[]) posts.get(index);
				forumPostMap.put(dateTopic[1], datePostPair[1]);
			}
			return forumPostMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * findLastPostsOfForums, findForumWatchByUser, findForumWatchedByUser,
	 * findTopicWatchedByUser and findTopicWatches methods need to a
	 * forumInstanceId argument to take only objects of a specific forum
	 * instance
	 */
	@Override
	public Map<Object, Post> findLastPostsOfForums(Integer indexInstance)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findLastPostsOfForums");
			query.setParameter("forumInstanceId", indexInstance);
			List<Object[]> createDates = query.getResultList();
			List<Object> dates = new LinkedList<Object>();
			for (Object[] post : createDates) {
				dates.add(post[0]);
			}

			// if there are no posts in all forums then return empty map
			if (dates.size() == 0) {
				return new HashMap<Object, Post>(0);
			}

			query = em.createNamedQuery("findLastPostsOfForumsCreateDate");
			query.setParameter("dates", dates);
			List<Post[]> posts = query.getResultList();
			Map<Object, Post> forumPostMap = new HashMap<Object, Post>(
					createDates.size());
			for (Object[] dateForum : createDates) {
				int index = Collections.binarySearch(posts, dateForum,
						new Comparator<Object>() {
							public int compare(Object o1, Object o2) {
								Object[] datePostPair1 = (Object[]) o1;
								Object[] datePostPair2 = (Object[]) o2;
								Date postDate1 = (Date) datePostPair1[0];
								Date postDate2 = (Date) datePostPair2[0];
								return postDate1.compareTo(postDate2);
							}
						});
				if (index < 0) {
					continue;
				}
				Object[] datePostPair = posts.get(index);
				forumPostMap.put(dateForum[1], (Post) datePostPair[1]);
			}
			return forumPostMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ForumWatch> findForumWatchByUser(User user,
			Integer indexInstance) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForumWatchByUser");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forum watch";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Forum> findForumWatchedByUser(User user, Integer indexInstance)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForumWatchedByUser");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forum watched";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicWatchedByUser(User user, Integer indexInstance)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopicWatchedByUser");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topic watched";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicWatchedByUser(User user, Date datePoint,
			Integer indexInstance) throws ModuleException {
		try {

			Query query = em
					.createNamedQuery("findTopicWatchedByUserCreateDate");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("datePoint", datePoint, TemporalType.TIMESTAMP);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topic watched";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	/**
	 * 
	 * This method returns Map<Integer,TopicWatch> pairs where Integer key is
	 * watched topic id.
	 * 
	 * @param user
	 * @return
	 * @throws ModuleException
	 */
	@Override
	public Map<Object, Object> findTopicWatches(User user, Integer indexInstance)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopicWatches");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			List<Object[]> results = query.getResultList();
			HashMap<Object, Object> map = new HashMap<Object, Object>(
					results.size());
			for (Object[] element : results) {
				map.put(element[0], element[1]);
			}
			return map;
		} catch (Exception e) {
			String message = "Cannot find topic watches";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	private List<Post> findPostsFromForum(Forum forum, int limit, String order)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPostsFromForum" + order);

			query.setParameter("forumId", forum.getId());

			query.setFirstResult(0);

			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsFromForumAsc(Forum forum, int limit)
			throws ModuleException {
		return findPostsFromForum(forum, limit, "asc");
	}

	@Override
	public List<Post> findPostsFromForumDesc(Forum forum, int limit)
			throws ModuleException {
		return findPostsFromForum(forum, limit, "desc");
	}

	private List<Post> findPostsFromCategory(Category category, int limit,
			String order) throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPostsFromCategory" + order);

			query.setParameter("categoryId", category.getId());

			query.setFirstResult(0);

			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsFromCategoryAsc(Category category, int limit)
			throws ModuleException {
		return findPostsFromCategory(category, limit, "asc");
	}

	@Override
	public List<Post> findPostsFromCategoryDesc(Category category, int limit)
			throws ModuleException {
		return findPostsFromCategory(category, limit, "desc");
	}

	private List<Post> findPosts(int limit, String order)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findPostsOrder" + order);
			query.setFirstResult(0);

			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsAsc(int limit) throws ModuleException {
		return findPosts(limit, "asc");
	}

	@Override
	public List<Post> findPostsDesc(int limit) throws ModuleException {
		return findPosts(limit, "desc");
	}

	@Override
	public void createWatch(Poster poster, Forum forum, int mode)
			throws ModuleException {
		try {

			if (poster == null) {
				throw new ModuleException("poster must not be null");
			}

			if (forum == null) {
				throw new ModuleException("forum must not be null");
			}

			Poster posterOld = findPosterByUserId(poster.getUserId());
			if (posterOld == null) {
				em.persist(poster);
			}
			em.merge(poster);

			ForumWatch forumWatch = new ForumWatch();
			forumWatch.setPoster(poster);
			forumWatch.setForum(forum);
			forumWatch.setMode(mode);
			em.persist(forumWatch);
			em.flush(); // it is required
			// for clustered versions

		} catch (Exception e) {
			String errorMessage = "Cannot create forum watch";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removeWatch(Watch watch) throws ModuleException {

		try {
			em.remove(em.find(Watch.class, watch.getId()));
			em.flush(); // it is required
			// for clustered versions
		} catch (Exception e) {
			String errorMessage = "Cannot delete watch";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public ForumWatch findForumWatchById(Integer forumWatchId)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForumWatchById");
			query.setParameter("forumWatchId", forumWatchId);
			return (ForumWatch) uniqueElement(query.getResultList());
		} catch (Exception e) {
			String message = "Cannot find forum watch";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	/**
	 * 
	 * This method returns Map<Integer,ForumWatch> pairs where Integer key is
	 * watched forum id.
	 * 
	 * @param user
	 * @return
	 * @throws ModuleException
	 */
	/*
	 * findForumWatches method need to a forumInstanceId argument to take only
	 * forums of a specific forum instance
	 */
	@Override
	public Map<Object, Object> findForumWatches(User user, Integer indexInstance)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForumWatches");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			List<Object[]> results = query.getResultList();
			HashMap<Object, Object> map = new HashMap<Object, Object>(
					results.size());
			for (Object[] element : results) {
				map.put(element[0], element[1]);
			}
			return map;
		} catch (Exception e) {
			String message = "Cannot find forum watches";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public ForumWatch findForumWatchByUserAndForum(User user, int forumId)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findForumWatchByUserAndForum");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumId", forumId);
			Object obj = uniqueElement(query.getResultList());
			if (obj == null) {
				return null;
			} else {
				return (ForumWatch) obj;
			}
		} catch (Exception e) {
			String message = "Cannot find forum watch";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public TopicWatch findTopicWatchByUserAndTopic(User user, int topicId)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopicWatchByUserAndTopic");
			query.setParameter("userId", user.getId().toString());
			query.setParameter("topicId", topicId);
			Object obj = uniqueElement(query.getResultList());
			if (obj == null) {
				return null;
			} else {
				return (TopicWatch) obj;
			}
		} catch (Exception e) {
			String message = "Cannot find topic watch";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public void createWatch(Poster poster, Topic topic, int mode)
			throws ModuleException {
		try {

			if (poster == null) {
				throw new ModuleException("poster must not be null");
			}

			if (topic == null) {
				throw new ModuleException("topic must not be null");
			}

			Poster posterOld = findPosterByUserId(poster.getUserId());
			if (posterOld == null) {
				em.persist(poster);
			}
			em.merge(poster);

			em.flush(); // it is required
			// for clustered versions

			TopicWatch topicWatch = new TopicWatch();
			topicWatch.setPoster(poster);
			topicWatch.setTopic(topic);
			topicWatch.setMode(mode);
			em.persist(topicWatch);
			em.flush(); // it is required
			// for clustered versions
		} catch (Exception e) {
			String errorMessage = "Cannot create topic watch";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public TopicWatch findTopicWatchById(Integer topicWatchId)
			throws ModuleException {
		try {

			Query query = em.createNamedQuery("findTopicWatchById");
			query.setParameter("topicWatchId", topicWatchId.toString());
			return (TopicWatch) uniqueElement(query.getResultList());
		} catch (Exception e) {
			String message = "Cannot find topic watch";
			logger.error(message, e);
			throw new ModuleException(message, e);
		}
	}

	@Override
	public void processNotifications(Integer postId, int watchType,
			String postUrl, String replyUrl) {
		notificationEngine.schedule(postId, watchType, postUrl, replyUrl);
	}

	/*
	 * I add createForumInstance, removeForumInstance and findForumInstanceById
	 * methods to manage ForumInstance object
	 */
	@Override
	public ForumInstance createForumInstance(Integer indexInstance, String name)
			throws ModuleException {
		try {

			ForumInstance forumInstance = new ForumInstance();
			forumInstance.setId(indexInstance);
			forumInstance.setName(name);
			em.persist(forumInstance);
			em.flush(); // it is required
			// for clustered versions
			return forumInstance;
		} catch (Exception e) {
			String errorMessage = "Cannot create forum Instance";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removeForumInstance(int forumInstanceId) throws ModuleException {

		try {
			// em.flush();
			ForumInstance forumInstance = em.find(ForumInstance.class,
					forumInstanceId);
			em.remove(forumInstance);
			em.flush(); // it is required
			// for clustered versions
			// em.flush();
		} catch (Exception e) {
			String errorMessage = "Cannot delete forum Instance";
			logger.error(errorMessage, e);
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public ForumInstance findForumInstanceById(Integer id)
			throws ModuleException {
		if (id != null) {
			try {

				ForumInstance forumInstance = (ForumInstance) em.find(
						ForumInstance.class, id);
				return forumInstance;
			} catch (Exception e) {
				String message = "Cannot find forum instance by id " + id;
				logger.error(message, e);
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public void update(Object object) {

		em.merge(object);
		em.flush();
	}

	@Override
	public Post addAttachments(Collection<Attachment> attachments, Post post) {
		if (attachments != null) {
			for (Attachment attachment : attachments) {
				attachment.setId(null);
				attachment.setPost(post);
				em.persist(attachment);
			}
			em.flush();
		}
		post = em.find(Post.class, post.getId());
		em.refresh(post);
		return post;
	}

	@Override
	public Post removeAttachments(Post post) {
		Collection<Attachment> attachments = findAttachments(post);
		if (attachments != null) {
			for (Attachment attachment : attachments) {
				em.remove(em.find(Attachment.class, attachment.getId()));
			}
		}
		return post;
	}

	@Override
	public Post updateAttachments(Collection<Attachment> attachments, Post post) {
		Post postForRemove = removeAttachments(post);
		Post updatedPost = addAttachments(attachments, postForRemove);
		return updatedPost;
	}

	@Override
	public Collection<Attachment> findAttachments(Post post) {
		if (post != null) {
			Query query = em.createNamedQuery("findAttachments");
			query.setParameter("postId", post.getId());
			return query.getResultList();
		} else
			return null;
	}

	static Object uniqueElement(List<Object> list)
			throws NonUniqueResultException {
		int size = list.size();
		if (size == 0)
			return null;
		Object first = list.get(0);
		for (int i = 1; i < size; i++) {
			if (list.get(i) != first) {
				throw new NonUniqueResultException(list.size() + "");
			}
		}
		return first;
	}
}
