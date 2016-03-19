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

import static it.vige.rubia.model.TopicType.ADVICE;
import static it.vige.rubia.model.TopicType.IMPORTANT;
import static it.vige.rubia.model.TopicType.NORMAL;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class OperationPollTest {

	@Drone
	private WebDriver driver;

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
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
		message = createTopic(driver, new Topic(new Forum("First Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				IMPORTANT,
				new Poll("Second Test Question", asList(
						new PollOption[] { new PollOption("Third Test Answer"), new PollOption("Fourth Test Answer") }),
						8)));
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver,
				new Forum("Second Test Forum", "Second Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(driver, new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				ADVICE,
				new Poll("Third Test Question", asList(
						new PollOption[] { new PollOption("Fifth Test with Truncation over 25 characters Answer"), new PollOption("Sixth Test Answer") }),
						9)));
		assertTrue(message.equals("Third Test Topic"));
		message = createTopic(driver, new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body",
						asList(new Attachment("fourth", "Fourth Test File"), new Attachment("fifth", "Fifth Test with Truncation over 25 characters File"),
								new Attachment("sixth", "Sixth Test File"))) }),
				IMPORTANT, new Poll("Fourth Test Question", asList(new PollOption[] {
						new PollOption("Seventh Test Answer"), new PollOption("Eight Test Answer") }), 0)));
		assertTrue(message.equals("Fourth Test Topic"));
	}

	@Test
	public void verifyCreatedPolls() {
		List<Poll> polls = getPollsOfTopics(driver, new Topic("First Test Topic"), new Topic("Second Test Topic"),
				new Topic("Third Test Topic"), new Topic("Fourth Test Topic"));
		assertEquals(4, polls.size());

		Poll secondTestPoll = polls.get(0);
		assertEquals("Second Test Question", secondTestPoll.getTitle());
		assertEquals(secondTestPoll.getVotesSum(), 0);
		List<PollOption> options = secondTestPoll.getOptions();
		assertEquals("Third Test Answer", options.get(0).getQuestion());
		assertEquals("Fourth Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		Poll firstTestPoll = polls.get(1);
		assertEquals("First Test Question", firstTestPoll.getTitle());
		assertEquals(0, firstTestPoll.getVotesSum());
		options = firstTestPoll.getOptions();
		assertEquals("First Test Answer", options.get(0).getQuestion());
		assertEquals("Second Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		Poll thirdTestPoll = polls.get(2);
		assertEquals("Third Test Question", thirdTestPoll.getTitle());
		assertEquals(0, thirdTestPoll.getVotesSum());
		options = thirdTestPoll.getOptions();
		assertEquals("Fifth Test with Truncation over 25 characters Answer", options.get(0).getQuestion());
		assertEquals("Sixth Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		Poll fourthTestPoll = polls.get(3);
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
		Topic topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		pollToUpdate.setTitle("Second Test Question");
		pollToUpdate = updatePoll(driver, pollToUpdate);
		pollToUpdate.getOptions().get(0).setQuestion("Third Test Answer");
		pollToUpdate.getOptions().get(1).setQuestion("Fourth Test Answer");
		Poll updatedPoll = updateOptions(driver, pollToUpdate);
		assertEquals("Second Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		List<PollOption> options = updatedPoll.getOptions();
		assertEquals("Fourth Test Answer", options.get(0).getQuestion());
		assertEquals("Third Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());

		pollToUpdate.setTitle("Fourth Test Question");
		updatePoll(driver, pollToUpdate);
		pollToUpdate.getOptions().get(0).setQuestion("Eight Test Answer");
		pollToUpdate.getOptions().get(1).setQuestion("Seventh Test Answer");
		updatedPoll = updateOptions(driver, pollToUpdate);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		options = updatedPoll.getOptions();
		assertEquals("Seventh Test Answer", options.get(0).getQuestion());
		assertEquals("Eight Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());
		pollToUpdate.setTitle("Second Test Question");
		updatePoll(driver, pollToUpdate);
	}

	@Test
	public void verifyVote() {
		Topic topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		Poll updatedPoll = vote(driver, pollToUpdate, 0);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(100, updatedPoll.getVotesSum());
		List<PollOption> options = updatedPoll.getOptions();
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
	}

	@Test
	public void verifyAddDeleteOptions() {
		Topic topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		pollToUpdate.getOptions().clear();
		pollToUpdate.getOptions().add(new PollOption("Third Test Answer"));
		pollToUpdate.getOptions().add(new PollOption("Fifth Test with Truncation over 25 characters Answer"));
		Poll updatedPoll = addOptions(driver, pollToUpdate);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		List<PollOption> options = updatedPoll.getOptions();
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
		pollToUpdate.getOptions().add(new PollOption("Third Test Answer"));
		pollToUpdate.getOptions().add(new PollOption("Fifth Test with Truncation over 25 characters Answer"));
		updatedPoll = deleteOptions(driver, pollToUpdate);
		assertEquals("Fourth Test Question", updatedPoll.getTitle());
		assertEquals(0, updatedPoll.getVotesSum());
		options = updatedPoll.getOptions();
		assertEquals("Eight Test Answer", options.get(0).getQuestion());
		assertEquals("Seventh Test Answer", options.get(1).getQuestion());
		assertEquals(0, options.get(0).getVotes());
		assertEquals(0, options.get(1).getVotes());
		assertEquals(2, options.size());
	}

	@After
	public void stop() {
		Topic topic = new Topic(new Forum("First Test Forum"), "First Test Topic",
				asList(new Post[] { new Post("First Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				NORMAL,
				new Poll("First Test Question", asList(
						new PollOption[] { new PollOption("First Test Answer"), new PollOption("Second Test Answer") }),
						4));
		String message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("First Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				IMPORTANT,
				new Poll("Second Test Question", asList(
						new PollOption[] { new PollOption("Third Test Answer"), new PollOption("Fourth Test Answer") }),
						8));
		message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))) }),
				ADVICE,
				new Poll("Third Test Question", asList(
						new PollOption[] { new PollOption("Fifth Test with Truncation over 25 characters Answer"), new PollOption("Sixth Test Answer") }),
						9));
		message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body",
						asList(new Attachment("fourth", "Fourth Test File"), new Attachment("fifth", "Fifth Test with Truncation over 25 characters File"),
								new Attachment("sixth", "Sixth Test File"))) }),
				IMPORTANT, new Poll("Fourth Test Question", asList(new PollOption[] {
						new PollOption("Seventh Test Answer"), new PollOption("Eight Test Answer") }), 0));
		message = removePoll(driver, topic);
		assertTrue(message.equals(OK));
		message = removeTopic(driver, topic);
		assertTrue(message.equals(OK));
		message = removeForum(driver, new Forum("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"), SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
