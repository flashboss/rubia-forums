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
import static it.vige.rubia.search.DisplayAs.TOPICS;
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
import static it.vige.rubia.selenium.forum.action.SubscriptionForum.registerForum;
import static it.vige.rubia.selenium.forum.action.SubscriptionForum.unregisterForum;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllEditForumsRemoveForum;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsRemoveForum.viewAllForumsRemoveForum;
import static it.vige.rubia.selenium.search.action.ViewPageSearch.goTo;
import static it.vige.rubia.selenium.search.action.ViewPageSearch.reset;
import static it.vige.rubia.selenium.search.action.ViewPageTopicSearch.getLastPostOfCurrentForum;
import static it.vige.rubia.selenium.search.action.ViewPageTopicSearch.getPoster;
import static it.vige.rubia.selenium.search.action.ViewPageTopicSearch.getPosterLastPost;
import static it.vige.rubia.selenium.search.action.ViewPageTopicSearch.getTopic;
import static it.vige.rubia.selenium.search.action.ViewPageTopicSearch.getTopics;
import static it.vige.rubia.selenium.search.action.ViewPageTopicSearch.searchTopic;
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
public class SearchTopicTest {

	@Drone
	private static WebDriver driver;

	@BeforeClass
	public static void setUp() {
		driver.get(HOME_URL);
	/*	String message = createCategory(driver, new CategoryBean("First Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, new CategoryBean("Second Test Category"));
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		ForumBean forum = new ForumBean("First Test Forum", "First Test Description",
				new CategoryBean("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"),
						"First Test with a large subject name triing to truncate over the 25 character Topic",
						asList(new PostBean[] { new PostBean("First Test Body",
								asList(new AttachmentBean("first", "First Test File"),
										new AttachmentBean("second", "Second Test File"),
										new AttachmentBean("third", "Third Test File"))) }),
						NORMAL,
						new PollBean("First Test Question", asList(new PollOptionBean[] {
								new PollOptionBean("First Test Answer"), new PollOptionBean("Second Test Answer") }),
								4)));
		assertTrue(
				message.equals("First Test with a large subject name triing to truncate over the 25 character Topic"));
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
		forum = new ForumBean("Fifth Test with Truncation over 25 characters Forum",
				"Fifth Test with Truncation over 25 characters Description", new CategoryBean("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_4_MESSAGE));
		message = registerForum(driver, forum, EMAIL_NO_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Forum"));*/
	}

	@Test
	public void searchTopics() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<TopicBean> topics = searchTopic(driver, searchForumCriteria);
		Date today = new Date();
		assertTrue(topics != null);
		assertEquals(4, topics.size());
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(1, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(
				topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("First Test with a large s..."));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Second Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());
		assertEquals("Third Test Topic", topics.get(2).getSubject());
		assertEquals("root", topics.get(2).getPoster().getUserId());
		assertEquals(0, topics.get(2).getReplies());
		assertEquals(0, topics.get(2).getViewCount());
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(2).getPosts().size());
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject().startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(2).getPosts().get(0).getPoster().getUserId());
		assertEquals("Fourth Test Topic", topics.get(3).getSubject());
		assertEquals("root", topics.get(3).getPoster().getUserId());
		assertEquals(0, topics.get(3).getReplies());
		assertEquals(0, topics.get(3).getViewCount());
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(3).getPosts().size());
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject().startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(3).getPosts().get(0).getPoster().getUserId());

		goTo(driver);
		searchForumCriteria.setCategory("First Test Category");
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(4, topics.size());
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(1, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(
				topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("First Test with a large s..."));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Second Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());
		assertEquals("Third Test Topic", topics.get(2).getSubject());
		assertEquals("root", topics.get(2).getPoster().getUserId());
		assertEquals(0, topics.get(2).getReplies());
		assertEquals(0, topics.get(2).getViewCount());
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(2).getPosts().size());
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject().startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(2).getPosts().get(0).getPoster().getUserId());
		assertEquals("Fourth Test Topic", topics.get(3).getSubject());
		assertEquals("root", topics.get(3).getPoster().getUserId());
		assertEquals(0, topics.get(3).getReplies());
		assertEquals(0, topics.get(3).getViewCount());
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(3).getPosts().size());
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject().startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(3).getPosts().get(0).getPoster().getUserId());

		goTo(driver);
		searchForumCriteria.setCategory("Second Test Category");
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);

		goTo(driver);
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setForum("First Test Forum");
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(2, topics.size());
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(1, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(
				topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("First Test with a large s..."));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Second Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());

		goTo(driver);
		searchForumCriteria.setForum("Second Test Forum");
		topics = searchTopic(driver, searchForumCriteria);
		assertEquals(2, topics.size());
		assertEquals("Third Test Topic", topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(0, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("Third Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Fourth Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Fourth Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());

		ResourceBundle bundle = getBundle("ResourceJSF");
		goTo(driver);
		searchForumCriteria.setForum(null);
		searchForumCriteria.setTimePeriod(bundle.getString("period_7_Days"));
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(4, topics.size());
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(1, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(
				topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("First Test with a large s..."));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Second Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());
		assertEquals("Third Test Topic", topics.get(2).getSubject());
		assertEquals("root", topics.get(2).getPoster().getUserId());
		assertEquals(0, topics.get(2).getReplies());
		assertEquals(0, topics.get(2).getViewCount());
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(2).getPosts().size());
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject().startsWith("Third Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(2).getPosts().get(0).getPoster().getUserId());
		assertEquals("Fourth Test Topic", topics.get(3).getSubject());
		assertEquals("root", topics.get(3).getPoster().getUserId());
		assertEquals(0, topics.get(3).getReplies());
		assertEquals(0, topics.get(3).getViewCount());
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(3).getPosts().size());
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject().startsWith("Fourth Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(3).getPosts().get(0).getPoster().getUserId());

		goTo(driver);
		searchForumCriteria.setTimePeriod(null);
		searchForumCriteria.setSortBy(bundle.getString("Search_post_subject"));
		searchForumCriteria.setSortOrder(ASC.name());
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(4, topics.size());
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(1, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(
				topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("First Test with a large s..."));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Fourth Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Fourth Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());
		assertEquals("Second Test Topic", topics.get(2).getSubject());
		assertEquals("root", topics.get(2).getPoster().getUserId());
		assertEquals(0, topics.get(2).getReplies());
		assertEquals(0, topics.get(2).getViewCount());
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(2).getPosts().size());
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject().startsWith("Second Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(2).getPosts().get(0).getPoster().getUserId());
		assertEquals("Third Test Topic", topics.get(3).getSubject());
		assertEquals("root", topics.get(3).getPoster().getUserId());
		assertEquals(0, topics.get(3).getReplies());
		assertEquals(0, topics.get(3).getViewCount());
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(3).getPosts().size());
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject().startsWith("Third Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(3).getPosts().get(0).getPoster().getUserId());

		goTo(driver);
		searchForumCriteria.setSortOrder(DESC.name());
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics != null);
		assertEquals(4, topics.size());
		assertEquals("Third Test Topic", topics.get(0).getSubject());
		assertEquals("root", topics.get(0).getPoster().getUserId());
		assertEquals(0, topics.get(0).getReplies());
		assertEquals(0, topics.get(0).getViewCount());
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(0).getPosts().size());
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject().startsWith("Third Test Topic"));
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(0).getPosts().get(0).getPoster().getUserId());
		assertEquals("Second Test Topic", topics.get(1).getSubject());
		assertEquals("root", topics.get(1).getPoster().getUserId());
		assertEquals(0, topics.get(1).getReplies());
		assertEquals(0, topics.get(1).getViewCount());
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(1).getPosts().size());
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject().startsWith("Second Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(1).getPosts().get(0).getPoster().getUserId());
		assertEquals("Fourth Test Topic", topics.get(2).getSubject());
		assertEquals("root", topics.get(2).getPoster().getUserId());
		assertEquals(0, topics.get(2).getReplies());
		assertEquals(0, topics.get(2).getViewCount());
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(2).getPosts().size());
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject().startsWith("Fourth Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(2).getPosts().get(0).getPoster().getUserId());
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				topics.get(3).getSubject());
		assertEquals("root", topics.get(3).getPoster().getUserId());
		assertEquals(0, topics.get(3).getReplies());
		assertEquals(1, topics.get(3).getViewCount());
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(1, topics.get(3).getPosts().size());
		assertTrue(
				topics.get(3).getPosts().get(0).getMessage().getSubject().startsWith("First Test with a large s..."));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate().compareTo(today) < 0);
		assertEquals("root", topics.get(3).getPosts().get(0).getPoster().getUserId());

		goTo(driver);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setSearching(MSG.name());
		topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsNoFields() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		List<TopicBean> topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsWithReset() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor(null);
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("First");
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		reset(driver, searchForumCriteria);
		List<TopicBean> topics = getTopics(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void searchTopicsNoResults() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor(null);
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Firstaaaaa");
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<TopicBean> topics = searchTopic(driver, searchForumCriteria);
		assertTrue(topics == null);
	}

	@Test
	public void verifyPostFromTopicPage() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<TopicBean> topics = searchTopic(driver, searchForumCriteria);
		TopicBean topic = getTopic(driver, topics.get(3));
		assertTrue(topic != null);
		assertTrue(topic.getPosts() != null);
		assertEquals("Fourth Test Topic", topic.getSubject());
		assertEquals("root", topic.getPoster().getUserId());
		assertEquals(1, topic.getPosts().size());
		assertEquals("Fourth Test Topic", topic.getPosts().get(0).getMessage().getSubject());
		assertEquals("Fourth Test Body", topic.getPosts().get(0).getMessage().getText());
		assertEquals("Fourth Test Question", topic.getPoll().getTitle());
	}

	@Test
	public void verifyPostProfileFromTopicPage() {
		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<TopicBean> topics = searchTopic(driver, searchForumCriteria);
		TopicBean topic = topics.get(2);
		PosterBean poster = getPoster(driver, topic);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 4);
	}

	@Test
	public void verifyPostFromTopicPageLastPost() {
/*		goTo(driver);
		SearchCriteria searchForumCriteria = new SearchCriteria();
		searchForumCriteria.setAuthor("root");
		searchForumCriteria.setCategory(null);
		searchForumCriteria.setDisplayAs(TOPICS.name());
		searchForumCriteria.setForum(null);
		searchForumCriteria.setKeywords("Topic");
		searchForumCriteria.setSearching(null);
		searchForumCriteria.setSortBy(null);
		searchForumCriteria.setSortOrder(null);
		searchForumCriteria.setTimePeriod(null);
		List<TopicBean> topics = searchTopic(driver, searchForumCriteria);
		PosterBean poster = getPosterLastPost(driver, topics.get(0));
		goTo(driver);
		topics = searchTopic(driver, searchForumCriteria);
		PostBean post = getLastPostOfCurrentForum(driver, topics.get(0));
		assertTrue(post != null);
		assertEquals("First Test with a large subject name triing to truncate over the 25 character Topic",
				post.getMessage().getSubject());
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 4);*/
	}

	@AfterClass
	public static void stop() {
	/*	String message = removeTopic(driver,
				new TopicBean(new ForumBean("First Test Forum"),
						"First Test with a large subject name triing to truncate over the 25 character Topic",
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
		message = removeForum(driver, forum, "Second Test Forum");
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
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));*/
	}
}
