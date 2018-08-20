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

import static it.vige.rubia.Constants.RE;
import static it.vige.rubia.dto.TopicType.ADVICE;
import static it.vige.rubia.dto.TopicType.IMPORTANT;
import static it.vige.rubia.dto.TopicType.NORMAL;
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
import static it.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.forum.action.VerifyCategory.goTo;
import static it.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static it.vige.rubia.selenium.forum.action.VerifyPost.getLastPostOfCurrentForum;
import static it.vige.rubia.selenium.forum.action.VerifyPost.getPosterFromButton;
import static it.vige.rubia.selenium.forum.action.VerifyPost.getPosterFromLink;
import static it.vige.rubia.selenium.forum.action.VerifyPost.getPostsOfTopics;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.getPoster;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.getPosterLastPost;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;

@RunWith(Arquillian.class)
@RunAsClient
public class OperationPostTest {

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
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "First Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("First Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Second Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Second Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Third Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Third Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Fourth Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Fourth Test Post"));
/*		message = createTopic(driver,
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
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"),
						"Fifth Test with Truncation over 25 characters Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"), "Sixth Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Sixth Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"), "Seventh Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Seventh Test Post"));
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
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic"), "Eight Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Eight Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic"), "Ninth Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Ninth Test Post"));
		message = createTopic(driver, new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))) }),
				IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }), 0)));
		assertTrue(message.equals("Fourth Test Topic"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Ten Test Post",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))));
		assertTrue(message.equals("Ten Test Post"));*/
	}

	@Test
	public void verifyCreatedPosts() {
/*		List<PostBean> posts = getPostsOfTopics(driver, new TopicBean("First Test Topic"),
				new TopicBean("Second Test Topic"), new TopicBean("Third Test Topic"),
				new TopicBean("Fourth Test Topic"));
		assertEquals(14, posts.size());
		Date today = new Date();

		PostBean secondTestPost = posts.get(0);
		Date createdDate1 = secondTestPost.getCreateDate();
		assertFalse(createdDate1.after(today));
		assertEquals(0, secondTestPost.getEditCount());
		assertNull(secondTestPost.getEditDate());
		assertEquals("Second Test Topic", secondTestPost.getMessage().getSubject());
		assertEquals("Second Test Body", secondTestPost.getMessage().getText());
		assertEquals("root", secondTestPost.getPoster().getUserId());
		assertEquals("Second Test Topic", secondTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", secondTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", secondTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextFifthTestPost = posts.get(1);
		Date createdNextFifthTestPostDate = nextFifthTestPost.getCreateDate();
		assertFalse(createdNextFifthTestPostDate.after(today));
		assertFalse(createdDate1.after(createdNextFifthTestPostDate));
		assertEquals(0, nextFifthTestPost.getEditCount());
		assertNull(nextFifthTestPost.getEditDate());
		assertEquals(RE + "Second Test Topic", nextFifthTestPost.getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Post", nextFifthTestPost.getMessage().getText());
		assertEquals("root", nextFifthTestPost.getPoster().getUserId());
		assertEquals("Second Test Topic", nextFifthTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextFifthTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextFifthTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextSixthTestPost = posts.get(2);
		Date createdNextSixthTestPostDate = nextSixthTestPost.getCreateDate();
		assertFalse(createdNextSixthTestPostDate.after(today));
		assertFalse(createdNextFifthTestPostDate.after(createdNextSixthTestPostDate));
		assertEquals(0, nextSixthTestPost.getEditCount());
		assertNull(nextSixthTestPost.getEditDate());
		assertEquals(RE + "Second Test Topic", nextSixthTestPost.getMessage().getSubject());
		assertEquals("Sixth Test Post", nextSixthTestPost.getMessage().getText());
		assertEquals("root", nextSixthTestPost.getPoster().getUserId());
		assertEquals("Second Test Topic", nextSixthTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextSixthTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextSixthTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextSeventhTestPost = posts.get(3);
		Date createdNextSeventhTestPostDate = nextSeventhTestPost.getCreateDate();
		assertFalse(createdNextSeventhTestPostDate.after(today));
		assertFalse(createdNextSixthTestPostDate.after(createdNextSeventhTestPostDate));
		assertEquals(0, nextSeventhTestPost.getEditCount());
		assertNull(nextSeventhTestPost.getEditDate());
		assertEquals(RE + "Second Test Topic", nextSeventhTestPost.getMessage().getSubject());
		assertEquals("Seventh Test Post", nextSeventhTestPost.getMessage().getText());
		assertEquals("root", nextSeventhTestPost.getPoster().getUserId());
		assertEquals("Second Test Topic", nextSeventhTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextSeventhTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextSeventhTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean firstTestPost = posts.get(4);
		Date createdDate2 = firstTestPost.getCreateDate();
		assertFalse(createdDate2.after(today));
		assertFalse(createdDate1.before(createdDate2));
		assertEquals(0, firstTestPost.getEditCount());
		assertNull(firstTestPost.getEditDate());
		assertEquals("First Test Topic", firstTestPost.getMessage().getSubject());
		assertEquals("First Test Body", firstTestPost.getMessage().getText());
		assertEquals("root", firstTestPost.getPoster().getUserId());
		assertEquals("First Test Topic", firstTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", firstTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", firstTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextFirstTestPost = posts.get(5);
		Date createdNextFirstTestPostDate = nextFirstTestPost.getCreateDate();
		assertFalse(createdNextFirstTestPostDate.after(today));
		assertFalse(createdDate2.after(createdNextFirstTestPostDate));
		assertEquals(0, nextFirstTestPost.getEditCount());
		assertNull(nextFirstTestPost.getEditDate());
		assertEquals(RE + "First Test Topic", nextFirstTestPost.getMessage().getSubject());
		assertEquals("First Test Post", nextFirstTestPost.getMessage().getText());
		assertEquals("root", nextFirstTestPost.getPoster().getUserId());
		assertEquals("First Test Topic", nextFirstTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextFirstTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextFirstTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextSecondTestPost = posts.get(6);
		Date createdNextSecondTestPostDate = nextSecondTestPost.getCreateDate();
		assertFalse(createdNextSecondTestPostDate.after(today));
		assertFalse(createdNextFirstTestPostDate.after(createdNextSecondTestPostDate));
		assertEquals(0, nextSecondTestPost.getEditCount());
		assertNull(nextSecondTestPost.getEditDate());
		assertEquals(RE + "First Test Topic", nextSecondTestPost.getMessage().getSubject());
		assertEquals("Second Test Post", nextSecondTestPost.getMessage().getText());
		assertEquals("root", nextSecondTestPost.getPoster().getUserId());
		assertEquals("First Test Topic", nextSecondTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextSecondTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextSecondTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextThirdTestPost = posts.get(7);
		Date createdNextThirdTestPostDate = nextThirdTestPost.getCreateDate();
		assertFalse(createdNextThirdTestPostDate.after(today));
		assertFalse(createdNextSecondTestPostDate.after(createdNextThirdTestPostDate));
		assertEquals(0, nextThirdTestPost.getEditCount());
		assertNull(nextThirdTestPost.getEditDate());
		assertEquals(RE + "First Test Topic", nextThirdTestPost.getMessage().getSubject());
		assertEquals("Third Test Post", nextThirdTestPost.getMessage().getText());
		assertEquals("root", nextThirdTestPost.getPoster().getUserId());
		assertEquals("First Test Topic", nextThirdTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextThirdTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextThirdTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextFourthTestPost = posts.get(8);
		Date createdNextFourthTestPostDate = nextFourthTestPost.getCreateDate();
		assertFalse(createdNextFourthTestPostDate.after(today));
		assertFalse(createdNextThirdTestPostDate.after(createdNextFourthTestPostDate));
		assertEquals(0, nextFourthTestPost.getEditCount());
		assertNull(nextFourthTestPost.getEditDate());
		assertEquals(RE + "First Test Topic", nextFourthTestPost.getMessage().getSubject());
		assertEquals("Fourth Test Post", nextFourthTestPost.getMessage().getText());
		assertEquals("root", nextFourthTestPost.getPoster().getUserId());
		assertEquals("First Test Topic", nextFourthTestPost.getTopic().getSubject());
		assertEquals("First Test Forum", nextFourthTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextFourthTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean thirdTestPost = posts.get(9);
		Date createdDate3 = thirdTestPost.getCreateDate();
		assertFalse(createdDate3.after(today));
		assertFalse(createdDate2.after(createdDate3));
		assertEquals(0, thirdTestPost.getEditCount());
		assertNull(thirdTestPost.getEditDate());
		assertEquals("Third Test Topic", thirdTestPost.getMessage().getSubject());
		assertEquals("Third Test Body", thirdTestPost.getMessage().getText());
		assertEquals("root", thirdTestPost.getPoster().getUserId());
		assertEquals("Third Test Topic", thirdTestPost.getTopic().getSubject());
		assertEquals("Second Test Forum", thirdTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", thirdTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextEightTestPost = posts.get(10);
		Date createdNextEightTestPostDate = nextEightTestPost.getCreateDate();
		assertFalse(createdNextEightTestPostDate.after(today));
		assertFalse(createdDate3.after(createdNextEightTestPostDate));
		assertEquals(0, nextEightTestPost.getEditCount());
		assertNull(nextEightTestPost.getEditDate());
		assertEquals(RE + "Third Test Topic", nextEightTestPost.getMessage().getSubject());
		assertEquals("Eight Test Post", nextEightTestPost.getMessage().getText());
		assertEquals("root", nextEightTestPost.getPoster().getUserId());
		assertEquals("Third Test Topic", nextEightTestPost.getTopic().getSubject());
		assertEquals("Second Test Forum", nextEightTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextEightTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextNinthTestPost = posts.get(11);
		Date createdNextNinthTestPostDate = nextNinthTestPost.getCreateDate();
		assertFalse(createdNextNinthTestPostDate.after(today));
		assertFalse(createdNextEightTestPostDate.after(createdNextNinthTestPostDate));
		assertEquals(0, nextNinthTestPost.getEditCount());
		assertNull(nextNinthTestPost.getEditDate());
		assertEquals(RE + "Third Test Topic", nextNinthTestPost.getMessage().getSubject());
		assertEquals("Ninth Test Post", nextNinthTestPost.getMessage().getText());
		assertEquals("root", nextNinthTestPost.getPoster().getUserId());
		assertEquals("Third Test Topic", nextNinthTestPost.getTopic().getSubject());
		assertEquals("Second Test Forum", nextNinthTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextNinthTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean fourthTestPost = posts.get(12);
		Date createdDate4 = fourthTestPost.getCreateDate();
		assertFalse(createdDate4.after(today));
		assertFalse(createdDate3.after(createdDate4));
		assertEquals(0, fourthTestPost.getEditCount());
		assertNull(fourthTestPost.getEditDate());
		assertEquals("Fourth Test Topic", fourthTestPost.getMessage().getSubject());
		assertEquals("Fourth Test Body", fourthTestPost.getMessage().getText());
		assertEquals("root", fourthTestPost.getPoster().getUserId());
		assertEquals("Fourth Test Topic", fourthTestPost.getTopic().getSubject());
		assertEquals("Second Test Forum", fourthTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", fourthTestPost.getTopic().getForum().getCategory().getTitle());

		PostBean nextTenTestPost = posts.get(13);
		Date createdNextTenTestPostDate = nextTenTestPost.getCreateDate();
		assertFalse(createdNextTenTestPostDate.after(today));
		assertFalse(createdDate4.after(createdNextTenTestPostDate));
		assertEquals(0, nextTenTestPost.getEditCount());
		assertNull(nextTenTestPost.getEditDate());
		assertEquals(RE + "Fourth Test Topic", nextTenTestPost.getMessage().getSubject());
		assertEquals("Ten Test Post", nextTenTestPost.getMessage().getText());
		assertEquals("root", nextTenTestPost.getPoster().getUserId());
		assertEquals("Fourth Test Topic", nextTenTestPost.getTopic().getSubject());
		assertEquals("Second Test Forum", nextTenTestPost.getTopic().getForum().getName());
		assertEquals("First Test Category", nextTenTestPost.getTopic().getForum().getCategory().getTitle());*/
	}

	@Test
	public void verifyPostProfileFromCategoryPage() {
	/*	CategoryBean category = new CategoryBean("First Test Category");
		goTo(driver, category);
		ForumBean forum = new ForumBean("Second Test Forum", "Second Test Description", category);
		PosterBean poster = getPoster(driver, forum);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 14);*/
	}

	@Test
	public void verifyPostProfileFromForumPage() {
	/*	ForumBean forum = new ForumBean("Second Test Forum", "Second Test Description",
				new CategoryBean("First Test Category"));
		goTo(driver, forum);
		TopicBean topic = new TopicBean(forum, "Third Test Topic");
		PosterBean poster = getPoster(driver, topic);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 14);*/
	}

	@Test
	public void verifyPostFromForumPageLastPost() {
/*		ForumBean forum = new ForumBean("Second Test Forum", "Second Test Description",
				new CategoryBean("First Test Category"));
		goTo(driver, forum);
		TopicBean topic = new TopicBean(forum, "Third Test Topic");
		PosterBean poster = getPosterLastPost(driver, topic);
		goTo(driver, forum);
		PostBean post = getLastPostOfCurrentForum(driver, topic);
		assertTrue(post != null);
		assertEquals(RE + "Third Test Topic", post.getMessage().getSubject());
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 14);*/
	}

	@Test
	public void verifyPostProfileFromTopicPage() {
	/*	goTo(driver, new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic"));
		PostBean post = new PostBean("Ninth Test Post");
		PosterBean poster = getPosterFromLink(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 14); */
	}

	@Test
	public void verifyPostProfileFromTopicPageButton() {
	/*	goTo(driver, new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"));
		PostBean post = new PostBean("Fourth Test Body");
		PosterBean poster = getPosterFromButton(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 14); */
	}

	@AfterClass
	public static void stop() {
		String message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "First Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Second Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Third Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Fourth Test Post"));
		assertTrue(message.equals(OK));
		/*message = removePost(driver, new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"),
				"Fifth Test with Truncation over 25 characters Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"), "Sixth Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver, new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"),
				"Seventh Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic"), "Eight Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic"), "Ninth Test Post"));
		assertTrue(message.equals(OK));
		message = removePost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Ten Test Post"));
		assertTrue(message.equals(OK));
		message = removeTopic(driver, new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
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
		message = removeForum(driver, new ForumBean("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new ForumBean("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));*/
	}
}
