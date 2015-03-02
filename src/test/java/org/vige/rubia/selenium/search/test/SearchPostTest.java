package org.vige.rubia.selenium.search.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static org.vige.rubia.properties.OperationType.CONFIRM;
import static org.vige.rubia.search.DisplayAs.POSTS;
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
import static org.vige.rubia.selenium.search.action.ViewPagePostSearch.getPosterFromButton;
import static org.vige.rubia.selenium.search.action.ViewPagePostSearch.getPosterFromLink;
import static org.vige.rubia.selenium.search.action.ViewPagePostSearch.getPosts;
import static org.vige.rubia.selenium.search.action.ViewPagePostSearch.searchPost;
import static org.vige.rubia.selenium.search.action.ViewPageSearch.goTo;
import static org.vige.rubia.selenium.search.action.ViewPageSearch.reset;

import java.util.Date;
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
import org.vige.rubia.model.Poster;
import org.vige.rubia.model.Topic;
import org.vige.rubia.search.SearchCriteria;

@RunWith(Arquillian.class)
public class SearchPostTest {

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
	public void searchPosts() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor("root");
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Body");
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		Date today = new Date();
		assertTrue(posts != null);
		assertEquals(posts.size(), 6);
		assertEquals(posts.get(0).getPoster().getUserId(), "root");
		assertEquals(posts.get(0).getMessage().getSubject(), "First Test Topic");
		assertEquals(posts.get(0).getMessage().getText(), "First Test Body");
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(posts.get(0).getAttachments().size(), 3);
		assertEquals(posts.get(1).getPoster().getUserId(), "root");
		assertEquals(posts.get(1).getMessage().getSubject(),
				"Second Test Topic");
		assertEquals(posts.get(1).getMessage().getText(), "Second Test Body");
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(posts.get(1).getAttachments().size(), 3);
		assertEquals(posts.get(2).getPoster().getUserId(), "root");
		assertEquals(posts.get(2).getMessage().getSubject(), "Third Test Topic");
		assertEquals(posts.get(2).getMessage().getText(), "Third Test Body");
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(posts.get(2).getAttachments().size(), 3);
		assertEquals(posts.get(3).getPoster().getUserId(), "root");
		assertEquals(posts.get(3).getMessage().getSubject(),
				"Fourth Test Topic");
		assertEquals(posts.get(3).getMessage().getText(), "Fourth Test Body");
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(posts.get(3).getAttachments().size(), 3);
		assertEquals(posts.get(4).getPoster().getUserId(), "root");
		assertEquals(posts.get(4).getMessage().getSubject(), "Fifth Test Topic");
		assertEquals(posts.get(4).getMessage().getText(), "Fifth Test Body");
		assertTrue(posts.get(4).getCreateDate().compareTo(today) < 0);
		assertEquals(posts.get(4).getAttachments().size(), 3);
		assertEquals(posts.get(5).getPoster().getUserId(), "root");
		assertEquals(posts.get(5).getMessage().getSubject(), "Sixth Test Topic");
		assertEquals(posts.get(5).getMessage().getText(), "Sixth Test Body");
		assertTrue(posts.get(5).getCreateDate().compareTo(today) < 0);
		assertEquals(posts.get(5).getAttachments().size(), 3);
	}

	@Test
	public void searchPostsNoFields() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts == null);
	}

	@Test
	public void searchPostsWithReset() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor(null);
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("First");
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		reset(driver, searchTopicCriteria);
		List<Post> posts = getPosts(driver, searchTopicCriteria);
		assertTrue(posts == null);
	}

	@Test
	public void searchPostsNoResults() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor(null);
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Firstaaaaaa");
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts == null);
	}

	@Test
	public void verifyPostProfileFromTopicPage() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor("root");
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Body");
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		Post post = posts.get(2);
		Poster poster = getPosterFromLink(driver, post);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertTrue(poster.getPostCount() >= 36);
	}

	@Test
	public void verifyPostProfileFromTopicPageButton() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor("root");
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Body");
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		Post post = posts.get(3);
		Poster poster = getPosterFromButton(driver, post);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertTrue(poster.getPostCount() >= 6);
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
