package it.vige.rubia.selenium.links.test;

import static it.vige.rubia.Constants.RE;
import static it.vige.rubia.model.TopicType.NORMAL;
import static it.vige.rubia.selenium.Constants.OK;
import static it.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static it.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static it.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static it.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.links.action.ViewFooterLink.getPage;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;
import it.vige.rubia.selenium.links.action.ViewAtomLink;
import it.vige.rubia.selenium.links.action.ViewRSSLink;

@RunWith(Arquillian.class)
public class ExternalLinksTest {

	@Drone
	private WebDriver driver;

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, new Category("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		Forum forum = new Forum("First Test Forum", "First Test Description", new Category("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver, new Topic(new Forum("First Test Forum"), "First Test Topic", asList(new Post[] {
				new Post("First Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))),
				new Post("Second Test Body", asList(new Attachment("Fourth", "Fourth Test File"))) }),
				NORMAL,
				new Poll("First Test Question", asList(
						new PollOption[] { new PollOption("First Test Answer"), new PollOption("Second Test Answer") }),
						4)));
		assertTrue(message.equals("First Test Topic"));
	}

	@Test
	public void verifyFooterLink() {
		String message = getPage(driver);
		assertNotEquals(message, "Invalid URL");
	}

	@Test
	public void verifyRSSForum() {
		Forum forum = new Forum("First Test Forum");
		Forum result = ViewRSSLink.getPage(driver, forum);
		assertNotNull(result);
		assertEquals("First Test Category", result.getCategory().getTitle());
		assertEquals(forum.getName(), result.getName());
		assertEquals(1, result.getTopics().size());
		assertEquals("First Test Topic", result.getTopics().get(0).getSubject());
		assertEquals(result.getTopics().get(0).getPosts().size(), 2);
		assertEquals("root", result.getTopics().get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("root", result.getTopics().get(0).getPosts().get(1).getPoster().getUserId());
		assertNotNull(result.getTopics().get(0).getPosts().get(0).getCreateDate());
		assertNotNull(result.getTopics().get(0).getPosts().get(1).getCreateDate());
		assertEquals(RE + "First Test Topic", result.getTopics().get(0).getPosts().get(0).getMessage().getSubject());
		assertEquals("First Test Topic", result.getTopics().get(0).getPosts().get(1).getMessage().getSubject());
		assertEquals("Second Test Body", result.getTopics().get(0).getPosts().get(0).getMessage().getText());
		assertEquals("First Test Body", result.getTopics().get(0).getPosts().get(1).getMessage().getText());
	}

	@Test
	public void verifyAtomForum() {
		Forum forum = new Forum("First Test Forum");
		Forum result = ViewAtomLink.getPage(driver, forum);
		assertNotNull(result);
		assertEquals("First Test Category", result.getCategory().getTitle());
		assertEquals(forum.getName(), result.getName());
		assertEquals(1, result.getTopics().size());
		assertEquals("First Test Topic", result.getTopics().get(0).getSubject());
		assertEquals("First Test Question", result.getTopics().get(0).getPoll().getTitle());
		assertEquals(2, result.getTopics().get(0).getPoll().getOptions().size());
		assertEquals("Second Test Answer", result.getTopics().get(0).getPoll().getOptions().get(0).getQuestion());
		assertEquals("First Test Answer", result.getTopics().get(0).getPoll().getOptions().get(1).getQuestion());
		assertEquals(2, result.getTopics().get(0).getPosts().size());
		assertEquals("root", result.getTopics().get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("root", result.getTopics().get(0).getPosts().get(1).getPoster().getUserId());
		assertNotNull(result.getTopics().get(0).getPosts().get(0).getCreateDate());
		assertNotNull(result.getTopics().get(0).getPosts().get(1).getCreateDate());
		assertEquals(RE + "First Test Topic", result.getTopics().get(0).getPosts().get(0).getMessage().getSubject());
		assertEquals("First Test Topic", result.getTopics().get(0).getPosts().get(1).getMessage().getSubject());
		assertEquals("Second Test Body", result.getTopics().get(0).getPosts().get(0).getMessage().getText());
		assertEquals("First Test Body", result.getTopics().get(0).getPosts().get(1).getMessage().getText());
		assertNotNull(result.getTopics().get(0).getPosts().get(0).getAttachments());
		assertEquals(1, result.getTopics().get(0).getPosts().get(0).getAttachments().size());
		assertEquals("Fourth Test File",
				((Attachment) result.getTopics().get(0).getPosts().get(0).getAttachments().toArray()[0]).getComment());
		assertNotNull(result.getTopics().get(0).getPosts().get(1).getAttachments());
		assertEquals(3, result.getTopics().get(0).getPosts().get(1).getAttachments().size());
		assertEquals("First Test File",
				((Attachment) result.getTopics().get(0).getPosts().get(1).getAttachments().toArray()[0]).getComment());
		assertEquals("Second Test File",
				((Attachment) result.getTopics().get(0).getPosts().get(1).getAttachments().toArray()[1]).getComment());
		assertEquals("Third Test File",
				((Attachment) result.getTopics().get(0).getPosts().get(1).getAttachments().toArray()[2]).getComment());
	}

	@Test
	public void verifyRSSTopic() {
		Topic topic = new Topic(new Forum("First Test Forum"), "First Test Topic");
		Topic result = ViewRSSLink.getPage(driver, topic);
		assertNotNull(result);
		assertEquals("First Test Category", result.getForum().getCategory().getTitle());
		assertEquals(topic.getForum().getName(), result.getForum().getName());
		assertEquals(2, result.getPosts().size());
		assertEquals("root", result.getPosts().get(0).getPoster().getUserId());
		assertEquals("root", result.getPosts().get(1).getPoster().getUserId());
		assertEquals("First Test Topic", result.getPosts().get(0).getMessage().getSubject());
		assertEquals(RE + "First Test Topic", result.getPosts().get(1).getMessage().getSubject());
		assertEquals("First Test Body", result.getPosts().get(0).getMessage().getText());
		assertEquals("Second Test Body", result.getPosts().get(1).getMessage().getText());
		assertNotNull(result.getPosts().get(0).getCreateDate());
		assertNotNull(result.getPosts().get(1).getCreateDate());
		assertEquals("First Test Topic", result.getSubject());
	}

	@Test
	public void verifyAtomTopic() {
		Topic topic = new Topic(new Forum("First Test Forum"), "First Test Topic");
		Topic result = ViewAtomLink.getPage(driver, topic);
		assertNotNull(result);
		assertEquals("First Test Category", result.getForum().getCategory().getTitle());
		assertEquals(topic.getForum().getName(), result.getForum().getName());
		assertEquals(2, result.getPosts().size());
		assertEquals("root", result.getPosts().get(0).getPoster().getUserId());
		assertEquals("root", result.getPosts().get(1).getPoster().getUserId());
		assertEquals("First Test Topic", result.getPosts().get(0).getMessage().getSubject());
		assertEquals(RE + "First Test Topic", result.getPosts().get(1).getMessage().getSubject());
		assertEquals("First Test Body", result.getPosts().get(0).getMessage().getText());
		assertEquals("Second Test Body", result.getPosts().get(1).getMessage().getText());
		assertNotNull(result.getPosts().get(0).getCreateDate());
		assertNotNull(result.getPosts().get(1).getCreateDate());
		assertEquals("First Test Topic", result.getSubject());
		assertEquals("First Test Question", result.getPoll().getTitle());
		assertEquals(2, result.getPoll().getOptions().size());
		assertEquals("Second Test Answer", result.getPoll().getOptions().get(0).getQuestion());
		assertEquals("First Test Answer", result.getPoll().getOptions().get(1).getQuestion());
		assertNotNull(result.getPosts().get(0).getAttachments());
		assertEquals(3, result.getPosts().get(0).getAttachments().size());
		assertEquals("First Test File",
				((Attachment) result.getPosts().get(0).getAttachments().toArray()[0]).getComment());
		assertEquals("Second Test File",
				((Attachment) result.getPosts().get(0).getAttachments().toArray()[1]).getComment());
		assertEquals("Third Test File",
				((Attachment) result.getPosts().get(0).getAttachments().toArray()[2]).getComment());
		assertNotNull(result.getPosts().get(1).getAttachments());
		assertEquals(1, result.getPosts().get(1).getAttachments().size());
		assertEquals("Fourth Test File",
				((Attachment) result.getPosts().get(1).getAttachments().toArray()[0]).getComment());
	}

	@After
	public void stop() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = removeTopic(driver, new Topic(new Forum("First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body"), new Post("Second Test Body") })));
		assertTrue(message.equals(OK));
		Forum forum = new Forum("First Test Forum");
		message = removeForum(driver, forum, forum.getName());
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
	}
}
