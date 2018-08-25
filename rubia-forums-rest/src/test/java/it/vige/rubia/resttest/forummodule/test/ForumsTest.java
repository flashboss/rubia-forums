package it.vige.rubia.resttest.forummodule.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.resttest.RestCaller;

public class ForumsTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(ForumsTest.class);

	@Test
	public void addForum() {
		Response response = get(url + "findForums/1", authorization);
		List<ForumBean> forums = response.readEntity(new GenericType<List<ForumBean>>() {
		});
		log.debug("calling forums by forum instance =" + forums);
		assertNotNull(forums, "Forums loaded");
		assertEquals(2, forums.size(), "Forums are two");
		response.close();

	}
}
