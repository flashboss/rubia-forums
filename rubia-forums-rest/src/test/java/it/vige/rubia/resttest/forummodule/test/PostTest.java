package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
		postBean.setPoster(post.getPoster());
		postBean.setCreateDate(today);
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		assertNotNull(post);
		assertTrue(post.getAttachments().isEmpty());
		assertEquals(today, post.getCreateDate());
		assertEquals(0, post.getEditCount());
		assertNull(post.getEditDate());
		assertNotEquals(0, post.getId());
		MessageBean message = post.getMessage();
		assertTrue(message.getBBCodeEnabled());
		assertTrue(message.getHTMLEnabled());
		assertTrue(message.getSignatureEnabled());
		assertFalse(message.getSmiliesEnabled());
		assertTrue(message.getSubject().isEmpty());
		assertEquals("First Test Post", message.getText());
		poster = post.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		topicBean = post.getTopic();
		assertNotEquals(0, topicBean.getId());
		assertEquals(today, topicBean.getLastPostDate());
		PollBean poll = topicBean.getPoll();
		assertNull(poll.getCreationDate());
		assertNotEquals(0, poll.getId());
		assertEquals(0, poll.getLength());
		assertTrue(poll.getOptions().isEmpty());
		assertNull(poll.getTitle());
		assertTrue(poll.getVoted().isEmpty());
		assertEquals(0, poll.getVotesSum());
		poster = topicBean.getPoster();
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(1, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		assertNull(categoryBean.getForumInstance());
		assertTrue(categoryBean.getForums().isEmpty());
		assertNotEquals(0, categoryBean.getId());
		assertEquals(20, categoryBean.getOrder());
		assertEquals("First Test Category", categoryBean.getTitle());
		assertNull(post.getUser());
		assertEquals("First Test Description", forumBean.getDescription());
		assertNull(forumBean.getForumWatch());
		assertNotEquals(0, forumBean.getId());
		assertEquals("First Test Forum", forumBean.getName());
		assertEquals(10, forumBean.getOrder());
		assertEquals(1, forumBean.getPostCount());
		assertFalse(forumBean.getPruneEnable());
		assertEquals(0, forumBean.getPruneNext());
		assertEquals(0, forumBean.getStatus());
		assertEquals(1, forumBean.getTopicCount());
		assertEquals(1, forumBean.getTopics().size());
		topicBean = forumBean.getTopics().get(0);
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
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		assertNull(topicBean.getForum());
		assertNull(forumBean.getWatches());
		postBean.setMessage(new MessageBean("Second Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		assertNotNull(post);
		assertTrue(post.getAttachments().isEmpty());
		assertEquals(today, post.getCreateDate());
		assertEquals(0, post.getEditCount());
		assertNull(post.getEditDate());
		assertNotEquals(0, post.getId());
		message = post.getMessage();
		assertTrue(message.getBBCodeEnabled());
		assertTrue(message.getHTMLEnabled());
		assertTrue(message.getSignatureEnabled());
		assertFalse(message.getSmiliesEnabled());
		assertTrue(message.getSubject().isEmpty());
		assertEquals("Second Test Post", message.getText());
		poster = post.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		topicBean = post.getTopic();
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
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(1, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		assertNull(categoryBean.getForumInstance());
		assertTrue(categoryBean.getForums().isEmpty());
		assertNotEquals(0, categoryBean.getId());
		assertEquals(20, categoryBean.getOrder());
		assertEquals("First Test Category", categoryBean.getTitle());
		assertNull(post.getUser());
		assertEquals("First Test Description", forumBean.getDescription());
		assertNull(forumBean.getForumWatch());
		assertNotEquals(0, forumBean.getId());
		assertEquals("First Test Forum", forumBean.getName());
		assertEquals(10, forumBean.getOrder());
		assertEquals(1, forumBean.getPostCount());
		assertFalse(forumBean.getPruneEnable());
		assertEquals(0, forumBean.getPruneNext());
		assertEquals(0, forumBean.getStatus());
		assertEquals(1, forumBean.getTopicCount());
		assertEquals(1, forumBean.getTopics().size());
		topicBean = forumBean.getTopics().get(0);
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
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		assertNull(topicBean.getForum());
		assertNull(forumBean.getWatches());
		postBean.setMessage(new MessageBean("Third Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		assertNotNull(post);
		assertTrue(post.getAttachments().isEmpty());
		assertEquals(today, post.getCreateDate());
		assertEquals(0, post.getEditCount());
		assertNull(post.getEditDate());
		assertNotEquals(0, post.getId());
		message = post.getMessage();
		assertTrue(message.getBBCodeEnabled());
		assertTrue(message.getHTMLEnabled());
		assertTrue(message.getSignatureEnabled());
		assertFalse(message.getSmiliesEnabled());
		assertTrue(message.getSubject().isEmpty());
		assertEquals("Third Test Post", message.getText());
		poster = post.getPoster();
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		topicBean = post.getTopic();
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
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(1, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		assertNull(categoryBean.getForumInstance());
		assertTrue(categoryBean.getForums().isEmpty());
		assertNotEquals(0, categoryBean.getId());
		assertEquals(20, categoryBean.getOrder());
		assertEquals("First Test Category", categoryBean.getTitle());
		assertNull(post.getUser());
		assertEquals("First Test Description", forumBean.getDescription());
		assertNull(forumBean.getForumWatch());
		assertNotEquals(0, forumBean.getId());
		assertEquals("First Test Forum", forumBean.getName());
		assertEquals(10, forumBean.getOrder());
		assertEquals(1, forumBean.getPostCount());
		assertFalse(forumBean.getPruneEnable());
		assertEquals(0, forumBean.getPruneNext());
		assertEquals(0, forumBean.getStatus());
		assertEquals(1, forumBean.getTopicCount());
		assertEquals(1, forumBean.getTopics().size());
		topicBean = forumBean.getTopics().get(0);
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
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals("root", poster.getUserId());
		assertTrue(topicBean.getPosts().isEmpty());
		assertEquals(0, topicBean.getReplies());
		assertEquals(0, topicBean.getStatus());
		assertTrue(topicBean.getSubject().isEmpty());
		assertEquals(NORMAL, topicBean.getType());
		assertEquals(0, topicBean.getViewCount());
		assertNull(topicBean.getWatches());
		assertNull(topicBean.getForum());
		assertNull(forumBean.getWatches());
	}

	@Test
	public void findAndUpdate() {

	}

	/*
	 * @POST
	 * 
	 * @Path("createPost")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public PostBean createPost(TopicBean topic,
	 * ForumBean forum, MessageBean message, Date creationTime, PosterBean poster,
	 * Collection<AttachmentBean> attachments) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findAnnouncements")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean> findAnnouncements(ForumBean
	 * forum) throws ModuleException;
	 * 
	 * @GET
	 * 
	 * @Path("findPosts/{indexInstance}")
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPosts(@PathParam("indexInstance") Integer indexInstance) throws
	 * ModuleException;
	 * 
	 * @GET
	 * 
	 * @Path("findPostById/{id}")
	 * 
	 * @Produces(APPLICATION_JSON) public PostBean findPostById(@PathParam("id")
	 * Integer id) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsByTopicId")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsByTopicId(TopicBean topic) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsByIdsAscFetchAttachmentsAndPosters")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Integer> posts) throws
	 * ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsByIdsDescFetchAttachmentsAndPosters")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Integer> posts)
	 * throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostIdsAsc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<Integer> findPostIdsAsc(TopicBean
	 * topic, int start, int limit) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostIdsDesc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<Integer> findPostIdsDesc(TopicBean
	 * topic, int start, int limit) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsByTopicIdAsc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsByTopicIdAsc(TopicBean topic, int start, int limit) throws
	 * ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsByTopicIdDesc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsByTopicIdDesc(TopicBean topic, int start, int limit) throws
	 * ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findLastPostDateForUser")
	 * 
	 * @Consumes(APPLICATION_JSON) public Date findLastPostDateForUser(User user)
	 * throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findLastPost")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public PostBean findLastPost(ForumBean forum)
	 * throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findFirstPost")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public PostBean findFirstPost(TopicBean topic)
	 * throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findLastPost")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public PostBean findLastPost(TopicBean topic)
	 * throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findLastPostsOfTopics")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public Map<Object, Object>
	 * findLastPostsOfTopics(Collection<TopicBean> topics) throws ModuleException;
	 * 
	 * @GET
	 * 
	 * @Path("findLastPostsOfForums/{indexInstance}")
	 * 
	 * @Produces(APPLICATION_JSON) public Map<Object, PostBean>
	 * findLastPostsOfForums(@PathParam("indexInstance") Integer indexInstance)
	 * throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsFromForumAsc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsFromForumAsc(ForumBean forum, int limit) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsFromForumDesc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsFromForumDesc(ForumBean forum, int limit) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsFromCategoryAsc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsFromCategoryAsc(CategoryBean category, int limit) throws
	 * ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("findPostsFromCategoryDesc")
	 * 
	 * @Consumes(APPLICATION_JSON)
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsFromCategoryDesc(CategoryBean category, int limit) throws
	 * ModuleException;
	 * 
	 * @GET
	 * 
	 * @Path("findPostsDesc/{limit}")
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsDesc(@PathParam("limit") int limit) throws ModuleException;
	 * 
	 * @GET
	 * 
	 * @Path("findPostsAsc/{limit}")
	 * 
	 * @Produces(APPLICATION_JSON) public List<PostBean>
	 * findPostsAsc(@PathParam("limit") int limit) throws ModuleException;
	 * 
	 * @POST
	 * 
	 * @Path("updatePost")
	 * 
	 * @Consumes(APPLICATION_JSON) public void update(PostBean post);
	 * 
	 * @GET
	 * 
	 * @Path("removePost/{postId}/{isLastPost}") public void
	 * removePost(@PathParam("postId") int postId, @PathParam("isLastPost") boolean
	 * isLastPost) throws ModuleException;
	 */
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
