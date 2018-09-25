package it.vige.rubia.resttest.forummodule.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.resttest.RestCaller;

public class ForumInstanceTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(ForumInstanceTest.class);

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
		Response response = get(url + "createForumInstance/2/myforuminstance", authorization);
		ForumInstanceBean forumInstance = response.readEntity(ForumInstanceBean.class);
		assertNotNull(forumInstance);
		assertEquals(2, forumInstance.getId().intValue());
		assertEquals("myforuminstance", forumInstance.getName());
	}

	@Test
	public void findAndUpdate() {
		Response response = get(url + "findForumInstanceById/2", authorization);
		ForumInstanceBean forumInstance = response.readEntity(ForumInstanceBean.class);
		assertNotNull(forumInstance);
		assertEquals(2, forumInstance.getId().intValue());
		assertEquals("myforuminstance", forumInstance.getName());
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
		Response response = get(url + "removeForumInstance/2", authorization);
		response.readEntity(ForumInstanceBean.class);
		response = get(url + "findForumInstanceById/2", authorization);
		ForumInstanceBean forumInstance = response.readEntity(ForumInstanceBean.class);
		assertNull(forumInstance);
	}
}
