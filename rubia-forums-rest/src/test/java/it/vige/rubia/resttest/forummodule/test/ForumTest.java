package it.vige.rubia.resttest.forummodule.test;

import static it.vige.rubia.Constants.FORUM_LOCKED;
import static it.vige.rubia.Constants.FORUM_UNLOCKED;
import static java.util.Arrays.asList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.resttest.RestCaller;

public class ForumTest extends RestCaller {

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
		CategoryBean category = response.readEntity(CategoryBean.class);
		assertEquals("First Test Category", category.getTitle(), "Category title");
		assertNotNull(category.getForums(), "Category forums");
		assertTrue(category.getForums().isEmpty(), "Category forums are empty");
		assertNotNull(category.getForumInstance(), "Category Forum Instance");
		assertNull(category.getForumInstance().getCategories(), "Category Forum Instance categories");
		assertEquals(1, category.getForumInstance().getId().intValue(), "Category Forum Instance id");
		assertNull(category.getForumInstance().getName(), "Category Forum Instance name");
		assertEquals(20, category.getOrder(), "Category order");

		ForumBean forumBean = new ForumBean("First Test Forum", "First Test Description", category);
		response = post(url + "createForum", authorization, forumBean);
		forumBean = response.readEntity(ForumBean.class);
		assertEquals("First Test Category", forumBean.getCategory().getTitle(), "Category title");
		assertNotNull(forumBean.getCategory().getForums(), "Category forums");
		assertTrue(forumBean.getCategory().getForums().isEmpty(), "Category forums are empty");
		assertNull(forumBean.getCategory().getForumInstance(), "Category Forum Instance");
		assertEquals(20, forumBean.getCategory().getOrder(), "Category order");
		assertNotNull(forumBean.getTopics(), "Forum topics");
		assertTrue(forumBean.getTopics().isEmpty(), "Forum topics are empty");
		assertNull(forumBean.getWatches(), "Forum watches");
		assertNull(forumBean.getForumWatch(), "Forum watch");
		assertEquals(10, forumBean.getOrder(), "Forum order");
		assertEquals("First Test Forum", forumBean.getName(), "Forum name");
		assertEquals("First Test Description", forumBean.getDescription(), "Forum description");
		assertEquals(0, forumBean.getPostCount(), "Forum post count");
		assertEquals(false, forumBean.getPruneEnable(), "Forum prune enable");
		assertEquals(0, forumBean.getPruneNext(), "Forum prune next");
		assertEquals(0, forumBean.getStatus(), "Forum status");
		assertEquals(0, forumBean.getTopicCount(), "Forum topic count");

		forumBean = new ForumBean("Second Test Forum", "Second Test Description", category);
		response = post(url + "createForum", authorization, forumBean);
		forumBean = response.readEntity(ForumBean.class);
		assertEquals("First Test Category", forumBean.getCategory().getTitle(), "Category title");
		assertNotNull(forumBean.getCategory().getForums(), "Category forums");
		assertTrue(forumBean.getCategory().getForums().isEmpty(), "Category forums are empty");
		assertNull(forumBean.getCategory().getForumInstance(), "Category Forum Instance");
		assertEquals(20, forumBean.getCategory().getOrder(), "Category order");
		assertNotNull(forumBean.getTopics(), "Forum topics");
		assertTrue(forumBean.getTopics().isEmpty(), "Forum topics are empty");
		assertNull(forumBean.getWatches(), "Forum watches");
		assertNull(forumBean.getForumWatch(), "Forum watch");
		assertEquals(20, forumBean.getOrder(), "Forum order");
		assertEquals("Second Test Forum", forumBean.getName(), "Forum name");
		assertEquals("Second Test Description", forumBean.getDescription(), "Forum description");
		assertEquals(0, forumBean.getPostCount(), "Forum post count");
		assertEquals(false, forumBean.getPruneEnable(), "Forum prune enable");
		assertEquals(0, forumBean.getPruneNext(), "Forum prune next");
		assertEquals(0, forumBean.getStatus(), "Forum status");
		assertEquals(0, forumBean.getTopicCount(), "Forum topic count");

		categoryBean = new CategoryBean("Second Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		response = post(url + "createCategory", authorization, categoryBean);
		category = response.readEntity(CategoryBean.class);
		assertEquals("Second Test Category", category.getTitle(), "Category title");
		assertNotNull(category.getForums(), "Category forums");
		assertTrue(category.getForums().isEmpty(), "Category forums are empty");
		assertNotNull(category.getForumInstance(), "Category Forum Instance");
		assertNull(category.getForumInstance().getCategories(), "Category Forum Instance categories");
		assertEquals(1, category.getForumInstance().getId().intValue(), "Category Forum Instance id");
		assertNull(category.getForumInstance().getName(), "Category Forum Instance name");
		assertEquals(30, category.getOrder(), "Category order");

		response.close();
	}

	@Test
	public void verifyMoveForum() {
		Response response = get(url + "findForums/1", authorization);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(4, forumBeans.size(), "Forums size");

		response = get(url + "findForumById/" + forumBeans.get(1).getId(), authorization);
		ForumBean forumBean = response.readEntity(ForumBean.class);
		assertEquals(10, forumBean.getOrder(), "Forum is up");

		response = get(url + "findForumById/" + forumBeans.get(3).getId(), authorization);
		ForumBean forumBean2 = response.readEntity(ForumBean.class);
		assertEquals(20, forumBean2.getOrder(), "Forum is down");

		forumBean.setOrder(20);
		response = post(url + "updateForum", authorization, forumBean);
		response = get(url + "findForumById/" + forumBeans.get(1).getId(), authorization);
		forumBean = response.readEntity(ForumBean.class);

		forumBean2.setOrder(10);
		response = post(url + "updateForum", authorization, forumBean2);
		response = get(url + "findForumById/" + forumBeans.get(3).getId(), authorization);
		forumBean2 = response.readEntity(ForumBean.class);

		assertEquals(20, forumBean.getOrder(), "Forum is down");
		assertEquals(10, forumBean2.getOrder(), "Forum is up");

		forumBean.setOrder(10);
		response = post(url + "updateForum", authorization, forumBean);
		response = get(url + "findForumById/" + forumBeans.get(1).getId(), authorization);
		forumBean = response.readEntity(ForumBean.class);

		forumBean2.setOrder(20);
		response = post(url + "updateForum", authorization, forumBean2);
		response = get(url + "findForumById/" + forumBeans.get(3).getId(), authorization);
		forumBean2 = response.readEntity(ForumBean.class);

		assertEquals(10, forumBean.getOrder(), "Forum is up");
		assertEquals(20, forumBean2.getOrder(), "Forum is down");

		response.close();
	}

	@Test
	public void verifyLockForum() {

		Response response = get(url + "findCategories/1", authorization);
		List<CategoryBean> categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});
		assertEquals(3, categoryBeans.size(), "Categories size");

		CategoryBean categoryBean = categoryBeans.get(1);
		response = post(url + "findForumsByCategory", authorization, categoryBean);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(2, forumBeans.size(), "Forums size");

		ForumBean forumBean = forumBeans.get(1);
		forumBean.setStatus(FORUM_LOCKED);
		response = post(url + "updateForum", authorization, forumBean);
		response = get(url + "findForumById/" + forumBean.getId(), authorization);
		forumBean = response.readEntity(ForumBean.class);
		assertEquals(FORUM_LOCKED, forumBean.getStatus(), "Forum locked");

		forumBean.setStatus(FORUM_UNLOCKED);
		response = post(url + "updateForum", authorization, forumBean);
		response = get(url + "findForumById/" + forumBean.getId(), authorization);
		forumBean = response.readEntity(ForumBean.class);
		assertEquals(FORUM_UNLOCKED, forumBean.getStatus(), "Forum unlocked");

		MultivaluedMap<Integer, CategoryBean> multiValedMap = new MultivaluedHashMap<Integer, CategoryBean>();
		multiValedMap.put(1, asList(categoryBean, categoryBeans.get(1)));
		response = post(url + "addAllForums", authorization, multiValedMap);

		response = get(url + "findForumByIdFetchTopics/" + forumBean.getId(), authorization);
		forumBean = response.readEntity(ForumBean.class);
		assertEquals(FORUM_UNLOCKED, forumBean.getStatus(), "Forum fetch topics");

		multiValedMap.put(1, asList(categoryBeans.get(1), categoryBean));
		response = post(url + "addAllForums", authorization, multiValedMap);

		response = get(url + "findForumByIdFetchTopics/" + forumBean.getId(), authorization);
		forumBean = response.readEntity(ForumBean.class);
		assertEquals(FORUM_UNLOCKED, forumBean.getStatus(), "Forum fetch topics");

		response = get(url + "findCategoryByIdFetchForums/" + categoryBeans.get(1).getId(), authorization);
		categoryBean = response.readEntity(CategoryBean.class);
		assertEquals("First Test Category", categoryBean.getTitle(), "Category by id fetch forum");

		response.close();
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
		Response response = get(url + "findForums/1", authorization);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(4, forumBeans.size(), "Forums size");

		response = get(url + "findCategories/1", authorization);
		List<CategoryBean> categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});
		assertEquals(3, categoryBeans.size(), "Categories size");

		CategoryBean categoryBean = categoryBeans.get(1);
		response = post(url + "findForumsByCategory", authorization, categoryBean);
		forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		assertEquals(2, forumBeans.size(), "Forums size");

		response = get(url + "removeForum/" + forumBeans.get(0).getId(), authorization);
		response = get(url + "findForumById/" + forumBeans.get(0).getId(), authorization);
		try {
			response.readEntity(ForumBean.class);
			fail("Forum not removed");
		} catch (ProcessingException ex) {
		}
		response = get(url + "removeForum/" + forumBeans.get(1).getId(), authorization);
		response = get(url + "findForumById/" + forumBeans.get(1).getId(), authorization);
		try {
			response.readEntity(ForumBean.class);
			fail("Category not removed");
		} catch (ProcessingException ex) {
		}

		response = get(url + "removeCategory/" + categoryBean.getId(), authorization);
		response = get(url + "findCategoryById/" + categoryBean.getId(), authorization);
		try {
			response.readEntity(CategoryBean.class);
			fail("Category not removed");
		} catch (ProcessingException ex) {
		}
		response = get(url + "removeCategory/" + categoryBeans.get(2).getId(), authorization);
		response = get(url + "findCategoryById/" + categoryBeans.get(2).getId(), authorization);
		try {
			response.readEntity(CategoryBean.class);
			fail("Category not removed");
		} catch (ProcessingException ex) {
		}

		response.close();
	}
}
