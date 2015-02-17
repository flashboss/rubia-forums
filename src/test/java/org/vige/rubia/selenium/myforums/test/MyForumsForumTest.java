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
package org.vige.rubia.selenium.myforums.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static org.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static org.vige.rubia.properties.OperationType.CANCEL;
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
import static org.vige.rubia.selenium.forum.action.SubscriptionForum.isRegistered;
import static org.vige.rubia.selenium.forum.action.SubscriptionForum.registerForum;
import static org.vige.rubia.selenium.forum.action.SubscriptionForum.unregisterForum;
import static org.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static org.vige.rubia.selenium.myforums.action.ViewAllForums.viewAllForums;
import static org.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllEditForumsRemoveForum;
import static org.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllForumsRemoveForum;
import static org.vige.rubia.selenium.myforums.action.ViewAllForumsUpdateForum.viewAllForumsUpdateForum;

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
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import org.vige.rubia.model.Topic;
import org.vige.rubia.selenium.myforums.action.ViewAllForumsSelectForum;
import org.vige.rubia.selenium.myforums.action.ViewAllForumsSelectPost;

@RunWith(Arquillian.class)
public class MyForumsForumTest {

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
	public void verifyRegisteredCanceledForums() {
		Forum forum = new Forum("Fourth Test Forum", "Fourth Test Description",
				new Category("Second Test Category"));
		String message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION,
				CANCEL);
		assertTrue(message.equals("Fourth Test Forum"));
		assertTrue(!isRegistered(driver, forum));
	}

	@Test
	public void verifyProfile() {
		Post post = new Post();
		Message message = new Message();
		message.setSubject("Second Test Topic");
		post.setMessage(message);
		Poster poster = ViewAllForumsSelectForum.selectProfile(driver, post);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertEquals(poster.getPostCount(), 12);
	}

	@Test
	public void verifyPost() {
		Post post = new Post();
		Message message = new Message();
		message.setSubject("Second Test Topic");
		post.setMessage(message);
		Topic result = ViewAllForumsSelectPost.selectPost(driver, post);
		assertTrue(result != null);
		assertEquals(result.getSubject(), message.getSubject());
		assertEquals(result.getPoster().getUserId(), "root");
		assertEquals(result.getPosts().size(), 1);
		assertEquals(result.getPosts().get(0).getMessage().getSubject(),
				message.getSubject());
		assertEquals(result.getPosts().get(0).getMessage().getText(),
				"Second Test Body");
		assertEquals(result.getPoll().getTitle(), "Second Test Question");
	}

	@Test
	public void verifyAllForumsForum() {
		Forum forum = new Forum("Third Test Forum", "Third Test Description",
				new Category("Second Test Category"));
		Forum result = ViewAllForumsSelectForum.selectForum(driver, forum);
		assertTrue(result != null);
		assertEquals(result.getName(), forum.getName());
		assertEquals(result.getTopics().size(), 0);
		assertEquals(result.getCategory().getTitle(), forum.getCategory()
				.getTitle());
	}

	@Test
	public void verifyAllForumsProfile() {
		Post post = new Post();
		Message message = new Message();
		message.setSubject("Fourth Test Topic");
		post.setMessage(message);
		Poster poster = ViewAllForumsSelectForum.selectAllForumsProfile(driver,
				post);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertEquals(poster.getPostCount(), 16);
	}

	@Test
	public void verifyAllForumsPost() {
		Post post = new Post();
		Message message = new Message();
		message.setSubject("Second Test Topic");
		post.setMessage(message);
		Topic result = ViewAllForumsSelectPost
				.selectAllForumsPost(driver, post);
		assertTrue(result != null);
		assertEquals(result.getSubject(), message.getSubject());
		assertEquals(result.getPoster().getUserId(), "root");
		assertEquals(result.getPosts().size(), 1);
		assertEquals(result.getPosts().get(0).getMessage().getSubject(),
				message.getSubject());
		assertEquals(result.getPosts().get(0).getMessage().getText(),
				"Second Test Body");
		assertEquals(result.getPoll().getTitle(), "Second Test Question");
	}

	@Test
	public void verifyRegisteredAuthorizedForums() {
		List<Forum> forums = viewAllForums(driver);
		for (Forum forum : forums) {
			assertTrue(isRegistered(driver, forum));
		}
		Forum fourthForum = new Forum("Fourth Test Forum");
		goTo(driver, fourthForum);
		assertTrue(!isRegistered(driver, fourthForum));
		assertEquals(forums.get(0).getName(), "First Test Forum");
		assertEquals(forums.get(1).getName(), "Second Test Forum");
		assertEquals(forums.get(2).getName(), "Third Test Forum");
		assertEquals(forums.get(3).getName(), "Fifth Test Forum");
		assertEquals(forums.size(), 4);
	}

	@Test
	public void verifyUpdatedForum() {
		Forum forum = new Forum("Second Test Forum");
		String notificationType = viewAllForumsUpdateForum(driver, forum,
				EMAIL_NO_NOTIFICATION);
		assertEquals(notificationType, EMAIL_NO_NOTIFICATION.toString());
		notificationType = viewAllForumsUpdateForum(driver, forum,
				EMAIL_EMBEDED_NOTIFICATION);
		assertEquals(notificationType, EMAIL_EMBEDED_NOTIFICATION.toString());
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
		message = removeForum(driver, forum, "Second Test Forum");
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
