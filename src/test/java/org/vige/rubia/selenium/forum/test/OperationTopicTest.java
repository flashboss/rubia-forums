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
package org.vige.rubia.selenium.forum.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
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
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.getTopicsOfForums;

import java.util.Date;
import java.util.List;

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

@RunWith(Arquillian.class)
public class OperationTopicTest {

	@Drone
	private FirefoxDriver driver;

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
	}

	@Test
	public void verifyCreatedTopics() {
		List<Topic> topics = getTopicsOfForums(driver, new Forum(
				"First Test Forum"), new Forum("Second Test Forum"));
		assertEquals(topics.size(), 4);
		Date today = new Date();

		Topic secondTestTopic = topics.get(0);
		assertEquals(secondTestTopic.getForum().getCategory().getTitle(),
				"First Test Category");
		assertEquals(secondTestTopic.getForum().getName(), "First Test Forum");
		assertTrue(secondTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals(secondTestTopic.getSubject(), "Second Test Topic");
		assertEquals(secondTestTopic.getType(), IMPORTANT);
		assertEquals(secondTestTopic.getViewCount(), 0);
		assertEquals(secondTestTopic.getReplies(), 0);
		assertEquals(secondTestTopic.getPoster().getUserId(), "root");
		assertEquals(secondTestTopic.getPosts().size(), 1);

		Topic firstTestTopic = topics.get(1);
		assertEquals(firstTestTopic.getForum().getCategory().getTitle(),
				"First Test Category");
		assertEquals(firstTestTopic.getForum().getName(), "First Test Forum");
		assertTrue(firstTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals(firstTestTopic.getSubject(), "First Test Topic");
		assertEquals(firstTestTopic.getType(), NORMAL);
		assertEquals(firstTestTopic.getViewCount(), 0);
		assertEquals(firstTestTopic.getReplies(), 0);
		assertEquals(firstTestTopic.getPoster().getUserId(), "root");
		assertEquals(firstTestTopic.getPosts().size(), 1);

		Topic thirdTestTopic = topics.get(2);
		assertEquals(thirdTestTopic.getForum().getCategory().getTitle(),
				"First Test Category");
		assertEquals(thirdTestTopic.getForum().getName(), "Second Test Forum");
		assertTrue(thirdTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals(thirdTestTopic.getSubject(), "Third Test Topic");
		assertEquals(thirdTestTopic.getType(), ADVICE);
		assertEquals(thirdTestTopic.getViewCount(), 0);
		assertEquals(thirdTestTopic.getReplies(), 0);
		assertEquals(thirdTestTopic.getPoster().getUserId(), "root");
		assertEquals(thirdTestTopic.getPosts().size(), 1);

		Topic fourthTestTopic = topics.get(3);
		assertEquals(fourthTestTopic.getForum().getCategory().getTitle(),
				"First Test Category");
		assertEquals(fourthTestTopic.getForum().getName(), "Second Test Forum");
		assertTrue(fourthTestTopic.getLastPostDate().compareTo(today) < 0);
		assertEquals(fourthTestTopic.getSubject(), "Fourth Test Topic");
		assertEquals(fourthTestTopic.getType(), IMPORTANT);
		assertEquals(fourthTestTopic.getViewCount(), 0);
		assertEquals(fourthTestTopic.getReplies(), 0);
		assertEquals(fourthTestTopic.getPoster().getUserId(), "root");
		assertEquals(fourthTestTopic.getPosts().size(), 1);
	}

	@After
	public void stop() {
		String message = removeTopic(driver, new Topic(new Forum(
				"First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, new Topic(new Forum("First Test Forum"),
				"Second Test Topic", asList(new Post[] { new Post(
						"Second Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"),
				"Third Test Topic", asList(new Post[] { new Post(
						"Third Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic", asList(new Post[] { new Post(
						"Fourth Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeForum(driver, new Forum("First Test Forum"),
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"),
				SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
