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
package it.vige.rubia.selenium.myforums.test;

import static it.vige.rubia.model.TopicType.ADVICE;
import static it.vige.rubia.model.TopicType.IMPORTANT;
import static it.vige.rubia.model.TopicType.NORMAL;
import static it.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static it.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static it.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static it.vige.rubia.properties.OperationType.CANCEL;
import static it.vige.rubia.properties.OperationType.CONFIRM;
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
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.forum.action.SubscriptionTopic.isRegistered;
import static it.vige.rubia.selenium.forum.action.SubscriptionTopic.registerTopic;
import static it.vige.rubia.selenium.forum.action.SubscriptionTopic.unregisterTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.goTo;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.viewAllTopics;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopicsRemoveTopic.viewAllTopicsRemoveTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopicsUpdateTopic.viewAllTopicsUpdateTopic;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.selenium.myforums.action.ViewAllTopicsSelectPost;
import it.vige.rubia.selenium.myforums.action.ViewAllTopicsSelectTopic;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class MyForumsTopicTest {

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
	public void verifyRegisteredCanceledTopics() {
		Topic topic = new Topic(new Forum("Third Test Forum"),
				"Sixth Test Topic", asList(new Post[] { new Post(
						"Sixth Test Body", asList(new Attachment("ten",
								"Ten Test File"), new Attachment("eleven",
								"Eleven Test File"), new Attachment("twelve",
								"Twelve Test File"))) }), IMPORTANT, new Poll(
						"Fourth Test Question", asList(new PollOption[] {
								new PollOption("Ninth Test Answer"),
								new PollOption("Ten Test Answer") }), 8));
		String message = registerTopic(driver, topic, EMAIL_NO_NOTIFICATION,
				CANCEL);
		assertTrue(message.equals("Sixth Test Topic"));
		assertTrue(!isRegistered(driver, topic));
	}

	@Test
	public void verifyRegisteredAuthorizedTopics() {
		List<Topic> topics = viewAllTopics(driver);
		for (Topic topic : topics) {
			assertTrue(isRegistered(driver, topic));
			goTo(driver);
		}
		assertEquals(topics.get(0).getSubject(), "Fifth Test Topic");
		assertEquals(topics.get(1).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(2).getSubject(), "Third Test Topic");
		assertEquals(topics.get(3).getSubject(), "Second Test Topic");
		assertEquals(topics.get(4).getSubject(), "First Test Topic");
		assertEquals(topics.size(), 5);
	}

	@Test
	public void verifyUpdatedTopic() {
		Topic topic = new Topic();
		topic.setSubject("Third Test Topic");
		String notificationType = viewAllTopicsUpdateTopic(driver, topic,
				EMAIL_EMBEDED_NOTIFICATION);
		assertEquals(notificationType, EMAIL_EMBEDED_NOTIFICATION.toString());
		notificationType = viewAllTopicsUpdateTopic(driver, topic,
				EMAIL_NO_NOTIFICATION);
		assertEquals(notificationType, EMAIL_NO_NOTIFICATION.toString());
	}

	@Test
	public void verifyProfile() {
		Topic topic = new Topic(new Forum("First Test Forum"),
				"First Test Topic", asList(new Post[] { new Post(
						"First Test Body") }));
		Poster poster = ViewAllTopicsSelectTopic.selectProfile(driver, topic);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertTrue(poster.getPostCount() >= 18);
	}

	@Test
	public void verifyPost() {
		Post post = new Post();
		Message message = new Message();
		message.setSubject("First Test Topic");
		post.setMessage(message);
		Topic result = ViewAllTopicsSelectPost.selectPost(driver, post);
		assertTrue(result != null);
		assertEquals(result.getPosts().size(), 1);
		assertEquals(result.getPosts().get(0).getPoster().getUserId(), "root");
		assertEquals(result.getPosts().get(0).getMessage().getSubject(),
				message.getSubject());
		assertEquals(result.getPosts().get(0).getMessage().getText(),
				"First Test Body");
		assertEquals(result.getSubject(), message.getSubject());
	}

	@Test
	public void verifyPostProfile() {
		Post post = new Post();
		Message message = new Message();
		message.setSubject("Fourth Test Topic");
		post.setMessage(message);
		Poster poster = ViewAllTopicsSelectPost.selectProfile(driver, post);
		assertTrue(poster != null);
		assertEquals(poster.getUserId(), "root");
		assertTrue(poster.getPostCount() >= 12);
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
