package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.TopicRequestBean;
import it.vige.rubia.resttest.RestCaller;

public class TopicTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(ForumTest.class);

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
		ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
		forumInstanceBean.setId(1);

		CategoryBean categoryBean = new CategoryBean("First Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		Response response = post(url + "createCategory", authorization, categoryBean);

		ForumBean forumBean = new ForumBean("First Test Forum", "First Test Description", categoryBean);
		response = post(url + "createForum", authorization, forumBean);

		PostBean post = new PostBean("First Test Body");
		post.setCreateDate(new Date());
		TopicBean topic = new TopicBean(forumBean, "First Test Topic", asList(new PostBean[] { post }), NORMAL, null);
		topic.setPoster(new PosterBean("root"));
		response = post(url + "createTopic", authorization, topic);
		PostBean postBean = response.readEntity(PostBean.class);
		assertNotNull(postBean);

		post = new PostBean("Second Test Body");
		post.setCreateDate(new Date());
		topic = new TopicBean(forumBean, "Second Test Topic", asList(new PostBean[] { post }), NORMAL, null);
		topic.setPoster(new PosterBean("root"));
		response = post(url + "createTopicWithPoster", authorization, topic);
		TopicBean topicBean = response.readEntity(TopicBean.class);
		assertNotNull(topicBean);
	}

	@Test
	public void findAndUpdate() {
		Response response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});

		TopicBean topicBean = topicBeans.get(0);
		TopicRequestBean topicRequestBean = new TopicRequestBean();
		topicRequestBean.setTopic(topicBean);
		topicRequestBean.setStart(1);
		topicRequestBean.setPerPage(3);
		post(url + "findTopicsDesc", authorization, topicRequestBean);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		post(url + "findTopics", authorization, topicBean.getForum());
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		get(url + "findTopicsHot/2/3/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		get(url + "findTopicsByLatestPosts/3/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		get(url + "findTopicsHottest/" + new Date() + "/3/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		get(url + "findTopicsMostViewed/" + new Date() + "/3/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		get(url + "findTopicById/" + topicBean.getId(), authorization);
		topicBean = response.readEntity(new GenericType<TopicBean>() {
		});
		post(url + "updateTopic", authorization, topicBean);
		response.readEntity(new GenericType<List<TopicBean>>() {
		});
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");

		Response response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(2, topicBeans.size(), "Forums size");

		topicBeans.forEach(x -> {
			get(url + "removeTopic/" + x.getId(), authorization);
		});

		response = get(url + "findForums/1", authorization);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(4, forumBeans.size(), "Forums size");

		response = get(url + "findCategories/1", authorization);
		List<CategoryBean> categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});
		assertEquals(2, categoryBeans.size(), "Categories size");

		get(url + "removeForum/" + forumBeans.get(1).getId(), authorization);
		get(url + "removeCategory/" + categoryBeans.get(1).getId(), authorization);
	}

}
