package it.vige.rubia.selenium.links.test;

import static it.vige.rubia.Constants.RE;
import static it.vige.rubia.dto.TopicType.NORMAL;
import static it.vige.rubia.selenium.Constants.HOME_URL;
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
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.links.action.ViewFooterLink.getPage;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.selenium.links.action.ViewAtomLink;
import it.vige.rubia.selenium.links.action.ViewRSSLink;

@RunWith(Arquillian.class)
@RunAsClient
public class ExternalLinksTest {

	@Drone
	private static WebDriver driver;

	public static final String INVALID_URL = getBundle("ResourceJSF").getString("Invalid_URL");

	@BeforeClass
	public static void setUp() {
		driver.get(HOME_URL);
		String message = createCategory(driver, new CategoryBean("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		ForumBean forum = new ForumBean("First Test Forum", "First Test Description",
				new CategoryBean("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"), "First Test Topic", asList(new PostBean[] {
						new PostBean("First Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))),
						new PostBean("Second Test Body", asList(new AttachmentBean("Fourth", "Fourth Test File"))) }),
						NORMAL,
						new PollBean("First Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("First Test Answer"), new PollOptionBean("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
	}

	@Test
	public void verifyFooterLink() {
		String message = getPage(driver);
		assertNotEquals(message, INVALID_URL);
	}

	@Test
	public void verifyRSSForum() {
		ForumBean forum = new ForumBean("First Test Forum");
		ForumBean result = ViewRSSLink.getPage(driver, forum);
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
		ForumBean forum = new ForumBean("First Test Forum");
		ForumBean result = ViewAtomLink.getPage(driver, forum);
		assertNotNull(result);
		assertEquals("First Test Category", result.getCategory().getTitle());
		assertEquals(forum.getName(), result.getName());
		assertEquals(1, result.getTopics().size());
		assertEquals("First Test Topic", result.getTopics().get(0).getSubject());
		assertEquals("First Test Question", result.getTopics().get(0).getPoll().getTitle());
		assertEquals(2, result.getTopics().get(0).getPoll().getOptions().size());
		assertEquals("First Test Answer", result.getTopics().get(0).getPoll().getOptions().get(0).getQuestion());
		assertEquals("Second Test Answer", result.getTopics().get(0).getPoll().getOptions().get(1).getQuestion());
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
				((AttachmentBean) result.getTopics().get(0).getPosts().get(0).getAttachments().toArray()[0])
						.getComment());
		assertNotNull(result.getTopics().get(0).getPosts().get(1).getAttachments());
		assertEquals(3, result.getTopics().get(0).getPosts().get(1).getAttachments().size());
		assertEquals("First Test File",
				((AttachmentBean) result.getTopics().get(0).getPosts().get(1).getAttachments().toArray()[0])
						.getComment());
		assertEquals("Second Test File",
				((AttachmentBean) result.getTopics().get(0).getPosts().get(1).getAttachments().toArray()[1])
						.getComment());
		assertEquals("Third Test File",
				((AttachmentBean) result.getTopics().get(0).getPosts().get(1).getAttachments().toArray()[2])
						.getComment());
	}

	@Test
	public void verifyRSSTopic() {
		TopicBean topic = new TopicBean(new ForumBean("First Test Forum"), "First Test Topic");
		TopicBean result = ViewRSSLink.getPage(driver, topic);
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
		TopicBean topic = new TopicBean(new ForumBean("First Test Forum"), "First Test Topic");
		TopicBean result = ViewAtomLink.getPage(driver, topic);
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
		assertEquals("First Test Answer", result.getPoll().getOptions().get(0).getQuestion());
		assertEquals("Second Test Answer", result.getPoll().getOptions().get(1).getQuestion());
		assertNotNull(result.getPosts().get(0).getAttachments());
		assertEquals(3, result.getPosts().get(0).getAttachments().size());
		assertEquals("First Test File",
				((AttachmentBean) result.getPosts().get(0).getAttachments().toArray()[0]).getComment());
		assertEquals("Second Test File",
				((AttachmentBean) result.getPosts().get(0).getAttachments().toArray()[1]).getComment());
		assertEquals("Third Test File",
				((AttachmentBean) result.getPosts().get(0).getAttachments().toArray()[2]).getComment());
		assertNotNull(result.getPosts().get(1).getAttachments());
		assertEquals(1, result.getPosts().get(1).getAttachments().size());
		assertEquals("Fourth Test File",
				((AttachmentBean) result.getPosts().get(1).getAttachments().toArray()[0]).getComment());
	}

	@AfterClass
	public static void stop() {
		String message = removeTopic(driver, new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
				asList(new PostBean[] { new PostBean("First Test Body"), new PostBean("Second Test Body") })));
		assertTrue(message.equals(OK));
		ForumBean forum = new ForumBean("First Test Forum");
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
	}
}
