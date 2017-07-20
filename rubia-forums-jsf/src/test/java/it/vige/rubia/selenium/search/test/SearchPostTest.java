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

import static it.vige.rubia.model.TopicType.ADVICE;
import static it.vige.rubia.model.TopicType.IMPORTANT;
import static it.vige.rubia.model.TopicType.NORMAL;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.search.SearchCriteria;

@RunWith(Arquillian.class)
@RunAsClient
public class SearchPostTest {

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
		message = registerTopic(driver, new Topic("First Test Topic"), EMAIL_LINKED_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Second Test Topic"), EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Third Test Topic"), EMAIL_NO_NOTIFICATION, CONFIRM);
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
		message = registerTopic(driver, new Topic("Fourth Test Topic"), EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fourth Test Topic"));
		message = createForum(driver,
				new Forum("Third Test Forum", "Third Test Description", new Category("Second Test Category")));
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		message = createTopic(driver,
				new Topic(new Forum("Third Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
						asList(new Post[] { new Post("Fifth Test with Truncation over 25 characters Body",
								asList(new Attachment("seventh", "Seventh Test File"),
										new Attachment("eight", "Eight Test File"),
										new Attachment("ninth", "Ninth Test File"))) }),
						IMPORTANT, new Poll("Third Test Question", asList(new PollOption[] {
								new PollOption("Seventh Test Answer"), new PollOption("Eight Test Answer") }), 8)));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
		message = registerTopic(driver, new Topic("Fifth Test with Truncation over 25 characters Topic"),
				EMAIL_EMBEDED_NOTIFICATION, CONFIRM);
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Topic"));
		message = createTopic(driver, new Topic(new Forum("Third Test Forum"), "Sixth Test Topic",
				asList(new Post[] { new Post("Sixth Test Body",
						asList(new Attachment("ten", "Ten Test File"), new Attachment("eleven", "Eleven Test File"),
								new Attachment("twelve", "Twelve Test File"))) }),
				IMPORTANT,
				new Poll("Fourth Test Question", asList(
						new PollOption[] { new PollOption("Ninth Test Answer"), new PollOption("Ten Test Answer") }),
						8)));
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
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
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
		List<Post> posts = searchPost(driver, searchTopicCriteria);
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
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		reset(driver, searchTopicCriteria);
		List<Post> posts = getPosts(driver, searchTopicCriteria);
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
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
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
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		Post post = posts.get(2);
		Poster poster = getPosterFromLink(driver, post);
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
		searchTopicCriteria.setPageNumber(0);
		searchTopicCriteria.setPageSize(0);
		searchTopicCriteria.setSearching(null);
		searchTopicCriteria.setSortBy(null);
		searchTopicCriteria.setSortOrder(null);
		searchTopicCriteria.setTimePeriod(null);
		List<Post> posts = searchPost(driver, searchTopicCriteria);
		Post post = posts.get(3);
		Poster poster = getPosterFromButton(driver, post);
		assertTrue(poster != null);
		assertEquals("root", poster.getUserId());
		assertTrue(poster.getPostCount() >= 6);
	}

	@AfterClass
	public static void stop() {
		Topic topic = new Topic(new Forum("First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body") }));
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
		topic = new Topic(new Forum("Third Test Forum"), "Fifth Test with Truncation over 25 characters Topic",
				asList(new Post[] { new Post("Fifth Test with Truncation over 25 characters Body") }));
		message = viewAllTopicsRemoveTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Third Test Forum"), "Sixth Test Topic",
				asList(new Post[] { new Post("Sixth Test Body") }));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeForum(driver, new Forum("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeForum(driver, new Forum("Third Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_2_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}

}
