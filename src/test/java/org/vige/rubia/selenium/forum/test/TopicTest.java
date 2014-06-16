package org.vige.rubia.selenium.forum.test;

import static org.junit.Assert.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.Operation.SUBMIT;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.TopicType.ADVICE;
import static org.vige.rubia.selenium.forum.action.TopicType.IMPORTANT;
import static org.vige.rubia.selenium.forum.action.TopicType.NORMAL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vige.rubia.selenium.adminpanel.test.CategoryTest;

@RunWith(Arquillian.class)
public class TopicTest {

	@Drone
	private FirefoxDriver driver;

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, "First Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, "Second Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver, "First Test Forum",
				"First Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		Map<File, String> files = new HashMap<File, String>();
		files.put(new File("/first"), "First Test File");
		files.put(new File("/second"), "Second Test File");
		files.put(new File("/third"), "Third Test File");
		message = createTopic(driver, "First Test Forum", "First Test Topic",
				"First Test Body", NORMAL, "First Test Question", new String[] {
						"First Test Answer", "Second Test Answer" }, 4, files,
				SUBMIT);
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver, "First Test Forum", "Second Test Topic",
				"Second Test Body", IMPORTANT, "Second Test Question",
				new String[] { "Third Test Answer", "Fourth Test Answer" }, 8,
				files, SUBMIT);
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver, "Second Test Forum",
				"Second Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver, "Second Test Forum", "Third Test Topic",
				"Third Test Body", ADVICE, "Third Test Question", new String[] {
						"Fifth Test Answer", "Sixth Test Answer" }, 9, files,
				SUBMIT);
		assertTrue(message.equals("Third Test Topic"));
		files.clear();
		files.put(new File("/fourth"), "Fourth Test File");
		files.put(new File("/fifth"), "Fifth Test File");
		files.put(new File("/sixth"), "Sixth Test File");
		message = createTopic(driver, "Second Test Forum", "Fourth Test Topic",
				"Foruth Test Body", IMPORTANT, "Fourth Test Question",
				new String[] { "Seventh Test Answer", "Eight Test Answer" }, 0,
				files, SUBMIT);
		assertTrue(message.equals("Fourth Test Topic"));
	}

	@After
	public void stop() {
		String message = removeTopic(driver, "First Test Forum",
				"First Test Topic", "First Test Body");
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, "First Test Forum", "Second Test Topic",
				"Second Test Body");
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, "Second Test Forum", "Third Test Topic",
				"Third Test Body");
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, "Second Test Forum", "Fourth Test Topic",
				"Fourth Test Body");
		assertTrue(message.equals("OK"));
		message = removeForum(driver, "First Test Forum", "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, "Second Test Forum", SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, "First Test Category",
				CategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, "Second Test Category",
				CategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}

	@Test
	public void test() {

	}
}
