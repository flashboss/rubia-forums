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
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_3_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_4_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_2_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_3_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_4_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.forum.action.SubscriptionForum.isRegistered;
import static it.vige.rubia.selenium.forum.action.SubscriptionForum.registerForum;
import static it.vige.rubia.selenium.forum.action.SubscriptionForum.unregisterForum;
import static it.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static it.vige.rubia.selenium.myforums.action.ViewAllForums.viewAllForums;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllEditForumsRemoveForum;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllForumsRemoveForum;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsSelectForum.selectAllForumsProfile;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsSelectForum.selectForum;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsSelectForum.selectProfile;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsSelectPost.selectAllForumsPost;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsSelectPost.selectPost;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsUpdateForum.viewAllForumsUpdateForum;
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

@RunWith(Arquillian.class)
@RunAsClient
public class MyForumsForumTest {

	@Drone
	private static WebDriver driver;

	@BeforeClass
	public static void setUp() {
		driver.get(HOME_URL);
		String message = createCategory(driver, new CategoryBean("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, new CategoryBean("Second Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		ForumBean forum = new ForumBean("First Test Forum", "First Test Description",
				new CategoryBean("First Test Category"));
		message = createForum(driver, forum);
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
		message = createTopic(driver, new TopicBean(forum, "Second Test Topic", asList(new PostBean[] { new PostBean(
				"Second Test Body",
				asList(new AttachmentBean("first", "First Test File"), new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File"))) }),
				IMPORTANT, new PollBean("Second Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Third Test Answer"), new PollOptionBean("Fourth Test Answer") }), 8)));
		assertTrue(message.equals("Second Test Topic"));
		message = registerForum(driver, forum, EMAIL_LINKED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("First Test Forum"));
		forum = new ForumBean("Second Test Forum", "Second Test Description", new CategoryBean("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver, new TopicBean(forum, "Third Test Topic", asList(new PostBean[] { new PostBean(
				"Third Test Body",
				asList(new AttachmentBean("first", "First Test File"), new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File"))) }),
				ADVICE,
				new PollBean("Third Test Question",
						asList(new PollOptionBean[] {
								new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"),
								new PollOptionBean("Sixth Test Answer") }),
						9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver,
				new TopicBean(forum, "Fourth Test Topic", asList(new PostBean[] { new PostBean("Fourth Test Body",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))) }),
						IMPORTANT,
						new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = registerForum(driver, forum, EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Second Test Forum"));
		forum = new ForumBean("Third Test Forum", "Third Test Description", new CategoryBean("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Third Test Forum"));
		forum = new ForumBean("Fourth Test Forum", "Fourth Test Description", new CategoryBean("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_3_MESSAGE));
		forum = new ForumBean("Fifth Test with Truncation over 25 characters Forum", "Fifth Test Description",
				new CategoryBean("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_4_MESSAGE));
		message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Forum"));
	}

	@Test
	public void verifyRegisteredCanceledForums() {
		ForumBean forum = new ForumBean("Fourth Test Forum", "Fourth Test Description",
				new CategoryBean("Second Test Category"));
		String message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION, CANCEL);
		assertTrue(message.equals("Fourth Test Forum"));
		assertTrue(!isRegistered(driver, forum));
	}

	@Test
	public void verifyProfile() {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject("Second Test Topic");
		post.setMessage(message);
		PosterBean poster = selectProfile(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 4);
	}

	@Test
	public void verifyPost() {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject("Second Test Topic");
		post.setMessage(message);
		TopicBean result = selectPost(driver, post);
		assertTrue(result != null);
		assertEquals(message.getSubject(), result.getSubject());
		assertEquals("root", result.getPoster().getUserId());
		assertEquals(1, result.getPosts().size());
		assertEquals(message.getSubject(), result.getPosts().get(0).getMessage().getSubject());
		assertEquals("Second Test Body", result.getPosts().get(0).getMessage().getText());
		assertEquals("Second Test Question", result.getPoll().getTitle());
	}

	@Test
	public void verifyAllForumsForum() {
		ForumBean forum = new ForumBean("Third Test Forum", "Third Test Description",
				new CategoryBean("Second Test Category"));
		ForumBean result = selectForum(driver, forum);
		assertTrue(result != null);
		assertEquals(forum.getName(), result.getName());
		assertEquals(0, result.getTopics().size());
		assertEquals(forum.getCategory().getTitle(), result.getCategory().getTitle());
	}

	@Test
	public void verifyAllForumsProfile() {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject("Fourth Test Topic");
		post.setMessage(message);
		PosterBean poster = selectAllForumsProfile(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 4);
	}

	@Test
	public void verifyAllForumsPost() {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject("Second Test Topic");
		post.setMessage(message);
		TopicBean result = selectAllForumsPost(driver, post);
		assertTrue(result != null);
		assertEquals(message.getSubject(), result.getSubject());
		assertEquals("root", result.getPoster().getUserId());
		assertEquals(1, result.getPosts().size());
		assertEquals(message.getSubject(), result.getPosts().get(0).getMessage().getSubject());
		assertEquals("Second Test Body", result.getPosts().get(0).getMessage().getText());
		assertEquals("Second Test Question", result.getPoll().getTitle());
	}

	@Test
	public void verifyRegisteredAuthorizedForums() {
		List<ForumBean> forums = viewAllForums(driver);
		for (ForumBean forum : forums) {
			assertTrue(isRegistered(driver, forum));
		}
		ForumBean fourthForum = new ForumBean("Fourth Test Forum");
		goTo(driver, fourthForum);
		assertTrue(!isRegistered(driver, fourthForum));
		assertEquals("First Test Forum", forums.get(0).getName());
		assertEquals("Second Test Forum", forums.get(1).getName());
		assertEquals("Third Test Forum", forums.get(2).getName());
		assertEquals("Fifth Test with Truncation over 25 characters Forum", forums.get(3).getName());
		assertEquals(4, forums.size());
	}

	@Test
	public void verifyUpdatedForum() {
		ForumBean forum = new ForumBean("Second Test Forum");
		String notificationType = viewAllForumsUpdateForum(driver, forum, EMAIL_NO_NOTIFICATION);
		assertEquals(EMAIL_NO_NOTIFICATION.toString(), notificationType);
		notificationType = viewAllForumsUpdateForum(driver, forum, EMAIL_EMBEDED_NOTIFICATION);
		assertEquals(EMAIL_EMBEDED_NOTIFICATION.toString(), notificationType);
	}

	@AfterClass
	public static void stop() {
		String message = removeTopic(driver, new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
				asList(new PostBean[] { new PostBean("First Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic",
				asList(new PostBean[] { new PostBean("Second Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic",
				asList(new PostBean[] { new PostBean("Third Test Body") })));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body") })));
		assertTrue(message.equals(OK));
		ForumBean forum = new ForumBean("First Test Forum");
		message = unregisterForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, "Third Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		forum = new ForumBean("Second Test Forum");
		message = unregisterForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		forum = new ForumBean("Third Test Forum");
		message = viewAllForumsRemoveForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_2_MESSAGE));
		forum = new ForumBean("Fourth Test Forum");
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_3_MESSAGE));
		forum = new ForumBean("Fifth Test with Truncation over 25 characters Forum");
		message = viewAllEditForumsRemoveForum(driver, forum);
		assertTrue(message.equals(OK));
		message = removeForum(driver, forum, SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_4_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
