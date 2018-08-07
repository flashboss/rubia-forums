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
import static it.vige.rubia.selenium.forum.action.CreatePoll.addPoll;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemovePoll.removePoll;
import static it.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static it.vige.rubia.selenium.forum.action.UpdatePoll.addOptions;
import static it.vige.rubia.selenium.forum.action.UpdatePoll.deleteOptions;
import static it.vige.rubia.selenium.forum.action.UpdatePoll.updateOptions;
import static it.vige.rubia.selenium.forum.action.UpdatePoll.updatePoll;
import static it.vige.rubia.selenium.forum.action.UpdatePoll.vote;
import static it.vige.rubia.selenium.forum.action.VerifyPoll.getPollOfCurrentTopic;
import static it.vige.rubia.selenium.forum.action.VerifyPoll.getPollsOfTopics;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
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
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;

@RunWith(Arquillian.class)
@RunAsClient
public class OperationPollTest {

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
		message = createTopic(driver, new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))) }),
				IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }), 0)));
		assertTrue(message.equals("Fourth Test Topic"));
	}

	@Test
	public void verifyCreatedPolls() {
		List<PollBean> polls = getPollsOfTopics(driver, new TopicBean("First Test Topic"),
				new TopicBean("Second Test Topic"), new TopicBean("Third Test Topic"),
				new TopicBean("Fourth Test Topic"));
		assertEquals(4, polls.size());

		PollBean secondTestPoll = polls.get(0);
		assertEquals("Second Test Question", secondTestPoll.getTitle());
		assertEquals(secondTestPoll.getVotesSum(), 0);
		List<PollOptionBean> options = secondTestPoll.getOptions();
		assertEquals("Third Test Answer", options.get(0).getQuestion());
		assertEquals("Fourth Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		PollBean firstTestPoll = polls.get(1);
		assertEquals("First Test Question", firstTestPoll.getTitle());
		assertEquals(0, firstTestPoll.getVotesSum());
		options = firstTestPoll.getOptions();
		assertEquals("First Test Answer", options.get(0).getQuestion());
		assertEquals("Second Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		PollBean thirdTestPoll = polls.get(2);
		assertEquals("Third Test Question", thirdTestPoll.getTitle());
		assertEquals(0, thirdTestPoll.getVotesSum());
		options = thirdTestPoll.getOptions();
		assertEquals("Fifth Test with Truncation over 25 characters Answer", options.get(0).getQuestion());
		assertEquals("Sixth Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		PollBean fourthTestPoll = polls.get(3);
		assertEquals("Fourth Test Question", fourthTestPoll.getTitle());
		assertEquals(0, fourthTestPoll.getVotesSum());
		options = fourthTestPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());
	}

	@Test
	public void verifyUpdatePoll() {
		TopicBean topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic");
		goTo(driver, topic);
		PollBean pollToUpdate = getPollOfCurrentTopic(driver);
		pollToUpdate.setTitle("Second Test Question");
		pollToUpdate = updatePoll(driver, pollToUpdate);
		pollToUpdate.getOptions().get(0).setQuestion("Third Test Answer");
		pollToUpdate.getOptions().get(1).setQuestion("Fourth Test Answer");
		PollBean updatedPoll = updateOptions(driver, pollToUpdate);
		assertEquals("Second Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		List<PollOptionBean> options = updatedPoll.getOptions();
		assertEquals("Third Test Answer", options.get(0).getQuestion());
		assertEquals("Fourth Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		pollToUpdate.setTitle("Fourth Test Question");
		updatePoll(driver, pollToUpdate);
		pollToUpdate.getOptions().get(0).setQuestion("Seventh Test Answer");
		pollToUpdate.getOptions().get(1).setQuestion("Eight Test Answer");
		updatedPoll = updateOptions(driver, pollToUpdate);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		options = updatedPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());
	}

	@Test
	public void verifyVote() {
		TopicBean topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic");
		goTo(driver, topic);
		PollBean pollToUpdate = getPollOfCurrentTopic(driver);
		PollBean updatedPoll = vote(driver, pollToUpdate, 0);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(100, updatedPoll.getVotesSum());
		List<PollOptionBean> options = updatedPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals(100, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());
		updatedPoll = vote(driver, pollToUpdate, 1);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(100, updatedPoll.getVotesSum());
		options = updatedPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals(50, options.get(0).getVotes());
		assertEquals(50, options.get(1).getVotes());
		assertEquals(2, options.size());
		topic.setPoll(updatedPoll);
		String message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		String[] createdOptions = addPoll(driver, updatedPoll);
		assertEquals(2, createdOptions.length);
	}

	@Test
	public void verifyAddDeleteOptions() {
		TopicBean topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic");
		goTo(driver, topic);
		PollBean pollToUpdate = getPollOfCurrentTopic(driver);
		pollToUpdate.getOptions().clear();
		pollToUpdate.getOptions().add(new PollOptionBean("Third Test Answer"));
		pollToUpdate.getOptions().add(new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"));
		PollBean updatedPoll = addOptions(driver, pollToUpdate);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		List<PollOptionBean> options = updatedPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals("Third Test Answer", options.get(2).getQuestion());
		assertEquals("Fifth Test with Truncation over 25 characters Answer", options.get(3).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(0, options.get(2).getVotes());
		assertEquals(0, options.get(3).getVotes());
		assertEquals(4, options.size());
		pollToUpdate.getOptions().clear();
		pollToUpdate.getOptions().add(new PollOptionBean("Third Test Answer"));
		pollToUpdate.getOptions().add(new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"));
		updatedPoll = deleteOptions(driver, pollToUpdate);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		options = updatedPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());
	}

	@AfterClass
	public static void stop() {
		TopicBean topic = new TopicBean(new ForumBean("First Test Forum"), "First Test Topic",
				asList(new PostBean[] { new PostBean("First Test Body",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))) }),
				NORMAL, new PollBean("First Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("First Test Answer"), new PollOptionBean("Second Test Answer") }), 4));
		String message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic",
				asList(new PostBean[] { new PostBean("Second Test Body",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))) }),
				IMPORTANT, new PollBean("Second Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Third Test Answer"), new PollOptionBean("Fourth Test Answer") }), 8));
		message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic",
				asList(new PostBean[] { new PostBean("Third Test Body",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))) }),
				ADVICE,
				new PollBean("Third Test Question",
						asList(new PollOptionBean[] {
								new PollOptionBean("Fifth Test with Truncation over 25 characters Answer"),
								new PollOptionBean("Sixth Test Answer") }),
						9));
		message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic",
				asList(new PostBean[] { new PostBean("Fourth Test Body",
						asList(new AttachmentBean("fourth", "Fourth Test File"),
								new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
								new AttachmentBean("sixth", "Sixth Test File"))) }),
				IMPORTANT, new PollBean("Fourth Test Question", asList(new PollOptionBean[] {
						new PollOptionBean("Seventh Test Answer"), new PollOptionBean("Eight Test Answer") }), 0));
		message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeForum(driver, new ForumBean("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new ForumBean("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
