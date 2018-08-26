package it.vige.rubia.resttest.forummodule.adminpanel.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.resttest.RestCaller;

public class AdminPanelCategoryTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(AdminPanelCategoryTest.class);

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
		assertEquals(30, category.getOrder(), "Category order");
	}

	@Test
	public void verifyMoveCategory() {
		Response response = get(url + "findCategoryById/1", authorization);
		CategoryBean categoryBean = response.readEntity(CategoryBean.class);
		assertEquals(20, categoryBean.getOrder(), "Category is up");

		response = get(url + "findCategoryById/2", authorization);
		CategoryBean categoryBean2 = response.readEntity(CategoryBean.class);
		assertEquals(30, categoryBean2.getOrder(), "Category is down");

		categoryBean.setOrder(30);
		response = post(url + "updateCategory", authorization, categoryBean);
		response = get(url + "findCategoryById/1", authorization);
		categoryBean = response.readEntity(CategoryBean.class);

		categoryBean2.setOrder(20);
		response = post(url + "updateCategory", authorization, categoryBean2);
		response = get(url + "findCategoryById/2", authorization);
		categoryBean2 = response.readEntity(CategoryBean.class);

		assertEquals(30, categoryBean.getOrder(), "Category is down");
		assertEquals(20, categoryBean2.getOrder(), "Category is up");

		categoryBean.setOrder(20);
		response = post(url + "updateCategory", authorization, categoryBean);
		response = get(url + "findCategoryById/1", authorization);
		categoryBean = response.readEntity(CategoryBean.class);

		categoryBean2.setOrder(30);
		response = post(url + "updateCategory", authorization, categoryBean2);
		response = get(url + "findCategoryById/2", authorization);
		categoryBean2 = response.readEntity(CategoryBean.class);

		assertEquals(20, categoryBean.getOrder(), "Category is up");
		assertEquals(30, categoryBean2.getOrder(), "Category is down");
		response.close();
	}

	@Test
	public void verifyUpdateCategory() {

		Response response = get(url + "findCategoryById/1", authorization);
		CategoryBean categoryBean = response.readEntity(CategoryBean.class);
		assertEquals("First Test Category", categoryBean.getTitle(), "Category title");

		categoryBean.setTitle("Third Test Category");
		response = post(url + "updateCategory", authorization, categoryBean);
		response = get(url + "findCategoryById/1", authorization);
		categoryBean = response.readEntity(CategoryBean.class);
		assertEquals("Third Test Category", categoryBean.getTitle(), "Category changed title");

		categoryBean.setTitle("First Test Category");
		response = post(url + "updateCategory", authorization, categoryBean);
		response = get(url + "findCategoryById/1", authorization);
		categoryBean = response.readEntity(CategoryBean.class);
		assertEquals("First Test Category", categoryBean.getTitle(), "Category rechanged title");
		response.close();
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
		Response response = get(url + "removeCategory/1", authorization);
		response = get(url + "findCategoryById/1", authorization);
		try {
			response.readEntity(CategoryBean.class);
			fail("Category not removed");
		} catch (ProcessingException ex) {
		}
		response = get(url + "removeCategory/2", authorization);
		response = get(url + "findCategoryById/2", authorization);
		try {
			response.readEntity(CategoryBean.class);
			fail("Category not removed");
		} catch (ProcessingException ex) {
		}
		response.close();
	}
}
