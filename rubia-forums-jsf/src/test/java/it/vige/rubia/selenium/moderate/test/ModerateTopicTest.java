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
import static it.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreatePost.LOCKED;
import static it.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static it.vige.rubia.selenium.moderate.action.LockTopic.lockTopic;
import static it.vige.rubia.selenium.moderate.action.MoveTopic.goToSplitPanel;
import static it.vige.rubia.selenium.moderate.action.MoveTopic.moveTopic;
import static it.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicFromSelectedDown;
import static it.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicFromSelectedUp;
import static it.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicSelectedDown;
import static it.vige.rubia.selenium.moderate.action.MoveTopic.moveTopicSelectedUp;
import static it.vige.rubia.selenium.moderate.action.RemoveTopic.removeTopic;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class ModerateTopicTest {

	@Drone
	private static WebDriver driver;

	public final static String ERR_NO_POST_SELECTED = getBundle("ResourceJSF").getString("ERR_NO_POST_SELECTED");
	public final static String ERR_DEST_FORUM = getBundle("ResourceJSF").getString("ERR_DEST_FORUM");
	public final static String ERR_NO_SUBJECT_GIVEN = getBundle("ResourceJSF").getString("ERR_NO_SUBJECT_GIVEN");
	public final static String SUCC_TOPIC_SPLITTED = getBundle("ResourceJSF").getString("SUCC_TOPIC_SPLITTED");
	public final static String ERR_SPLIT_ALL = getBundle("ResourceJSF").getString("ERR_SPLIT_ALL");
	public final static String SUCC_TOPIC_REMOVED = getBundle("ResourceJSF").getString("SUCC_TOPIC_REMOVED");
	public final static String SUCC_TOPIC_LOCKED = getBundle("ResourceJSF").getString("SUCC_TOPIC_LOCKED");
	public final static String SUCC_TOPIC_UNLOCKED = getBundle("ResourceJSF").getString("SUCC_TOPIC_UNLOCKED");
	public final static String ERR_NO_DEST_FORUM = getBundle("ResourceJSF").getString("ERR_NO_DEST_FORUM");
	public final static String SUCC_TOPIC_MOVED = getBundle("ResourceJSF").getString("SUCC_TOPIC_MOVED");
	public final static String SUCC_VIEW_RESULTS = getBundle("ResourceJSF").getString("View_results");

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
				new TopicBean(
						new ForumBean("First Test Forum"), "First Test Topic", asList(
								new PostBean[] {
										new PostBean("First Test Body",
												asList(new AttachmentBean("first", "First Test File"),
														new AttachmentBean("second", "Second Test File"),
														new AttachmentBean("third", "Third Test File"))),
										new PostBean("Ninteen Test Body"), new PostBean("Twenty Test Body"),
										new PostBean("Twentyone Test Body"), new PostBean("Twentytwo Test Body"),
										new PostBean("Twentythree Test Body"), new PostBean("Twentyfour Test Body") }),
						NORMAL,
						new PollBean("First Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("First Test Answer"), new PollOptionBean("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver,
				new TopicBean(
						new ForumBean("First Test Forum"), "Second Test Topic", asList(
								new PostBean[] {
										new PostBean("Second Test Body",
												asList(new AttachmentBean("first", "First Test File"),
														new AttachmentBean("second", "Second Test File"),
														new AttachmentBean("third", "Third Test File"))),
										new PostBean("Twentyfive Test Body"), new PostBean("Twentysix Test Body"),
										new PostBean("Twentyseven Test Body"), new PostBean("Twentyeight Test Body"),
										new PostBean("Twentynine Test Body"), new PostBean("Thirty Test Body") }),
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
						asList(new PostBean[] {
								new PostBean("Third Test Body",
										asList(new AttachmentBean("first", "First Test File"),
												new AttachmentBean("second", "Second Test File"),
												new AttachmentBean("third", "Third Test File"))),
								new PostBean("Thirtyone Test Body"), new PostBean("Thirtytwo Test Body"),
								new PostBean("Thirtythree Test Body"), new PostBean("Thirtyfour Test Body"),
								new PostBean("Thirtyfive Test Body"), new PostBean("Thirtysix Test Body") }),
						ADVICE,
						new PollBean("Third Test Question",
								asList(new PollOptionBean[] {
										new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"),
										new PollOptionBean("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver,
				new TopicBean(
						new ForumBean("Second Test Forum"), "Fourth Test Topic", asList(
								new PostBean[] {
										new PostBean("Fourth Test Body",
												asList(new AttachmentBean("fourth", "Fourth Test File"),
														new AttachmentBean("fifth",
																"Fifth Test with Truncation over 25 characters File"),
														new AttachmentBean("sixth", "Sixth Test File"))),
										new PostBean("Thirtyseven Test Body"), new PostBean("Thirtyeight Test Body"),
										new PostBean("Thirtynine Test Body"), new PostBean("Fourty Test Body"),
										new PostBean("Fourtyone Test Body"), new PostBean("Fourtytwo Test Body") }),
						IMPORTANT,
						new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = createForum(driver,
				new ForumBean("Third Test Forum", "Third Test Description", new CategoryBean("Second Test Category")));
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = createTopic(
				driver, new TopicBean(new ForumBean("Third Test Forum"),
						"Fifth Test with Truncation over 25 characters Topic", asList(
								new PostBean[] {
										new PostBean("Fifth Test with Truncation over 25 characters Body",
												asList(new AttachmentBean("seventh", "Seventh Test File"),
														new AttachmentBean("eight", "Eight Test File"),
														new AttachmentBean("ninth", "Ninth Test File"))),
										new PostBean("Fourtythree Test Body"), new PostBean("Fourtyfour Test Body"),
										new PostBean("Fourtyfive Test Body"), new PostBean("Fourtysix Test Body"),
										new PostBean("Fourtyseven Test Body"), new PostBean("Fourtyeight Test Body") }),
						IMPORTANT,
						new PollBean("Third Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								8)));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
						asList(new PostBean[] {
								new PostBean("Sixth Test Body",
										asList(new AttachmentBean("ten", "Ten Test File"),
												new AttachmentBean("eleven", "Eleven Test File"),
												new AttachmentBean("twelve", "Twelve Test File"))),
								new PostBean("Fourtynine Test Body"), new PostBean("Fifty Test Body"),
								new PostBean("Fiftyone Test Body"), new PostBean("Fiftytwo Test Body"),
								new PostBean("Fiftythree Test Body"), new PostBean("Fiftyfour Test Body") }),
						IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Ninth Test Answer"), new PollOptionBean("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void move() {
		ForumBean srcForum = new ForumBean("First Test Forum");
		TopicBean topic = new TopicBean(srcForum, "First Test Topic");
		ForumBean destForum = new ForumBean("Second Test Forum");
		goTo(driver, topic);
		String message = moveTopic(driver, null, CONFIRM);
		assertEquals(ERR_NO_DEST_FORUM, message);
		goTo(driver, topic);
		message = moveTopic(driver, destForum, CANCEL);
		assertTrue(message.contains(SUCC_VIEW_RESULTS));
		message = moveTopic(driver, destForum, CONFIRM);
		assertEquals(SUCC_TOPIC_MOVED, message);
		topic.setForum(destForum);
		goTo(driver, topic);
		message = moveTopic(driver, srcForum, CONFIRM);
		assertEquals(SUCC_TOPIC_MOVED, message);
	}

	@Test
	public void lock() {
		ForumBean forum = new ForumBean("Second Test Forum");
		TopicBean topic = new TopicBean(forum, "Third Test Topic");
		String message = lockTopic(driver, topic);
		assertEquals(SUCC_TOPIC_LOCKED, message);
		message = createPost(driver, new PostBean(topic, "new Test Post"));
		assertEquals(LOCKED, message);
		message = lockTopic(driver, topic);
		assertEquals(SUCC_TOPIC_UNLOCKED, message);
	}

	@Test
	public void moveSelectedUp() {
		TopicBean topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicSelectedUp(driver, topic, null);
		assertEquals(ERR_NO_POST_SELECTED, message);
		topic.setPosts(
				asList(new PostBean[] {
						new PostBean("Fourth Test Body",
								asList(new AttachmentBean("fourth", "Fourth Test File"),
										new AttachmentBean("fifth",
												"Fifth Test with Truncation over 25 characters File"),
										new AttachmentBean("sixth", "Sixth Test File"))),
						new PostBean("Thirtyeight Test Body"), new PostBean("Fourtyone Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(ERR_DEST_FORUM, message);
		ForumBean forum = new ForumBean("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(ERR_NO_SUBJECT_GIVEN, message);
		forum.setTopics(asList(new TopicBean[] { new TopicBean("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(SUCC_TOPIC_SPLITTED, message);
		forum.setName("Second Test Forum");
		forum.setTopics(asList(new TopicBean[] { new TopicBean("Fourth Test Topic") }));
		topic.setForum(new ForumBean("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(ERR_SPLIT_ALL, message);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setForum(new ForumBean("Second Test Forum"));
		topic.setSubject("Fourth Test Topic");
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(driver,
				new TopicBean(
						new ForumBean("Second Test Forum"), "Fourth Test Topic", asList(
								new PostBean[] {
										new PostBean("Fourth Test Body",
												asList(new AttachmentBean("fourth", "Fourth Test File"),
														new AttachmentBean("fifth",
																"Fifth Test with Truncation over 25 characters File"),
														new AttachmentBean("sixth", "Sixth Test File"))),
										new PostBean("Thirtyseven Test Body"), new PostBean("Thirtyeight Test Body"),
										new PostBean("Thirtynine Test Body"), new PostBean("Fourty Test Body"),
										new PostBean("Fourtyone Test Body"), new PostBean("Fourtytwo Test Body") }),
						IMPORTANT,
						new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
	}

	@Test
	public void moveFromSelectedUp() {
		TopicBean topic = new TopicBean(new ForumBean("Third Test Forum"),
				"Fifth Test with Truncation over 25 characters Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicFromSelectedUp(driver, topic, null);
		assertEquals(ERR_NO_POST_SELECTED, message);
		topic.setPosts(
				asList(new PostBean[] { new PostBean("Fourtyfive Test Body"), new PostBean("Fourtysix Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(ERR_DEST_FORUM, message);
		ForumBean forum = new ForumBean("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(ERR_NO_SUBJECT_GIVEN, message);
		forum.setTopics(asList(new TopicBean[] { new TopicBean("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(SUCC_TOPIC_SPLITTED, message);
		forum.setName("Third Test Forum");
		forum.setTopics(
				asList(new TopicBean[] { new TopicBean("Fifth Test with Truncation over 25 characters Topic") }));
		topic.setForum(new ForumBean("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(ERR_SPLIT_ALL, message);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setForum(new ForumBean("Third Test Forum"));
		topic.setSubject("Fifth Test with Truncation over 25 characters Topic");
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(
				driver, new TopicBean(new ForumBean("Third Test Forum"),
						"Fifth Test with Truncation over 25 characters Topic", asList(
								new PostBean[] {
										new PostBean("Fifth Test with Truncation over 25 characters Body",
												asList(new AttachmentBean("seventh", "Seventh Test File"),
														new AttachmentBean("eight", "Eight Test File"),
														new AttachmentBean("ninth", "Ninth Test File"))),
										new PostBean("Fourtythree Test Body"), new PostBean("Fourtyfour Test Body"),
										new PostBean("Fourtyfive Test Body"), new PostBean("Fourtysix Test Body"),
										new PostBean("Fourtyseven Test Body"), new PostBean("Fourtyeight Test Body") }),
						IMPORTANT,
						new PollBean("Third Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								8)));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
	}

	@Test
	public void moveSelectedDown() {
		TopicBean topic = new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(ERR_NO_POST_SELECTED, message);
		topic.setPosts(asList(new PostBean[] { new PostBean("Fifty Test Body"), new PostBean("Fiftytwo Test Body"),
				new PostBean("Fiftythree Test Body"), new PostBean("Fiftyfour Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(ERR_DEST_FORUM, message);
		ForumBean forum = new ForumBean("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(ERR_NO_SUBJECT_GIVEN, message);
		forum.setTopics(asList(new TopicBean[] { new TopicBean("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(SUCC_TOPIC_SPLITTED, message);
		forum.setName("Third Test Forum");
		forum.setTopics(asList(new TopicBean[] { new TopicBean("Sixth Test Topic") }));
		topic.setForum(new ForumBean("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(ERR_SPLIT_ALL, message);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setSubject("Sixth Test Topic");
		topic.setForum(new ForumBean("Third Test Forum"));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
						asList(new PostBean[] {
								new PostBean("Sixth Test Body",
										asList(new AttachmentBean("ten", "Ten Test File"),
												new AttachmentBean("eleven", "Eleven Test File"),
												new AttachmentBean("twelve", "Twelve Test File"))),
								new PostBean("Fourtynine Test Body"), new PostBean("Fifty Test Body"),
								new PostBean("Fiftyone Test Body"), new PostBean("Fiftytwo Test Body"),
								new PostBean("Fiftythree Test Body"), new PostBean("Fiftyfour Test Body") }),
						IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Ninth Test Answer"), new PollOptionBean("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void moveFromSelectedDown() {
		TopicBean topic = new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic");
		goToSplitPanel(driver, topic);
		String message = moveTopicFromSelectedDown(driver, topic, null);
		assertEquals(ERR_NO_POST_SELECTED, message);
		topic.setPosts(asList(new PostBean[] { new PostBean("Fifty Test Body"), new PostBean("Fiftyone Test Body"),
				new PostBean("Fiftytwo Test Body") }));
		message = moveTopicSelectedDown(driver, topic, null);
		assertEquals(ERR_DEST_FORUM, message);
		ForumBean forum = new ForumBean("First Test Forum");
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(ERR_NO_SUBJECT_GIVEN, message);
		forum.setTopics(asList(new TopicBean[] { new TopicBean("New Test Topic") }));
		message = moveTopicSelectedDown(driver, null, forum);
		assertEquals(SUCC_TOPIC_SPLITTED, message);
		forum.setName("Third Test Forum");
		forum.setTopics(asList(new TopicBean[] { new TopicBean("Sixth Test Topic") }));
		topic.setForum(new ForumBean("First Test Forum"));
		topic.setSubject("New Test Topic");
		goToSplitPanel(driver, topic);
		message = moveTopicSelectedDown(driver, topic, forum);
		assertEquals(ERR_SPLIT_ALL, message);
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic.setForum(new ForumBean("Third Test Forum"));
		topic.setSubject("Sixth Test Topic");
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
						asList(new PostBean[] {
								new PostBean("Sixth Test Body",
										asList(new AttachmentBean("ten", "Ten Test File"),
												new AttachmentBean("eleven", "Eleven Test File"),
												new AttachmentBean("twelve", "Twelve Test File"))),
								new PostBean("Fourtynine Test Body"), new PostBean("Fifty Test Body"),
								new PostBean("Fiftyone Test Body"), new PostBean("Fiftytwo Test Body"),
								new PostBean("Fiftythree Test Body"), new PostBean("Fiftyfour Test Body") }),
						IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Ninth Test Answer"), new PollOptionBean("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@AfterClass
	public static void stop() {
		TopicBean topic = new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
				asList(new PostBean[] { new PostBean("First Test Body") }));
		String message = removeTopic(driver, CANCEL, topic);
		assertTrue(message.equals(""));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic",
				asList(new PostBean[] { new PostBean("Second Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic",
				asList(new PostBean[] { new PostBean("Third Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new TopicBean(new ForumBean("Third Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
				asList(new PostBean[] { new PostBean("Fifth Test with Truncation over 25 characters Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		topic = new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
				asList(new PostBean[] { new PostBean("Sixth Test Body") }));
		message = removeTopic(driver, CONFIRM, topic);
		assertTrue(message.equals(SUCC_TOPIC_REMOVED));
		message = removeForum(driver, new ForumBean("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new ForumBean("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeForum(driver, new ForumBean("Third Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_2_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
