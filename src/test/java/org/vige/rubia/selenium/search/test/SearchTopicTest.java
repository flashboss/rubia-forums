package org.vige.rubia.selenium.search.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static org.vige.rubia.properties.OperationType.CONFIRM;
import static org.vige.rubia.selenium.Constants.OK;
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
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.SubscriptionTopic.registerTopic;
import static org.vige.rubia.selenium.forum.action.SubscriptionTopic.unregisterTopic;
import static org.vige.rubia.selenium.myforums.action.ViewAllTopicsRemoveTopic.viewAllTopicsRemoveTopic;
import static org.vige.rubia.selenium.search.action.ViewPageSearch.getTopics;
import static org.vige.rubia.selenium.search.action.ViewPageSearch.goTo;

import java.util.List;

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
import org.vige.rubia.search.SearchCriteria;

@RunWith(Arquillian.class)
public class SearchTopicTest {

	@Drone
	private WebDriver driver;

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
		message = registerTopic(driver, new Topic("First Test Topic"),
				EMAIL_LINKED_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Second Test Topic"),
				EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Third Test Topic"),
				EMAIL_NO_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Fourth Test Topic"),
				EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Fifth Test Topic"),
				EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
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
	public void searchTopics() {
		goTo(driver);
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setAuthor(null);
		searchCriteria.setCategory(null);
		searchCriteria.setDisplayAs(null);
		searchCriteria.setForum(null);
		searchCriteria.setKeywords("First");
		searchCriteria.setPageNumber(0);
		searchCriteria.setPageSize(0);
		searchCriteria.setSearching(null);
		searchCriteria.setSortBy(null);
		searchCriteria.setSortOrder(null);
		searchCriteria.setTimePeriod(null);
		List<Topic> topics = getTopics(driver, searchCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsNoFields() {
		goTo(driver);
		SearchCriteria searchCriteria = new SearchCriteria();
		List<Topic> topics = getTopics(driver, searchCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsNoResults() {
		goTo(driver);
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setAuthor(null);
		searchCriteria.setCategory(null);
		searchCriteria.setDisplayAs(null);
		searchCriteria.setForum(null);
		searchCriteria.setKeywords("First");
		searchCriteria.setPageNumber(0);
		searchCriteria.setPageSize(0);
		searchCriteria.setSearching(null);
		searchCriteria.setSortBy(null);
		searchCriteria.setSortOrder(null);
		searchCriteria.setTimePeriod(null);
		List<Topic> topics = getTopics(driver, searchCriteria);
		assertTrue(topics == null);
	}

	@After
	public void stop() {
		Topic topic = new Topic(new Forum("First Test Forum"),
				"First Test Topic", asList(new Post[] { new Post(
						"First Test Body") }));
		String message = unregisterTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("First Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body") }));
		message = unregisterTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body") }));
		message = unregisterTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body") }));
		message = viewAllTopicsRemoveTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Third Test Forum"), "Fifth Test Topic",
				asList(new Post[] { new Post("Fifth Test Body") }));
		message = viewAllTopicsRemoveTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Third Test Forum"), "Sixth Test Topic",
				asList(new Post[] { new Post("Sixth Test Body") }));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
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
