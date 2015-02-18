package org.vige.rubia.selenium.links.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.selenium.Constants.OK;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.links.action.ViewFooterLink.getPage;
import static org.vige.rubia.ui.Constants.RE;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;
import org.vige.rubia.selenium.links.action.ViewAtomLink;
import org.vige.rubia.selenium.links.action.ViewRSSLink;

@RunWith(Arquillian.class)
public class ExternalLinksTest {

	@Drone
	private FirefoxDriver driver;

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, new Category(
				"First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		Forum forum = new Forum("First Test Forum", "First Test Description",
				new Category("First Test Category"));
		message = createForum(driver, forum);
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
								new Post("Second Test Body",
										asList(new Attachment("Fourth",
												"Fourth Test File"))) }),
						NORMAL,
						new Poll(
								"First Test Question",
								asList(new PollOption[] {
										new PollOption("First Test Answer"),
										new PollOption("Second Test Answer") }),
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
		assertEquals(result.getCategory().getTitle(), "First Test Category");
		assertEquals(result.getName(), forum.getName());
		assertEquals(result.getTopics().size(), 1);
		assertEquals(result.getTopics().get(0).getSubject(), "First Test Topic");
		assertEquals(result.getTopics().get(0).getPosts().size(), 2);
		assertEquals(result.getTopics().get(0).getPosts().get(0).getPoster()
				.getUserId(), "root");
		assertEquals(result.getTopics().get(0).getPosts().get(1).getPoster()
				.getUserId(), "root");
		assertNotNull(result.getTopics().get(0).getPosts().get(0)
				.getCreateDate());
		assertNotNull(result.getTopics().get(0).getPosts().get(1)
				.getCreateDate());
		assertEquals(result.getTopics().get(0).getPosts().get(0).getMessage()
				.getSubject(), RE + "First Test Topic");
		assertEquals(result.getTopics().get(0).getPosts().get(1).getMessage()
				.getSubject(), "First Test Topic");
		assertEquals(result.getTopics().get(0).getPosts().get(0).getMessage()
				.getText(), "Second Test Body");
		assertEquals(result.getTopics().get(0).getPosts().get(1).getMessage()
				.getText(), "First Test Body");
	}

	@Test
	public void verifyAtomForum() {
		Forum forum = new Forum("First Test Forum");
		Forum result = ViewAtomLink.getPage(driver, forum);
		assertNotNull(result);
		assertEquals(result.getCategory().getTitle(), "First Test Category");
		assertEquals(result.getName(), forum.getName());
		assertEquals(result.getTopics().size(), 1);
		assertEquals(result.getTopics().get(0).getSubject(), "First Test Topic");
		assertEquals(result.getTopics().get(0).getPoll().getTitle(),
				"First Test Question");
		assertEquals(result.getTopics().get(0).getPoll().getOptions().size(), 2);
		assertEquals(result.getTopics().get(0).getPoll().getOptions().get(0)
				.getQuestion(), "Second Test Answer");
		assertEquals(result.getTopics().get(0).getPoll().getOptions().get(1)
				.getQuestion(), "First Test Answer");
		assertEquals(result.getTopics().get(0).getPosts().size(), 2);
		assertEquals(result.getTopics().get(0).getPosts().get(0).getPoster()
				.getUserId(), "root");
		assertEquals(result.getTopics().get(0).getPosts().get(1).getPoster()
				.getUserId(), "root");
		assertNotNull(result.getTopics().get(0).getPosts().get(0)
				.getCreateDate());
		assertNotNull(result.getTopics().get(0).getPosts().get(1)
				.getCreateDate());
		assertEquals(result.getTopics().get(0).getPosts().get(0).getMessage()
				.getSubject(), RE + "First Test Topic");
		assertEquals(result.getTopics().get(0).getPosts().get(1).getMessage()
				.getSubject(), "First Test Topic");
		assertEquals(result.getTopics().get(0).getPosts().get(0).getMessage()
				.getText(), "Second Test Body");
		assertEquals(result.getTopics().get(0).getPosts().get(1).getMessage()
				.getText(), "First Test Body");
		assertNotNull(result.getTopics().get(0).getPosts().get(0)
				.getAttachments());
		assertEquals(result.getTopics().get(0).getPosts().get(0)
				.getAttachments().size(), 1);
		assertEquals(((Attachment) result.getTopics().get(0).getPosts().get(0)
				.getAttachments().toArray()[0]).getComment(),
				"Fourth Test File");
		assertNotNull(result.getTopics().get(0).getPosts().get(1)
				.getAttachments());
		assertEquals(result.getTopics().get(0).getPosts().get(1)
				.getAttachments().size(), 3);
		assertEquals(((Attachment) result.getTopics().get(0).getPosts().get(1)
				.getAttachments().toArray()[0]).getComment(), "First Test File");
		assertEquals(((Attachment) result.getTopics().get(0).getPosts().get(1)
				.getAttachments().toArray()[1]).getComment(),
				"Second Test File");
		assertEquals(((Attachment) result.getTopics().get(0).getPosts().get(1)
				.getAttachments().toArray()[2]).getComment(), "Third Test File");
	}

	@Test
	public void verifyRSSTopic() {
		Topic topic = new Topic(new Forum("First Test Forum"),
				"First Test Topic");
		Topic result = ViewRSSLink.getPage(driver, topic);
		assertNotNull(result);
		assertEquals(result.getForum().getCategory().getTitle(),
				"First Test Category");
		assertEquals(result.getForum().getName(), topic.getForum().getName());
		assertEquals(result.getPosts().size(), 2);
		assertEquals(result.getPosts().get(0).getPoster().getUserId(), "root");
		assertEquals(result.getPosts().get(1).getPoster().getUserId(), "root");
		assertEquals(result.getPosts().get(0).getMessage().getSubject(),
				"First Test Topic");
		assertEquals(result.getPosts().get(1).getMessage().getSubject(), RE
				+ "First Test Topic");
		assertEquals(result.getPosts().get(0).getMessage().getText(),
				"First Test Body");
		assertEquals(result.getPosts().get(1).getMessage().getText(),
				"Second Test Body");
		assertNotNull(result.getPosts().get(0).getCreateDate());
		assertNotNull(result.getPosts().get(1).getCreateDate());
		assertEquals(result.getSubject(), "First Test Topic");
	}

	@Test
	public void verifyAtomTopic() {
		Topic topic = new Topic(new Forum("First Test Forum"),
				"First Test Topic");
		Topic result = ViewAtomLink.getPage(driver, topic);
		assertNotNull(result);
		assertEquals(result.getForum().getCategory().getTitle(),
				"First Test Category");
		assertEquals(result.getForum().getName(), topic.getForum().getName());
		assertEquals(result.getPosts().size(), 2);
		assertEquals(result.getPosts().get(0).getPoster().getUserId(), "root");
		assertEquals(result.getPosts().get(1).getPoster().getUserId(), "root");
		assertEquals(result.getPosts().get(0).getMessage().getSubject(),
				"First Test Topic");
		assertEquals(result.getPosts().get(1).getMessage().getSubject(), RE
				+ "First Test Topic");
		assertEquals(result.getPosts().get(0).getMessage().getText(),
				"First Test Body");
		assertEquals(result.getPosts().get(1).getMessage().getText(),
				"Second Test Body");
		assertNotNull(result.getPosts().get(0).getCreateDate());
		assertNotNull(result.getPosts().get(1).getCreateDate());
		assertEquals(result.getSubject(), "First Test Topic");
		assertEquals(result.getPoll().getTitle(), "First Test Question");
		assertEquals(result.getPoll().getOptions().size(), 2);
		assertEquals(result.getPoll().getOptions().get(0).getQuestion(),
				"Second Test Answer");
		assertEquals(result.getPoll().getOptions().get(1).getQuestion(),
				"First Test Answer");
		assertNotNull(result.getPosts().get(0).getAttachments());
		assertEquals(result.getPosts().get(0).getAttachments().size(), 3);
		assertEquals(((Attachment) result.getPosts().get(0).getAttachments()
				.toArray()[0]).getComment(), "First Test File");
		assertEquals(((Attachment) result.getPosts().get(0).getAttachments()
				.toArray()[1]).getComment(), "Second Test File");
		assertEquals(((Attachment) result.getPosts().get(0).getAttachments()
				.toArray()[2]).getComment(), "Third Test File");
		assertNotNull(result.getPosts().get(1).getAttachments());
		assertEquals(result.getPosts().get(1).getAttachments().size(), 1);
		assertEquals(((Attachment) result.getPosts().get(1).getAttachments()
				.toArray()[0]).getComment(), "Fourth Test File");
	}

	@After
	public void stop() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = removeTopic(driver, new Topic(new Forum(
				"First Test Forum"), "First Test Topic", asList(new Post[] {
				new Post("First Test Body"), new Post("Second Test Body") })));
		assertTrue(message.equals(OK));
		Forum forum = new Forum("First Test Forum");
		message = removeForum(driver, forum, forum.getName());
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
	}
}
