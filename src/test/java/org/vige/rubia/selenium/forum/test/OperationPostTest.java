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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.VerifyPost.getPostsOfTopics;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest;

@RunWith(Arquillian.class)
public class OperationPostTest {

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
		List<Attachment> files = new ArrayList<Attachment>();
		files.add(new Attachment("/first", "First Test File"));
		files.add(new Attachment("/second", "Second Test File"));
		files.add(new Attachment("/third", "Third Test File"));
		message = createTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"First Test Topic",
						Arrays.asList(new Post[] { new Post("First Test Body",
								files) }),
						NORMAL,
						new Poll(
								"First Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("First Test Answer"),
										new PollOption("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "First Test Post",
				files));
		assertTrue(message.equals("First Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Second Test Post",
				files));
		assertTrue(message.equals("Second Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Third Test Post",
				files));
		assertTrue(message.equals("Third Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Fourth Test Post",
				files));
		assertTrue(message.equals("Fourth Test Post"));
		message = createTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"Second Test Topic",
						Arrays.asList(new Post[] { new Post("Second Test Body",
								files) }),
						IMPORTANT,
						new Poll(
								"Second Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("Third Test Answer"),
										new PollOption("Fourth Test Answer") }),
								8)));
		assertTrue(message.equals("Second Test Topic"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "Second Test Topic"), "Fifth Test Post",
				files));
		assertTrue(message.equals("Fifth Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "Second Test Topic"), "Sixth Test Post",
				files));
		assertTrue(message.equals("Sixth Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "Second Test Topic"), "Seventh Test Post",
				files));
		assertTrue(message.equals("Seventh Test Post"));
		message = createForum(driver, new Forum("Second Test Forum",
				"Second Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Third Test Topic",
						Arrays.asList(new Post[] { new Post("Third Test Body",
								files) }), ADVICE, new Poll(
								"Third Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("Fifth Test Answer"),
										new PollOption("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Third Test Topic"), "Eight Test Post",
				files));
		assertTrue(message.equals("Eight Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Third Test Topic"), "Ninth Test Post",
				files));
		assertTrue(message.equals("Ninth Test Post"));
		files.clear();
		files.add(new Attachment("/fourth", "Fourth Test File"));
		files.add(new Attachment("/fifth", "Fifth Test File"));
		files.add(new Attachment("/sixth", "Sixth Test File"));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
						Arrays.asList(new Post[] { new Post("Fourth Test Body",
								files) }), IMPORTANT, new Poll(
								"Fourth Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("Seventh Test Answer"),
										new PollOption("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Ten Test Post",
				files));
		assertTrue(message.equals("Ten Test Post"));
	}

	@Test
	public void verifyCreatedPosts() {
		List<Post> posts = getPostsOfTopics(driver, new Topic(
				"First Test Topic"), new Topic("Second Test Topic"), new Topic(
				"Third Test Topic"), new Topic("Fourth Test Topic"));
		assertEquals(posts.size(), 14);
		Date today = new Date();

		Post secondTestPost = posts.get(0);
		Date createdDate1 = secondTestPost.getCreateDate();
		assertFalse(createdDate1.after(today));
		assertEquals(secondTestPost.getEditCount(), 0);
		assertNull(secondTestPost.getEditDate());
		assertEquals(secondTestPost.getMessage().getSubject(),
				"Second Test Topic");
		assertEquals(secondTestPost.getMessage().getText(), "Second Test Body");
		assertEquals(secondTestPost.getPoster().getUserId(), "root");
		assertEquals(secondTestPost.getTopic().getSubject(),
				"Second Test Topic");
		assertEquals(secondTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(secondTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextFifthTestPost = posts.get(1);
		Date createdNextFifthTestPostDate = nextFifthTestPost.getCreateDate();
		assertFalse(createdNextFifthTestPostDate.after(today));
		assertFalse(createdDate1.after(createdNextFifthTestPostDate));
		assertEquals(nextFifthTestPost.getEditCount(), 0);
		assertNull(nextFifthTestPost.getEditDate());
		assertEquals(nextFifthTestPost.getMessage().getSubject(),
				"Re: Second Test Topic");
		assertEquals(nextFifthTestPost.getMessage().getText(),
				"Fifth Test Post");
		assertEquals(nextFifthTestPost.getPoster().getUserId(), "root");
		assertEquals(nextFifthTestPost.getTopic().getSubject(),
				"Second Test Topic");
		assertEquals(nextFifthTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextFifthTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextSixthTestPost = posts.get(2);
		Date createdNextSixthTestPostDate = nextSixthTestPost.getCreateDate();
		assertFalse(createdNextSixthTestPostDate.after(today));
		assertFalse(createdNextFifthTestPostDate
				.after(createdNextSixthTestPostDate));
		assertEquals(nextSixthTestPost.getEditCount(), 0);
		assertNull(nextSixthTestPost.getEditDate());
		assertEquals(nextSixthTestPost.getMessage().getSubject(),
				"Re: Second Test Topic");
		assertEquals(nextSixthTestPost.getMessage().getText(),
				"Sixth Test Post");
		assertEquals(nextSixthTestPost.getPoster().getUserId(), "root");
		assertEquals(nextSixthTestPost.getTopic().getSubject(),
				"Second Test Topic");
		assertEquals(nextSixthTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextSixthTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextSeventhTestPost = posts.get(3);
		Date createdNextSeventhTestPostDate = nextSeventhTestPost
				.getCreateDate();
		assertFalse(createdNextSeventhTestPostDate.after(today));
		assertFalse(createdNextSixthTestPostDate
				.after(createdNextSeventhTestPostDate));
		assertEquals(nextSeventhTestPost.getEditCount(), 0);
		assertNull(nextSeventhTestPost.getEditDate());
		assertEquals(nextSeventhTestPost.getMessage().getSubject(),
				"Re: Second Test Topic");
		assertEquals(nextSeventhTestPost.getMessage().getText(),
				"Seventh Test Post");
		assertEquals(nextSeventhTestPost.getPoster().getUserId(), "root");
		assertEquals(nextSeventhTestPost.getTopic().getSubject(),
				"Second Test Topic");
		assertEquals(nextSeventhTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextSeventhTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post firstTestPost = posts.get(4);
		Date createdDate2 = firstTestPost.getCreateDate();
		assertFalse(createdDate2.after(today));
		assertFalse(createdDate1.before(createdDate2));
		assertEquals(firstTestPost.getEditCount(), 0);
		assertNull(firstTestPost.getEditDate());
		assertEquals(firstTestPost.getMessage().getSubject(),
				"First Test Topic");
		assertEquals(firstTestPost.getMessage().getText(), "First Test Body");
		assertEquals(firstTestPost.getPoster().getUserId(), "root");
		assertEquals(firstTestPost.getTopic().getSubject(), "First Test Topic");
		assertEquals(firstTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(firstTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextFirstTestPost = posts.get(5);
		Date createdNextFirstTestPostDate = nextFirstTestPost.getCreateDate();
		assertFalse(createdNextFirstTestPostDate.after(today));
		assertFalse(createdDate2.after(createdNextFirstTestPostDate));
		assertEquals(nextFirstTestPost.getEditCount(), 0);
		assertNull(nextFirstTestPost.getEditDate());
		assertEquals(nextFirstTestPost.getMessage().getSubject(),
				"Re: First Test Topic");
		assertEquals(nextFirstTestPost.getMessage().getText(),
				"First Test Post");
		assertEquals(nextFirstTestPost.getPoster().getUserId(), "root");
		assertEquals(nextFirstTestPost.getTopic().getSubject(),
				"First Test Topic");
		assertEquals(nextFirstTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextFirstTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextSecondTestPost = posts.get(6);
		Date createdNextSecondTestPostDate = nextSecondTestPost.getCreateDate();
		assertFalse(createdNextSecondTestPostDate.after(today));
		assertFalse(createdNextFirstTestPostDate
				.after(createdNextSecondTestPostDate));
		assertEquals(nextSecondTestPost.getEditCount(), 0);
		assertNull(nextSecondTestPost.getEditDate());
		assertEquals(nextSecondTestPost.getMessage().getSubject(),
				"Re: First Test Topic");
		assertEquals(nextSecondTestPost.getMessage().getText(),
				"Second Test Post");
		assertEquals(nextSecondTestPost.getPoster().getUserId(), "root");
		assertEquals(nextSecondTestPost.getTopic().getSubject(),
				"First Test Topic");
		assertEquals(nextSecondTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextSecondTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextThirdTestPost = posts.get(7);
		Date createdNextThirdTestPostDate = nextThirdTestPost.getCreateDate();
		assertFalse(createdNextThirdTestPostDate.after(today));
		assertFalse(createdNextSecondTestPostDate
				.after(createdNextThirdTestPostDate));
		assertEquals(nextThirdTestPost.getEditCount(), 0);
		assertNull(nextThirdTestPost.getEditDate());
		assertEquals(nextThirdTestPost.getMessage().getSubject(),
				"Re: First Test Topic");
		assertEquals(nextThirdTestPost.getMessage().getText(),
				"Third Test Post");
		assertEquals(nextThirdTestPost.getPoster().getUserId(), "root");
		assertEquals(nextThirdTestPost.getTopic().getSubject(),
				"First Test Topic");
		assertEquals(nextThirdTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextThirdTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextFourthTestPost = posts.get(8);
		Date createdNextFourthTestPostDate = nextFourthTestPost.getCreateDate();
		assertFalse(createdNextFourthTestPostDate.after(today));
		assertFalse(createdNextThirdTestPostDate
				.after(createdNextFourthTestPostDate));
		assertEquals(nextFourthTestPost.getEditCount(), 0);
		assertNull(nextFourthTestPost.getEditDate());
		assertEquals(nextFourthTestPost.getMessage().getSubject(),
				"Re: First Test Topic");
		assertEquals(nextFourthTestPost.getMessage().getText(),
				"Fourth Test Post");
		assertEquals(nextFourthTestPost.getPoster().getUserId(), "root");
		assertEquals(nextFourthTestPost.getTopic().getSubject(),
				"First Test Topic");
		assertEquals(nextFourthTestPost.getTopic().getForum().getName(),
				"First Test Forum");
		assertEquals(nextFourthTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post thirdTestPost = posts.get(9);
		Date createdDate3 = thirdTestPost.getCreateDate();
		assertFalse(createdDate3.after(today));
		assertFalse(createdDate2.after(createdDate3));
		assertEquals(thirdTestPost.getEditCount(), 0);
		assertNull(thirdTestPost.getEditDate());
		assertEquals(thirdTestPost.getMessage().getSubject(),
				"Third Test Topic");
		assertEquals(thirdTestPost.getMessage().getText(), "Third Test Body");
		assertEquals(thirdTestPost.getPoster().getUserId(), "root");
		assertEquals(thirdTestPost.getTopic().getSubject(), "Third Test Topic");
		assertEquals(thirdTestPost.getTopic().getForum().getName(),
				"Second Test Forum");
		assertEquals(thirdTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextEightTestPost = posts.get(10);
		Date createdNextEightTestPostDate = nextEightTestPost.getCreateDate();
		assertFalse(createdNextEightTestPostDate.after(today));
		assertFalse(createdDate3.after(createdNextEightTestPostDate));
		assertEquals(nextEightTestPost.getEditCount(), 0);
		assertNull(nextEightTestPost.getEditDate());
		assertEquals(nextEightTestPost.getMessage().getSubject(),
				"Re: Third Test Topic");
		assertEquals(nextEightTestPost.getMessage().getText(),
				"Eight Test Post");
		assertEquals(nextEightTestPost.getPoster().getUserId(), "root");
		assertEquals(nextEightTestPost.getTopic().getSubject(),
				"Third Test Topic");
		assertEquals(nextEightTestPost.getTopic().getForum().getName(),
				"Second Test Forum");
		assertEquals(nextEightTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextNinthTestPost = posts.get(11);
		Date createdNextNinthTestPostDate = nextNinthTestPost.getCreateDate();
		assertFalse(createdNextNinthTestPostDate.after(today));
		assertFalse(createdNextEightTestPostDate
				.after(createdNextNinthTestPostDate));
		assertEquals(nextNinthTestPost.getEditCount(), 0);
		assertNull(nextNinthTestPost.getEditDate());
		assertEquals(nextNinthTestPost.getMessage().getSubject(),
				"Re: Third Test Topic");
		assertEquals(nextNinthTestPost.getMessage().getText(),
				"Ninth Test Post");
		assertEquals(nextNinthTestPost.getPoster().getUserId(), "root");
		assertEquals(nextNinthTestPost.getTopic().getSubject(),
				"Third Test Topic");
		assertEquals(nextNinthTestPost.getTopic().getForum().getName(),
				"Second Test Forum");
		assertEquals(nextNinthTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post fourthTestPost = posts.get(12);
		Date createdDate4 = fourthTestPost.getCreateDate();
		assertFalse(createdDate4.after(today));
		assertFalse(createdDate3.after(createdDate4));
		assertEquals(fourthTestPost.getEditCount(), 0);
		assertNull(fourthTestPost.getEditDate());
		assertEquals(fourthTestPost.getMessage().getSubject(),
				"Fourth Test Topic");
		assertEquals(fourthTestPost.getMessage().getText(), "Fourth Test Body");
		assertEquals(fourthTestPost.getPoster().getUserId(), "root");
		assertEquals(fourthTestPost.getTopic().getSubject(),
				"Fourth Test Topic");
		assertEquals(fourthTestPost.getTopic().getForum().getName(),
				"Second Test Forum");
		assertEquals(fourthTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");

		Post nextTenTestPost = posts.get(13);
		Date createdNextTenTestPostDate = nextTenTestPost.getCreateDate();
		assertFalse(createdNextTenTestPostDate.after(today));
		assertFalse(createdDate4.after(createdNextTenTestPostDate));
		assertEquals(nextTenTestPost.getEditCount(), 0);
		assertNull(nextTenTestPost.getEditDate());
		assertEquals(nextTenTestPost.getMessage().getSubject(),
				"Re: Fourth Test Topic");
		assertEquals(nextTenTestPost.getMessage().getText(), "Ten Test Post");
		assertEquals(nextTenTestPost.getPoster().getUserId(), "root");
		assertEquals(nextTenTestPost.getTopic().getSubject(),
				"Fourth Test Topic");
		assertEquals(nextTenTestPost.getTopic().getForum().getName(),
				"Second Test Forum");
		assertEquals(nextTenTestPost.getTopic().getForum().getCategory()
				.getTitle(), "First Test Category");
	}

	@After
	public void stop() {
		String message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "First Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Second Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Third Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Fourth Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "Second Test Topic"), "Fifth Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "Second Test Topic"), "Sixth Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "Second Test Topic"), "Seventh Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Third Test Topic"), "Eight Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Third Test Topic"), "Ninth Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Ten Test Post"));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"First Test Topic",
						Arrays.asList(new Post[] { new Post("First Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"Second Test Topic",
						Arrays.asList(new Post[] { new Post("Second Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("Second Test Forum"),
						"Third Test Topic",
						Arrays.asList(new Post[] { new Post("Third Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("Second Test Forum"),
						"Fourth Test Topic",
						Arrays.asList(new Post[] { new Post("Fourth Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeForum(driver, new Forum("First Test Forum"),
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"),
				SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
