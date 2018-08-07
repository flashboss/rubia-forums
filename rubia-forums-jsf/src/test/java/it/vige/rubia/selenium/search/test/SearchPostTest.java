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
package it.vige.rubia.selenium.search.test;

import static it.vige.rubia.dto.TopicType.ADVICE;
import static it.vige.rubia.dto.TopicType.IMPORTANT;
import static it.vige.rubia.dto.TopicType.NORMAL;
import static it.vige.rubia.properties.NotificationType.EMAIL_EMBEDED_NOTIFICATION;
import static it.vige.rubia.properties.NotificationType.EMAIL_LINKED_NOTIFICATION;
import static it.vige.rubia.properties.NotificationType.EMAIL_NO_NOTIFICATION;
import static it.vige.rubia.properties.OperationType.CONFIRM;
import static it.vige.rubia.search.DisplayAs.POSTS;
import static it.vige.rubia.search.Searching.MSG;
import static it.vige.rubia.search.SortOrder.ASC;
import static it.vige.rubia.search.SortOrder.DESC;
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
import static it.vige.rubia.selenium.forum.action.SubscriptionTopic.registerTopic;
import static it.vige.rubia.selenium.forum.action.SubscriptionTopic.unregisterTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopicsRemoveTopic.viewAllTopicsRemoveTopic;
import static it.vige.rubia.selenium.search.action.ViewPagePostSearch.getPosterFromButton;
import static it.vige.rubia.selenium.search.action.ViewPagePostSearch.getPosterFromLink;
import static it.vige.rubia.selenium.search.action.ViewPagePostSearch.getPosts;
import static it.vige.rubia.selenium.search.action.ViewPagePostSearch.searchPost;
import static it.vige.rubia.selenium.search.action.ViewPageSearch.goTo;
import static it.vige.rubia.selenium.search.action.ViewPageSearch.reset;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
import it.vige.rubia.search.SearchCriteria;

@RunWith(Arquillian.class)
@RunAsClient
public class SearchPostTest {

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
	public void searchPosts() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor("root");
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Body");
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<PostBean> posts = searchPost(driver, searchTopicCriteria);
		Date today = new Date();
		assertTrue(posts != null);
		assertEquals(6, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());
		assertEquals("root", posts.get(2).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(2).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(2).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(2).getAttachments().size());
		assertEquals("root", posts.get(3).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(3).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(3).getMessage().getText());
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(3).getAttachments().size());
		assertEquals("root", posts.get(4).getPoster().getUserId());
		assertEquals("Fifth Test with Truncation over 25 characters Topic", posts.get(4).getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Body", posts.get(4).getMessage().getText());
		assertTrue(posts.get(4).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(4).getAttachments().size());
		assertEquals("root", posts.get(5).getPoster().getUserId());
		assertEquals("Sixth Test Topic", posts.get(5).getMessage().getSubject());
		assertEquals("Sixth Test Body", posts.get(5).getMessage().getText());
		assertTrue(posts.get(5).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(5).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setCategory("First Test Category");
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(4, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());
		assertEquals("root", posts.get(2).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(2).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(2).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(2).getAttachments().size());
		assertEquals("root", posts.get(3).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(3).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(3).getMessage().getText());
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(3).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setCategory("Second Test Category");
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(2, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("Fifth Test with Truncation over 25 characters Topic", posts.get(0).getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Sixth Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Sixth Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setForum("First Test Forum");
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(2, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setForum("Second Test Forum");
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(2, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());

		ResourceBundle bundle = getBundle("ResourceJSF");
		goTo(driver);
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setTimePeriod(bundle.getString("period_7_Days"));
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(6, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());
		assertEquals("root", posts.get(2).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(2).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(2).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(2).getAttachments().size());
		assertEquals("root", posts.get(3).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(3).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(3).getMessage().getText());
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(3).getAttachments().size());
		assertEquals("root", posts.get(4).getPoster().getUserId());
		assertEquals("Fifth Test with Truncation over 25 characters Topic", posts.get(4).getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Body", posts.get(4).getMessage().getText());
		assertTrue(posts.get(4).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(4).getAttachments().size());
		assertEquals("root", posts.get(5).getPoster().getUserId());
		assertEquals("Sixth Test Topic", posts.get(5).getMessage().getSubject());
		assertEquals("Sixth Test Body", posts.get(5).getMessage().getText());
		assertTrue(posts.get(5).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(5).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setTimePeriod(null);
		searchTopicCriteria.setSortBy(bundle.getString("Search_post_subject"));
		searchTopicCriteria.setSortOrder(ASC.name());
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(6, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("Fifth Test with Truncation over 25 characters Topic", posts.get(0).getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());
		assertEquals("root", posts.get(2).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(2).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(2).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(2).getAttachments().size());
		assertEquals("root", posts.get(3).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(3).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(3).getMessage().getText());
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(3).getAttachments().size());
		assertEquals("root", posts.get(4).getPoster().getUserId());
		assertEquals("Sixth Test Topic", posts.get(4).getMessage().getSubject());
		assertEquals("Sixth Test Body", posts.get(4).getMessage().getText());
		assertTrue(posts.get(4).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(4).getAttachments().size());
		assertEquals("root", posts.get(5).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(5).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(5).getMessage().getText());
		assertTrue(posts.get(5).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(5).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setSortOrder(DESC.name());
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(6, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Sixth Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Sixth Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());
		assertEquals("root", posts.get(2).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(2).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(2).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(2).getAttachments().size());
		assertEquals("root", posts.get(3).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(3).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(3).getMessage().getText());
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(3).getAttachments().size());
		assertEquals("root", posts.get(4).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(4).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(4).getMessage().getText());
		assertTrue(posts.get(4).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(4).getAttachments().size());
		assertEquals("root", posts.get(5).getPoster().getUserId());
		assertEquals("Fifth Test with Truncation over 25 characters Topic", posts.get(5).getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Body", posts.get(5).getMessage().getText());
		assertTrue(posts.get(5).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(5).getAttachments().size());

		goTo(driver);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setSearching(MSG.name());
		posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts != null);
		assertEquals(6, posts.size());
		assertEquals("root", posts.get(0).getPoster().getUserId());
		assertEquals("First Test Topic", posts.get(0).getMessage().getSubject());
		assertEquals("First Test Body", posts.get(0).getMessage().getText());
		assertTrue(posts.get(0).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(0).getAttachments().size());
		assertEquals("root", posts.get(1).getPoster().getUserId());
		assertEquals("Second Test Topic", posts.get(1).getMessage().getSubject());
		assertEquals("Second Test Body", posts.get(1).getMessage().getText());
		assertTrue(posts.get(1).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(1).getAttachments().size());
		assertEquals("root", posts.get(2).getPoster().getUserId());
		assertEquals("Third Test Topic", posts.get(2).getMessage().getSubject());
		assertEquals("Third Test Body", posts.get(2).getMessage().getText());
		assertTrue(posts.get(2).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(2).getAttachments().size());
		assertEquals("root", posts.get(3).getPoster().getUserId());
		assertEquals("Fourth Test Topic", posts.get(3).getMessage().getSubject());
		assertEquals("Fourth Test Body", posts.get(3).getMessage().getText());
		assertTrue(posts.get(3).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(3).getAttachments().size());
		assertEquals("root", posts.get(4).getPoster().getUserId());
		assertEquals("Fifth Test with Truncation over 25 characters Topic", posts.get(4).getMessage().getSubject());
		assertEquals("Fifth Test with Truncation over 25 characters Body", posts.get(4).getMessage().getText());
		assertTrue(posts.get(4).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(4).getAttachments().size());
		assertEquals("root", posts.get(5).getPoster().getUserId());
		assertEquals("Sixth Test Topic", posts.get(5).getMessage().getSubject());
		assertEquals("Sixth Test Body", posts.get(5).getMessage().getText());
		assertTrue(posts.get(5).getCreateDate().compareTo(today) < 0);
		assertEquals(3, posts.get(5).getAttachments().size());
	}

	@Test
	public void searchPostsNoFields() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		List<PostBean> posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts == null);
	}

	@Test
	public void searchPostsWithReset() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor(null);
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("First");
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		reset(driver, searchTopicCriteria);
		List<PostBean> posts = getPosts(driver, searchTopicCriteria);
		assertTrue(posts == null);
	}

	@Test
	public void searchPostsNoResults() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor(null);
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Firstaaaaaa");
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<PostBean> posts = searchPost(driver, searchTopicCriteria);
		assertTrue(posts == null);
	}

	@Test
	public void verifyPostProfileFromTopicPage() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor("root");
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Body");
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<PostBean> posts = searchPost(driver, searchTopicCriteria);
		PostBean post = posts.get(2);
		PosterBean poster = getPosterFromLink(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 6);
	}

	@Test
	public void verifyPostProfileFromTopicPageButton() {
		goTo(driver);
		SearchCriteria searchTopicCriteria = new SearchCriteria();
		searchTopicCriteria.setAuthor("root");
		searchTopicCriteria.setCategory(null);
		searchTopicCriteria.setDisplayAs(POSTS.name());
		searchTopicCriteria.setForum(null);
		searchTopicCriteria.setKeywords("Body");
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<PostBean> posts = searchPost(driver, searchTopicCriteria);
		PostBean post = posts.get(3);
		PosterBean poster = getPosterFromButton(driver, post);
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
