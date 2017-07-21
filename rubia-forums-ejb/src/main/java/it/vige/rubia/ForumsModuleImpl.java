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

import static it.vige.rubia.Constants.TOPIC_UNLOCKED;
import static it.vige.rubia.model.TopicType.ADVICE;
import static it.vige.rubia.util.NotificationEngine.MODE_POST;
import static it.vige.rubia.util.NotificationEngine.MODE_REPLY;
import static org.jboss.logging.Logger.getLogger;

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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import it.vige.rubia.auth.User;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.ForumInstance;
import it.vige.rubia.model.ForumWatch;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.model.TopicType;
import it.vige.rubia.model.TopicWatch;
import it.vige.rubia.model.Watch;
import it.vige.rubia.util.NotificationEngine;

/**
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * @version $Revision: 3217 $
 */

@Stateless
public class ForumsModuleImpl implements ForumsModule {

	private static Logger log = getLogger(ForumsModuleImpl.class);

	@PersistenceContext(unitName = "forums")
	private EntityManager em;

	@EJB
	private NotificationEngine notificationEngine;

	private String guestUserName = "guest";

	private String fromAddress = "portal@example.com";

	@PostConstruct
	public void startService() {
		notificationEngine.setFrom(fromAddress);
	}

	/**
	 * @see it.vige.rubia.ForumsModule#getGuestUserName()
	 */
	@Override
	public String getGuestUserName() {
		return guestUserName;
	}

	/**
	 * @see it.vige.rubia.ForumsModule#setGuestUserName(java.lang.String)
	 */
	@Override
	public void setGuestUserName(String guestUserName) {
		this.guestUserName = guestUserName;
	}

	/**
	 * @see it.vige.rubia.ForumsModule#getFromAddress()
	 */
	@Override
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * @see it.vige.rubia.ForumsModule#setFromAddress(java.lang.String)
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
				TypedQuery<Forum> query = em.createNamedQuery("findForumByIdFetchTopics", Forum.class);
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
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Category findCategoryByIdFetchForums(Integer id) throws ModuleException {
		if (id != null) {
			try {
				TypedQuery<Category> query = em.createNamedQuery("findCategoryByIdFetchForums", Category.class);
				query.setParameter("categoryId", id);
				Category category = (Category) uniqueElement(query.getResultList());
				if (category == null) {
					throw new ModuleException("No category found for " + id);
				}

				return category;
			} catch (Exception e) {
				String message = "Cannot find category by id " + id;
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
				TypedQuery<Poster> query = em.createNamedQuery("findPosterByUserId", Poster.class);
				query.setParameter("userId", userId);
				Poster user = uniqueElement(query.getResultList());
				return user;
			} catch (Exception e) {
				String message = "Cannot find poster by name " + userId;
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
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("user name cannot be null");
		}
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findCategories(java.lang.Integer)
	 */
	@Override
	public List<Category> findCategories(Integer indexInstance) throws ModuleException {
		try {
			TypedQuery<Category> query = em.createNamedQuery("findCategories", Category.class);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find categories";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Category> findCategoriesFetchForums(Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Category> query = em.createNamedQuery("findCategoriesFetchForums", Category.class);
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
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Forum> findForums(Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Forum> query = em.createNamedQuery("findForums", Forum.class);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forums";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Forum> findForumsByCategory(Category category) throws ModuleException {
		try {

			TypedQuery<Forum> query = em.createNamedQuery("findForumsByCategoryId", Forum.class);
			query.setParameter("categoryId", category);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forums";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findAnnouncements(Forum forum) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findAnnouncements", Post.class);
			query.setParameter("forumid", "" + forum.getId());
			query.setParameter("type", "" + ADVICE);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forums";
			throw new ModuleException(message, e);
		}
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findTopics(java.lang.Integer)
	 */
	@Override
	public List<Topic> findTopics(Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Topic> query = em.createNamedQuery("findTopics", Topic.class);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
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
	private List<Topic> findTopics(Forum forum, TopicType type, int start, int perPage, String order)
			throws ModuleException {
		try {

			TypedQuery<Topic> query = em.createNamedQuery("findTopicsType" + order, Topic.class);
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
	private List<Topic> findTopics(Forum forum, int start, int perPage, String order) throws ModuleException {
		try {

			TypedQuery<Topic> query = em.createNamedQuery("findTopicsForum" + order, Topic.class);
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
	public List<Topic> findTopicsAsc(Forum forum, TopicType type, int start, int perPage) throws ModuleException {
		return findTopics(forum, type, start, perPage, "asc");
	}

	@Override
	public List<Topic> findTopicsDesc(Forum forum, TopicType type, int start, int perPage) throws ModuleException {
		return findTopics(forum, type, start, perPage, "desc");
	}

	@Override
	public List<Topic> findTopicsAsc(Forum forum, int start, int perPage) throws ModuleException {
		return findTopics(forum, start, perPage, "asc");
	}

	@Override
	public List<Topic> findTopicsDesc(Forum forum, int start, int perPage) throws ModuleException {
		return findTopics(forum, start, perPage, "desc");
	}

	@Override
	public List<Topic> findTopicsBefore(Forum forum, TopicType type, int start, int perPage, Date date)
			throws ModuleException {
		return null;
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findTopicsHot(int,int,java.lang.Integer)
	 */
	@Override
	public List<Topic> findTopicsHot(int replies, int limit, Integer indexInstance) throws ModuleException {
		try {
			TypedQuery<Topic> query = em.createNamedQuery("findTopicsHot", Topic.class);
			query.setMaxResults(limit);
			query.setParameter("replies", replies);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsByLatestPosts(int limit, Integer indexInstance) throws ModuleException {
		try {
			TypedQuery<Topic> query = em.createNamedQuery("findTopicsByLatestPosts", Topic.class);
			query.setMaxResults(limit);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsHottest(Date after, int limit, Integer indexInstance) throws ModuleException {
		try {
			TypedQuery<Topic> query = em.createNamedQuery("findTopicsHottest", Topic.class);
			query.setMaxResults(limit);
			query.setParameter("after", after, TemporalType.DATE);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicsMostViewed(Date after, int limit, Integer indexInstance) throws ModuleException {
		try {
			TypedQuery<Topic> query = em.createNamedQuery("findTopicsMostViewed", Topic.class);
			query.setMaxResults(limit);
			query.setParameter("after", after, TemporalType.DATE);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topics";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public Post createTopic(Forum forum, Message message, Date creationDate, Poster poster, Poll poll,
			Collection<Attachment> attachments, TopicType type) throws ModuleException {
		try {

			if (poster.getId() == null || em.find(Poster.class, poster.getId()) == null)
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
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Topic createTopic(Forum forum, String userId, String subject, TopicType type) throws ModuleException {
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
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Post createPost(Topic topic, Forum forum, Message message, Date creationDate, Poster poster,
			Collection<Attachment> attachments) throws ModuleException {
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
			notificationEngine.scheduleForNotification(post.getId(), MODE_REPLY);
			em.flush();
			return post;
		} catch (Exception e) {
			String errorMessage = "Cannot create topic";
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public Poll addPollToTopic(Topic topic, Poll poll) throws ModuleException {
		try {
			TypedQuery<Poll> query = em.createNamedQuery("findPoll", Poll.class);
			query.setParameter("topicid", topic.getId());
			Poll oldpoll = null;
			try {
				oldpoll = query.getSingleResult();
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
			throw new ModuleException(errorMessage, e);
		}
	}

	/**
	 * getLastCategoryOrder and createCategory methods need to a forumInstanceId
	 * argument to take only categories of a specific forum instance
	 * 
	 * @return the order number of the last category
	 */
	private int getLastCategoryOrder(Integer indexInstance) {
		try {

			TypedQuery<Integer> query = em.createNamedQuery("getLastCategoryOrder", Integer.class);
			query.setParameter("forumInstanceId", indexInstance);
			Integer lastCategoryOrder = uniqueElement(query.getResultList());
			return (lastCategoryOrder != null) ? lastCategoryOrder.intValue() : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Category createCategory(String name, ForumInstance forumInstance) throws ModuleException {
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
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removePost(int postId, boolean isLastPost) throws ModuleException {

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
				forum.setPostCount(forum.getPostCount() - topic.getReplies() - 1);
				forum.setTopicCount(forum.getTopicCount() - 1);
				em.merge(forum);
				em.remove(em.merge(topic));
			} else {
				em.remove(topic);
			}
			em.flush();
		} catch (Exception e) {
			String errorMessage = "Cannot delete topic";
			throw new ModuleException(errorMessage, e);
		}
	}

	private int getLastForumOrder(Category category) {
		try {

			TypedQuery<Integer> query = em.createNamedQuery("getLastForumOrder", Integer.class);
			query.setParameter("categoryId", category.getId());
			Integer lastForumOrder = uniqueElement(query.getResultList());
			return (lastForumOrder != null) ? lastForumOrder.intValue() : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Forum createForum(Category category, String name, String description) throws ModuleException {
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

				TypedQuery<Topic> query = em.createNamedQuery("findTopicsForumNoOrder", Topic.class);
				query.setParameter("forumid", forum);
				return query.getResultList();
			} catch (Exception e) {
				String message = "Cannot find topics";
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("forum cannot be null");
		}
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findPosts(java.lang.Integer)
	 */
	@Override
	public List<Post> findPosts(Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findPosts", Post.class);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
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
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	@Override
	public Attachment findAttachmentById(Integer attachID) throws ModuleException {
		if (attachID != null) {
			try {

				Attachment attach = em.find(Attachment.class, attachID);
				if (attach == null) {
					throw new ModuleException("No attachment found for " + attachID);
				}

				return attach;
			} catch (Exception e) {
				String message = "Cannot find attachment by id " + attachID;
				throw new ModuleException(message, e);
			}
		} else {
			throw new IllegalArgumentException("id cannot be null");
		}
	}

	private List<Post> findPostsByTopicId(Topic topic, int start, int limit, String order) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findPostsByTopicId" + order, Post.class);
			query.setParameter("topicId", topic);
			query.setFirstResult(start);
			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			throw new ModuleException(message, e);
		}
	}

	private List<Post> findPostsByIdsFetchAttachmentsAndPosters(Collection<Integer> posts, String order)
			throws ModuleException {

		if (posts == null || posts.size() == 0) {
			return new LinkedList<Post>();
		}

		try {

			TypedQuery<Post> query = em.createNamedQuery("findPostsByIdsFetchAttachmentsAndPosters" + order,
					Post.class);
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
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Integer> posts) throws ModuleException {
		return findPostsByIdsFetchAttachmentsAndPosters(posts, "asc");
	}

	@Override
	public List<Post> findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Integer> posts) throws ModuleException {
		return findPostsByIdsFetchAttachmentsAndPosters(posts, "desc");
	}

	private List<Integer> findPostIds(Topic topic, int start, int limit, String order) throws ModuleException {
		try {

			TypedQuery<Integer> query = em.createNamedQuery("findPostIds" + order, Integer.class);
			query.setParameter("topicId", topic);
			query.setFirstResult(start);
			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find post ids";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Integer> findPostIdsAsc(Topic topic, int start, int limit) throws ModuleException {
		return findPostIds(topic, start, limit, "asc");
	}

	@Override
	public List<Integer> findPostIdsDesc(Topic topic, int start, int limit) throws ModuleException {
		return findPostIds(topic, start, limit, "desc");
	}

	@Override
	public List<Post> findPostsByTopicId(Topic topic) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findPostsByTopicIdNoOrder", Post.class);
			query.setParameter("topicId", topic);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public void addAllForums(Category source, Category target) throws ModuleException {
		List<Forum> sourceForums = findForumsByCategory(source);
		List<Forum> targetForums = findForumsByCategory(target);
		targetForums.addAll(sourceForums);
		for (Forum forum : targetForums) {
			forum.setCategory(target);
		}
		source.setForums(new ArrayList<Forum>());
	}

	@Override
	public List<Post> findPostsByTopicIdAsc(Topic topic, int start, int limit) throws ModuleException {
		return findPostsByTopicId(topic, start, limit, "asc");
	}

	@Override
	public List<Post> findPostsByTopicIdDesc(Topic topic, int start, int limit) throws ModuleException {
		return findPostsByTopicId(topic, start, limit, "desc");
	}

	@Override
	public Date findLastPostDateForUser(User user) throws ModuleException {
		try {

			TypedQuery<Date> query = em.createNamedQuery("findLastPostDateForUser", Date.class);
			query.setParameter("userId", "" + user.getId().toString());
			Date lastPostDate = uniqueElement(query.getResultList());
			return lastPostDate;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public Post findLastPost(Forum forum) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findLastPost", Post.class);
			query.setParameter("forumId", "" + forum.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			Post lastPost = uniqueElement(query.getResultList());
			return lastPost;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public Post findFirstPost(Topic topic) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findFirstPost", Post.class);
			query.setParameter("lastPostDate", topic.getLastPostDate(), TemporalType.DATE);
			query.setParameter("topicId", "" + topic.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			Post firstPost = uniqueElement(query.getResultList());
			return firstPost;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public Post findLastPost(Topic topic) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findLastPostOrder", Post.class);
			query.setParameter("topicId", "" + topic.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			Post lastPost = (Post) uniqueElement(query.getResultList());
			return lastPost;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public Map<Object, Object> findLastPostsOfTopics(Collection<Topic> topics) throws ModuleException {
		try {

			List<Object[]> lastPostDates = new ArrayList<Object[]>(topics.size());
			List<Date> dates = new LinkedList<Date>();
			for (Topic tmpTopic : topics) {
				dates.add(tmpTopic.getLastPostDate());
				lastPostDates.add(new Object[] { tmpTopic.getLastPostDate(), tmpTopic.getId() });
			}

			// if there are no createDates then we return an empty map
			if (dates.size() == 0) {
				return new HashMap<Object, Object>(0);
			}

			TypedQuery<Object[]> query = em.createNamedQuery("findLastPostsOfTopicsCreateDate", Object[].class);
			query.setParameter("dates", dates);
			List<Object[]> posts = query.getResultList();
			Map<Object, Object> forumPostMap = new HashMap<Object, Object>(dates.size());
			for (Object[] dateTopic : lastPostDates) {
				int index = Collections.binarySearch(posts, dateTopic, new Comparator<Object>() {
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
			log.error(e);
			return null;
		}
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findLastPostsOfForums(java.lang.Integer)
	 */
	@Override
	public Map<Object, Post> findLastPostsOfForums(Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Object[]> query = em.createNamedQuery("findLastPostsOfForums", Object[].class);
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

			query = em.createNamedQuery("findLastPostsOfForumsCreateDate", Object[].class);
			query.setParameter("dates", dates);
			List<Object[]> posts = query.getResultList();
			Map<Object, Post> forumPostMap = new HashMap<Object, Post>(createDates.size());
			for (Object[] dateForum : createDates) {
				int index = Collections.binarySearch(posts, dateForum, new Comparator<Object>() {
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
			log.error(e);
			return null;
		}
	}

	@Override
	public List<ForumWatch> findForumWatchByUser(User user, Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<ForumWatch> query = em.createNamedQuery("findForumWatchByUser", ForumWatch.class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forum watch";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Forum> findForumWatchedByUser(User user, Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Forum> query = em.createNamedQuery("findForumWatchedByUser", Forum.class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find forum watched";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicWatchedByUser(User user, Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Topic> query = em.createNamedQuery("findTopicWatchedByUser", Topic.class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topic watched";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Topic> findTopicWatchedByUser(User user, Date datePoint, Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Topic> query = em.createNamedQuery("findTopicWatchedByUserCreateDate", Topic.class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("datePoint", datePoint, TemporalType.TIMESTAMP);
			query.setParameter("forumInstanceId", indexInstance);
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find topic watched";
			throw new ModuleException(message, e);
		}
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findTopicWatches(it.vige.rubia.auth.User,
	 *      java.lang.Integer)
	 */
	@Override
	public Map<Object, Object> findTopicWatches(User user, Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Object[]> query = em.createNamedQuery("findTopicWatches", Object[].class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			List<Object[]> results = query.getResultList();
			HashMap<Object, Object> map = new HashMap<Object, Object>(results.size());
			for (Object[] element : results) {
				map.put(element[0], element[1]);
			}
			return map;
		} catch (Exception e) {
			String message = "Cannot find topic watches";
			throw new ModuleException(message, e);
		}
	}

	private List<Post> findPostsFromForum(Forum forum, int limit, String order) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findPostsFromForum" + order, Post.class);

			query.setParameter("forumId", forum.getId());

			query.setFirstResult(0);

			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsFromForumAsc(Forum forum, int limit) throws ModuleException {
		return findPostsFromForum(forum, limit, "asc");
	}

	@Override
	public List<Post> findPostsFromForumDesc(Forum forum, int limit) throws ModuleException {
		return findPostsFromForum(forum, limit, "desc");
	}

	private List<Post> findPostsFromCategory(Category category, int limit, String order) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findPostsFromCategory" + order, Post.class);

			query.setParameter("categoryId", category.getId());

			query.setFirstResult(0);

			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public List<Post> findPostsFromCategoryAsc(Category category, int limit) throws ModuleException {
		return findPostsFromCategory(category, limit, "asc");
	}

	@Override
	public List<Post> findPostsFromCategoryDesc(Category category, int limit) throws ModuleException {
		return findPostsFromCategory(category, limit, "desc");
	}

	private List<Post> findPosts(int limit, String order) throws ModuleException {
		try {

			TypedQuery<Post> query = em.createNamedQuery("findPostsOrder" + order, Post.class);
			query.setFirstResult(0);

			if (limit != 0) {
				query.setMaxResults(limit);
			}
			return query.getResultList();
		} catch (Exception e) {
			String message = "Cannot find posts";
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
	public void createWatch(Poster poster, Forum forum, int mode) throws ModuleException {
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
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public ForumWatch findForumWatchById(Integer forumWatchId) throws ModuleException {
		try {

			TypedQuery<ForumWatch> query = em.createNamedQuery("findForumWatchById", ForumWatch.class);
			query.setParameter("forumWatchId", forumWatchId);
			return uniqueElement(query.getResultList());
		} catch (Exception e) {
			String message = "Cannot find forum watch";
			throw new ModuleException(message, e);
		}
	}

	/**
	 * @see it.vige.rubia.ForumsModule#findForumWatches(it.vige.rubia.auth.User,
	 *      java.lang.Integer)
	 */
	@Override
	public Map<Object, Object> findForumWatches(User user, Integer indexInstance) throws ModuleException {
		try {

			TypedQuery<Object[]> query = em.createNamedQuery("findForumWatches", Object[].class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumInstanceId", indexInstance);
			List<Object[]> results = query.getResultList();
			HashMap<Object, Object> map = new HashMap<Object, Object>(results.size());
			for (Object[] element : results) {
				map.put(element[0], element[1]);
			}
			return map;
		} catch (Exception e) {
			String message = "Cannot find forum watches";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public ForumWatch findForumWatchByUserAndForum(User user, int forumId) throws ModuleException {
		try {

			TypedQuery<ForumWatch> query = em.createNamedQuery("findForumWatchByUserAndForum", ForumWatch.class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("forumId", forumId);
			return uniqueElement(query.getResultList());
		} catch (Exception e) {
			String message = "Cannot find forum watch";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public TopicWatch findTopicWatchByUserAndTopic(User user, int topicId) throws ModuleException {
		try {

			TypedQuery<TopicWatch> query = em.createNamedQuery("findTopicWatchByUserAndTopic", TopicWatch.class);
			query.setParameter("userId", user.getId().toString());
			query.setParameter("topicId", topicId);
			return uniqueElement(query.getResultList());
		} catch (Exception e) {
			String message = "Cannot find topic watch";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public void createWatch(Poster poster, Topic topic, int mode) throws ModuleException {
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
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public TopicWatch findTopicWatchById(Integer topicWatchId) throws ModuleException {
		try {

			TypedQuery<TopicWatch> query = em.createNamedQuery("findTopicWatchById", TopicWatch.class);
			query.setParameter("topicWatchId", topicWatchId.toString());
			return uniqueElement(query.getResultList());
		} catch (Exception e) {
			String message = "Cannot find topic watch";
			throw new ModuleException(message, e);
		}
	}

	@Override
	public void processNotifications(Integer postId, int watchType, String postUrl, String replyUrl) {
		notificationEngine.schedule(postId, watchType, postUrl, replyUrl);
	}

	/**
	 * @see it.vige.rubia.ForumsModule#createForumInstance(java.lang.Integer,
	 *      java.lang.String)
	 */
	@Override
	public ForumInstance createForumInstance(Integer indexInstance, String name) throws ModuleException {
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
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public void removeForumInstance(int forumInstanceId) throws ModuleException {

		try {
			ForumInstance forumInstance = em.find(ForumInstance.class, forumInstanceId);
			em.remove(forumInstance);
			em.flush(); // it is required
			// for clustered versions
		} catch (Exception e) {
			String errorMessage = "Cannot delete forum Instance";
			throw new ModuleException(errorMessage, e);
		}
	}

	@Override
	public ForumInstance findForumInstanceById(Integer id) throws ModuleException {
		if (id != null) {
			try {

				ForumInstance forumInstance = (ForumInstance) em.find(ForumInstance.class, id);
				return forumInstance;
			} catch (Exception e) {
				String message = "Cannot find forum instance by id " + id;
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
			TypedQuery<Attachment> query = em.createNamedQuery("findAttachments", Attachment.class);
			query.setParameter("postId", post.getId());
			return query.getResultList();
		} else
			return null;
	}

	static <T> T uniqueElement(List<T> list) throws NonUniqueResultException {
		int size = list.size();
		if (size == 0)
			return null;
		T first = list.get(0);
		for (int i = 1; i < size; i++) {
			if (list.get(i) != first) {
				throw new NonUniqueResultException(list.size() + "");
			}
		}
		return first;
	}
}
