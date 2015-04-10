package org.vige.rubia.selenium.moderate.test;

import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.properties.OperationType.CANCEL;
import static org.vige.rubia.properties.OperationType.CONFIRM;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreatePost.LOCKED;
import static org.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static org.vige.rubia.selenium.moderate.action.LockTopic.lockTopic;
import static org.vige.rubia.selenium.moderate.action.MoveTopic.goToSplitPanel;
import static org.vige.rubia.selenium.moderate.action.MoveTopic.moveTopic;
import static org.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicFromSelectedDown;
import static org.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicFromSelectedUp;
import static org.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicSelectedDown;
import static org.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicSelectedUp;
import static org.vige.rubia.selenium.moderate.action.RemoveTopic.removeTopic;

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
public class ModerateTopicTest {

	@Drone
	private WebDriver driver;

	public final static String ERR_NO_POST_SELECTED = getBundle("ResourceJSF")
			.getString("ERR_NO_POST_SELECTED");
	public final static String ERR_DEST_FORUM = getBundle("ResourceJSF")
			.getString("ERR_DEST_FORUM");
	public final static String ERR_NO_SUBJECT_GIVEN = getBundle("ResourceJSF")
			.getString("ERR_NO_SUBJECT_GIVEN");
	public final static String SUCC_TOPIC_SPLITTED = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_SPLITTED");
	public final static String ERR_SPLIT_ALL = getBundle("ResourceJSF")
			.getString("ERR_SPLIT_ALL");
	public final static String SUCC_TOPIC_REMOVED = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_REMOVED");
	public final static String SUCC_TOPIC_LOCKED = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_LOCKED");
	public final static String SUCC_TOPIC_UNLOCKED = getBundle("ResourceJSF")
			.getString("SUCC_TOPIC_UNLOCKED");
	public final static String ERR_NO_DEST_FORUM = getBundle("ResourceJSF")
			.getString("ERR_NO_DEST_FORUM");
	public final static String SUCC_TOPIC_MOVED = getBundle("ResourceJSF")
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
						asList(new Post[] {
								new Post("First Test Body", asList(
										new Attachment("first",
												"First Test File"),
										new Attachment("second",
												"Second Test File"),
										new Attachment("third",
												"Third Test File"))),
								new Post("Ninteen Test Body"),
								new Post("Twenty Test Body"),
								new Post("Twentyone Test Body"),
								new Post("Twentytwo Test Body"),
								new Post("Twentythree Test Body"),
								new Post("Twentyfour Test Body") }),
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
						asList(new Post[] {
								new Post("Second Test Body", asList(
										new Attachment("first",
												"First Test File"),
										new Attachment("second",
												"Second Test File"),
										new Attachment("third",
												"Third Test File"))),
								new Post("Twentyfive Test Body"),
								new Post("Twentysix Test Body"),
								new Post("Twentyseven Test Body"),
								new Post("Twentyeight Test Body"),
								new Post("Twentynine Test Body"),
								new Post("Thirty Test Body") }),
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
				new Topic(new Forum("Second Test Forum"), "Third Test Topic",
						asList(new Post[] {
								new Post("Third Test Body", asList(
										new Attachment("first",
												"First Test File"),
										new Attachment("second",
												"Second Test File"),
										new Attachment("third",
												"Third Test File"))),
								new Post("Thirtyone Test Body"),
								new Post("Thirtytwo Test Body"),
								new Post("Thirtythree Test Body"),
								new Post("Thirtyfour Test Body"),
								new Post("Thirtyfive Test Body"),
								new Post("Thirtysix Test Body") }), ADVICE,
						new Poll("Third Test Question",
								asList(new PollOption[] {
										new PollOption("Fifth Test Answer"),
										new PollOption("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
						asList(new Post[] {
								new Post("Fourth Test Body", asList(
										new Attachment("fourth",
												"Fourth Test File"),
										new Attachment("fifth",
												"Fifth Test File"),
										new Attachment("sixth",
												"Sixth Test File"))),
								new Post("Thirtyseven Test Body"),
								new Post("Thirtyeight Test Body"),
								new Post("Thirtynine Test Body"),
								new Post("Fourty Test Body"),
								new Post("Fourtyone Test Body"),
								new Post("Fourtytwo Test Body") }), IMPORTANT,
						new Poll("Fourth Test Question",
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
				new Topic(new Forum("Third Test Forum"), "Fifth Test Topic",
						asList(new Post[] {
								new Post("Fifth Test Body", asList(
										new Attachment("seventh",
												"Seventh Test File"),
										new Attachment("eight",
												"Eight Test File"),
										new Attachment("ninth",
												"Ninth Test File"))),
								new Post("Fourtythree Test Body"),
								new Post("Fourtyfour Test Body"),
								new Post("Fourtyfive Test Body"),
								new Post("Fourtysix Test Body"),
								new Post("Fourtyseven Test Body"),
								new Post("Fourtyeight Test Body") }),
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
						asList(new Post[] {
								new Post("Sixth Test Body", asList(
										new Attachment("ten", "Ten Test File"),
										new Attachment("eleven",
												"Eleven Test File"),
										new Attachment("twelve",
												"Twelve Test File"))),
								new Post("Fourtynine Test Body"),
								new Post("Fifty Test Body"),
								new Post("Fiftyone Test Body"),
								new Post("Fiftytwo Test Body"),
								new Post("Fiftythree Test Body"),
								new Post("Fiftyfour Test Body") }),
						IMPORTANT,
						new Poll("Fourth Test Question",
								asList(new PollOption[] {
										new PollOption("Ninth Test Answer"),
										new PollOption("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void move() {
		Forum srcForum = new Forum("First Test Forum");
		Topic topic = new Topic(srcForum, "First Test Topic");
		Forum destForum = new Forum("Second Test Forum");
		goTo(driver, topic);
		String message = moveTopic(driver, null, CONFIRM);
		assertEquals(message, ERR_NO_DEST_FORUM);
		goTo(driver, topic);
		message = moveTopic(driver, destForum, CANCEL);
		assertEquals(message, "");
		message = moveTopic(driver, destForum, CONFIRM);
		assertEquals(message, SUCC_TOPIC_MOVED);
		topic.setForum(destForum);
		goTo(driver, topic);
		message = moveTopic(driver, srcForum, CONFIRM);
		assertEquals(message, SUCC_TOPIC_MOVED);
	}

	@Test
	public void lock() {
		Forum forum = new Forum("Second Test Forum");
		Topic topic = new Topic(forum, "Third Test Topic");
		String message = lockTopic(driver, topic);
		assertEquals(message, SUCC_TOPIC_LOCKED);
		message = createPost(driver, new Post(topic, "new Test Post"));
		assertEquals(message, LOCKED);
		message = lockTopic(driver, topic);
		assertEquals(message, SUCC_TOPIC_UNLOCKED);
	}

	@Test
	public void moveSelectedUp() {
		Topic topic = new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicSelectedUp(driver, topic, null);
		assertEquals(message, ERR_NO_POST_SELECTED);
		topic.setPosts(asList(new Post[] {
				new Post("Fourth Test Body", asList(new Attachment("fourth",
						"Fourth Test File"), new Attachment("fifth",
						"Fifth Test File"), new Attachment("sixth",
						"Sixth Test File"))),
				new Post("Thirtyeight Test Body"),
				new Post("Fourtyone Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(message, ERR_DEST_FORUM);
		Forum forum = new Forum("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, ERR_NO_SUBJECT_GIVEN);
		forum.setTopics(asList(new Topic[] { new Topic("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, SUCC_TOPIC_SPLITTED);
		forum.setName("Second Test Forum");
		forum.setTopics(asList(new Topic[] { new Topic("Fourth Test Topic") }));
		topic.setForum(new Forum("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(message, ERR_SPLIT_ALL);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setForum(new Forum("Second Test Forum"));
		topic.setSubject("Fourth Test Topic");
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
						asList(new Post[] {
								new Post("Fourth Test Body", asList(
										new Attachment("fourth",
												"Fourth Test File"),
										new Attachment("fifth",
												"Fifth Test File"),
										new Attachment("sixth",
												"Sixth Test File"))),
								new Post("Thirtyseven Test Body"),
								new Post("Thirtyeight Test Body"),
								new Post("Thirtynine Test Body"),
								new Post("Fourty Test Body"),
								new Post("Fourtyone Test Body"),
								new Post("Fourtytwo Test Body") }), IMPORTANT,
						new Poll("Fourth Test Question",
								asList(new PollOption[] {
										new PollOption("Seventh Test Answer"),
										new PollOption("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
	}

	@Test
	public void moveFromSelectedUp() {
		Topic topic = new Topic(new Forum("Third Test Forum"),
				"Fifth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicFromSelectedUp(driver, topic, null);
		assertEquals(message, ERR_NO_POST_SELECTED);
		topic.setPosts(asList(new Post[] { new Post("Fourtyfive Test Body"),
				new Post("Fourtysix Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(message, ERR_DEST_FORUM);
		Forum forum = new Forum("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, ERR_NO_SUBJECT_GIVEN);
		forum.setTopics(asList(new Topic[] { new Topic("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, SUCC_TOPIC_SPLITTED);
		forum.setName("Third Test Forum");
		forum.setTopics(asList(new Topic[] { new Topic("Fifth Test Topic") }));
		topic.setForum(new Forum("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(message, ERR_SPLIT_ALL);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setForum(new Forum("Third Test Forum"));
		topic.setSubject("Fifth Test Topic");
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(
				driver,
				new Topic(new Forum("Third Test Forum"), "Fifth Test Topic",
						asList(new Post[] {
								new Post("Fifth Test Body", asList(
										new Attachment("seventh",
												"Seventh Test File"),
										new Attachment("eight",
												"Eight Test File"),
										new Attachment("ninth",
												"Ninth Test File"))),
								new Post("Fourtythree Test Body"),
								new Post("Fourtyfour Test Body"),
								new Post("Fourtyfive Test Body"),
								new Post("Fourtysix Test Body"),
								new Post("Fourtyseven Test Body"),
								new Post("Fourtyeight Test Body") }),
						IMPORTANT, new Poll("Third Test Question",
								asList(new PollOption[] {
										new PollOption("Seventh Test Answer"),
										new PollOption("Eight Test Answer") }),
								8)));
		assertTrue(message.equals("Fifth Test Topic"));
	}

	@Test
	public void moveSelectedDown() {
		Topic topic = new Topic(new Forum("Third Test Forum"),
				"Sixth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(message, ERR_NO_POST_SELECTED);
		topic.setPosts(asList(new Post[] { new Post("Fifty Test Body"),
				new Post("Fiftytwo Test Body"),
				new Post("Fiftythree Test Body"),
				new Post("Fiftyfour Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(message, ERR_DEST_FORUM);
		Forum forum = new Forum("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, ERR_NO_SUBJECT_GIVEN);
		forum.setTopics(asList(new Topic[] { new Topic("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, SUCC_TOPIC_SPLITTED);
		forum.setName("Third Test Forum");
		forum.setTopics(asList(new Topic[] { new Topic("Sixth Test Topic") }));
		topic.setForum(new Forum("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(message, ERR_SPLIT_ALL);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setSubject("Sixth Test Topic");
		topic.setForum(new Forum("Third Test Forum"));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(
				driver,
				new Topic(
						new Forum("Third Test Forum"),
						"Sixth Test Topic",
						asList(new Post[] {
								new Post("Sixth Test Body", asList(
										new Attachment("ten", "Ten Test File"),
										new Attachment("eleven",
												"Eleven Test File"),
										new Attachment("twelve",
												"Twelve Test File"))),
								new Post("Fourtynine Test Body"),
								new Post("Fifty Test Body"),
								new Post("Fiftyone Test Body"),
								new Post("Fiftytwo Test Body"),
								new Post("Fiftythree Test Body"),
								new Post("Fiftyfour Test Body") }),
						IMPORTANT,
						new Poll("Fourth Test Question",
								asList(new PollOption[] {
										new PollOption("Ninth Test Answer"),
										new PollOption("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void moveFromSelectedDown() {
		Topic topic = new Topic(new Forum("Third Test Forum"),
				"Sixth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicFromSelectedDown(driver, topic, null);
		assertEquals(message, ERR_NO_POST_SELECTED);
		topic.setPosts(asList(new Post[] { new Post("Fifty Test Body"),
				new Post("Fiftyone Test Body"), new Post("Fiftytwo Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(message, ERR_DEST_FORUM);
		Forum forum = new Forum("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, ERR_NO_SUBJECT_GIVEN);
		forum.setTopics(asList(new Topic[] { new Topic("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(message, SUCC_TOPIC_SPLITTED);
		forum.setName("Third Test Forum");
		forum.setTopics(asList(new Topic[] { new Topic("Sixth Test Topic") }));
		topic.setForum(new Forum("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(message, ERR_SPLIT_ALL);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setForum(new Forum("Third Test Forum"));
		topic.setSubject("Sixth Test Topic");
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(
				driver,
				new Topic(
						new Forum("Third Test Forum"),
						"Sixth Test Topic",
						asList(new Post[] {
								new Post("Sixth Test Body", asList(
										new Attachment("ten", "Ten Test File"),
										new Attachment("eleven",
												"Eleven Test File"),
										new Attachment("twelve",
												"Twelve Test File"))),
								new Post("Fourtynine Test Body"),
								new Post("Fifty Test Body"),
								new Post("Fiftyone Test Body"),
								new Post("Fiftytwo Test Body"),
								new Post("Fiftythree Test Body"),
								new Post("Fiftyfour Test Body") }),
						IMPORTANT,
						new Poll("Fourth Test Question",
								asList(new PollOption[] {
										new PollOption("Ninth Test Answer"),
										new PollOption("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@After
	public void stop() {
		Topic topic = new Topic(new Forum("First Test Forum"),
				"First Test Topic", asList(new Post[] { new Post(
						"First Test Body") }));
		String message = removeTopic(driver, CANCEL, topic);
		assertTrue(message.equals(""));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new Topic(new Forum("First Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new Topic(new Forum("Third Test Forum"), "Fifth Test Topic",
				asList(new Post[] { new Post("Fifth Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new Topic(new Forum("Third Test Forum"), "Sixth Test Topic",
				asList(new Post[] { new Post("Sixth Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = removeForum(driver, new Forum("First Test Forum"),
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"),
				SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeForum(driver, new Forum("Third Test Forum"),
				SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_2_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
