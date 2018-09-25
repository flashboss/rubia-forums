package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.resttest.RestCaller;

public class PostTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(PostTest.class);

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
		PosterBean poster = new PosterBean("root");
		topicBean.setPoster(poster);
		topicBean.setPoll(new PollBean());
		response = post(url + "createTopic", authorization, topicBean);
		PostBean post = response.readEntity(PostBean.class);
		topicBean = post.getTopic();
		postBean = new PostBean();
		postBean.setTopic(topicBean);
		postBean.setMessage(new MessageBean("First Test Post"));
		postBean.setPoster(poster);
		postBean.setCreateDate(today);
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		assertNotNull(post);
		postBean.setMessage(new MessageBean("Second Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		assertNotNull(post);
		postBean.setMessage(new MessageBean("Third Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		assertNotNull(post);
	}

	@Test
	public void findAndUpdate() {

	}
/*
	@POST
	@Path("createPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean createPost(TopicBean topic, ForumBean forum, MessageBean message, Date creationTime,
			PosterBean poster, Collection<AttachmentBean> attachments) throws ModuleException;
	
	@POST
	@Path("findAnnouncements")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findAnnouncements(ForumBean forum) throws ModuleException;

	@GET
	@Path("findPosts/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPosts(@PathParam("indexInstance") Integer indexInstance) throws ModuleException;

	@GET
	@Path("findPostById/{id}")
	@Produces(APPLICATION_JSON)
	public PostBean findPostById(@PathParam("id") Integer id) throws ModuleException;

	@POST
	@Path("findPostsByTopicId")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByTopicId(TopicBean topic) throws ModuleException;
	
	@POST
	@Path("findPostsByIdsAscFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Integer> posts)
			throws ModuleException;
	
	@POST
	@Path("findPostsByIdsDescFetchAttachmentsAndPosters")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Integer> posts)
			throws ModuleException;
	
	@POST
	@Path("findPostIdsAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<Integer> findPostIdsAsc(TopicBean topic, int start, int limit) throws ModuleException;
	
	@POST
	@Path("findPostIdsDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<Integer> findPostIdsDesc(TopicBean topic, int start, int limit) throws ModuleException;
	
	@POST
	@Path("findPostsByTopicIdAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByTopicIdAsc(TopicBean topic, int start, int limit) throws ModuleException;
	
	@POST
	@Path("findPostsByTopicIdDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsByTopicIdDesc(TopicBean topic, int start, int limit) throws ModuleException;
	
	@POST
	@Path("findLastPostDateForUser")
	@Consumes(APPLICATION_JSON)
	public Date findLastPostDateForUser(User user) throws ModuleException;
	
	@POST
	@Path("findLastPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean findLastPost(ForumBean forum) throws ModuleException;
	
	@POST
	@Path("findFirstPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean findFirstPost(TopicBean topic) throws ModuleException;
	
	@POST
	@Path("findLastPost")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public PostBean findLastPost(TopicBean topic) throws ModuleException;
	
	@POST
	@Path("findLastPostsOfTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Map<Object, Object> findLastPostsOfTopics(Collection<TopicBean> topics) throws ModuleException;
	
	@GET
	@Path("findLastPostsOfForums/{indexInstance}")
	@Produces(APPLICATION_JSON)
	public Map<Object, PostBean> findLastPostsOfForums(@PathParam("indexInstance") Integer indexInstance)
			throws ModuleException;
	
	@POST
	@Path("findPostsFromForumAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsFromForumAsc(ForumBean forum, int limit) throws ModuleException;
	
	@POST
	@Path("findPostsFromForumDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsFromForumDesc(ForumBean forum, int limit) throws ModuleException;
	
	@POST
	@Path("findPostsFromCategoryAsc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsFromCategoryAsc(CategoryBean category, int limit) throws ModuleException;
	
	@POST
	@Path("findPostsFromCategoryDesc")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsFromCategoryDesc(CategoryBean category, int limit) throws ModuleException;
	
	@GET
	@Path("findPostsDesc/{limit}")
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsDesc(@PathParam("limit") int limit) throws ModuleException;
	
	@GET
	@Path("findPostsAsc/{limit}")
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPostsAsc(@PathParam("limit") int limit) throws ModuleException;
	
	@POST
	@Path("updatePost")
	@Consumes(APPLICATION_JSON)
	public void update(PostBean post);
	
	@GET
	@Path("removePost/{postId}/{isLastPost}")
	public void removePost(@PathParam("postId") int postId, @PathParam("isLastPost") boolean isLastPost)
			throws ModuleException;
*/
	@AfterAll
	public static void stop() {
		log.debug("stopped test");

		ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
		forumInstanceBean.setId(1);
		
		CategoryBean categoryBean = new CategoryBean("First Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		Response response = post(url + "findPostsFromCategoryDesc", authorization, categoryBean);
		List<PostBean> postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
		assertEquals(3, postBeans.size(), "Topics size");
		
		response = get(url + "removePost/1/false", authorization);
		response = get(url + "removePost/2/true", authorization);
		response = get(url + "removePost/3/true", authorization);
		
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
