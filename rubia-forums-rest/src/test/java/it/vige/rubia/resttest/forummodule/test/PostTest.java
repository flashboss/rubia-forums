package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static org.jboss.logging.Logger.getLogger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.ModuleException;
import it.vige.rubia.auth.User;
import it.vige.rubia.dto.AttachmentBean;
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
		topicBean.setPoster(new PosterBean("root"));
		topicBean.setPoll(new PollBean());
		response = post(url + "createTopic", authorization, topicBean);
		PostBean post = response.readEntity(PostBean.class);
	}

	@Test
	public void findAndUpdate() {

	}
/*
	public List<PostBean> findAnnouncements(ForumBean forum) throws ModuleException;

	public List<PostBean> findPosts(@PathParam("indexInstance") Integer indexInstance) throws ModuleException;

	public PostBean findPostById(@PathParam("id") Integer id) throws ModuleException;

	public PostBean createPost(TopicBean topic, ForumBean forum, MessageBean message, Date creationTime,
			PosterBean poster, Collection<AttachmentBean> attachments) throws ModuleException;

	public List<PostBean> findPostsByTopicId(TopicBean topic) throws ModuleException;
	
	public List<PostBean> findPostsByIdsAscFetchAttachmentsAndPosters(Collection<Integer> posts)
			throws ModuleException;
	
	public List<PostBean> findPostsByIdsDescFetchAttachmentsAndPosters(Collection<Integer> posts)
			throws ModuleException;
	
	public List<Integer> findPostIdsAsc(TopicBean topic, int start, int limit) throws ModuleException;
	
	public List<Integer> findPostIdsDesc(TopicBean topic, int start, int limit) throws ModuleException;
	
	public List<PostBean> findPostsByTopicIdAsc(TopicBean topic, int start, int limit) throws ModuleException;
	
	public List<PostBean> findPostsByTopicIdDesc(TopicBean topic, int start, int limit) throws ModuleException;
	
	public Date findLastPostDateForUser(User user) throws ModuleException;
	
	public PostBean findLastPost(ForumBean forum) throws ModuleException;
	
	public PostBean findFirstPost(TopicBean topic) throws ModuleException;
	
	public PostBean findLastPost(TopicBean topic) throws ModuleException;
	
	public Map<Object, Object> findLastPostsOfTopics(Collection<TopicBean> topics) throws ModuleException;
	
	public Map<Object, PostBean> findLastPostsOfForums(@PathParam("indexInstance") Integer indexInstance)
			throws ModuleException;
	
	public List<PostBean> findPostsFromForumAsc(ForumBean forum, int limit) throws ModuleException;
	
	public List<PostBean> findPostsFromForumDesc(ForumBean forum, int limit) throws ModuleException;
	
	public List<PostBean> findPostsFromCategoryAsc(CategoryBean category, int limit) throws ModuleException;
	
	public List<PostBean> findPostsFromCategoryDesc(CategoryBean category, int limit) throws ModuleException;
	
	public List<PostBean> findPostsDesc(@PathParam("limit") int limit) throws ModuleException;
	
	public List<PostBean> findPostsAsc(@PathParam("limit") int limit) throws ModuleException;
	
	public void update(PostBean post);
	*/
	@AfterAll
	public static void stop() {
		log.debug("stopped test");
	}

}
