package org.vige.rubia.selenium.search.test;

import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static org.vige.rubia.properties.OperationType.CONFIRM;
import static org.vige.rubia.search.DisplayAs.TOPICS;
import static org.vige.rubia.search.Searching.MSG;
import static org.vige.rubia.search.SortOrder.ASC;
import static org.vige.rubia.search.SortOrder.DESC;
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
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_3_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_4_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_3_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_4_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.SubscriptionForum.registerForum;
import static org.vige.rubia.selenium.forum.action.SubscriptionForum.unregisterForum;
import static org.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllEditForumsRemoveForum;
import static org.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllForumsRemoveForum;
import static org.vige.rubia.selenium.search.action.ViewPageSearch.goTo;
import static org.vige.rubia.selenium.search.action.ViewPageSearch.reset;
import static org.vige.rubia.selenium.search.action.ViewPageTopicSearch.getLastPostOfCurrentForum;
import static org.vige.rubia.selenium.search.action.ViewPageTopicSearch.getPoster;
import static org.vige.rubia.selenium.search.action.ViewPageTopicSearch.getPosterLastPost;
import static org.vige.rubia.selenium.search.action.ViewPageTopicSearch.getTopic;
import static org.vige.rubia.selenium.search.action.ViewPageTopicSearch.getTopics;
import static org.vige.rubia.selenium.search.action.ViewPageTopicSearch.searchTopic;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
import org.vige.rubia.model.Poster;
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
		Forum forum = new Forum("First Test Forum", "First Test Description",
				new Category("First Test Category"));
		message = createForum(driver, forum);
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
						forum,
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
		message = registerForum(driver, forum, EMAIL_LINKED_NOTIFICATION,
				CONFIRM);
		assertTrue(message.equals("First Test Forum"));
		forum = new Forum("Second Test Forum", "Second Test Description",
				new Category("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(
				driver,
				new Topic(
						forum,
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
				new Topic(forum, "Fourth Test Topic",
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
		message = registerForum(driver, forum, EMAIL_EMBEDED_NOTIFICATION,
				CONFIRM);
		assertTrue(message.equals("Second Test Forum"));
		forum = new Forum("Third Test Forum", "Third Test Description",
				new Category("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Third Test Forum"));
		forum = new Forum("Fourth Test Forum", "Fourth Test Description",
				new Category("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_3_MESSAGE));
		forum = new Forum("Fifth Test Forum", "Fifth Test Description",
				new Category("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_4_MESSAGE));
		message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fifth Test Forum"));
	}

	@Test
	public void searchTopics() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setPageNumber(0);
		searchForumCriteria.setPageSize(0);
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<Topic> topics = searchTopic(driver, searchForumCriteria);
		Date today = new Date();
		assertTrue(topics != null);
		assertEquals(topics.size(), 4);
		assertEquals(topics.get(0).getSubject(), "First Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Second Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Third Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 0);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 0);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");

		goTo(driver);
		searchForumCriteria.setCategory("First Test Category");
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(topics.size(), 4);
		assertEquals(topics.get(0).getSubject(), "First Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Second Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Third Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 0);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 0);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");

		goTo(driver);
		searchForumCriteria.setCategory("Second Test Category");
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);

		goTo(driver);
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setForum("First Test Forum");
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(topics.size(), 2);
		assertEquals(topics.get(0).getSubject(), "First Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Second Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");

		goTo(driver);
		searchForumCriteria.setForum("Second Test Forum");
		topics = searchTopic(driver, searchForumCriteria);
		assertEquals(topics.size(), 2);
		assertEquals(topics.get(0).getSubject(), "Third Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");

		ResourceBundle bundle = getBundle("ResourceJSF");
		goTo(driver);
		searchForumCriteria.setForum(null);
		searchForumCriteria.setTimePeriod(bundle.getString("period_7_Days"));
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(topics.size(), 4);
		assertEquals(topics.get(0).getSubject(), "First Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Second Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Third Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 0);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 0);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");

		goTo(driver);
		searchForumCriteria.setTimePeriod(null);
		searchForumCriteria.setSortBy(bundle.getString("Post_subject"));
		searchForumCriteria.setSortOrder(ASC.name());
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(topics.size(), 4);
		assertEquals(topics.get(0).getSubject(), "First Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Second Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Third Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 0);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 0);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");

		goTo(driver);
		searchForumCriteria.setSortOrder(DESC.name());
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(topics.size(), 4);
		assertEquals(topics.get(0).getSubject(), "First Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 0);
		assertEquals(topics.get(0).getViewCount(), 0);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 1);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Second Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Third Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 0);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 0);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");

		goTo(driver);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setSearching(MSG.name());
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsNoFields() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		List<Topic> topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsWithReset() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor(null);
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("First");
		searchForumCriteria.setPageNumber(0);
		searchForumCriteria.setPageSize(0);
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		reset(driver, searchForumCriteria);
		List<Topic> topics = getTopics(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsNoResults() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor(null);
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Firstaaaaa");
		searchForumCriteria.setPageNumber(0);
		searchForumCriteria.setPageSize(0);
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<Topic> topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void verifyPostFromTopicPage() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setPageNumber(0);
		searchForumCriteria.setPageSize(0);
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<Topic> topics = searchTopic(driver, searchForumCriteria);
		Topic topic = getTopic(driver, topics.get(3));
		assertTrue(topic != null);
		assertTrue(topic.getPosts() != null);
		assertEquals(topic.getSubject(), "Fourth Test Topic");
		assertEquals(topic.getPoster().getUserId(), "root");
		assertEquals(topic.getPosts().size(), 1);
		assertEquals(topic.getPosts().get(0).getMessage().getSubject(),
				"Fourth Test Topic");
		assertEquals(topic.getPosts().get(0).getMessage().getText(),
				"Fourth Test Body");
		assertEquals(topic.getPoll().getTitle(), "Fourth Test Question");
	}

	@Test
	public void verifyPostProfileFromTopicPage() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setPageNumber(0);
		searchForumCriteria.setPageSize(0);
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<Topic> topics = searchTopic(driver, searchForumCriteria);
		Topic topic = topics.get(2);
		Poster poster = getPoster(driver, topic);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertTrue(poster.getPostCount() >= 20);
	}

	@Test
	public void verifyPostFromTopicPageLastPost() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setPageNumber(0);
		searchForumCriteria.setPageSize(0);
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<Topic> topics = searchTopic(driver, searchForumCriteria);
		Poster poster = getPosterLastPost(driver, topics.get(0));
		goTo(driver);
		topics = searchTopic(driver, searchForumCriteria);
		Post post = getLastPostOfCurrentForum(driver, topics.get(0));
		assertTrue(post != null);
		assertEquals(post.getMessage().getSubject(), "First Test Topic");
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertTrue(poster.getPostCount() >= 4);
	}

	@After
	public void stop() {
		String message = removeTopic(driver, new Topic(new Forum(
				"First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new Topic(new Forum("First Test Forum"),
				"Second Test Topic", asList(new Post[] { new Post(
						"Second Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"),
				"Third Test Topic", asList(new Post[] { new Post(
						"Third Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic", asList(new Post[] { new Post(
						"Fourth Test Body") })));
		assertTrue(message.equals(OK));
		Forum forum = new Forum("First Test Forum");
		message = unregisterForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, "First Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		forum = new Forum("Second Test Forum");
		message = unregisterForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		forum = new Forum("Third Test Forum");
		message = viewAllForumsRemoveForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_2_MESSAGE));
		forum = new Forum("Fourth Test Forum");
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_3_MESSAGE));
		forum = new Forum("Fifth Test Forum");
		message = viewAllEditForumsRemoveForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_4_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
