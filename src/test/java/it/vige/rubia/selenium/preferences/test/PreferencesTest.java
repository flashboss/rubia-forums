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
package it.vige.rubia.selenium.preferences.test;

import static it.vige.rubia.model.TopicType.ADVICE;
import static it.vige.rubia.model.TopicType.IMPORTANT;
import static it.vige.rubia.model.TopicType.NORMAL;
import static it.vige.rubia.selenium.Constants.OK;
import static it.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static it.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static it.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
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
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.forum.action.VerifyForum.getForumsOfCategories;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopicsOfForums;
import static it.vige.rubia.selenium.preferences.action.ViewPageForumPreferences.reset;
import static it.vige.rubia.selenium.preferences.action.ViewPageForumPreferences.submit;
import static it.vige.rubia.selenium.preferences.action.ViewPagePreferences.addKeys;
import static it.vige.rubia.selenium.preferences.action.ViewPagePreferences.goTo;
import static it.vige.rubia.selenium.summary.action.ViewSummary.getDetail;
import static it.vige.rubia.selenium.summary.action.ViewSummary.viewSize;
import static it.vige.rubia.selenium.summary.action.ViewSummary.viewSummary;
import static it.vige.rubia.ui.Constants.RE;
import static it.vige.rubia.ui.view.SummaryMode.BLOCK_TOPICS_MODE_HOT_TOPICS;
import static it.vige.rubia.ui.view.SummaryMode.BLOCK_TOPICS_MODE_LATEST_POSTS;
import static it.vige.rubia.ui.view.SummaryMode.BLOCK_TOPICS_MODE_MOST_VIEWED;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;
import it.vige.rubia.ui.action.PreferenceController;

import java.util.Date;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class PreferencesTest {

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
		Forum forum = new Forum("First Test Forum", "First Test Description",
				new Category("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createTopic(driver, new Topic(forum, "First Test Topic",
				asList(new Post[] { new Post("First Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("First Test Topic"));
		message = createTopic(driver, new Topic(forum, "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body") }), IMPORTANT,
				null));
		assertTrue(message.equals("Second Test Topic"));
		message = createTopic(driver, new Topic(forum, "Ten Test Topic",
				asList(new Post[] { new Post("Ten Test Body") }), IMPORTANT,
				null));
		assertTrue(message.equals("Ten Test Topic"));
		message = createTopic(driver, new Topic(forum, "Eleven Test Topic",
				asList(new Post[] { new Post("Eleven Test Body") }), IMPORTANT,
				null));
		assertTrue(message.equals("Eleven Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twelve Test Topic",
				asList(new Post[] { new Post("Twelve Test Body") }), IMPORTANT,
				null));
		assertTrue(message.equals("Twelve Test Topic"));
		message = createTopic(driver, new Topic(forum, "Thirteen Test Topic",
				asList(new Post[] { new Post("Thirteen Test Body") }),
				IMPORTANT, null));
		assertTrue(message.equals("Thirteen Test Topic"));
		message = createTopic(driver, new Topic(forum, "Fourteen Test Topic",
				asList(new Post[] { new Post("Fourteen Test Body") }),
				IMPORTANT, null));
		assertTrue(message.equals("Fourteen Test Topic"));
		message = createTopic(driver, new Topic(forum, "Fifteen Test Topic",
				asList(new Post[] { new Post("Fifteen Test Body") }),
				IMPORTANT, null));
		assertTrue(message.equals("Fifteen Test Topic"));
		message = createTopic(driver, new Topic(forum, "Sixteen Test Topic",
				asList(new Post[] { new Post("Sixteen Test Body") }),
				IMPORTANT, null));
		assertTrue(message.equals("Sixteen Test Topic"));
		message = createTopic(driver, new Topic(forum, "Seventeen Test Topic",
				asList(new Post[] { new Post("Seventeen Test Body") }),
				IMPORTANT, null));
		assertTrue(message.equals("Seventeen Test Topic"));
		message = createTopic(driver, new Topic(forum, "Eighteen Test Topic",
				asList(new Post[] { new Post("Eighteen Test Body") }),
				IMPORTANT, null));
		assertTrue(message.equals("Eighteen Test Topic"));
		message = createTopic(
				driver,
				new Topic(
						forum,
						"Ninteen Test Topic",
						asList(new Post[] {
								new Post("Ninteen Test Body"),
								new Post(
										"<ul><li>Ninteen4</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen5</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen6</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen7</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen8</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen9</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen10</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen11</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen12</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen13</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen14</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen15</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen16</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen17</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen18</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen19</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen20</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen21</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen22</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen23</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen24</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen25</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen26</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen27</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen28</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen29</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen30</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen31</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen32</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen33</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen34</li><li>Test Body</li></ul>"),
								new Post(
										"<ul><li>Ninteen35</li><li>Test Body</li></ul>") }),
						IMPORTANT, null));
		assertTrue(message.equals("Ninteen Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twelve Test Topic",
				asList(new Post[] { new Post("Twelve Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("Twelve Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twentyone Test Topic",
				asList(new Post[] { new Post("Twentyone Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("Twentyone Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twentytwo Test Topic",
				asList(new Post[] { new Post("Twentytwo Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("Twentytwo Test Topic"));
		message = createTopic(driver, new Topic(forum,
				"Twentythree Test Topic", asList(new Post[] { new Post(
						"Twentythree Test Body") }), NORMAL, null));
		assertTrue(message.equals("Twentythree Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twentyfour Test Topic",
				asList(new Post[] { new Post("Twentyfour Test Body") }),
				NORMAL, null));
		assertTrue(message.equals("Twentyfour Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twentyfive Test Topic",
				asList(new Post[] { new Post("Twentyfive Test Body") }),
				NORMAL, null));
		assertTrue(message.equals("Twentyfive Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twentysix Test Topic",
				asList(new Post[] { new Post("Twentysix Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("Twentysix Test Topic"));
		message = createTopic(driver, new Topic(forum,
				"Twentyseven Test Topic", asList(new Post[] { new Post(
						"Twentyseven Test Body") }), NORMAL, null));
		assertTrue(message.equals("Twentyseven Test Topic"));
		message = createTopic(driver, new Topic(forum,
				"Twentyeight Test Topic", asList(new Post[] { new Post(
						"Twentyeight Test Body") }), NORMAL, null));
		assertTrue(message.equals("Twentyeight Test Topic"));
		message = createTopic(driver, new Topic(forum, "Twentynine Test Topic",
				asList(new Post[] { new Post("Twentynine Test Body") }),
				NORMAL, null));
		assertTrue(message.equals("Twentynine Test Topic"));
		message = createTopic(driver, new Topic(forum, "Thirty Test Topic",
				asList(new Post[] { new Post("Thirty Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("Thirty Test Topic"));
		message = createTopic(driver, new Topic(forum, "Thirtyone Test Topic",
				asList(new Post[] { new Post("Thirtyone Test Body") }), NORMAL,
				null));
		assertTrue(message.equals("Thirtyone Test Topic"));
		forum = new Forum("Second Test Forum", "Second Test Description",
				new Category("First Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver, new Topic(forum, "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body") }), ADVICE,
				null));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver, new Topic(forum, "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body") }), IMPORTANT,
				null));
		assertTrue(message.equals("Fourth Test Topic"));
		forum = new Forum("Third Test Forum", "Third Test Description",
				new Category("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_2_MESSAGE));
		forum = new Forum("Fourth Test Forum", "Fourth Test Description",
				new Category("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_3_MESSAGE));
		forum = new Forum("Fifth Test Forum", "Fifth Test Description",
				new Category("Second Test Category"));
		message = createForum(driver, forum);
		assertTrue(message.equals(CREATED_FORUM_4_MESSAGE));
	}

	@Test
	public void startTests() {
		scenary1();
		scenary2();
		scenary3();
		scenary4();
		submitsWithReset();
	}

	public void scenary1() {
		Category firstTestCategory = new Category("First Test Category");
		Category secondTestCategory = new Category("Second Test Category");
		goTo(driver);
		PreferenceController submitCriteria = new PreferenceController();
		submitCriteria.setAlwaysAddSignature(true);
		submitCriteria.setAlwaysAllowHtml(true);
		submitCriteria.setNotifyOnReply(true);
		submitCriteria.setPostOrder("descending");
		submitCriteria.setPostsPerTopic(25);
		submitCriteria.setSignature("My New Signature");
		submitCriteria.setSummaryMode(BLOCK_TOPICS_MODE_HOT_TOPICS);
		submitCriteria.setSummaryTopicDays(2);
		submitCriteria.setSummaryTopicLimit(3);
		submitCriteria.setSummaryTopicReplies(4);
		submitCriteria.setTopicsPerForum(5);
		submit(driver, submitCriteria);
		List<Topic> summaryTopics = viewSummary(driver);
		int sizeSummaryTopics = viewSize(driver);
		Topic summaryTopicDetail = getDetail(driver, "Ninteen Test Topic");
		assertTrue(summaryTopics != null);
		assertEquals(summaryTopics.size(), 1);
		assertEquals(sizeSummaryTopics, 1);
		assertEquals(summaryTopics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(summaryTopicDetail.getSubject(), "Ninteen Test Topic");
		Topic newTopic = new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null);
		String message = createTopic(driver, newTopic);
		assertTrue(message.equals("Thirtytwo Test Topic"));
		List<Forum> forums = getForumsOfCategories(driver, firstTestCategory,
				secondTestCategory);
		Date today = new Date();
		List<Topic> topics = getTopicsOfForums(driver,
				forums.toArray(new Forum[0]));
		assertTrue(topics != null);
		assertEquals(topics.size(), 19);
		assertEquals(topics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 32);
		assertEquals(topics.get(0).getViewCount(), 98);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 25);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(0).getMessage().getText(),
				"<ul><li>Ninteen35</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(1).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(1).getMessage().getText(),
				"<ul><li>Ninteen34</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(1).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(1).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(2).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(2).getMessage().getText(),
				"<ul><li>Ninteen33</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(2).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(2).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(3).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(3).getMessage().getText(),
				"<ul><li>Ninteen32</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(3).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(3).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(4).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(4).getMessage().getText(),
				"<ul><li>Ninteen31</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(4).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(4).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(5).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(5).getMessage().getText(),
				"<ul><li>Ninteen30</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(5).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(5).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(6).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(6).getMessage().getText(),
				"<ul><li>Ninteen29</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(6).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(6).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(7).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(7).getMessage().getText(),
				"<ul><li>Ninteen28</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(7).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(7).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(8).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(8).getMessage().getText(),
				"<ul><li>Ninteen27</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(8).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(8).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(9).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(9).getMessage().getText(),
				"<ul><li>Ninteen26</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(9).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(9).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(10).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(10).getMessage().getText(),
				"<ul><li>Ninteen25</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(10).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(10).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(11).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(11).getMessage().getText(),
				"<ul><li>Ninteen24</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(11).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(11).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(12).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(12).getMessage().getText(),
				"<ul><li>Ninteen23</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(12).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(12).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(13).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(13).getMessage().getText(),
				"<ul><li>Ninteen22</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(13).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(13).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(14).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(14).getMessage().getText(),
				"<ul><li>Ninteen21</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(14).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(14).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(15).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(15).getMessage().getText(),
				"<ul><li>Ninteen20</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(15).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(15).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(16).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(16).getMessage().getText(),
				"<ul><li>Ninteen19</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(16).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(16).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(17).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(17).getMessage().getText(),
				"<ul><li>Ninteen18</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(17).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(17).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(18).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(18).getMessage().getText(),
				"<ul><li>Ninteen17</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(18).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(18).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(19).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(19).getMessage().getText(),
				"<ul><li>Ninteen16</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(19).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(19).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(20).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(20).getMessage().getText(),
				"<ul><li>Ninteen15</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(20).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(20).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(21).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(21).getMessage().getText(),
				"<ul><li>Ninteen14</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(21).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(21).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(22).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(22).getMessage().getText(),
				"<ul><li>Ninteen13</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(22).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(22).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(23).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(23).getMessage().getText(),
				"<ul><li>Ninteen12</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(23).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(23).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(24).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(24).getMessage().getText(),
				"<ul><li>Ninteen11</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(24).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(24).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Eighteen Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 0);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eighteen Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Seventeen Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 0);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Seventeen Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Sixteen Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 0);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Sixteen Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(4).getSubject(), "Fifteen Test Topic");
		assertEquals(topics.get(4).getPoster().getUserId(), "root");
		assertEquals(topics.get(4).getReplies(), 0);
		assertEquals(topics.get(4).getViewCount(), 0);
		assertTrue(topics.get(4).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().size(), 1);
		assertTrue(topics.get(4).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fifteen Test Topic"));
		assertTrue(topics.get(4).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(5).getSubject(), "Fourteen Test Topic");
		assertEquals(topics.get(5).getPoster().getUserId(), "root");
		assertEquals(topics.get(5).getReplies(), 0);
		assertEquals(topics.get(5).getViewCount(), 0);
		assertTrue(topics.get(5).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().size(), 1);
		assertTrue(topics.get(5).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourteen Test Topic"));
		assertTrue(topics.get(5).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(6).getSubject(), "Thirteen Test Topic");
		assertEquals(topics.get(6).getPoster().getUserId(), "root");
		assertEquals(topics.get(6).getReplies(), 0);
		assertEquals(topics.get(6).getViewCount(), 0);
		assertTrue(topics.get(6).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().size(), 1);
		assertTrue(topics.get(6).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirteen Test Topic"));
		assertTrue(topics.get(6).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(7).getSubject(), "Twelve Test Topic");
		assertEquals(topics.get(7).getPoster().getUserId(), "root");
		assertEquals(topics.get(7).getReplies(), 0);
		assertEquals(topics.get(7).getViewCount(), 0);
		assertTrue(topics.get(7).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().size(), 1);
		assertTrue(topics.get(7).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twelve Test Topic"));
		assertTrue(topics.get(7).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(8).getSubject(), "Eleven Test Topic");
		assertEquals(topics.get(8).getPoster().getUserId(), "root");
		assertEquals(topics.get(8).getReplies(), 0);
		assertEquals(topics.get(8).getViewCount(), 0);
		assertTrue(topics.get(8).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().size(), 1);
		assertTrue(topics.get(8).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eleven Test Topic"));
		assertTrue(topics.get(8).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(9).getSubject(), "Ten Test Topic");
		assertEquals(topics.get(9).getPoster().getUserId(), "root");
		assertEquals(topics.get(9).getReplies(), 0);
		assertEquals(topics.get(9).getViewCount(), 0);
		assertTrue(topics.get(9).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().size(), 1);
		assertTrue(topics.get(9).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ten Test Topic"));
		assertTrue(topics.get(9).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(10).getSubject(), "Second Test Topic");
		assertEquals(topics.get(10).getPoster().getUserId(), "root");
		assertEquals(topics.get(10).getReplies(), 0);
		assertEquals(topics.get(10).getViewCount(), 0);
		assertTrue(topics.get(10).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().size(), 1);
		assertTrue(topics.get(10).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(10).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(11).getSubject(), "Thirtyone Test Topic");
		assertEquals(topics.get(11).getPoster().getUserId(), "root");
		assertEquals(topics.get(11).getReplies(), 0);
		assertEquals(topics.get(11).getViewCount(), 0);
		assertTrue(topics.get(11).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().size(), 1);
		assertTrue(topics.get(11).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtyone Test Topic"));
		assertTrue(topics.get(11).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(12).getSubject(), "Thirty Test Topic");
		assertEquals(topics.get(12).getPoster().getUserId(), "root");
		assertEquals(topics.get(12).getReplies(), 0);
		assertEquals(topics.get(12).getViewCount(), 0);
		assertTrue(topics.get(12).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().size(), 1);
		assertTrue(topics.get(12).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirty Test Topic"));
		assertTrue(topics.get(12).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(13).getSubject(), "Twentynine Test Topic");
		assertEquals(topics.get(13).getPoster().getUserId(), "root");
		assertEquals(topics.get(13).getReplies(), 0);
		assertEquals(topics.get(13).getViewCount(), 0);
		assertTrue(topics.get(13).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().size(), 1);
		assertTrue(topics.get(13).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentynine Test Topic"));
		assertTrue(topics.get(13).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(14).getSubject(), "Twentyeight Test Topic");
		assertEquals(topics.get(14).getPoster().getUserId(), "root");
		assertEquals(topics.get(14).getReplies(), 0);
		assertEquals(topics.get(14).getViewCount(), 0);
		assertTrue(topics.get(14).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().size(), 1);
		assertTrue(topics.get(14).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyeight Test Topic"));
		assertTrue(topics.get(14).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(15).getSubject(), "Twentyseven Test Topic");
		assertEquals(topics.get(15).getPoster().getUserId(), "root");
		assertEquals(topics.get(15).getReplies(), 0);
		assertEquals(topics.get(15).getViewCount(), 0);
		assertTrue(topics.get(15).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().size(), 1);
		assertTrue(topics.get(15).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyseven Test Topic"));
		assertTrue(topics.get(15).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(16).getSubject(), "Third Test Topic");
		assertEquals(topics.get(16).getPoster().getUserId(), "root");
		assertEquals(topics.get(16).getReplies(), 0);
		assertEquals(topics.get(16).getViewCount(), 0);
		assertTrue(topics.get(16).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().size(), 1);
		assertTrue(topics.get(16).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(16).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(17).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(17).getPoster().getUserId(), "root");
		assertEquals(topics.get(17).getReplies(), 0);
		assertEquals(topics.get(17).getViewCount(), 0);
		assertTrue(topics.get(17).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().size(), 1);
		assertTrue(topics.get(17).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(17).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(18).getSubject(), "Thirtytwo Test Topic");
		assertEquals(topics.get(18).getPoster().getUserId(), "root");
		assertEquals(topics.get(18).getReplies(), 0);
		assertEquals(topics.get(18).getViewCount(), 0);
		assertTrue(topics.get(18).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().size(), 1);
		assertTrue(topics.get(18).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtytwo Test Topic"));
		assertTrue(topics.get(18).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().get(0).getPoster().getUserId(),
				"root");
		message = removeTopic(driver, new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null));
		assertTrue(message.equals(OK));
	}

	public void scenary2() {
		Category firstTestCategory = new Category("First Test Category");
		Category secondTestCategory = new Category("Second Test Category");
		goTo(driver);
		PreferenceController submitCriteria = new PreferenceController();
		submitCriteria.setAlwaysAddSignature(false);
		submitCriteria.setSummaryTopicLimit(5);
		submitCriteria.setSummaryTopicReplies(5);
		submitCriteria.setSummaryTopicDays(1);
		submitCriteria.setNotifyOnReply(false);
		submitCriteria.setSummaryMode(BLOCK_TOPICS_MODE_MOST_VIEWED);
		submitCriteria.setAlwaysAllowHtml(false);
		submitCriteria.setSignature("My Old Signature");
		submitCriteria.setDateFormat("d-MM-yyyy");
		submit(driver, submitCriteria);
		List<Topic> summaryTopics = viewSummary(driver);
		int sizeSummaryTopics = viewSize(driver);
		Topic summaryTopicDetail = getDetail(driver, "Ninteen Test Topic");
		assertTrue(summaryTopics != null);
		assertEquals(summaryTopics.size(), 5);
		assertEquals(sizeSummaryTopics, 5);
		assertEquals(summaryTopics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(summaryTopicDetail.getSubject(), "Ninteen Test Topic");
		Topic newTopic = new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null);
		String message = createTopic(driver, newTopic);
		assertTrue(message.equals("Thirtytwo Test Topic"));
		List<Forum> forums = getForumsOfCategories(driver, firstTestCategory,
				secondTestCategory);
		Date today = new Date();
		List<Topic> topics = getTopicsOfForums(driver,
				forums.toArray(new Forum[0]));
		assertTrue(topics != null);
		assertEquals(topics.size(), 24);
		assertEquals(topics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 32);
		assertEquals(topics.get(0).getViewCount(), 102);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 15);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(0).getMessage().getText(),
				"Ninteen Test Body");
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(1).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(1).getMessage().getText(),
				"<ul><li>Ninteen4</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(1).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(1).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(2).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(2).getMessage().getText(),
				"<ul><li>Ninteen5</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(2).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(2).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(3).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(3).getMessage().getText(),
				"<ul><li>Ninteen6</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(3).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(3).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(4).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(4).getMessage().getText(),
				"<ul><li>Ninteen7</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(4).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(4).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(5).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(5).getMessage().getText(),
				"<ul><li>Ninteen8</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(5).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(5).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(6).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(6).getMessage().getText(),
				"<ul><li>Ninteen9</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(6).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(6).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(7).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(7).getMessage().getText(),
				"<ul><li>Ninteen10</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(7).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(7).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(8).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(8).getMessage().getText(),
				"<ul><li>Ninteen11</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(8).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(8).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(9).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(9).getMessage().getText(),
				"<ul><li>Ninteen12</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(9).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(9).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(10).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(10).getMessage().getText(),
				"<ul><li>Ninteen13</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(10).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(10).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(11).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(11).getMessage().getText(),
				"<ul><li>Ninteen14</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(11).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(11).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(12).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(12).getMessage().getText(),
				"<ul><li>Ninteen15</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(12).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(12).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(13).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(13).getMessage().getText(),
				"<ul><li>Ninteen16</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(13).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(13).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(14).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(14).getMessage().getText(),
				"<ul><li>Ninteen17</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(14).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(14).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Eighteen Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 2);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eighteen Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Seventeen Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 2);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Seventeen Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Sixteen Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 2);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Sixteen Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(4).getSubject(), "Fifteen Test Topic");
		assertEquals(topics.get(4).getPoster().getUserId(), "root");
		assertEquals(topics.get(4).getReplies(), 0);
		assertEquals(topics.get(4).getViewCount(), 2);
		assertTrue(topics.get(4).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().size(), 1);
		assertTrue(topics.get(4).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fifteen Test Topic"));
		assertTrue(topics.get(4).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(5).getSubject(), "Fourteen Test Topic");
		assertEquals(topics.get(5).getPoster().getUserId(), "root");
		assertEquals(topics.get(5).getReplies(), 0);
		assertEquals(topics.get(5).getViewCount(), 2);
		assertTrue(topics.get(5).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().size(), 1);
		assertTrue(topics.get(5).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourteen Test Topic"));
		assertTrue(topics.get(5).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(6).getSubject(), "Thirteen Test Topic");
		assertEquals(topics.get(6).getPoster().getUserId(), "root");
		assertEquals(topics.get(6).getReplies(), 0);
		assertEquals(topics.get(6).getViewCount(), 2);
		assertTrue(topics.get(6).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().size(), 1);
		assertTrue(topics.get(6).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirteen Test Topic"));
		assertTrue(topics.get(6).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(7).getSubject(), "Twelve Test Topic");
		assertEquals(topics.get(7).getPoster().getUserId(), "root");
		assertEquals(topics.get(7).getReplies(), 0);
		assertEquals(topics.get(7).getViewCount(), 2);
		assertTrue(topics.get(7).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().size(), 1);
		assertTrue(topics.get(7).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twelve Test Topic"));
		assertTrue(topics.get(7).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(8).getSubject(), "Eleven Test Topic");
		assertEquals(topics.get(8).getPoster().getUserId(), "root");
		assertEquals(topics.get(8).getReplies(), 0);
		assertEquals(topics.get(8).getViewCount(), 2);
		assertTrue(topics.get(8).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().size(), 1);
		assertTrue(topics.get(8).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eleven Test Topic"));
		assertTrue(topics.get(8).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(9).getSubject(), "Ten Test Topic");
		assertEquals(topics.get(9).getPoster().getUserId(), "root");
		assertEquals(topics.get(9).getReplies(), 0);
		assertEquals(topics.get(9).getViewCount(), 2);
		assertTrue(topics.get(9).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().size(), 1);
		assertTrue(topics.get(9).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ten Test Topic"));
		assertTrue(topics.get(9).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(10).getSubject(), "Second Test Topic");
		assertEquals(topics.get(10).getPoster().getUserId(), "root");
		assertEquals(topics.get(10).getReplies(), 0);
		assertEquals(topics.get(10).getViewCount(), 2);
		assertTrue(topics.get(10).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().size(), 1);
		assertTrue(topics.get(10).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(10).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(11).getSubject(), "Thirtyone Test Topic");
		assertEquals(topics.get(11).getPoster().getUserId(), "root");
		assertEquals(topics.get(11).getReplies(), 0);
		assertEquals(topics.get(11).getViewCount(), 2);
		assertTrue(topics.get(11).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().size(), 1);
		assertTrue(topics.get(11).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtyone Test Topic"));
		assertTrue(topics.get(11).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(12).getSubject(), "Thirty Test Topic");
		assertEquals(topics.get(12).getPoster().getUserId(), "root");
		assertEquals(topics.get(12).getReplies(), 0);
		assertEquals(topics.get(12).getViewCount(), 2);
		assertTrue(topics.get(12).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().size(), 1);
		assertTrue(topics.get(12).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirty Test Topic"));
		assertTrue(topics.get(12).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(13).getSubject(), "Twentynine Test Topic");
		assertEquals(topics.get(13).getPoster().getUserId(), "root");
		assertEquals(topics.get(13).getReplies(), 0);
		assertEquals(topics.get(13).getViewCount(), 2);
		assertTrue(topics.get(13).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().size(), 1);
		assertTrue(topics.get(13).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentynine Test Topic"));
		assertTrue(topics.get(13).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(14).getSubject(), "Twentyeight Test Topic");
		assertEquals(topics.get(14).getPoster().getUserId(), "root");
		assertEquals(topics.get(14).getReplies(), 0);
		assertEquals(topics.get(14).getViewCount(), 2);
		assertTrue(topics.get(14).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().size(), 1);
		assertTrue(topics.get(14).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyeight Test Topic"));
		assertTrue(topics.get(14).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(15).getSubject(), "Twentyseven Test Topic");
		assertEquals(topics.get(15).getPoster().getUserId(), "root");
		assertEquals(topics.get(15).getReplies(), 0);
		assertEquals(topics.get(15).getViewCount(), 2);
		assertTrue(topics.get(15).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().size(), 1);
		assertTrue(topics.get(15).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyseven Test Topic"));
		assertTrue(topics.get(15).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(16).getSubject(), "Twentysix Test Topic");
		assertEquals(topics.get(16).getPoster().getUserId(), "root");
		assertEquals(topics.get(16).getReplies(), 0);
		assertEquals(topics.get(16).getViewCount(), 0);
		assertTrue(topics.get(16).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().size(), 1);
		assertTrue(topics.get(16).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentysix Test Topic"));
		assertTrue(topics.get(16).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(17).getSubject(), "Twentyfive Test Topic");
		assertEquals(topics.get(17).getPoster().getUserId(), "root");
		assertEquals(topics.get(17).getReplies(), 0);
		assertEquals(topics.get(17).getViewCount(), 0);
		assertTrue(topics.get(17).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().size(), 1);
		assertTrue(topics.get(17).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfive Test Topic"));
		assertTrue(topics.get(17).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(18).getSubject(), "Twentyfour Test Topic");
		assertEquals(topics.get(18).getPoster().getUserId(), "root");
		assertEquals(topics.get(18).getReplies(), 0);
		assertEquals(topics.get(18).getViewCount(), 0);
		assertTrue(topics.get(18).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().size(), 1);
		assertTrue(topics.get(18).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfour Test Topic"));
		assertTrue(topics.get(18).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(19).getSubject(), "Twentythree Test Topic");
		assertEquals(topics.get(19).getPoster().getUserId(), "root");
		assertEquals(topics.get(19).getReplies(), 0);
		assertEquals(topics.get(19).getViewCount(), 0);
		assertTrue(topics.get(19).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().size(), 1);
		assertTrue(topics.get(19).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentythree Test Topic"));
		assertTrue(topics.get(19).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(20).getSubject(), "Twentytwo Test Topic");
		assertEquals(topics.get(20).getPoster().getUserId(), "root");
		assertEquals(topics.get(20).getReplies(), 0);
		assertEquals(topics.get(20).getViewCount(), 0);
		assertTrue(topics.get(20).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().size(), 1);
		assertTrue(topics.get(20).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentytwo Test Topic"));
		assertTrue(topics.get(20).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(21).getSubject(), "Third Test Topic");
		assertEquals(topics.get(21).getPoster().getUserId(), "root");
		assertEquals(topics.get(21).getReplies(), 0);
		assertEquals(topics.get(21).getViewCount(), 2);
		assertTrue(topics.get(21).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().size(), 1);
		assertTrue(topics.get(21).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(21).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(22).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(22).getPoster().getUserId(), "root");
		assertEquals(topics.get(22).getReplies(), 0);
		assertEquals(topics.get(22).getViewCount(), 2);
		assertTrue(topics.get(22).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().size(), 1);
		assertTrue(topics.get(22).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(22).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(23).getSubject(), "Thirtytwo Test Topic");
		assertEquals(topics.get(23).getPoster().getUserId(), "root");
		assertEquals(topics.get(23).getReplies(), 0);
		assertEquals(topics.get(23).getViewCount(), 0);
		assertTrue(topics.get(23).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().size(), 1);
		assertTrue(topics.get(23).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtytwo Test Topic"));
		assertTrue(topics.get(23).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().get(0).getPoster().getUserId(),
				"root");
		message = removeTopic(driver, new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null));
		assertTrue(message.equals(OK));
	}

	public void scenary3() {
		Category firstTestCategory = new Category("First Test Category");
		Category secondTestCategory = new Category("Second Test Category");
		goTo(driver);
		PreferenceController submitCriteria = new PreferenceController();
		submitCriteria.setPostOrder("ascending");
		submitCriteria.setTopicsPerForum(20);
		submitCriteria.setAlwaysAddSignature(false);
		submitCriteria.setDateFormat("yyyy-MM-d");
		submit(driver, submitCriteria);
		List<Topic> summaryTopics = viewSummary(driver);
		int sizeSummaryTopics = viewSize(driver);
		Topic summaryTopicDetail = getDetail(driver, "Fourth Test Topic");
		assertTrue(summaryTopics != null);
		assertEquals(summaryTopics.size(), 6);
		assertEquals(sizeSummaryTopics, 6);
		assertEquals(summaryTopics.get(0).getSubject(), "Fourth Test Topic");
		assertEquals(summaryTopicDetail.getSubject(), "Fourth Test Topic");
		Topic newTopic = new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null);
		String message = createTopic(driver, newTopic);
		assertTrue(message.equals("Thirtytwo Test Topic"));
		List<Forum> forums = getForumsOfCategories(driver, firstTestCategory,
				secondTestCategory);
		Date today = new Date();
		List<Topic> topics = getTopicsOfForums(driver,
				forums.toArray(new Forum[0]));
		assertTrue(topics != null);
		assertEquals(topics.size(), 27);
		assertEquals(topics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 32);
		assertEquals(topics.get(0).getViewCount(), 104);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 15);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(0).getMessage().getText(),
				"Ninteen Test Body");
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(1).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(1).getMessage().getText(),
				"<ul><li>Ninteen4</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(1).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(1).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(2).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(2).getMessage().getText(),
				"<ul><li>Ninteen5</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(2).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(2).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(3).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(3).getMessage().getText(),
				"<ul><li>Ninteen6</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(3).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(3).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(4).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(4).getMessage().getText(),
				"<ul><li>Ninteen7</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(4).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(4).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(5).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(5).getMessage().getText(),
				"<ul><li>Ninteen8</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(5).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(5).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(6).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(6).getMessage().getText(),
				"<ul><li>Ninteen9</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(6).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(6).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(7).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(7).getMessage().getText(),
				"<ul><li>Ninteen10</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(7).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(7).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(8).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(8).getMessage().getText(),
				"<ul><li>Ninteen11</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(8).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(8).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(9).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(9).getMessage().getText(),
				"<ul><li>Ninteen12</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(9).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(9).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(10).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(10).getMessage().getText(),
				"<ul><li>Ninteen13</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(10).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(10).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(11).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(11).getMessage().getText(),
				"<ul><li>Ninteen14</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(11).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(11).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(12).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(12).getMessage().getText(),
				"<ul><li>Ninteen15</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(12).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(12).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(13).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(13).getMessage().getText(),
				"<ul><li>Ninteen16</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(13).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(13).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(14).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(14).getMessage().getText(),
				"<ul><li>Ninteen17</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(14).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(14).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Eighteen Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 4);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eighteen Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Seventeen Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 4);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Seventeen Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Sixteen Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 4);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Sixteen Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(4).getSubject(), "Fifteen Test Topic");
		assertEquals(topics.get(4).getPoster().getUserId(), "root");
		assertEquals(topics.get(4).getReplies(), 0);
		assertEquals(topics.get(4).getViewCount(), 4);
		assertTrue(topics.get(4).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().size(), 1);
		assertTrue(topics.get(4).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fifteen Test Topic"));
		assertTrue(topics.get(4).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(5).getSubject(), "Fourteen Test Topic");
		assertEquals(topics.get(5).getPoster().getUserId(), "root");
		assertEquals(topics.get(5).getReplies(), 0);
		assertEquals(topics.get(5).getViewCount(), 4);
		assertTrue(topics.get(5).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().size(), 1);
		assertTrue(topics.get(5).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourteen Test Topic"));
		assertTrue(topics.get(5).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(6).getSubject(), "Thirteen Test Topic");
		assertEquals(topics.get(6).getPoster().getUserId(), "root");
		assertEquals(topics.get(6).getReplies(), 0);
		assertEquals(topics.get(6).getViewCount(), 4);
		assertTrue(topics.get(6).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().size(), 1);
		assertTrue(topics.get(6).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirteen Test Topic"));
		assertTrue(topics.get(6).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(7).getSubject(), "Twelve Test Topic");
		assertEquals(topics.get(7).getPoster().getUserId(), "root");
		assertEquals(topics.get(7).getReplies(), 0);
		assertEquals(topics.get(7).getViewCount(), 4);
		assertTrue(topics.get(7).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().size(), 1);
		assertTrue(topics.get(7).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twelve Test Topic"));
		assertTrue(topics.get(7).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(8).getSubject(), "Eleven Test Topic");
		assertEquals(topics.get(8).getPoster().getUserId(), "root");
		assertEquals(topics.get(8).getReplies(), 0);
		assertEquals(topics.get(8).getViewCount(), 4);
		assertTrue(topics.get(8).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().size(), 1);
		assertTrue(topics.get(8).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eleven Test Topic"));
		assertTrue(topics.get(8).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(9).getSubject(), "Ten Test Topic");
		assertEquals(topics.get(9).getPoster().getUserId(), "root");
		assertEquals(topics.get(9).getReplies(), 0);
		assertEquals(topics.get(9).getViewCount(), 4);
		assertTrue(topics.get(9).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().size(), 1);
		assertTrue(topics.get(9).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ten Test Topic"));
		assertTrue(topics.get(9).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(10).getSubject(), "Second Test Topic");
		assertEquals(topics.get(10).getPoster().getUserId(), "root");
		assertEquals(topics.get(10).getReplies(), 0);
		assertEquals(topics.get(10).getViewCount(), 4);
		assertTrue(topics.get(10).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().size(), 1);
		assertTrue(topics.get(10).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(10).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(11).getSubject(), "Thirtyone Test Topic");
		assertEquals(topics.get(11).getPoster().getUserId(), "root");
		assertEquals(topics.get(11).getReplies(), 0);
		assertEquals(topics.get(11).getViewCount(), 4);
		assertTrue(topics.get(11).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().size(), 1);
		assertTrue(topics.get(11).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtyone Test Topic"));
		assertTrue(topics.get(11).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(12).getSubject(), "Thirty Test Topic");
		assertEquals(topics.get(12).getPoster().getUserId(), "root");
		assertEquals(topics.get(12).getReplies(), 0);
		assertEquals(topics.get(12).getViewCount(), 4);
		assertTrue(topics.get(12).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().size(), 1);
		assertTrue(topics.get(12).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirty Test Topic"));
		assertTrue(topics.get(12).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(13).getSubject(), "Twentynine Test Topic");
		assertEquals(topics.get(13).getPoster().getUserId(), "root");
		assertEquals(topics.get(13).getReplies(), 0);
		assertEquals(topics.get(13).getViewCount(), 4);
		assertTrue(topics.get(13).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().size(), 1);
		assertTrue(topics.get(13).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentynine Test Topic"));
		assertTrue(topics.get(13).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(14).getSubject(), "Twentyeight Test Topic");
		assertEquals(topics.get(14).getPoster().getUserId(), "root");
		assertEquals(topics.get(14).getReplies(), 0);
		assertEquals(topics.get(14).getViewCount(), 4);
		assertTrue(topics.get(14).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().size(), 1);
		assertTrue(topics.get(14).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyeight Test Topic"));
		assertTrue(topics.get(14).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(15).getSubject(), "Twentyseven Test Topic");
		assertEquals(topics.get(15).getPoster().getUserId(), "root");
		assertEquals(topics.get(15).getReplies(), 0);
		assertEquals(topics.get(15).getViewCount(), 4);
		assertTrue(topics.get(15).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().size(), 1);
		assertTrue(topics.get(15).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyseven Test Topic"));
		assertTrue(topics.get(15).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(16).getSubject(), "Twentysix Test Topic");
		assertEquals(topics.get(16).getPoster().getUserId(), "root");
		assertEquals(topics.get(16).getReplies(), 0);
		assertEquals(topics.get(16).getViewCount(), 2);
		assertTrue(topics.get(16).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().size(), 1);
		assertTrue(topics.get(16).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentysix Test Topic"));
		assertTrue(topics.get(16).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(17).getSubject(), "Twentyfive Test Topic");
		assertEquals(topics.get(17).getPoster().getUserId(), "root");
		assertEquals(topics.get(17).getReplies(), 0);
		assertEquals(topics.get(17).getViewCount(), 2);
		assertTrue(topics.get(17).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().size(), 1);
		assertTrue(topics.get(17).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfive Test Topic"));
		assertTrue(topics.get(17).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(18).getSubject(), "Twentyfour Test Topic");
		assertEquals(topics.get(18).getPoster().getUserId(), "root");
		assertEquals(topics.get(18).getReplies(), 0);
		assertEquals(topics.get(18).getViewCount(), 2);
		assertTrue(topics.get(18).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().size(), 1);
		assertTrue(topics.get(18).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfour Test Topic"));
		assertTrue(topics.get(18).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(19).getSubject(), "Twentythree Test Topic");
		assertEquals(topics.get(19).getPoster().getUserId(), "root");
		assertEquals(topics.get(19).getReplies(), 0);
		assertEquals(topics.get(19).getViewCount(), 2);
		assertTrue(topics.get(19).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().size(), 1);
		assertTrue(topics.get(19).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentythree Test Topic"));
		assertTrue(topics.get(19).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(20).getSubject(), "Twentytwo Test Topic");
		assertEquals(topics.get(20).getPoster().getUserId(), "root");
		assertEquals(topics.get(20).getReplies(), 0);
		assertEquals(topics.get(20).getViewCount(), 2);
		assertTrue(topics.get(20).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().size(), 1);
		assertTrue(topics.get(20).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentytwo Test Topic"));
		assertTrue(topics.get(20).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(21).getSubject(), "Twentyone Test Topic");
		assertEquals(topics.get(21).getPoster().getUserId(), "root");
		assertEquals(topics.get(21).getReplies(), 0);
		assertEquals(topics.get(21).getViewCount(), 0);
		assertTrue(topics.get(21).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().size(), 1);
		assertTrue(topics.get(21).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyone Test Topic"));
		assertTrue(topics.get(21).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(22).getSubject(), "Twelve Test Topic");
		assertEquals(topics.get(22).getPoster().getUserId(), "root");
		assertEquals(topics.get(22).getReplies(), 0);
		assertEquals(topics.get(22).getViewCount(), 0);
		assertTrue(topics.get(22).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().size(), 1);
		assertTrue(topics.get(22).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twelve Test Topic"));
		assertTrue(topics.get(22).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(23).getSubject(), "First Test Topic");
		assertEquals(topics.get(23).getPoster().getUserId(), "root");
		assertEquals(topics.get(23).getReplies(), 0);
		assertEquals(topics.get(23).getViewCount(), 0);
		assertTrue(topics.get(23).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().size(), 1);
		assertTrue(topics.get(23).getPosts().get(0).getMessage().getSubject()
				.startsWith("First Test Topic"));
		assertTrue(topics.get(23).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(24).getSubject(), "Third Test Topic");
		assertEquals(topics.get(24).getPoster().getUserId(), "root");
		assertEquals(topics.get(24).getReplies(), 0);
		assertEquals(topics.get(24).getViewCount(), 4);
		assertTrue(topics.get(24).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(24).getPosts().size(), 1);
		assertTrue(topics.get(24).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(24).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(24).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(25).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(25).getPoster().getUserId(), "root");
		assertEquals(topics.get(25).getReplies(), 0);
		assertEquals(topics.get(25).getViewCount(), 6);
		assertTrue(topics.get(25).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(25).getPosts().size(), 1);
		assertTrue(topics.get(25).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(25).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(25).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(26).getSubject(), "Thirtytwo Test Topic");
		assertEquals(topics.get(26).getPoster().getUserId(), "root");
		assertEquals(topics.get(26).getReplies(), 0);
		assertEquals(topics.get(26).getViewCount(), 0);
		assertTrue(topics.get(26).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(26).getPosts().size(), 1);
		assertTrue(topics.get(26).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtytwo Test Topic"));
		assertTrue(topics.get(26).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(26).getPosts().get(0).getPoster().getUserId(),
				"root");
		message = removeTopic(driver, new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null));
		assertTrue(message.equals(OK));
	}

	public void scenary4() {
		Category firstTestCategory = new Category("First Test Category");
		Category secondTestCategory = new Category("Second Test Category");
		goTo(driver);
		PreferenceController submitCriteria = new PreferenceController();
		submitCriteria.setPostOrder("descending");
		submitCriteria.setPostsPerTopic(10);
		submitCriteria.setSummaryTopicDays(1);
		submitCriteria.setDateFormat("d-MM-yyyy HH:mm");
		submit(driver, submitCriteria);
		List<Topic> summaryTopics = viewSummary(driver);
		int sizeSummaryTopics = viewSize(driver);
		Topic summaryTopicDetail = getDetail(driver, "Fourth Test Topic");
		assertTrue(summaryTopics != null);
		assertEquals(summaryTopics.size(), 6);
		assertEquals(sizeSummaryTopics, 6);
		assertEquals(summaryTopics.get(0).getSubject(), "Fourth Test Topic");
		assertEquals(summaryTopicDetail.getSubject(), "Fourth Test Topic");
		Topic newTopic = new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null);
		String message = createTopic(driver, newTopic);
		assertTrue(message.equals("Thirtytwo Test Topic"));
		List<Forum> forums = getForumsOfCategories(driver, firstTestCategory,
				secondTestCategory);
		Date today = new Date();
		List<Topic> topics = getTopicsOfForums(driver,
				forums.toArray(new Forum[0]));
		assertTrue(topics != null);
		assertEquals(topics.size(), 24);
		assertEquals(topics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 32);
		assertEquals(topics.get(0).getViewCount(), 106);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 10);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(0).getMessage().getText(),
				"<ul><li>Ninteen35</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(1).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(1).getMessage().getText(),
				"<ul><li>Ninteen34</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(1).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(1).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(2).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(2).getMessage().getText(),
				"<ul><li>Ninteen33</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(2).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(2).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(3).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(3).getMessage().getText(),
				"<ul><li>Ninteen32</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(3).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(3).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(4).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(4).getMessage().getText(),
				"<ul><li>Ninteen31</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(4).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(4).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(5).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(5).getMessage().getText(),
				"<ul><li>Ninteen30</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(5).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(5).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(6).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(6).getMessage().getText(),
				"<ul><li>Ninteen29</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(6).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(6).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(7).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(7).getMessage().getText(),
				"<ul><li>Ninteen28</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(7).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(7).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(8).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(8).getMessage().getText(),
				"<ul><li>Ninteen27</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(8).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(8).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(9).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(9).getMessage().getText(),
				"<ul><li>Ninteen26</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(9).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(9).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Eighteen Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 6);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eighteen Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Seventeen Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 6);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Seventeen Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Sixteen Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 6);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Sixteen Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(4).getSubject(), "Fifteen Test Topic");
		assertEquals(topics.get(4).getPoster().getUserId(), "root");
		assertEquals(topics.get(4).getReplies(), 0);
		assertEquals(topics.get(4).getViewCount(), 6);
		assertTrue(topics.get(4).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().size(), 1);
		assertTrue(topics.get(4).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fifteen Test Topic"));
		assertTrue(topics.get(4).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(5).getSubject(), "Fourteen Test Topic");
		assertEquals(topics.get(5).getPoster().getUserId(), "root");
		assertEquals(topics.get(5).getReplies(), 0);
		assertEquals(topics.get(5).getViewCount(), 6);
		assertTrue(topics.get(5).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().size(), 1);
		assertTrue(topics.get(5).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourteen Test Topic"));
		assertTrue(topics.get(5).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(6).getSubject(), "Thirteen Test Topic");
		assertEquals(topics.get(6).getPoster().getUserId(), "root");
		assertEquals(topics.get(6).getReplies(), 0);
		assertEquals(topics.get(6).getViewCount(), 6);
		assertTrue(topics.get(6).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().size(), 1);
		assertTrue(topics.get(6).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirteen Test Topic"));
		assertTrue(topics.get(6).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(7).getSubject(), "Twelve Test Topic");
		assertEquals(topics.get(7).getPoster().getUserId(), "root");
		assertEquals(topics.get(7).getReplies(), 0);
		assertEquals(topics.get(7).getViewCount(), 6);
		assertTrue(topics.get(7).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().size(), 1);
		assertTrue(topics.get(7).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twelve Test Topic"));
		assertTrue(topics.get(7).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(8).getSubject(), "Eleven Test Topic");
		assertEquals(topics.get(8).getPoster().getUserId(), "root");
		assertEquals(topics.get(8).getReplies(), 0);
		assertEquals(topics.get(8).getViewCount(), 6);
		assertTrue(topics.get(8).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().size(), 1);
		assertTrue(topics.get(8).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eleven Test Topic"));
		assertTrue(topics.get(8).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(9).getSubject(), "Ten Test Topic");
		assertEquals(topics.get(9).getPoster().getUserId(), "root");
		assertEquals(topics.get(9).getReplies(), 0);
		assertEquals(topics.get(9).getViewCount(), 6);
		assertTrue(topics.get(9).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().size(), 1);
		assertTrue(topics.get(9).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ten Test Topic"));
		assertTrue(topics.get(9).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(10).getSubject(), "Second Test Topic");
		assertEquals(topics.get(10).getPoster().getUserId(), "root");
		assertEquals(topics.get(10).getReplies(), 0);
		assertEquals(topics.get(10).getViewCount(), 6);
		assertTrue(topics.get(10).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().size(), 1);
		assertTrue(topics.get(10).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(10).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(11).getSubject(), "Thirtyone Test Topic");
		assertEquals(topics.get(11).getPoster().getUserId(), "root");
		assertEquals(topics.get(11).getReplies(), 0);
		assertEquals(topics.get(11).getViewCount(), 6);
		assertTrue(topics.get(11).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().size(), 1);
		assertTrue(topics.get(11).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtyone Test Topic"));
		assertTrue(topics.get(11).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(12).getSubject(), "Thirty Test Topic");
		assertEquals(topics.get(12).getPoster().getUserId(), "root");
		assertEquals(topics.get(12).getReplies(), 0);
		assertEquals(topics.get(12).getViewCount(), 6);
		assertTrue(topics.get(12).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().size(), 1);
		assertTrue(topics.get(12).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirty Test Topic"));
		assertTrue(topics.get(12).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(13).getSubject(), "Twentynine Test Topic");
		assertEquals(topics.get(13).getPoster().getUserId(), "root");
		assertEquals(topics.get(13).getReplies(), 0);
		assertEquals(topics.get(13).getViewCount(), 6);
		assertTrue(topics.get(13).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().size(), 1);
		assertTrue(topics.get(13).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentynine Test Topic"));
		assertTrue(topics.get(13).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(14).getSubject(), "Twentyeight Test Topic");
		assertEquals(topics.get(14).getPoster().getUserId(), "root");
		assertEquals(topics.get(14).getReplies(), 0);
		assertEquals(topics.get(14).getViewCount(), 6);
		assertTrue(topics.get(14).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().size(), 1);
		assertTrue(topics.get(14).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyeight Test Topic"));
		assertTrue(topics.get(14).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(15).getSubject(), "Twentyseven Test Topic");
		assertEquals(topics.get(15).getPoster().getUserId(), "root");
		assertEquals(topics.get(15).getReplies(), 0);
		assertEquals(topics.get(15).getViewCount(), 6);
		assertTrue(topics.get(15).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().size(), 1);
		assertTrue(topics.get(15).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyseven Test Topic"));
		assertTrue(topics.get(15).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(16).getSubject(), "Twentysix Test Topic");
		assertEquals(topics.get(16).getPoster().getUserId(), "root");
		assertEquals(topics.get(16).getReplies(), 0);
		assertEquals(topics.get(16).getViewCount(), 4);
		assertTrue(topics.get(16).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().size(), 1);
		assertTrue(topics.get(16).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentysix Test Topic"));
		assertTrue(topics.get(16).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(17).getSubject(), "Twentyfive Test Topic");
		assertEquals(topics.get(17).getPoster().getUserId(), "root");
		assertEquals(topics.get(17).getReplies(), 0);
		assertEquals(topics.get(17).getViewCount(), 4);
		assertTrue(topics.get(17).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().size(), 1);
		assertTrue(topics.get(17).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfive Test Topic"));
		assertTrue(topics.get(17).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(18).getSubject(), "Twentyfour Test Topic");
		assertEquals(topics.get(18).getPoster().getUserId(), "root");
		assertEquals(topics.get(18).getReplies(), 0);
		assertEquals(topics.get(18).getViewCount(), 4);
		assertTrue(topics.get(18).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().size(), 1);
		assertTrue(topics.get(18).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfour Test Topic"));
		assertTrue(topics.get(18).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(19).getSubject(), "Twentythree Test Topic");
		assertEquals(topics.get(19).getPoster().getUserId(), "root");
		assertEquals(topics.get(19).getReplies(), 0);
		assertEquals(topics.get(19).getViewCount(), 4);
		assertTrue(topics.get(19).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().size(), 1);
		assertTrue(topics.get(19).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentythree Test Topic"));
		assertTrue(topics.get(19).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(20).getSubject(), "Twentytwo Test Topic");
		assertEquals(topics.get(20).getPoster().getUserId(), "root");
		assertEquals(topics.get(20).getReplies(), 0);
		assertEquals(topics.get(20).getViewCount(), 4);
		assertTrue(topics.get(20).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().size(), 1);
		assertTrue(topics.get(20).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentytwo Test Topic"));
		assertTrue(topics.get(20).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(21).getSubject(), "Third Test Topic");
		assertEquals(topics.get(21).getPoster().getUserId(), "root");
		assertEquals(topics.get(21).getReplies(), 0);
		assertEquals(topics.get(21).getViewCount(), 6);
		assertTrue(topics.get(21).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().size(), 1);
		assertTrue(topics.get(21).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(21).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(22).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(22).getPoster().getUserId(), "root");
		assertEquals(topics.get(22).getReplies(), 0);
		assertEquals(topics.get(22).getViewCount(), 10);
		assertTrue(topics.get(22).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().size(), 1);
		assertTrue(topics.get(22).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(22).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(23).getSubject(), "Thirtytwo Test Topic");
		assertEquals(topics.get(23).getPoster().getUserId(), "root");
		assertEquals(topics.get(23).getReplies(), 0);
		assertEquals(topics.get(23).getViewCount(), 0);
		assertTrue(topics.get(23).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().size(), 1);
		assertTrue(topics.get(23).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtytwo Test Topic"));
		assertTrue(topics.get(23).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().get(0).getPoster().getUserId(),
				"root");
		message = removeTopic(driver, new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null));
		assertTrue(message.equals(OK));
	}

	public void submitsWithReset() {
		Category firstTestCategory = new Category("First Test Category");
		Category secondTestCategory = new Category("Second Test Category");
		goTo(driver);
		PreferenceController submitCriteria = new PreferenceController();
		submitCriteria.setAlwaysAddSignature(true);
		submitCriteria.setAlwaysAllowHtml(true);
		submitCriteria.setNotifyOnReply(true);
		submitCriteria.setPostOrder("descending");
		submitCriteria.setPostsPerTopic(10);
		submitCriteria.setSignature("My New Signature");
		submitCriteria.setSummaryMode(BLOCK_TOPICS_MODE_HOT_TOPICS);
		submitCriteria.setSummaryTopicDays(2);
		submitCriteria.setSummaryTopicLimit(3);
		submitCriteria.setSummaryTopicReplies(4);
		submitCriteria.setTopicsPerForum(5);
		submitCriteria.setDateFormat("d-MM-yyyy HH:mm");
		addKeys(driver, submitCriteria);
		reset(driver, submitCriteria);
		List<Topic> summaryTopics = viewSummary(driver);
		int sizeSummaryTopics = viewSize(driver);
		Topic summaryTopicDetail = getDetail(driver, "Fourth Test Topic");
		assertTrue(summaryTopics != null);
		assertEquals(summaryTopics.size(), 6);
		assertEquals(sizeSummaryTopics, 6);
		assertEquals(summaryTopics.get(0).getSubject(), "Fourth Test Topic");
		assertEquals(summaryTopicDetail.getSubject(), "Fourth Test Topic");
		Topic newTopic = new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null);
		String message = createTopic(driver, newTopic);
		assertTrue(message.equals("Thirtytwo Test Topic"));
		List<Forum> forums = getForumsOfCategories(driver, firstTestCategory,
				secondTestCategory);
		Date today = new Date();
		List<Topic> topics = getTopicsOfForums(driver,
				forums.toArray(new Forum[0]));
		assertEquals(topics.size(), 24);
		assertEquals(topics.get(0).getSubject(), "Ninteen Test Topic");
		assertEquals(topics.get(0).getPoster().getUserId(), "root");
		assertEquals(topics.get(0).getReplies(), 32);
		assertEquals(topics.get(0).getViewCount(), 108);
		assertTrue(topics.get(0).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().size(), 10);
		assertTrue(topics.get(0).getPosts().get(0).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(0).getMessage().getText(),
				"<ul><li>Ninteen35</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(1).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(1).getMessage().getText(),
				"<ul><li>Ninteen34</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(1).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(1).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(2).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(2).getMessage().getText(),
				"<ul><li>Ninteen33</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(2).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(2).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(3).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(3).getMessage().getText(),
				"<ul><li>Ninteen32</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(3).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(3).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(4).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(4).getMessage().getText(),
				"<ul><li>Ninteen31</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(4).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(4).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(5).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(5).getMessage().getText(),
				"<ul><li>Ninteen30</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(5).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(5).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(6).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(6).getMessage().getText(),
				"<ul><li>Ninteen29</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(6).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(6).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(7).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(7).getMessage().getText(),
				"<ul><li>Ninteen28</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(7).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(7).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(8).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(8).getMessage().getText(),
				"<ul><li>Ninteen27</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(8).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(8).getPoster().getUserId(),
				"root");
		assertTrue(topics.get(0).getPosts().get(9).getMessage().getSubject()
				.startsWith(RE + "Ninteen Test Topic"));
		assertEquals(topics.get(0).getPosts().get(9).getMessage().getText(),
				"<ul><li>Ninteen26</li><li>Test Body</li></ul>");
		assertTrue(topics.get(0).getPosts().get(9).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(0).getPosts().get(9).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(1).getSubject(), "Eighteen Test Topic");
		assertEquals(topics.get(1).getPoster().getUserId(), "root");
		assertEquals(topics.get(1).getReplies(), 0);
		assertEquals(topics.get(1).getViewCount(), 8);
		assertTrue(topics.get(1).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().size(), 1);
		assertTrue(topics.get(1).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eighteen Test Topic"));
		assertTrue(topics.get(1).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(1).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(2).getSubject(), "Seventeen Test Topic");
		assertEquals(topics.get(2).getPoster().getUserId(), "root");
		assertEquals(topics.get(2).getReplies(), 0);
		assertEquals(topics.get(2).getViewCount(), 8);
		assertTrue(topics.get(2).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().size(), 1);
		assertTrue(topics.get(2).getPosts().get(0).getMessage().getSubject()
				.startsWith("Seventeen Test Topic"));
		assertTrue(topics.get(2).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(2).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(3).getSubject(), "Sixteen Test Topic");
		assertEquals(topics.get(3).getPoster().getUserId(), "root");
		assertEquals(topics.get(3).getReplies(), 0);
		assertEquals(topics.get(3).getViewCount(), 8);
		assertTrue(topics.get(3).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().size(), 1);
		assertTrue(topics.get(3).getPosts().get(0).getMessage().getSubject()
				.startsWith("Sixteen Test Topic"));
		assertTrue(topics.get(3).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(3).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(4).getSubject(), "Fifteen Test Topic");
		assertEquals(topics.get(4).getPoster().getUserId(), "root");
		assertEquals(topics.get(4).getReplies(), 0);
		assertEquals(topics.get(4).getViewCount(), 8);
		assertTrue(topics.get(4).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().size(), 1);
		assertTrue(topics.get(4).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fifteen Test Topic"));
		assertTrue(topics.get(4).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(4).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(5).getSubject(), "Fourteen Test Topic");
		assertEquals(topics.get(5).getPoster().getUserId(), "root");
		assertEquals(topics.get(5).getReplies(), 0);
		assertEquals(topics.get(5).getViewCount(), 8);
		assertTrue(topics.get(5).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().size(), 1);
		assertTrue(topics.get(5).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourteen Test Topic"));
		assertTrue(topics.get(5).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(5).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(6).getSubject(), "Thirteen Test Topic");
		assertEquals(topics.get(6).getPoster().getUserId(), "root");
		assertEquals(topics.get(6).getReplies(), 0);
		assertEquals(topics.get(6).getViewCount(), 8);
		assertTrue(topics.get(6).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().size(), 1);
		assertTrue(topics.get(6).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirteen Test Topic"));
		assertTrue(topics.get(6).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(6).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(7).getSubject(), "Twelve Test Topic");
		assertEquals(topics.get(7).getPoster().getUserId(), "root");
		assertEquals(topics.get(7).getReplies(), 0);
		assertEquals(topics.get(7).getViewCount(), 8);
		assertTrue(topics.get(7).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().size(), 1);
		assertTrue(topics.get(7).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twelve Test Topic"));
		assertTrue(topics.get(7).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(7).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(8).getSubject(), "Eleven Test Topic");
		assertEquals(topics.get(8).getPoster().getUserId(), "root");
		assertEquals(topics.get(8).getReplies(), 0);
		assertEquals(topics.get(8).getViewCount(), 8);
		assertTrue(topics.get(8).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().size(), 1);
		assertTrue(topics.get(8).getPosts().get(0).getMessage().getSubject()
				.startsWith("Eleven Test Topic"));
		assertTrue(topics.get(8).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(8).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(9).getSubject(), "Ten Test Topic");
		assertEquals(topics.get(9).getPoster().getUserId(), "root");
		assertEquals(topics.get(9).getReplies(), 0);
		assertEquals(topics.get(9).getViewCount(), 8);
		assertTrue(topics.get(9).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().size(), 1);
		assertTrue(topics.get(9).getPosts().get(0).getMessage().getSubject()
				.startsWith("Ten Test Topic"));
		assertTrue(topics.get(9).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(9).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(10).getSubject(), "Second Test Topic");
		assertEquals(topics.get(10).getPoster().getUserId(), "root");
		assertEquals(topics.get(10).getReplies(), 0);
		assertEquals(topics.get(10).getViewCount(), 8);
		assertTrue(topics.get(10).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().size(), 1);
		assertTrue(topics.get(10).getPosts().get(0).getMessage().getSubject()
				.startsWith("Second Test Topic"));
		assertTrue(topics.get(10).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(10).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(11).getSubject(), "Thirtyone Test Topic");
		assertEquals(topics.get(11).getPoster().getUserId(), "root");
		assertEquals(topics.get(11).getReplies(), 0);
		assertEquals(topics.get(11).getViewCount(), 8);
		assertTrue(topics.get(11).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().size(), 1);
		assertTrue(topics.get(11).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtyone Test Topic"));
		assertTrue(topics.get(11).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(11).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(12).getSubject(), "Thirty Test Topic");
		assertEquals(topics.get(12).getPoster().getUserId(), "root");
		assertEquals(topics.get(12).getReplies(), 0);
		assertEquals(topics.get(12).getViewCount(), 8);
		assertTrue(topics.get(12).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().size(), 1);
		assertTrue(topics.get(12).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirty Test Topic"));
		assertTrue(topics.get(12).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(12).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(13).getSubject(), "Twentynine Test Topic");
		assertEquals(topics.get(13).getPoster().getUserId(), "root");
		assertEquals(topics.get(13).getReplies(), 0);
		assertEquals(topics.get(13).getViewCount(), 8);
		assertTrue(topics.get(13).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().size(), 1);
		assertTrue(topics.get(13).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentynine Test Topic"));
		assertTrue(topics.get(13).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(13).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(14).getSubject(), "Twentyeight Test Topic");
		assertEquals(topics.get(14).getPoster().getUserId(), "root");
		assertEquals(topics.get(14).getReplies(), 0);
		assertEquals(topics.get(14).getViewCount(), 8);
		assertTrue(topics.get(14).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().size(), 1);
		assertTrue(topics.get(14).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyeight Test Topic"));
		assertTrue(topics.get(14).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(14).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(15).getSubject(), "Twentyseven Test Topic");
		assertEquals(topics.get(15).getPoster().getUserId(), "root");
		assertEquals(topics.get(15).getReplies(), 0);
		assertEquals(topics.get(15).getViewCount(), 8);
		assertTrue(topics.get(15).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().size(), 1);
		assertTrue(topics.get(15).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyseven Test Topic"));
		assertTrue(topics.get(15).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(15).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(16).getSubject(), "Twentysix Test Topic");
		assertEquals(topics.get(16).getPoster().getUserId(), "root");
		assertEquals(topics.get(16).getReplies(), 0);
		assertEquals(topics.get(16).getViewCount(), 6);
		assertTrue(topics.get(16).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().size(), 1);
		assertTrue(topics.get(16).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentysix Test Topic"));
		assertTrue(topics.get(16).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(16).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(17).getSubject(), "Twentyfive Test Topic");
		assertEquals(topics.get(17).getPoster().getUserId(), "root");
		assertEquals(topics.get(17).getReplies(), 0);
		assertEquals(topics.get(17).getViewCount(), 6);
		assertTrue(topics.get(17).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().size(), 1);
		assertTrue(topics.get(17).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfive Test Topic"));
		assertTrue(topics.get(17).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(17).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(18).getSubject(), "Twentyfour Test Topic");
		assertEquals(topics.get(18).getPoster().getUserId(), "root");
		assertEquals(topics.get(18).getReplies(), 0);
		assertEquals(topics.get(18).getViewCount(), 6);
		assertTrue(topics.get(18).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().size(), 1);
		assertTrue(topics.get(18).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentyfour Test Topic"));
		assertTrue(topics.get(18).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(18).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(19).getSubject(), "Twentythree Test Topic");
		assertEquals(topics.get(19).getPoster().getUserId(), "root");
		assertEquals(topics.get(19).getReplies(), 0);
		assertEquals(topics.get(19).getViewCount(), 6);
		assertTrue(topics.get(19).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().size(), 1);
		assertTrue(topics.get(19).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentythree Test Topic"));
		assertTrue(topics.get(19).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(19).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(20).getSubject(), "Twentytwo Test Topic");
		assertEquals(topics.get(20).getPoster().getUserId(), "root");
		assertEquals(topics.get(20).getReplies(), 0);
		assertEquals(topics.get(20).getViewCount(), 6);
		assertTrue(topics.get(20).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().size(), 1);
		assertTrue(topics.get(20).getPosts().get(0).getMessage().getSubject()
				.startsWith("Twentytwo Test Topic"));
		assertTrue(topics.get(20).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(20).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(21).getSubject(), "Third Test Topic");
		assertEquals(topics.get(21).getPoster().getUserId(), "root");
		assertEquals(topics.get(21).getReplies(), 0);
		assertEquals(topics.get(21).getViewCount(), 8);
		assertTrue(topics.get(21).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().size(), 1);
		assertTrue(topics.get(21).getPosts().get(0).getMessage().getSubject()
				.startsWith("Third Test Topic"));
		assertTrue(topics.get(21).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(21).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(22).getSubject(), "Fourth Test Topic");
		assertEquals(topics.get(22).getPoster().getUserId(), "root");
		assertEquals(topics.get(22).getReplies(), 0);
		assertEquals(topics.get(22).getViewCount(), 14);
		assertTrue(topics.get(22).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().size(), 1);
		assertTrue(topics.get(22).getPosts().get(0).getMessage().getSubject()
				.startsWith("Fourth Test Topic"));
		assertTrue(topics.get(22).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(22).getPosts().get(0).getPoster().getUserId(),
				"root");
		assertEquals(topics.get(23).getSubject(), "Thirtytwo Test Topic");
		assertEquals(topics.get(23).getPoster().getUserId(), "root");
		assertEquals(topics.get(23).getReplies(), 0);
		assertEquals(topics.get(23).getViewCount(), 0);
		assertTrue(topics.get(23).getLastPostDate().compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().size(), 1);
		assertTrue(topics.get(23).getPosts().get(0).getMessage().getSubject()
				.startsWith("Thirtytwo Test Topic"));
		assertTrue(topics.get(23).getPosts().get(0).getCreateDate()
				.compareTo(today) < 0);
		assertEquals(topics.get(23).getPosts().get(0).getPoster().getUserId(),
				"root");
		message = removeTopic(driver, new Topic(
				new Forum("Third Test Forum", "Third Test Description",
						new Category("Second Test Category")),
				"Thirtytwo Test Topic", asList(new Post[] { new Post(
						"Thirtytwo Test Body") }), NORMAL, null));
		assertTrue(message.equals(OK));
	}

	@After
	public void stop() {
		goTo(driver);
		PreferenceController submitCriteria = new PreferenceController();
		submitCriteria.setAlwaysAddSignature(false);
		submitCriteria.setAlwaysAllowHtml(true);
		submitCriteria.setNotifyOnReply(true);
		submitCriteria.setPostOrder("ascending");
		submitCriteria.setPostsPerTopic(15);
		submitCriteria.setSignature("");
		submitCriteria.setSummaryMode(BLOCK_TOPICS_MODE_LATEST_POSTS);
		submitCriteria.setSummaryTopicDays(20);
		submitCriteria.setSummaryTopicLimit(6);
		submitCriteria.setSummaryTopicReplies(15);
		submitCriteria.setTopicsPerForum(10);
		submitCriteria.setDateFormat("EEE MMM d, yyyy HH:mm aaa");
		submit(driver, submitCriteria);
		String message = removeCategory(driver, new Category(
				"First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
