package it.vige.rubia.resttest.forummodule.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
