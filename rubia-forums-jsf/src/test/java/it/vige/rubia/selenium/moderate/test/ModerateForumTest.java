package it.vige.rubia.selenium.moderate.test;

import static it.vige.rubia.dto.TopicType.ADVICE;
import static it.vige.rubia.dto.TopicType.IMPORTANT;
import static it.vige.rubia.dto.TopicType.NORMAL;
import static it.vige.rubia.properties.OperationType.CANCEL;
import static it.vige.rubia.properties.OperationType.CONFIRM;
import static it.vige.rubia.selenium.Constants.HOME_URL;
import static it.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static it.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static it.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_2_MESSAGE;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.moderate.action.LockForum.lockForum;
import static it.vige.rubia.selenium.moderate.action.LockForum.unlockForum;
import static it.vige.rubia.selenium.moderate.action.MoveForum.moveForum;
import static it.vige.rubia.selenium.moderate.action.RemoveForum.removeForum;
import static it.vige.rubia.selenium.moderate.action.VerifyForum.goToModerate;
import static it.vige.rubia.selenium.moderate.action.VerifyTopic.verifyTopic;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

@RunWith(Arquillian.class)
@RunAsClient
public class ModerateForumTest {

	@Drone
	private static WebDriver driver;

	public final static String LOCKED_FORUM_MESSAGE = getBundle("ResourceJSF").getString("SUCC_TOPIC_LOCKED");
	public final static String UNLOCKED_FORUM_MESSAGE = getBundle("ResourceJSF").getString("SUCC_TOPIC_UNLOCKED");
	public final static String NOT_SELECTED_MESSAGE = getBundle("ResourceJSF").getString("None_selected");
	public final static String NO_DEST_FORUM_MESSAGE = getBundle("ResourceJSF").getString("ERR_NO_DEST_FORUM");
	public final static String REMOVED_FORUM_MESSAGE = getBundle("ResourceJSF").getString("SUCC_TOPIC_REMOVED");
	public final static String MOVED_FORUM_MESSAGE = getBundle("ResourceJSF").getString("SUCC_TOPIC_MOVED");

	@BeforeClass
	public static void setUp() {
		driver.get(HOME_URL);
		String message = createCategory(driver, new CategoryBean("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, new CategoryBean("Second Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver,
				new ForumBean("First Test Forum", "First Test Description", new CategoryBean("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
						asList(new PostBean[] { new PostBean("First Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						NORMAL,
						new PollBean("First Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("First Test Answer"), new PollOptionBean("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic",
						asList(new PostBean[] { new PostBean("Second Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						IMPORTANT,
						new PollBean("Second Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Third Test Answer"), new PollOptionBean("Fourth Test Answer") }),
								8)));
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver,
				new ForumBean("Second Test Forum", "Second Test Description", new CategoryBean("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic",
						asList(new PostBean[] { new PostBean("Third Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						ADVICE,
						new PollBean("Third Test Question",
								asList(new PollOptionBean[] {
										new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"),
										new PollOptionBean("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver, new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))) }),
				IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }), 0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = createForum(driver,
				new ForumBean("Third Test Forum", "Third Test Description", new CategoryBean("Second Test Category")));
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"),
						"Fifth Test with Truncation over 25 characters Topic",
						asList(new PostBean[] { new PostBean("Fifth Test with Truncation over 25 characters Body",
								asList(new AttachmentBean("seventh", "Seventh Test File"),
										new AttachmentBean("eight", "Eight Test File"),
										new AttachmentBean("ninth", "Ninth Test File"))) }),
						IMPORTANT,
						new PollBean("Third Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								8)));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
						asList(new PostBean[] { new PostBean("Sixth Test Body",
								asList(new AttachmentBean("ten", "Ten Test File"),
										new AttachmentBean("eleven", "Eleven Test File"),
										new AttachmentBean("twelve", "Twelve Test File"))) }),
						IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Ninth Test Answer"), new PollOptionBean("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void move() {
		ForumBean forum = new ForumBean("First Test Forum");
		forum.setTopics(asList(new TopicBean[] { new TopicBean("Second Test Topic") }));
		goToModerate(driver, forum);
		String message = moveForum(driver, CONFIRM, null, null);
		assertTrue(message.equals(NOT_SELECTED_MESSAGE));
		message = moveForum(driver, CANCEL, forum, null);
		assertTrue(message.equals(""));
		message = moveForum(driver, CONFIRM, forum, null);
		assertTrue(message.equals(NOT_SELECTED_MESSAGE));
		message = moveForum(driver, CONFIRM, forum, null);
		assertTrue(message.equals(NO_DEST_FORUM_MESSAGE));
		message = moveForum(driver, CONFIRM, forum, null);
		assertTrue(message.equals(NOT_SELECTED_MESSAGE));
		ForumBean destForum = new ForumBean("Second Test Forum");
		message = moveForum(driver, CONFIRM, forum, destForum);
		assertTrue(message.equals(MOVED_FORUM_MESSAGE));
		goToModerate(driver, destForum);
		destForum.setTopics(asList(new TopicBean[] { new TopicBean("Second Test Topic") }));
		TopicBean verifiedTopic = verifyTopic(driver, destForum.getTopics().get(0));
		assertNotNull(verifiedTopic);
		assertEquals("Second Test Topic", verifiedTopic.getSubject());
		assertEquals("root", verifiedTopic.getPoster().getUserId());
		assertEquals(1, verifiedTopic.getPosts().size());
		assertEquals("Second Test Topic", verifiedTopic.getPosts().get(0).getMessage().getSubject());
		assertEquals("Second Test Body", verifiedTopic.getPosts().get(0).getMessage().getText());
		assertEquals("Second Test Question", verifiedTopic.getPoll().getTitle());
		goToModerate(driver, destForum);
		message = moveForum(driver, CONFIRM, destForum, forum);
		assertTrue(message.equals(MOVED_FORUM_MESSAGE));
		goToModerate(driver, forum);
		verifiedTopic = verifyTopic(driver, forum.getTopics().get(0));
		assertNotNull(verifiedTopic);
		assertEquals("Second Test Topic", verifiedTopic.getSubject());
		assertEquals("root", verifiedTopic.getPoster().getUserId());
		assertEquals(1, verifiedTopic.getPosts().size());
		assertEquals("Second Test Topic", verifiedTopic.getPosts().get(0).getMessage().getSubject());
		assertEquals("Second Test Body", verifiedTopic.getPosts().get(0).getMessage().getText());
		assertEquals("Second Test Question", verifiedTopic.getPoll().getTitle());
	}

	@Test
	public void lock() {
		ForumBean forum = new ForumBean("Third Test Forum");
		forum.setTopics(
				asList(new TopicBean[] { new TopicBean("Fifth Test with Truncation over 25 characters Topic") }));
		goToModerate(driver, forum);
		String message = lockForum(driver, null);
		assertTrue(message.equals(NOT_SELECTED_MESSAGE));
		message = lockForum(driver, forum);
		assertTrue(message.equals(LOCKED_FORUM_MESSAGE));
		message = unlockForum(driver, forum);
		assertTrue(message.equals(NOT_SELECTED_MESSAGE));
		message = unlockForum(driver, forum);
		assertTrue(message.equals(UNLOCKED_FORUM_MESSAGE));
	}

	@AfterClass
	public static void stop() {
		ForumBean firstForum = new ForumBean("First Test Forum");
		firstForum.setTopics(
				asList(new TopicBean[] { new TopicBean("First Test Topic"), new TopicBean("Second Test Topic") }));
		ForumBean secondForum = new ForumBean("Second Test Forum");
		secondForum.setTopics(
				asList(new TopicBean[] { new TopicBean("Third Test Topic"), new TopicBean("Fourth Test Topic") }));
		ForumBean thirdForum = new ForumBean("Third Test Forum");
		thirdForum.setTopics(
				asList(new TopicBean[] { new TopicBean("Fifth Test with Truncation over 25 characters Topic"),
						new TopicBean("Sixth Test Topic") }));
		goToModerate(driver, firstForum);
		String message = removeForum(driver, CANCEL, firstForum);
		assertTrue(message.equals(""));
		goToModerate(driver, firstForum);
		message = removeForum(driver, CONFIRM, firstForum);
		assertTrue(message.equals(NOT_SELECTED_MESSAGE));
		message = removeForum(driver, CONFIRM, firstForum);
		assertTrue(message.equals(REMOVED_FORUM_MESSAGE));
		goToModerate(driver, secondForum);
		message = removeForum(driver, CONFIRM, secondForum);
		assertTrue(message.equals(REMOVED_FORUM_MESSAGE));
		goToModerate(driver, thirdForum);
		message = removeForum(driver, CONFIRM, thirdForum);
		assertTrue(message.equals(REMOVED_FORUM_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
