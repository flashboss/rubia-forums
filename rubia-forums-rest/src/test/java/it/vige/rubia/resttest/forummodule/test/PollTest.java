package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.resttest.RestCaller;

public class PollTest extends RestCaller implements Constants {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(PollTest.class);

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
		PollBean pollBean = new PollBean();
		pollBean.setCreationDate(today);
		pollBean.setLength(4);
		pollBean.setTitle("New Poll for topic");
		pollBean.setVoted(new HashSet<String>(asList(new String[] { "root" })));
		PollOptionBean pollOption = new PollOptionBean("this is a question");
		List<PollOptionBean> pollOptions = new ArrayList<PollOptionBean>();
		pollOptions.add(pollOption);
		pollBean.setOptions(pollOptions);
		topicBean.setPoll(pollBean);
		response = post(url + "createTopic", authorization, topicBean);
		PostBean post = response.readEntity(PostBean.class);
		TopicBean topic = post.getTopic();
		PollBean poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		List<PollOptionBean> pollOptionBeans = poll.getOptions();
		assertEquals(2, pollOptionBeans.size());
		pollOption = pollOptionBeans.get(0);
		assertNull(pollOption.getPoll());
		assertEquals(0, pollOption.getPollOptionPosition());
		assertNull(pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptionBeans.get(1);
		pollBean = pollOption.getPoll();
		assertNull(pollBean.getCreationDate());
		assertNotEquals(0, pollBean.getId());
		assertEquals(0, pollBean.getLength());
		assertTrue(pollBean.getOptions().isEmpty());
		assertNull(pollBean.getTitle());
		assertTrue(pollBean.getVoted().isEmpty());
		assertEquals(0, pollBean.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 5);
		assertEquals("this is a question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		assertEquals("New Poll for topic", poll.getTitle());
		assertEquals(1, poll.getVoted().size());
		assertEquals(0, poll.getVotesSum());
		List<PostBean> posts = topic.getPosts();
		assertTrue(posts.isEmpty());

		postBean = new PostBean("Second Test Body");
		postBean.setCreateDate(today);
		topicBean = new TopicBean(forumBean, "Second Test Topic", asList(new PostBean[] { postBean }), NORMAL, null);
		topicBean.setPoster(new PosterBean("root"));
		topicBean.setForum(forum);
		response = post(url + "createTopicWithPoster", authorization, topicBean);
		TopicBean topic2 = response.readEntity(TopicBean.class);
		poll = topic2.getPoll();
		assertNull(poll);
		posts = topic2.getPosts();
		assertTrue(posts.isEmpty());

		pollBean = new PollBean();
		pollBean.setCreationDate(today);
		pollBean.setLength(4);
		pollBean.setTitle("Other Poll for topic");
		pollBean.setVoted(new HashSet<String>(asList(new String[] { "root" })));
		pollOption = new PollOptionBean("this is a new question");
		pollOptions = new ArrayList<PollOptionBean>();
		pollOptions.add(pollOption);
		pollBean.setOptions(pollOptions);
		topic.setPoll(pollBean);
		response = post(url + "addPollToTopic", authorization, topic);
		pollBean = response.readEntity(PollBean.class);
		assertEquals(today, pollBean.getCreationDate());
		assertNotEquals(0, pollBean.getId());
		assertEquals(4, pollBean.getLength());
		pollOptions = pollBean.getOptions();
		assertEquals(1, pollOptions.size());
		pollOption = pollOptions.get(0);
		assertNull(pollOption.getPoll());
		assertEquals(0, pollOption.getPollOptionPosition());
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		assertEquals("Other Poll for topic", pollBean.getTitle());
		assertEquals(1, pollBean.getVoted().size());
		assertEquals("root", pollBean.getVoted().iterator().next());
		assertEquals(0, pollBean.getVotesSum());
	}

	@Test
	public void findAndUpdate() {
		Response response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		TopicBean topic = topicBeans.get(0);
		PollBean poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(1, poll.getOptions().size());
		PollOptionBean pollOption = poll.getOptions().get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		ForumBean forum = topic.getForum();
		List<TopicBean> topics = forum.getTopics();

		topic = topics.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(1, poll.getOptions().size());
		pollOption = poll.getOptions().get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());

		topic = topics.get(1);
		assertNull(topic.getPoll());

		topic = topicBeans.get(1);
		assertNull(topic.getPoll());

		forum = topic.getForum();
		topics = forum.getTopics();
		topic = topics.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(1, poll.getOptions().size());
		pollOption = poll.getOptions().get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());

		topic = topics.get(1);
		assertNull(topic.getPoll());

		pollOption = new PollOptionBean("Second question");
		pollOption.setPoll(poll);
		post(url + "updatePollOption", authorization, pollOption);
		response = get(url + "findTopics/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		topic = topicBeans.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(2, poll.getOptions().size());
		List<PollOptionBean> pollOptions = poll.getOptions();
		pollOption = pollOptions.get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptions.get(1);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 11);
		assertEquals("Second question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		forum = topic.getForum();
		topics = forum.getTopics();

		topic = topics.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(2, poll.getOptions().size());
		pollOptions = poll.getOptions();
		pollOption = pollOptions.get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptions.get(1);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 11);
		assertEquals("Second question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());

		topic = topics.get(1);
		assertNull(topic.getPoll());

		topic = topicBeans.get(1);
		assertNull(topic.getPoll());

		forum = topic.getForum();
		topics = forum.getTopics();
		topic = topics.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(2, poll.getOptions().size());
		pollOptions = poll.getOptions();
		pollOption = pollOptions.get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptions.get(1);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 11);
		assertEquals("Second question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());

		topic = topics.get(1);
		assertNull(topic.getPoll());
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");

		Response response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		TopicBean topic = topicBeans.get(0);
		PollBean poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(2, poll.getOptions().size());
		List<PollOptionBean> pollOptions = poll.getOptions();
		PollOptionBean pollOption = pollOptions.get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptions.get(1);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 11);
		assertEquals("Second question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		ForumBean forum = topic.getForum();
		List<TopicBean> topics = forum.getTopics();

		topic = topics.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(2, poll.getOptions().size());
		pollOptions = poll.getOptions();
		pollOption = pollOptions.get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptions.get(1);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 11);
		assertEquals("Second question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());

		topic = topics.get(1);
		assertNull(topic.getPoll());

		topic = topicBeans.get(1);
		assertNull(topic.getPoll());

		forum = topic.getForum();
		topics = forum.getTopics();
		topic = topics.get(0);
		poll = topic.getPoll();
		assertEquals(today, poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(4, poll.getLength());
		assertEquals("Other Poll for topic", poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertEquals(2, poll.getOptions().size());
		pollOptions = poll.getOptions();
		pollOption = pollOptions.get(0);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 10);
		assertEquals("this is a new question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());
		pollOption = pollOptions.get(1);
		poll = pollOption.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		assertTrue(pollOption.getPollOptionPosition() >= 11);
		assertEquals("Second question", pollOption.getQuestion());
		assertEquals(0, pollOption.getVotes());

		topic = topics.get(1);
		assertNull(topic.getPoll());

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
