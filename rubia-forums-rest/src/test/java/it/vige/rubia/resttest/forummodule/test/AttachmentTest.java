package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.ModuleException;
import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.CategoryRequestBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.resttest.RestCaller;

public class AttachmentTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(AttachmentTest.class);

	private static Date firstDate;

	private static Date secondDate;

	private static Date thirdDate;

	private static Date forthDate;

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
		firstDate = new Date();
		postBean.setCreateDate(firstDate);
		TopicBean topicBean = new TopicBean(forum, "First Test Topic", asList(new PostBean[] { postBean }), NORMAL,
				null);
		PosterBean poster = new PosterBean("root");
		topicBean.setPoster(poster);
		topicBean.setPoll(new PollBean());
		response = post(url + "createTopic", authorization, topicBean);
		PostBean post = response.readEntity(PostBean.class);
		topicBean = post.getTopic();
		postBean = new PostBean();
		postBean.setTopic(topicBean);
		postBean.setMessage(new MessageBean("First Test Post"));
		postBean.setPoster(post.getPoster());
		secondDate = new Date();
		postBean.setCreateDate(secondDate);
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		poster = post.getPoster();
		topicBean = post.getTopic();
		poster = topicBean.getPoster();
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		topicBean = forumBean.getTopics().get(0);

		thirdDate = new Date();
		postBean.setCreateDate(thirdDate);
		postBean.setMessage(new MessageBean("Second Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		poster = post.getPoster();
		topicBean = post.getTopic();
		poster = topicBean.getPoster();
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		topicBean = forumBean.getTopics().get(0);
		poster = topicBean.getPoster();

		forthDate = new Date();
		postBean.setCreateDate(forthDate);
		postBean.setMessage(new MessageBean("Third Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		poster = post.getPoster();
		topicBean = post.getTopic();
		poster = topicBean.getPoster();
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		topicBean = forumBean.getTopics().get(0);
	}

	@Test
	public void findAndUpdate() {

		Response response = get(url + "findPostsDesc/3", authorization);
		List<PostBean> postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
	}

	@POST
	@Path("findPostsByIdsAscFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByIdsAscFetchAttachmentsAndPosters(List<Integer> posts) throws ModuleException {
		return null;
	}

	@POST
	@Path("findPostsByIdsDescFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByIdsDescFetchAttachmentsAndPosters(List<Integer> posts) throws ModuleException {
		return null;
	}

	@GET
	@Path("findAttachmentById/{attachID}")
	@Produces(APPLICATION_JSON)
	public AttachmentBean findAttachmentById(@PathParam("attachID") Integer attachID) throws ModuleException {
		return null;
	}

	@POST
	@Path("addAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean addAttachments(List<AttachmentBean> attachments, PostBean post) {
		return null;
	}

	@POST
	@Path("findAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<AttachmentBean> findAttachments(PostBean post) {
		return null;
	}

	@POST
	@Path("removeAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean removeAttachments(PostBean post) {
		return null;
	}

	@POST
	@Path("updateAttachments")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean updateAttachments(List<AttachmentBean> attachments, PostBean post) {
		return null;
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");

		Response response = get(url + "findCategories/1", authorization);
		List<CategoryBean> categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});

		CategoryRequestBean categoryRequestBean = new CategoryRequestBean();
		categoryRequestBean.setCategory(categoryBeans.stream().filter(x -> x.getTitle().equals("First Test Category"))
				.collect(toList()).get(0));
		categoryRequestBean.setLimit(3);

		response = post(url + "findPostsFromCategoryDesc", authorization, categoryRequestBean);
		List<PostBean> postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
		assertEquals(3, postBeans.size(), "Posts size");

		response = get(url + "removePost/" + postBeans.get(0).getId() + "/false", authorization);
		response = get(url + "removePost/" + postBeans.get(1).getId() + "/false", authorization);
		response = get(url + "removePost/" + postBeans.get(2).getId() + "/true", authorization);

		response = post(url + "findPostsFromCategoryDesc", authorization, categoryRequestBean);
		postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
		assertEquals(1, postBeans.size(), "Posts size");

		response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(1, topicBeans.size(), "Topics size");
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
		categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});
		assertEquals(2, categoryBeans.size(), "Categories size");

		get(url + "removeForum/" + forumBeans.get(1).getId(), authorization);
		get(url + "removeCategory/" + categoryBeans.get(1).getId(), authorization);
	}

}
