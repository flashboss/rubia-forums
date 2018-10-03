package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static java.util.Calendar.getInstance;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.Constants;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.ForumWatchBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.TopicWatchBean;
import it.vige.rubia.dto.UserBean;
import it.vige.rubia.dto.WatchBean;
import it.vige.rubia.dto.WatchRequestBean;
import it.vige.rubia.resttest.RestCaller;

public class WatchTest extends RestCaller implements Constants {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(WatchTest.class);

	private static Date today = new Date();

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
		ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
		forumInstanceBean.setId(1);

		CategoryBean categoryBean = new CategoryBean("First Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		Response response = post(url + "createCategory", authorization, categoryBean);
		CategoryBean category = response.readEntity(CategoryBean.class);

		ForumBean forumBean = new ForumBean("First Test Forum", "First Test Description", category);
		response = post(url + "createForum", authorization, forumBean);
		ForumBean forum = response.readEntity(ForumBean.class);

		PostBean postBean = new PostBean("First Test Body");
		postBean.setCreateDate(today);
		TopicBean topicBean = new TopicBean(forum, "First Test Topic", asList(new PostBean[] { postBean }), NORMAL,
				null);
		topicBean.setPoster(new PosterBean("root"));
		topicBean.setPoll(new PollBean());
		response = post(url + "createTopic", authorization, topicBean);

		postBean = new PostBean("Second Test Body");
		postBean.setCreateDate(today);
		topicBean = new TopicBean(forumBean, "Second Test Topic", asList(new PostBean[] { postBean }), NORMAL, null);
		topicBean.setPoster(new PosterBean("root"));
		topicBean.setForum(forum);
		response = post(url + "createTopicWithPoster", authorization, topicBean);
		TopicBean topic = response.readEntity(TopicBean.class);
		TopicWatchBean topicWatchBean = new TopicWatchBean();
		topicWatchBean.setTopic(topic);
		topicWatchBean.setPoster(topic.getPoster());
		topicWatchBean.setMode(1);
		post(url + "createTopicWatch", authorization, topicWatchBean);
		WatchRequestBean watchRequestBean = new WatchRequestBean();
		watchRequestBean.setIndexInstance(1);
		UserBean user = new UserBean();
		user.setId(topic.getPoster().getUserId());
		watchRequestBean.setUser(user);
		response = post(url + "findTopicWatches", authorization, watchRequestBean);
		Map<String, TopicWatchBean> topicWatches = response.readEntity(new GenericType<Map<String, TopicWatchBean>>() {
		});
		assertEquals(1, topicWatches.size());
		topicWatchBean = topicWatches.get(topic.getId() + "");
		assertNotEquals(0, topicWatchBean.getId());
		assertEquals(1, topicWatchBean.getMode());
		PosterBean poster = topicWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		topic = topicWatchBean.getTopic();
		assertNotEquals(0, topicWatchBean.getId());
		assertTrue(topic.getPosts().isEmpty());
		assertNull(topic.getPoster());
		assertNull(topic.getLastPostDate());
		assertNull(topic.getPoll());
		assertEquals(0, topic.getReplies());
		assertEquals(0, topic.getStatus());
		assertNull(topic.getSubject());
		assertNull(topic.getType());
		assertEquals(0, topic.getViewCount());
		assertNull(topic.getWatches());
		assertNull(topic.getForum());

		ForumWatchBean forumWatchBean = new ForumWatchBean();
		forumWatchBean.setForum(forum);
		forumWatchBean.setPoster(poster);
		forumWatchBean.setMode(1);
		post(url + "createForumWatch", authorization, forumWatchBean);
		response = post(url + "findForumWatches", authorization, watchRequestBean);
		Map<String, ForumWatchBean> forumWatches = response.readEntity(new GenericType<Map<String, ForumWatchBean>>() {
		});
		assertEquals(1, forumWatches.size());
		forumWatchBean = forumWatches.get(forum.getId() + "");
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(1, forumWatchBean.getMode());
		poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());
	}

	@Test
	public void findAndUpdateForum() {
		WatchRequestBean watchRequestBean = new WatchRequestBean();
		watchRequestBean.setIndexInstance(1);
		UserBean user = new UserBean();
		user.setId("root");
		watchRequestBean.setUser(user);
		Response response = post(url + "findForumWatchByUser", authorization, watchRequestBean);
		List<ForumWatchBean> forumWatches = response.readEntity(new GenericType<List<ForumWatchBean>>() {
		});
		assertEquals(1, forumWatches.size());
		ForumWatchBean forumWatchBean = forumWatches.get(0);
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(1, forumWatchBean.getMode());
		PosterBean poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		ForumBean forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());

		response = post(url + "findForumWatchedByUser", authorization, watchRequestBean);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(1, forumWatches.size());
		forumWatchBean = forumWatches.get(0);
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(1, forumWatchBean.getMode());
		poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());

		ForumBean forumBean = forumBeans.get(0);
		response = get(url + "findForumWatchById/" + forumBean.getWatches().get(0).getId(), authorization);
		forumWatchBean = response.readEntity(ForumWatchBean.class);
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(1, forumWatchBean.getMode());
		poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());

		watchRequestBean.setForumId(forum.getId());
		response = post(url + "findForumWatchByUserAndForum", authorization, watchRequestBean);
		forumWatchBean = response.readEntity(ForumWatchBean.class);
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(1, forumWatchBean.getMode());
		poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());

		forumWatchBean.setMode(2);
		post(url + "updateForumWatch", authorization, forumWatchBean);
		response = post(url + "findForumWatchByUserAndForum", authorization, watchRequestBean);
		forumWatchBean = response.readEntity(ForumWatchBean.class);
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(2, forumWatchBean.getMode());
		poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());

		forumWatchBean.setMode(1);
		post(url + "updateForumWatch", authorization, forumWatchBean);
		response = post(url + "findForumWatchByUserAndForum", authorization, watchRequestBean);
		forumWatchBean = response.readEntity(ForumWatchBean.class);
		assertNotEquals(0, forumWatchBean.getId());
		assertEquals(1, forumWatchBean.getMode());
		poster = forumWatchBean.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		forum = forumWatchBean.getForum();
		assertNotEquals(0, forum.getId());
		assertNull(forum.getCategory());
		assertNull(forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNull(forum.getName());
		assertEquals(0, forum.getOrder());
		assertEquals(0, forum.getPostCount());
		assertFalse(forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(0, forum.getTopicCount());
		assertTrue(forum.getTopics().isEmpty());
		assertNull(forum.getWatches());
	}

	@Test
	public void findAndUpdateTopic() {
		WatchRequestBean watchRequestBean = new WatchRequestBean();
		watchRequestBean.setIndexInstance(1);
		UserBean user = new UserBean();
		user.setId("root");
		watchRequestBean.setUser(user);
		Response response = post(url + "findTopicWatchedByUser", authorization, watchRequestBean);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(1, topicBeans.size());
		TopicBean topicBean = topicBeans.get(0);
		assertNotEquals(0, topicBean.getId());
		assertNull(topicBean.getLastPostDate());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertEquals("Second Test Topic", topicBean.getSubject());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		ForumBean forum = topicBean.getForum();
		CategoryBean category = forum.getCategory();
		assertNull(category.getForumInstance());
		assertTrue(category.getForums().isEmpty());
		assertNotEquals(0, category.getId());
		assertEquals(20, category.getOrder());
		assertEquals("First Test Category", category.getTitle());
		assertEquals("First Test Description", forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNotEquals(0, forum.getId());
		assertEquals("First Test Forum", forum.getName());
		assertEquals(10, forum.getOrder());
		assertEquals(1, forum.getPostCount());
		assertEquals(1, forum.getTopicCount());
		assertEquals(false, forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(1, forum.getWatches().size());
		WatchBean watchBean = forum.getWatches().get(0);
		assertNotEquals(0, watchBean.getId());
		assertEquals(0, watchBean.getMode());
		assertNull(watchBean.getPoster());
		List<TopicBean> topics = forum.getTopics();
		assertEquals(2, topics.size());
		topicBean = topics.get(0);
		assertNull(topicBean.getForum());
		PosterBean poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		PollBean poll = topicBean.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		List<PostBean> posts = topicBean.getPosts();
		assertTrue(posts.isEmpty());
		topicBean = topics.get(1);
		assertNull(topicBean.getForum());
		poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertNull(topicBean.getPoll());
		posts = topicBean.getPosts();
		assertTrue(posts.isEmpty());

		topicBean = topics.get(1);
		assertNull(topicBean.getForum());
		poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertNull(topicBean.getPoll());
		posts = topicBean.getPosts();
		assertTrue(posts.isEmpty());
		topicBean = topics.get(1);
		assertNull(topicBean.getForum());
		poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertNull(topicBean.getPoll());
		posts = topicBean.getPosts();
		assertTrue(posts.isEmpty());

		Calendar calendar = getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		watchRequestBean.setDate(restDateFormat.format(calendar.getTime()));
		response = post(url + "findTopicWatchedByUser", authorization, watchRequestBean);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertTrue(topicBeans.isEmpty());
		PostBean postBean = new PostBean();
		postBean.setTopic(topicBean);
		topicBean.setForum(forum);
		forum.getTopics().clear();
		postBean.setMessage(new MessageBean("First Test Post"));
		postBean.setPoster(poster);
		postBean.setCreateDate(today);
		response = post(url + "createPost", authorization, postBean);
		postBean = response.readEntity(PostBean.class);
		watchRequestBean.setTopicId(topicBean.getId());
		response = post(url + "findTopicWatchedByUser", authorization, watchRequestBean);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(1, topicBeans.size());
		topicBean = topicBeans.get(0);
		assertNotNull(topicBean);
		assertNotEquals(0, topicBean.getId());
		assertEquals(today, topicBean.getLastPostDate());
		assertEquals(1, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertEquals("Second Test Topic", topicBean.getSubject());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		forum = topicBean.getForum();
		category = forum.getCategory();
		assertNull(category.getForumInstance());
		assertTrue(category.getForums().isEmpty());
		assertNotEquals(0, category.getId());
		assertEquals(20, category.getOrder());
		assertEquals("First Test Category", category.getTitle());
		assertEquals("First Test Description", forum.getDescription());
		assertNull(forum.getForumWatch());
		assertNotEquals(0, forum.getId());
		assertEquals("First Test Forum", forum.getName());
		assertEquals(10, forum.getOrder());
		assertEquals(2, forum.getPostCount());
		assertEquals(1, forum.getTopicCount());
		assertEquals(false, forum.getPruneEnable());
		assertEquals(0, forum.getPruneNext());
		assertEquals(0, forum.getStatus());
		assertEquals(1, forum.getWatches().size());
		watchBean = forum.getWatches().get(0);
		assertNotEquals(0, watchBean.getId());
		assertNull(watchBean.getPoster());
		assertEquals(0, watchBean.getMode());
		poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		poll = topicBean.getPoll();
		assertNull(poll);
		posts = topicBean.getPosts();
		assertEquals(1, posts.size());
		postBean = posts.get(0);
		assertTrue(postBean.getAttachments().isEmpty());
		assertNull(postBean.getCreateDate());
		assertEquals(0, postBean.getEditCount());
		assertNull(postBean.getEditDate());
		assertNotEquals(0, postBean.getId());
		assertNull(postBean.getMessage());
		assertNull(postBean.getPoster());
		assertNull(postBean.getTopic());
		assertNull(postBean.getUser());
		topics = forum.getTopics();
		assertEquals(2, topics.size());
		topicBean = topics.get(0);
		assertNull(topicBean.getForum());
		assertNotEquals(0, topicBean.getId());
		assertEquals(today, topicBean.getLastPostDate());
		poll = topicBean.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		topicBean = topics.get(1);
		assertNull(topicBean.getForum());
		assertNotEquals(0, topicBean.getId());
		assertEquals(today, topicBean.getLastPostDate());
		assertNull(topicBean.getPoll());
		poster = topicBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(1, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertEquals("Second Test Topic", topicBean.getSubject());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertEquals(1, topicBean.getWatches().size());
		watchBean = topicBean.getWatches().get(0);
		assertNotEquals(0, watchBean.getId());
		poster = watchBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertEquals(1, watchBean.getMode());
		response = post(url + "findTopicWatchByUserAndTopic/", authorization, watchRequestBean);
		TopicWatchBean topicWatchBean = response.readEntity(TopicWatchBean.class);
		assertNotEquals(0, topicWatchBean.getId());
		poster = topicWatchBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertEquals(1, topicWatchBean.getMode());
		topicBean = topicWatchBean.getTopic();
		assertNotNull(topicBean);
		assertNotEquals(0, topicBean.getId());
		assertNull(topicBean.getLastPostDate());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertNull(topicBean.getSubject());
		assertNull(topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getForum());
		assertNull(topicBean.getPoster());
		assertNull(topicBean.getPoll());
		assertTrue(topicBean.getPosts().isEmpty());
		topicWatchBean.setMode(2);
		post(url + "updateTopicWatch", authorization, topicWatchBean);
		response = post(url + "findTopicWatchByUserAndTopic/", authorization, watchRequestBean);
		topicWatchBean = response.readEntity(TopicWatchBean.class);
		assertNotEquals(0, topicWatchBean.getId());
		poster = topicWatchBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertEquals(2, topicWatchBean.getMode());
		topicBean = topicWatchBean.getTopic();
		assertNotNull(topicBean);
		assertNotEquals(0, topicBean.getId());
		assertNull(topicBean.getLastPostDate());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertNull(topicBean.getSubject());
		assertNull(topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getForum());
		assertNull(topicBean.getPoster());
		assertNull(topicBean.getPoll());
		assertTrue(topicBean.getPosts().isEmpty());
		topicWatchBean.setMode(1);
		post(url + "updateTopicWatch", authorization, topicWatchBean);
		response = post(url + "findTopicWatchByUserAndTopic/", authorization, watchRequestBean);
		topicWatchBean = response.readEntity(TopicWatchBean.class);
		assertNotEquals(0, topicWatchBean.getId());
		poster = topicWatchBean.getPoster();
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertNotEquals(0, poster.getId());
		assertEquals(1, topicWatchBean.getMode());
		topicBean = topicWatchBean.getTopic();
		assertNotNull(topicBean);
		assertNotEquals(0, topicBean.getId());
		assertNull(topicBean.getLastPostDate());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertNull(topicBean.getSubject());
		assertNull(topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getForum());
		assertNull(topicBean.getPoster());
		assertNull(topicBean.getPoll());
		assertTrue(topicBean.getPosts().isEmpty());
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
		WatchRequestBean watchRequestBean = new WatchRequestBean();
		watchRequestBean.setIndexInstance(1);
		UserBean user = new UserBean();
		user.setId("root");
		watchRequestBean.setUser(user);
		Response response = post(url + "findForumWatches", authorization, watchRequestBean);
		Map<String, ForumWatchBean> forumWatches = response.readEntity(new GenericType<Map<String, ForumWatchBean>>() {
		});
		assertEquals(1, forumWatches.size());
		ForumWatchBean forumWatchBean = forumWatches.values().iterator().next();
		post(url + "removeWatch", authorization, forumWatchBean);
		response = post(url + "findForumWatches", authorization, watchRequestBean);
		forumWatches = response.readEntity(new GenericType<Map<String, ForumWatchBean>>() {
		});
		assertEquals(0, forumWatches.size());

		response = post(url + "findTopicWatches", authorization, watchRequestBean);
		Map<String, TopicWatchBean> topicWatches = response.readEntity(new GenericType<Map<String, TopicWatchBean>>() {
		});
		assertEquals(1, topicWatches.size());
		TopicWatchBean topicWatchBean = topicWatches.values().iterator().next();
		post(url + "removeWatch", authorization, topicWatchBean);
		response = post(url + "findTopicWatches", authorization, watchRequestBean);
		topicWatches = response.readEntity(new GenericType<Map<String, TopicWatchBean>>() {
		});
		assertEquals(0, topicWatches.size());

		response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(2, topicBeans.size(), "Topics size");
		PosterBean posterBean = topicBeans.get(0).getPoster();

		topicBeans.forEach(x -> {
			get(url + "removeTopic/" + x.getId(), authorization);
		});
		response = get(url + "findTopics/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(0, topicBeans.size(), "Topics size");

		response = get(url + "removePoster/" + posterBean.getId(), authorization);
		PosterBean removedPosterBean = response.readEntity(PosterBean.class);
		assertNotNull(removedPosterBean, "Poster removed by poster operations");

		response = get(url + "findPosterByUserId/" + removedPosterBean.getUserId(), authorization);
		posterBean = response.readEntity(PosterBean.class);
		assertNull(posterBean, "Poster removed verified by poster operations");

		response = get(url + "findForums/1", authorization);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(3, forumBeans.size(), "Forums size");

		response = get(url + "findCategories/1", authorization);
		List<CategoryBean> categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});
		assertEquals(2, categoryBeans.size(), "Categories size");

		get(url + "removeForum/" + forumBeans.get(1).getId(), authorization);
		get(url + "removeCategory/" + categoryBeans.get(1).getId(), authorization);
	}

}
