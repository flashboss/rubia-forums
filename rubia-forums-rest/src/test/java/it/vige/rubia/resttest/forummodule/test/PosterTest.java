package it.vige.rubia.resttest.forummodule.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.resttest.RestCaller;

public class PosterTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(PosterTest.class);

	private final static String DEMO_USER = "demouser";

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
		Response response = get(url + "createPoster/" + DEMO_USER, authorization);
		PosterBean poster = response.readEntity(PosterBean.class);
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals(DEMO_USER, poster.getUserId());
		response.close();
	}

	@Test
	public void findAndUpdate() {
		Response response = get(url + "findPosterByUserId/" + DEMO_USER, authorization);
		PosterBean poster = response.readEntity(PosterBean.class);
		assertNotEquals(0, poster.getId());
		assertEquals(0, poster.getPostCount());
		assertEquals(DEMO_USER, poster.getUserId());
		response.close();
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
		Response response = get(url + "findPosterByUserId/" + DEMO_USER, authorization);
		PosterBean poster = response.readEntity(PosterBean.class);
		response = get(url + "removePoster/" + poster.getId(), authorization);
		PosterBean removedPosterBean = response.readEntity(PosterBean.class);
		assertNotNull(removedPosterBean, "Poster removed by poster operations");
		response = get(url + "findPosterByUserId/" + DEMO_USER, authorization);
		poster = response.readEntity(PosterBean.class);
		assertNull(poster);
		response.close();
	}

}
