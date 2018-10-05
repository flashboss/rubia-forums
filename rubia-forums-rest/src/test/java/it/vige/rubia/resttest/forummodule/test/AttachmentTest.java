package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
		AttachmentBean attachmentBean = new AttachmentBean();
		attachmentBean.setComment("A new attachment");
		String content = "This is the first content";
		attachmentBean.setContent(content.getBytes());
		attachmentBean.setContentType("text/plain");
		attachmentBean.setSize(content.length());
		attachmentBean.setName("First attachment");
		AttachmentBean attachmentBean2 = new AttachmentBean();
		attachmentBean2.setComment("A second attachment");
		String content2 = "This is the second content";
		attachmentBean2.setContent(content2.getBytes());
		attachmentBean2.setContentType("text/plain");
		attachmentBean2.setSize(content2.length());
		attachmentBean2.setName("Second attachment");
		postBean.setAttachments(asList(attachmentBean, attachmentBean2));
		TopicBean topicBean = new TopicBean(forum, "First Test Topic", asList(new PostBean[] { postBean }), NORMAL,
				null);
		PosterBean poster = new PosterBean("root");
		topicBean.setPoster(poster);
		topicBean.setPoll(new PollBean());
		response = post(url + "createTopic", authorization, topicBean);
		PostBean post = response.readEntity(PostBean.class);
		List<AttachmentBean> attachments = post.getAttachments();
		assertEquals(2, attachments.size());
		attachmentBean = attachments.get(0);
		assertEquals("A new attachment", attachmentBean.getComment());
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("First attachment", attachmentBean.getName());
		assertEquals(post, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean2 = attachments.get(1);
		assertEquals("A second attachment", attachmentBean2.getComment());
		assertEquals(content2, new String(attachmentBean2.getContent()));
		assertEquals("text/plain", attachmentBean2.getContentType());
		assertNotEquals(0, attachmentBean2.getId());
		assertEquals("Second attachment", attachmentBean2.getName());
		assertEquals(post, attachmentBean2.getPost());
		assertEquals(content2.length(), attachmentBean2.getSize());
		postBean = new PostBean();
		postBean.setTopic(post.getTopic());
		postBean.setMessage(new MessageBean("Second Test Post"));
		postBean.setPoster(post.getPoster());
		secondDate = new Date();
		postBean.setCreateDate(secondDate);
		AttachmentBean attachmentBean3 = new AttachmentBean();
		attachmentBean3.setComment("A third attachment");
		String content3 = "This is the third content";
		attachmentBean3.setContent(content3.getBytes());
		attachmentBean3.setContentType("text/plain");
		attachmentBean3.setSize(content.length());
		attachmentBean3.setName("Third attachment");
		AttachmentBean attachmentBean4 = new AttachmentBean();
		attachmentBean4.setComment("A forth attachment");
		String content4 = "This is the forth content";
		attachmentBean4.setContent(content4.getBytes());
		attachmentBean4.setContentType("text/plain");
		attachmentBean4.setSize(content4.length());
		attachmentBean4.setName("Forth attachment");
		postBean.setAttachments(asList(attachmentBean3, attachmentBean4));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		attachments = post.getAttachments();
		assertEquals(2, attachments.size());
		attachmentBean3 = attachments.get(0);
		assertEquals("A third attachment", attachmentBean3.getComment());
		assertEquals(content3, new String(attachmentBean3.getContent()));
		assertEquals("text/plain", attachmentBean3.getContentType());
		assertNotEquals(0, attachmentBean3.getId());
		assertEquals("Third attachment", attachmentBean3.getName());
		assertEquals(post, attachmentBean3.getPost());
		assertEquals(content3.length(), attachmentBean3.getSize());
		attachmentBean4 = attachments.get(1);
		assertEquals("A forth attachment", attachmentBean4.getComment());
		assertEquals(content4, new String(attachmentBean4.getContent()));
		assertEquals("text/plain", attachmentBean4.getContentType());
		assertNotEquals(0, attachmentBean4.getId());
		assertEquals("Forth attachment", attachmentBean4.getName());
		assertEquals(post, attachmentBean4.getPost());
		assertEquals(content4.length(), attachmentBean4.getSize());
	}

	@Test
	public void findAndUpdate() {

		Response response = get(url + "findPostsDesc/3", authorization);
		List<PostBean> postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
		assertEquals(2, postBeans.size());
		PostBean post = postBeans.get(1);
		List<AttachmentBean> attachments = post.getAttachments();
		assertEquals(2, attachments.size());
		AttachmentBean attachmentBean = attachments.get(0);
		String content = "This is the first content";
		assertEquals("A new attachment", attachmentBean.getComment());
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("First attachment", attachmentBean.getName());
		assertEquals(post, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		AttachmentBean attachmentBean2 = attachments.get(1);
		String content2 = "This is the second content";
		assertEquals("A second attachment", attachmentBean2.getComment());
		assertEquals(content2, new String(attachmentBean2.getContent()));
		assertEquals("text/plain", attachmentBean2.getContentType());
		assertNotEquals(0, attachmentBean2.getId());
		assertEquals("Second attachment", attachmentBean2.getName());
		assertEquals(post, attachmentBean2.getPost());
		assertEquals(content2.length(), attachmentBean2.getSize());
		post = postBeans.get(0);
		attachments = post.getAttachments();
		assertEquals(2, attachments.size());
		AttachmentBean attachmentBean3 = attachments.get(0);
		String content3 = "This is the third content";
		assertEquals("A third attachment", attachmentBean3.getComment());
		assertEquals(content3, new String(attachmentBean3.getContent()));
		assertEquals("text/plain", attachmentBean3.getContentType());
		assertNotEquals(0, attachmentBean3.getId());
		assertEquals("Third attachment", attachmentBean3.getName());
		assertEquals(post, attachmentBean3.getPost());
		assertEquals(content3.length(), attachmentBean3.getSize());
		AttachmentBean attachmentBean4 = attachments.get(1);
		String content4 = "This is the forth content";
		assertEquals("A forth attachment", attachmentBean4.getComment());
		assertEquals(content4, new String(attachmentBean4.getContent()));
		assertEquals("text/plain", attachmentBean4.getContentType());
		assertNotEquals(0, attachmentBean4.getId());
		assertEquals("Forth attachment", attachmentBean4.getName());
		assertEquals(post, attachmentBean4.getPost());
		assertEquals(content4.length(), attachmentBean4.getSize());

		response = get(url + "findAttachmentById/" + attachmentBean.getId(), authorization);
		attachmentBean = response.readEntity(AttachmentBean.class);
		assertEquals("A new attachment", attachmentBean.getComment());
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("First attachment", attachmentBean.getName());
		assertEquals(postBeans.get(1), attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());

		PostBean postBean = postBeans.get(0);
		attachmentBean = new AttachmentBean();
		attachmentBean.setComment("Fifth attachment");
		content = "This is the fifth content";
		attachmentBean.setContent(content.getBytes());
		attachmentBean.setContentType("text/plain");
		attachmentBean.setSize(content.length());
		attachmentBean.setName("Fifth attachment");
		postBean.setAttachments(asList(attachmentBean));
		response = post(url + "addAttachments", authorization, postBean);
		postBean = response.readEntity(PostBean.class);
		attachments = postBean.getAttachments();
		assertEquals(3, attachments.size());
		attachmentBean = attachments.get(0);
		assertEquals("A third attachment", attachmentBean.getComment());
		content = "This is the third content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Third attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean = attachments.get(1);
		assertEquals("A forth attachment", attachmentBean.getComment());
		content = "This is the forth content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Forth attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean = attachments.get(2);
		assertEquals("Fifth attachment", attachmentBean.getComment());
		content = "This is the fifth content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Fifth attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());

		response = post(url + "findAttachments", authorization, postBean);
		attachments = response.readEntity(new GenericType<List<AttachmentBean>>() {
		});
		assertEquals(3, attachments.size());
		attachmentBean = attachments.get(0);
		assertEquals("A third attachment", attachmentBean.getComment());
		content = "This is the third content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Third attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean = attachments.get(1);
		assertEquals("A forth attachment", attachmentBean.getComment());
		content = "This is the forth content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Forth attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean = attachments.get(2);
		assertEquals("Fifth attachment", attachmentBean.getComment());
		content = "This is the fifth content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Fifth attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());

		attachments.remove(attachmentBean);
		postBean.setAttachments(attachments);
		response = post(url + "updateAttachments", authorization, postBean);
		postBean = response.readEntity(PostBean.class);
		attachments = postBean.getAttachments();
		assertEquals(2, attachments.size());
		attachmentBean = attachments.get(0);
		assertEquals("A third attachment", attachmentBean.getComment());
		content = "This is the third content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Third attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean = attachments.get(1);
		assertEquals("A forth attachment", attachmentBean.getComment());
		content = "This is the forth content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Forth attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());

		response = post(url + "findAttachments", authorization, postBean);
		attachments = response.readEntity(new GenericType<List<AttachmentBean>>() {
		});
		assertEquals(2, attachments.size());
		attachmentBean = attachments.get(0);
		assertEquals("A third attachment", attachmentBean.getComment());
		content = "This is the third content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Third attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
		attachmentBean = attachments.get(1);
		assertEquals("A forth attachment", attachmentBean.getComment());
		content = "This is the forth content";
		assertEquals(content, new String(attachmentBean.getContent()));
		assertEquals("text/plain", attachmentBean.getContentType());
		assertNotEquals(0, attachmentBean.getId());
		assertEquals("Forth attachment", attachmentBean.getName());
		assertEquals(postBean, attachmentBean.getPost());
		assertEquals(content.length(), attachmentBean.getSize());
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
		assertEquals(2, postBeans.size(), "Posts size");
		PostBean postBean = postBeans.get(0);
		response = post(url + "removeAttachments", authorization, postBean);
		postBean = response.readEntity(PostBean.class);
		postBean = postBeans.get(1);
		response = post(url + "removeAttachments", authorization, postBean);
		postBean = response.readEntity(PostBean.class);

		response = post(url + "findPostsFromCategoryDesc", authorization, categoryRequestBean);
		postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
		assertTrue(postBeans.get(0).getAttachments().isEmpty());
		assertTrue(postBeans.get(1).getAttachments().isEmpty());

		get(url + "removePost/" + postBeans.get(1).getId() + "/false", authorization);

		response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		assertEquals(1, topicBeans.size(), "Topics size");
		PosterBean posterBean = topicBeans.get(0).getPoster();

		topicBeans.forEach(x -> {
			get(url + "removeTopic/" + x.getId(), authorization);
		});

		response = get(url + "removePoster/" + posterBean.getId(), authorization);
		PosterBean removedPosterBean = response.readEntity(PosterBean.class);
		assertNotNull(removedPosterBean, "Poster removed by poster operations");

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
