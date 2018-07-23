/*
 * Vige, Home of Professional Open Source
 * Copyright 2010, Vige, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.vige.rubia.selenium.forum.test;

import static it.vige.rubia.model.TopicType.ADVICE;
import static it.vige.rubia.model.TopicType.IMPORTANT;
import static it.vige.rubia.model.TopicType.NORMAL;
import static it.vige.rubia.selenium.Constants.HOME_URL;
import static it.vige.rubia.selenium.Constants.OK;
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
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopicsOfForums;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;

@RunWith(Arquillian.class)
@RunAsClient
public class OperationTopicTest {

	@Drone
	private static WebDriver driver;

	@BeforeClass
	public static void setUp() {
		driver.get(HOME_URL);
		String message = createCategory(driver, new Category("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, new Category("Second Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver,
				new Forum("First Test Forum", "First Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver, new Topic(new Forum("First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				NORMAL,
				new Poll("First Test Question", asList(
						new PollOption[] { new PollOption("First Test Answer"), new PollOption("Second Test Answer") }),
						4)));
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver, new Topic(new Forum("First Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				IMPORTANT,
				new Poll("Second Test Question", asList(
						new PollOption[] { new PollOption("Third Test Answer"), new PollOption("Fourth Test Answer") }),
						8)));
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver,
				new Forum("Second Test Forum", "Second Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver, new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				ADVICE,
				new Poll("Third Test Question",
						asList(new PollOption[] {
								new PollOption("Fifth Test with Truncation over 25 characters Answer"),
								new PollOption("Sixth Test Answer") }),
						9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver,
				new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
						asList(new Post[] { new Post("Fourth Test Body",
								asList(new Attachment("fourth", "Fourth Test File"),
										new Attachment("fifth", "Fifth Test with Truncation over 25 characters File"),
										new Attachment("sixth", "Sixth Test File"))) }),
						IMPORTANT, new Poll("Fourth Test Question", asList(new PollOption[] {
								new PollOption("Seventh Test Answer"), new PollOption("Eight Test Answer") }), 0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = createTopic(driver,
				new Topic(new Forum("Second Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
						asList(new Post[] { new Post("Fifth Test with Truncation over 25 characters Body", null) }),
						IMPORTANT, null));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
	}

	@Test
	public void verifyCreatedTopics() {
		List<Topic> topics = getTopicsOfForums(driver, new Forum("First Test Forum"), new Forum("Second Test Forum"));
		assertEquals(5, topics.size());
		Date today = new Date();

		Topic secondTestTopic = topics.get(0);
		assertEquals("First Test Category", secondTestTopic.getForum().getCategory().getTitle());
		assertEquals("First Test Forum", secondTestTopic.getForum().getName());
		assertTrue(secondTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals("Second Test Topic", secondTestTopic.getSubject());
		assertEquals(IMPORTANT, secondTestTopic.getType());
		assertEquals(0, secondTestTopic.getViewCount());
		assertEquals(0, secondTestTopic.getReplies());
		assertEquals("root", secondTestTopic.getPoster().getUserId());
		assertEquals(1, secondTestTopic.getPosts().size());

		Topic firstTestTopic = topics.get(1);
		assertEquals("First Test Category", firstTestTopic.getForum().getCategory().getTitle());
		assertEquals("First Test Forum", firstTestTopic.getForum().getName());
		assertTrue(firstTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals("First Test Topic", firstTestTopic.getSubject());
		assertEquals(NORMAL, firstTestTopic.getType());
		assertEquals(0, firstTestTopic.getViewCount());
		assertEquals(0, firstTestTopic.getReplies());
		assertEquals("root", firstTestTopic.getPoster().getUserId());
		assertEquals(1, firstTestTopic.getPosts().size());

		Topic thirdTestTopic = topics.get(2);
		assertEquals("First Test Category", thirdTestTopic.getForum().getCategory().getTitle());
		assertEquals("Second Test Forum", thirdTestTopic.getForum().getName());
		assertTrue(thirdTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals("Third Test Topic", thirdTestTopic.getSubject());
		assertEquals(ADVICE, thirdTestTopic.getType());
		assertEquals(0, thirdTestTopic.getViewCount());
		assertEquals(0, thirdTestTopic.getReplies());
		assertEquals("root", thirdTestTopic.getPoster().getUserId());
		assertEquals(1, thirdTestTopic.getPosts().size());

		Topic fifthTestTopic = topics.get(3);
		assertEquals("First Test Category", fifthTestTopic.getForum().getCategory().getTitle());
		assertEquals("Second Test Forum", fifthTestTopic.getForum().getName());
		assertTrue(fifthTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals("Fifth Test with Truncation over 25 characters Topic", fifthTestTopic.getSubject());
		assertEquals(IMPORTANT, fifthTestTopic.getType());
		assertEquals(0, fifthTestTopic.getViewCount());
		assertEquals(0, fifthTestTopic.getReplies());
		assertEquals("root", fifthTestTopic.getPoster().getUserId());
		assertEquals(1, fifthTestTopic.getPosts().size());

		Topic fourthTestTopic = topics.get(4);
		assertEquals("First Test Category", fourthTestTopic.getForum().getCategory().getTitle());
		assertEquals("Second Test Forum", fourthTestTopic.getForum().getName());
		assertTrue(fourthTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals("Fourth Test Topic", fourthTestTopic.getSubject());
		assertEquals(IMPORTANT, fourthTestTopic.getType());
		assertEquals(0, fourthTestTopic.getViewCount());
		assertEquals(0, fourthTestTopic.getReplies());
		assertEquals("root", fourthTestTopic.getPoster().getUserId());
		assertEquals(1, fourthTestTopic.getPosts().size());
	}

	@AfterClass
	public static void stop() {
		String message = removeTopic(driver, new Topic(new Forum("First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver,
				new Topic(new Forum("Second Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
						asList(new Post[] { new Post("Fifth Test with Truncation over 25 characters Body") })));
		assertTrue(message.equals(OK));
		message = removeForum(driver, new Forum("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body") })));
		assertTrue(message.equals(OK));
		message = removeForum(driver, new Forum("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
