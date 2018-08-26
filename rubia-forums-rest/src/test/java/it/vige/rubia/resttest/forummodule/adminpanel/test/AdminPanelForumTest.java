package it.vige.rubia.resttest.forummodule.adminpanel.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import it.vige.rubia.resttest.RestCaller;

public class AdminPanelForumTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(AdminPanelForumTest.class);

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
		ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
		forumInstanceBean.setId(1);

		CategoryBean categoryBean = new CategoryBean("First Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		Response response = post(url + "createCategory", authorization, categoryBean);
		CategoryBean category = response.readEntity(CategoryBean.class);
		assertEquals(1, category.getId().intValue(), "Category id");
		assertEquals("First Test Category", category.getTitle(), "Category title");
		assertNotNull(category.getForums(), "Category forums");
		assertTrue(category.getForums().isEmpty(), "Category forums are empty");
		assertNotNull(category.getForumInstance(), "Category Forum Instance");
		assertNull(category.getForumInstance().getCategories(), "Category Forum Instance categories");
		assertEquals(1, category.getForumInstance().getId().intValue(), "Category Forum Instance id");
		assertNull(category.getForumInstance().getName(), "Category Forum Instance name");
		assertEquals(20, category.getOrder(), "Category order");

		categoryBean = new CategoryBean("Second Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		response = post(url + "createCategory", authorization, categoryBean);
		category = response.readEntity(CategoryBean.class);
		assertEquals(2, category.getId().intValue(), "Category id");
		assertEquals("Second Test Category", category.getTitle(), "Category title");
		assertNotNull(category.getForums(), "Category forums");
		assertTrue(category.getForums().isEmpty(), "Category forums are empty");
		assertNotNull(category.getForumInstance(), "Category Forum Instance");
		assertNull(category.getForumInstance().getCategories(), "Category Forum Instance categories");
		assertEquals(1, category.getForumInstance().getId().intValue(), "Category Forum Instance id");
		assertNull(category.getForumInstance().getName(), "Category Forum Instance name");
		assertEquals(40, category.getOrder(), "Category order");
	}

	@Test
	public void verifyMoveCategory() {
		Response response = get(url + "findCategoryById/1", authorization);
		CategoryBean categoryBean = response.readEntity(CategoryBean.class);
		assertEquals(30, categoryBean.getOrder(), "Category is up");

		response = get(url + "findCategoryById/2", authorization);
		CategoryBean categoryBean2 = response.readEntity(CategoryBean.class);
		assertEquals(40, categoryBean2.getOrder(), "Category is down");

		categoryBean.setOrder(30);
		response = post(url + "updateCategory", authorization, categoryBean);
		categoryBean = response.readEntity(CategoryBean.class);

		categoryBean2.setOrder(40);
		response = post(url + "updateCategory", authorization, categoryBean2);
		categoryBean2 = response.readEntity(CategoryBean.class);

		assertEquals(40, categoryBean.getOrder(), "Category is down");
		assertEquals(30, categoryBean2.getOrder(), "Category is up");

		categoryBean.setOrder(40);
		response = post(url + "updateCategory", authorization, categoryBean);
		categoryBean = response.readEntity(CategoryBean.class);

		categoryBean2.setOrder(30);
		response = post(url + "updateCategory", authorization, categoryBean2);
		categoryBean2 = response.readEntity(CategoryBean.class);

		assertEquals(30, categoryBean.getOrder(), "Category is up");
		assertEquals(40, categoryBean2.getOrder(), "Category is down");
	}

	@Test
	public void readForum() {
		Response response = get(url + "findForums/1", authorization);
		List<ForumBean> forums = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		log.debug("calling forums by forum instance =" + forums);
		assertNotNull(forums, "Forums loaded");
		assertEquals(2, forums.size(), "Forums are two");
		ForumBean forum = forums.get(0);
		assertEquals(0, forum.getCategory().getId().intValue(), "Category id");
		assertEquals("Dummy demo category", forum.getCategory().getTitle(), "Category title");
		assertNotNull(forum.getCategory().getForums(), "Category forums");
		assertTrue(forum.getCategory().getForums().isEmpty(), "Category forums are empty");
		assertNull(forum.getCategory().getForumInstance(), "Category Forum Instance");
		assertEquals(10, forum.getCategory().getOrder(), "Category order");
		assertNotNull(forum.getTopics(), "Forum topics");
		assertTrue(forum.getTopics().isEmpty(), "Forum topics are empty");
		assertNotNull(forum.getWatches(), "Forum watches");
		assertTrue(forum.getWatches().isEmpty(), "Forum watches are empty");
		assertNull(forum.getForumWatch(), "Forum watch");
		assertEquals(0, forum.getId().intValue(), "Forum id");
		assertEquals(10, forum.getOrder(), "Forum order");
		assertEquals("First forum", forum.getName(), "Forum name");
		assertEquals("First description", forum.getDescription(), "Forum description");
		assertEquals(0, forum.getPostCount(), "Forum post count");
		assertEquals(false, forum.getPruneEnable(), "Forum prune enable");
		assertEquals(0, forum.getPruneNext(), "Forum prune next");
		assertEquals(0, forum.getStatus(), "Forum status");
		assertEquals(0, forum.getTopicCount(), "Forum topic count");
		ForumBean forum2 = forums.get(1);
		assertEquals(0, forum2.getCategory().getId().intValue(), "Category id");
		assertEquals("Dummy demo category", forum2.getCategory().getTitle(), "Category title");
		assertNotNull(forum2.getCategory().getForums(), "Category forums");
		assertTrue(forum2.getCategory().getForums().isEmpty(), "Category forums are empty");
		assertNull(forum2.getCategory().getForumInstance(), "Category Forum Instance");
		assertEquals(10, forum2.getCategory().getOrder(), "Category order");
		assertNotNull(forum2.getTopics(), "Forum topics");
		assertTrue(forum2.getTopics().isEmpty(), "Forum topics are empty");
		assertNotNull(forum2.getWatches(), "Forum watches");
		assertTrue(forum2.getWatches().isEmpty(), "Forum watches are empty");
		assertNull(forum2.getForumWatch(), "Forum watch");
		assertEquals(1, forum2.getId().intValue(), "Forum id");
		assertEquals(20, forum2.getOrder(), "Forum order");
		assertEquals("Second forum", forum2.getName(), "Forum name");
		assertEquals("Second description", forum2.getDescription(), "Forum description");
		assertEquals(0, forum2.getPostCount(), "Forum post count");
		assertEquals(false, forum2.getPruneEnable(), "Forum prune enable");
		assertEquals(0, forum2.getPruneNext(), "Forum prune next");
		assertEquals(0, forum2.getStatus(), "Forum status");
		assertEquals(0, forum2.getTopicCount(), "Forum topic count");
		response.close();

	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
		Response response = get(url + "removeCategory/1", authorization);
		assertNull(response.getEntity(), "Category was removed");
		response = get(url + "removeCategory/2", authorization);
		assertNull(response.getEntity(), "Category was removed");
	}
}
