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

import static it.vige.rubia.dto.TopicType.ADVICE;
import static it.vige.rubia.dto.TopicType.IMPORTANT;
import static it.vige.rubia.dto.TopicType.NORMAL;
import static it.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static it.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static it.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static it.vige.rubia.properties.OperationType.CANCEL;
import static it.vige.rubia.properties.OperationType.CONFIRM;
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
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.goTo;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.viewAllTopics;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopicsRemoveTopic.viewAllTopicsRemoveTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopicsUpdateTopic.viewAllTopicsUpdateTopic;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.selenium.myforums.action.ViewAllTopicsSelectPost;
import it.vige.rubia.selenium.myforums.action.ViewAllTopicsSelectTopic;

@RunWith(Arquillian.class)
@RunAsClient
public class MyForumsTopicTest {

	@Drone
	private static WebDriver driver;

	@BeforeClass
	public static void setUp() {
		driver.get(HOME_URL);
		String message = createCategory(driver, new CategoryBean("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, new CategoryBean("Second Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver,
				new ForumBean("First Test Forum", "First Test Description", new CategoryBean("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
						asList(new PostBean[] { new PostBean("First Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						NORMAL,
						new PollBean("First Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("First Test Answer"), new PollOptionBean("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
		message = registerTopic(driver, new TopicBean("First Test Topic"), EMAIL_LINKED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic",
						asList(new PostBean[] { new PostBean("Second Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						IMPORTANT,
						new PollBean("Second Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Third Test Answer"), new PollOptionBean("Fourth Test Answer") }),
								8)));
		assertTrue(message.equals("Second Test Topic"));
		message = registerTopic(driver, new TopicBean("Second Test Topic"), EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver,
				new ForumBean("Second Test Forum", "Second Test Description", new CategoryBean("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic",
						asList(new PostBean[] { new PostBean("Third Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						ADVICE,
						new PollBean("Third Test Question",
								asList(new PollOptionBean[] {
										new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"),
										new PollOptionBean("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		message = registerTopic(driver, new TopicBean("Third Test Topic"), EMAIL_NO_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver, new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))) }),
				IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }), 0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = registerTopic(driver, new TopicBean("Fourth Test Topic"), EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fourth Test Topic"));
		message = createForum(driver,
				new ForumBean("Third Test Forum", "Third Test Description", new CategoryBean("Second Test Category")));
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
						asList(new PostBean[] { new PostBean("Fifth Test with Truncation over 25 characters Body",
								asList(new AttachmentBean("seventh", "Seventh Test File"),
										new AttachmentBean("eight", "Eight Test File"),
										new AttachmentBean("ninth", "Ninth Test File"))) }),
						IMPORTANT,
						new PollBean("Third Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								8)));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
		message = registerTopic(driver, new TopicBean("Fifth Test with Truncation over 25 characters Topic"),
				EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
		message = createTopic(driver,
				new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
						asList(new PostBean[] { new PostBean("Sixth Test Body",
								asList(new AttachmentBean("ten", "Ten Test File"),
										new AttachmentBean("eleven", "Eleven Test File"),
										new AttachmentBean("twelve", "Twelve Test File"))) }),
						IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Ninth Test Answer"), new PollOptionBean("Ten Test Answer") }), 8)));
		assertTrue(message.equals("Sixth Test Topic"));
	}

	@Test
	public void verifyRegisteredCanceledTopics() {
		TopicBean topic = new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
				asList(new PostBean[] { new PostBean("Sixth Test Body",
						asList(new AttachmentBean("ten", "Ten Test File"),
								new AttachmentBean("eleven", "Eleven Test File"),
								new AttachmentBean("twelve", "Twelve Test File"))) }),
				IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Ninth Test Answer"), new PollOptionBean("Ten Test Answer") }), 8));
		goTo(driver, topic);
		String message = registerTopic(driver, topic, EMAIL_NO_NOTIFICATION, CANCEL);
		assertTrue(message.equals("Sixth Test Topic"));
		assertTrue(!isRegistered(driver, topic));
	}

	@Test
	public void verifyRegisteredAuthorizedTopics() {
		List<TopicBean> topics = viewAllTopics(driver);
		for (TopicBean topic : topics) {
			assertTrue(isRegistered(driver, topic));
			goTo(driver);
		}
		assertEquals("Fifth Test with Truncation over 25 characters Topic", topics.get(0).getSubject());
		assertEquals("Fourth Test Topic", topics.get(1).getSubject());
		assertEquals("Third Test Topic", topics.get(2).getSubject());
		assertEquals("Second Test Topic", topics.get(3).getSubject());
		assertEquals("First Test Topic", topics.get(4).getSubject());
		assertEquals(5, topics.size());
	}

	@Test
	public void verifyUpdatedTopic() {
		TopicBean topic = new TopicBean();
		topic.setSubject("Third Test Topic");
		String notificationType = viewAllTopicsUpdateTopic(driver, topic, EMAIL_EMBEDED_NOTIFICATION);
		assertEquals(EMAIL_EMBEDED_NOTIFICATION.toString(), notificationType);
		notificationType = viewAllTopicsUpdateTopic(driver, topic, EMAIL_NO_NOTIFICATION);
		assertEquals(EMAIL_NO_NOTIFICATION.toString(), notificationType);
	}

	@Test
	public void verifyProfile() {
		TopicBean topic = new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
				asList(new PostBean[] { new PostBean("First Test Body") }));
		PosterBean poster = ViewAllTopicsSelectTopic.selectProfile(driver, topic);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 6);
	}

	@Test
	public void verifyPost() {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject("First Test Topic");
		post.setMessage(message);
		TopicBean result = ViewAllTopicsSelectPost.selectPost(driver, post);
		assertTrue(result != null);
		assertEquals(1, result.getPosts().size());
		assertEquals("root", result.getPosts().get(0).getPoster().getUserId());
		assertEquals(message.getSubject(), result.getPosts().get(0).getMessage().getSubject());
		assertEquals("First Test Body", result.getPosts().get(0).getMessage().getText());
		assertEquals(message.getSubject(), result.getSubject());
	}

	@Test
	public void verifyPostProfile() {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject("Fourth Test Topic");
		post.setMessage(message);
		PosterBean poster = ViewAllTopicsSelectPost.selectProfile(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 6);
	}

	@AfterClass
	public static void stop() {
		TopicBean topic = new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
				asList(new PostBean[] { new PostBean("First Test Body") }));
		String message = unregisterTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic",
				asList(new PostBean[] { new PostBean("Second Test Body") }));
		message = unregisterTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic",
				asList(new PostBean[] { new PostBean("Third Test Body") }));
		message = unregisterTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body") }));
		message = viewAllTopicsRemoveTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("Third Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
				asList(new PostBean[] { new PostBean("Fifth Test with Truncation over 25 characters Body") }));
		message = viewAllTopicsRemoveTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("Third Test Forum"), "Sixth Test Topic",
				asList(new PostBean[] { new PostBean("Sixth Test Body") }));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeForum(driver, new ForumBean("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new ForumBean("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeForum(driver, new ForumBean("Third Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_2_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
