package org.vige.rubia.selenium.moderate.test;

import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.properties.OperationType.CANCEL;
import static org.vige.rubia.properties.OperationType.CONFIRM;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_2_MESSAGE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.moderate.action.LockForum.lockForum;
import static org.vige.rubia.selenium.moderate.action.LockForum.unlockForum;
import static org.vige.rubia.selenium.moderate.action.MoveForum.moveForum;
import static org.vige.rubia.selenium.moderate.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.moderate.action.VerifyForum.goToModerate;
import static org.vige.rubia.selenium.moderate.action.VerifyTopic.verifyTopic;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

@RunWith(Arquillian.class)
public class ModerateForumTest {

	@Drone
	private WebDriver driver;

	public final static String LOCKED_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_LOCKED");
	public final static String UNLOCKED_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_UNLOCKED");
	public final static String NOT_SELECTED_MESSAGE = getBundle("ResourceJSF")
			.getString("None_selected");
	public final static String NO_DEST_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("ERR_NO_DEST_FORUM");
	public final static String REMOVED_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_REMOVED");
	public final static String MOVED_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_MOVED");

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, new Category(
				"First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, new Category("Second Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver, new Forum("First Test Forum",
				"First Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"First Test Topic",
						asList(new Post[] { new Post("First Test Body", asList(
								new Attachment("first", "First Test File"),
								new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
						NORMAL,
						new Poll(
								"First Test Question",
								asList(new PollOption[] {
										new PollOption("First Test Answer"),
										new PollOption("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"Second Test Topic",
						asList(new Post[] { new Post("Second Test Body",
								asList(new Attachment("first",
										"First Test File"), new Attachment(
										"second", "Second Test File"),
										new Attachment("third",
												"Third Test File"))) }),
						IMPORTANT,
						new Poll(
								"Second Test Question",
								asList(new PollOption[] {
										new PollOption("Third Test Answer"),
										new PollOption("Fourth Test Answer") }),
								8)));
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver, new Forum("Second Test Forum",
				"Second Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(
				driver,
				new Topic(
						new Forum("Second Test Forum"),
						"Third Test Topic",
						asList(new Post[] { new Post("Third Test Body", asList(
								new Attachment("first", "First Test File"),
								new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
						ADVICE, new Poll("Third Test Question",
								asList(new PollOption[] {
										new PollOption("Fifth Test Answer"),
										new PollOption("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
						asList(new Post[] { new Post("Fourth Test Body",
								asList(new Attachment("fourth",
										"Fourth Test File"), new Attachment(
										"fifth", "Fifth Test File"),
										new Attachment("sixth",
												"Sixth Test File"))) }),
						IMPORTANT, new Poll("Fourth Test Question",
								asList(new PollOption[] {
										new PollOption("Seventh Test Answer"),
										new PollOption("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = createForum(driver, new Forum("Third Test Forum",
				"Third Test Description", new Category("Second Test Category")));
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = createTopic(
				driver,
				new Topic(
						new Forum("Third Test Forum"),
						"Fifth Test Topic",
						asList(new Post[] { new Post("Fifth Test Body", asList(
								new Attachment("seventh", "Seventh Test File"),
								new Attachment("eight", "Eight Test File"),
								new Attachment("ninth", "Ninth Test File"))) }),
						IMPORTANT, new Poll("Third Test Question",
								asList(new PollOption[] {
										new PollOption("Seventh Test Answer"),
										new PollOption("Eight Test Answer") }),
								8)));
		assertTrue(message.equals("Fifth Test Topic"));
		message = createTopic(
				driver,
				new Topic(
						new Forum("Third Test Forum"),
						"Sixth Test Topic",
						asList(new Post[] { new Post("Sixth Test Body", asList(
								new Attachment("ten", "Ten Test File"),
								new Attachment("eleven", "Eleven Test File"),
								new Attachment("twelve", "Twelve Test File"))) }),
						IMPORTANT,
						new Poll("Fourth Test Question",
								asList(new PollOption[] {
										new PollOption("Ninth Test Answer"),
										new PollOption("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void move() {
		Forum forum = new Forum("First Test Forum");
		forum.setTopics(asList(new Topic[] { new Topic("Second Test Topic") }));
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
		Forum destForum = new Forum("Second Test Forum");
		message = moveForum(driver, CONFIRM, forum, destForum);
		assertTrue(message.equals(MOVED_FORUM_MESSAGE));
		goToModerate(driver, destForum);
		destForum
				.setTopics(asList(new Topic[] { new Topic("Second Test Topic") }));
		Topic verifiedTopic = verifyTopic(driver, destForum.getTopics().get(0));
		assertNotNull(verifiedTopic);
		assertEquals(verifiedTopic.getSubject(), "Second Test Topic");
		assertEquals(verifiedTopic.getPoster().getUserId(), "root");
		assertEquals(verifiedTopic.getPosts().size(), 1);
		assertEquals(verifiedTopic.getPosts().get(0).getMessage().getSubject(),
				"Second Test Topic");
		assertEquals(verifiedTopic.getPosts().get(0).getMessage().getText(),
				"Second Test Body");
		assertEquals(verifiedTopic.getPoll().getTitle(), "Second Test Question");
		goToModerate(driver, destForum);
		message = moveForum(driver, CONFIRM, destForum, forum);
		assertTrue(message.equals(MOVED_FORUM_MESSAGE));
		goToModerate(driver, forum);
		verifiedTopic = verifyTopic(driver, forum.getTopics().get(0));
		assertNotNull(verifiedTopic);
		assertEquals(verifiedTopic.getSubject(), "Second Test Topic");
		assertEquals(verifiedTopic.getPoster().getUserId(), "root");
		assertEquals(verifiedTopic.getPosts().size(), 1);
		assertEquals(verifiedTopic.getPosts().get(0).getMessage().getSubject(),
				"Second Test Topic");
		assertEquals(verifiedTopic.getPosts().get(0).getMessage().getText(),
				"Second Test Body");
		assertEquals(verifiedTopic.getPoll().getTitle(), "Second Test Question");
	}

	@Test
	public void lock() {
		Forum forum = new Forum("Third Test Forum");
		forum.setTopics(asList(new Topic[] { new Topic("Fifth Test Topic") }));
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

	@After
	public void stop() {
		Forum firstForum = new Forum("First Test Forum");
		firstForum
				.setTopics(asList(new Topic[] { new Topic("First Test Topic"),
						new Topic("Second Test Topic") }));
		Forum secondForum = new Forum("Second Test Forum");
		secondForum
				.setTopics(asList(new Topic[] { new Topic("Third Test Topic"),
						new Topic("Fourth Test Topic") }));
		Forum thirdForum = new Forum("Third Test Forum");
		thirdForum
				.setTopics(asList(new Topic[] { new Topic("Fifth Test Topic"),
						new Topic("Sixth Test Topic") }));
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
		message = removeCategory(driver, new Category("First Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
