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
package org.vige.rubia.selenium.forum.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.forum.action.RemovePoll.removePoll;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.SELECT_CATEGORY_TYPE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.selenium.forum.action.UpdatePoll.vote;
import static org.vige.rubia.selenium.forum.action.UpdatePoll.addOptions;
import static org.vige.rubia.selenium.forum.action.UpdatePoll.updateOptions;
import static org.vige.rubia.selenium.forum.action.UpdatePoll.updatePoll;
import static org.vige.rubia.selenium.forum.action.VerifyPoll.getPollOfCurrentTopic;
import static org.vige.rubia.selenium.forum.action.VerifyPoll.getPollsOfTopics;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.goTo;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

@RunWith(Arquillian.class)
public class OperationPollTest {

	@Drone
	private FirefoxDriver driver;

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
	}

	@Test
	public void verifyUpdatePoll() {
		Topic topic = new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		pollToUpdate.setTitle("Second Test Question");
		updatePoll(driver, pollToUpdate);
		pollToUpdate.getOptions().get(0).setQuestion("Third Test Answer");
		pollToUpdate.getOptions().get(1).setQuestion("Fourth Test Answer");
		Poll updatedPoll = updateOptions(driver, pollToUpdate);
		assertEquals(updatedPoll.getTitle(), "Second Test Question");
		assertEquals(updatedPoll.getVotesSum(), 0);
		List<PollOption> options = updatedPoll.getOptions();
		assertEquals(options.get(1).getQuestion(), "Third Test Answer");
		assertEquals(options.get(0).getQuestion(), "Fourth Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);

		pollToUpdate.setTitle("Fourth Test Question");
		updatePoll(driver, pollToUpdate);
		pollToUpdate.getOptions().get(0).setQuestion("Seventh Test Answer");
		pollToUpdate.getOptions().get(1).setQuestion("Eight Test Answer");
		updatedPoll = updateOptions(driver, pollToUpdate);
		assertEquals(updatedPoll.getTitle(), "Fourth Test Question");
		assertEquals(updatedPoll.getVotesSum(), 0);
		options = updatedPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Seventh Test Answer");
		assertEquals(options.get(1).getQuestion(), "Eight Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);
		pollToUpdate.setTitle("Second Test Question");
		updatePoll(driver, pollToUpdate);
	}

	@Test
	public void verifyUpdateOptions() {
		Topic topic = new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		updateOptions(driver, pollToUpdate);
		Poll updatedPoll = getPollOfCurrentTopic(driver);
		assertEquals(updatedPoll.getTitle(), "Second Test Question");
		assertEquals(updatedPoll.getVotesSum(), 0);
		List<PollOption> options = updatedPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Third Test Answer");
		assertEquals(options.get(1).getQuestion(), "Fourth Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);
	}

	@Test
	public void verifyVote() {
		Topic topic = new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		Poll updatedPoll = vote(driver, pollToUpdate, 0);
		assertEquals(updatedPoll.getTitle(), "Fourth Test Question");
		assertEquals(updatedPoll.getVotesSum(), 100);
		List<PollOption> options = updatedPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Seventh Test Answer");
		assertEquals(options.get(1).getQuestion(), "Eight Test Answer");
		assertEquals(options.get(0).getVotes(), 100);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);
		updatedPoll = vote(driver, pollToUpdate, 1);
		assertEquals(updatedPoll.getTitle(), "Fourth Test Question");
		assertEquals(updatedPoll.getVotesSum(), 100);
		options = updatedPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Seventh Test Answer");
		assertEquals(options.get(1).getQuestion(), "Eight Test Answer");
		assertEquals(options.get(0).getVotes(), 50);
		assertEquals(options.get(1).getVotes(), 50);
		assertEquals(options.size(), 2);
	}

	@Test
	public void verifyAddOptions() {
		Topic topic = new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic");
		goTo(driver, topic);
		Poll pollToUpdate = getPollOfCurrentTopic(driver);
		addOptions(driver, pollToUpdate);
		Poll updatedPoll = getPollOfCurrentTopic(driver);
		assertEquals(updatedPoll.getTitle(), "Second Test Question");
		assertEquals(updatedPoll.getVotesSum(), 0);
		List<PollOption> options = updatedPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Third Test Answer");
		assertEquals(options.get(1).getQuestion(), "Fourth Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);
	}

	@Test
	public void verifyCreatedPolls() {
		List<Poll> polls = getPollsOfTopics(driver, new Topic(
				"First Test Topic"), new Topic("Second Test Topic"), new Topic(
				"Third Test Topic"), new Topic("Fourth Test Topic"));
		assertEquals(polls.size(), 4);

		Poll secondTestPoll = polls.get(0);
		assertEquals(secondTestPoll.getTitle(), "Second Test Question");
		assertEquals(secondTestPoll.getVotesSum(), 0);
		List<PollOption> options = secondTestPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Third Test Answer");
		assertEquals(options.get(1).getQuestion(), "Fourth Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);

		Poll firstTestPoll = polls.get(1);
		assertEquals(firstTestPoll.getTitle(), "First Test Question");
		assertEquals(firstTestPoll.getVotesSum(), 0);
		options = firstTestPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "First Test Answer");
		assertEquals(options.get(1).getQuestion(), "Second Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);

		Poll thirdTestPoll = polls.get(2);
		assertEquals(thirdTestPoll.getTitle(), "Third Test Question");
		assertEquals(thirdTestPoll.getVotesSum(), 0);
		options = thirdTestPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Fifth Test Answer");
		assertEquals(options.get(1).getQuestion(), "Sixth Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);

		Poll fourthTestPoll = polls.get(3);
		assertEquals(fourthTestPoll.getTitle(), "Fourth Test Question");
		assertEquals(fourthTestPoll.getVotesSum(), 0);
		options = fourthTestPoll.getOptions();
		assertEquals(options.get(0).getQuestion(), "Seventh Test Answer");
		assertEquals(options.get(1).getQuestion(), "Eight Test Answer");
		assertEquals(options.get(0).getVotes(), 0);
		assertEquals(options.get(1).getVotes(), 0);
		assertEquals(options.size(), 2);
	}

	@After
	public void stop() {
		Topic topic = new Topic(new Forum("First Test Forum"),
				"First Test Topic", asList(new Post[] { new Post(
						"First Test Body", asList(new Attachment("first",
								"First Test File"), new Attachment("second",
								"Second Test File"), new Attachment("third",
								"Third Test File"))) }), NORMAL, new Poll(
						"First Test Question", asList(new PollOption[] {
								new PollOption("First Test Answer"),
								new PollOption("Second Test Answer") }), 4));
		String message = removePoll(driver, topic);
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, topic);
		assertTrue(message.equals("OK"));
		topic = new Topic(new Forum("First Test Forum"), "Second Test Topic",
				asList(new Post[] { new Post("Second Test Body", asList(
						new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File"))) }),
				IMPORTANT, new Poll("Second Test Question",
						asList(new PollOption[] {
								new PollOption("Third Test Answer"),
								new PollOption("Fourth Test Answer") }), 8));
		message = removePoll(driver, topic);
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, topic);
		assertTrue(message.equals("OK"));
		topic = new Topic(new Forum("Second Test Forum"), "Third Test Topic",
				asList(new Post[] { new Post("Third Test Body", asList(
						new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File"))) }),
				ADVICE, new Poll("Third Test Question",
						asList(new PollOption[] {
								new PollOption("Fifth Test Answer"),
								new PollOption("Sixth Test Answer") }), 9));
		message = removePoll(driver, topic);
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, topic);
		assertTrue(message.equals("OK"));
		topic = new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
				asList(new Post[] { new Post("Fourth Test Body", asList(
						new Attachment("fourth", "Fourth Test File"),
						new Attachment("fifth", "Fifth Test File"),
						new Attachment("sixth", "Sixth Test File"))) }),
				IMPORTANT, new Poll("Fourth Test Question",
						asList(new PollOption[] {
								new PollOption("Seventh Test Answer"),
								new PollOption("Eight Test Answer") }), 0));
		message = removePoll(driver, topic);
		assertTrue(message.equals("OK"));
		message = removeTopic(driver, topic);
		assertTrue(message.equals("OK"));
		message = removeForum(driver, new Forum("First Test Forum"),
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"),
				SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
