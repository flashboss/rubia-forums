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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static org.vige.rubia.selenium.forum.action.RemoveTopic.removeTopic;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfTopics;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest;

@RunWith(Arquillian.class)
public class OperationAttachmentTest {

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
		List<Attachment> files = new ArrayList<Attachment>();
		files.add(new Attachment("/first", "First Test File"));
		files.add(new Attachment("/second", "Second Test File"));
		files.add(new Attachment("/third", "Third Test File"));
		message = createTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"First Test Topic",
						Arrays.asList(new Post[] { new Post("First Test Body",
								files) }),
						NORMAL,
						new Poll(
								"First Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("First Test Answer"),
										new PollOption("Second Test Answer") }),
								4)));
		assertTrue(message.equals("First Test Topic"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "First Test Post",
				files));
		assertTrue(message.equals("First Test Post"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Second Test Post",
				files));
		assertTrue(message.equals("Second Test Post"));
		message = createTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"Second Test Topic",
						Arrays.asList(new Post[] { new Post("Second Test Body",
								files) }),
						IMPORTANT,
						new Poll(
								"Second Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("Third Test Answer"),
										new PollOption("Fourth Test Answer") }),
								8)));
		assertTrue(message.equals("Second Test Topic"));
		message = createForum(driver, new Forum("Second Test Forum",
				"Second Test Description", new Category("First Test Category")));
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Third Test Topic",
						Arrays.asList(new Post[] { new Post("Third Test Body",
								files) }), ADVICE, new Poll(
								"Third Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("Fifth Test Answer"),
										new PollOption("Sixth Test Answer") }),
								9)));
		assertTrue(message.equals("Third Test Topic"));
		files.clear();
		files.add(new Attachment("/fourth", "Fourth Test File"));
		files.add(new Attachment("/fifth", "Fifth Test File"));
		files.add(new Attachment("/sixth", "Sixth Test File"));
		message = createTopic(
				driver,
				new Topic(new Forum("Second Test Forum"), "Fourth Test Topic",
						Arrays.asList(new Post[] { new Post("Fourth Test Body",
								files) }), IMPORTANT, new Poll(
								"Fourth Test Question",
								Arrays.asList(new PollOption[] {
										new PollOption("Seventh Test Answer"),
										new PollOption("Eight Test Answer") }),
								0)));
		assertTrue(message.equals("Fourth Test Topic"));
		files.clear();
		files.add(new Attachment("/seventh", "Seventh Test File"));
		files.add(new Attachment("/eight", "Eight Test File"));
		files.add(new Attachment("/ninth", "Ninth Test File"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
				files));
		assertTrue(message.equals("Third Test Post"));
		files.clear();
		files.add(new Attachment("/ten", "Ten Test File"));
		files.add(new Attachment("/eleven", "Eleven Test File"));
		files.add(new Attachment("/twelve", "Twelve Test File"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Fourth Test Post",
				files));
		assertTrue(message.equals("Fourth Test Post"));
		files.clear();
		files.add(new Attachment("/thirteen", "Thirteen Test File"));
		files.add(new Attachment("/fourteen", "Fourteen Test File"));
		files.add(new Attachment("/fifteen", "Fifteen Test File"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Fifth Test Post",
				files));
		assertTrue(message.equals("Fifth Test Post"));
		files.clear();
		files.add(new Attachment("/sixteen", "Sixteen Test File"));
		files.add(new Attachment("/seventeen", "Seventeen Test File"));
		files.add(new Attachment("/eighteen", "Eighteen Test File"));
		message = createPost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Sixth Test Post",
				files));
		assertTrue(message.equals("Sixth Test Post"));
	}

	@Test
	public void verifyCreatedAttachments() {
		List<Attachment> attachments = getAttachmentsOfTopics(driver,
				new Topic("First Test Topic"), new Topic("Second Test Topic"),
				new Topic("Third Test Topic"), new Topic("Fourth Test Topic"));
		assertEquals(attachments.size(), 30);

		Attachment firstAttachment = attachments.get(0);
		assertEquals(firstAttachment.getName(), "first");
		assertNull(firstAttachment.getContent());
		assertNull(firstAttachment.getContentType());
		assertEquals(firstAttachment.getComment(), "First Test File");
		assertEquals(firstAttachment.getSize(), 0);

		Attachment secondAttachment = attachments.get(1);
		assertEquals(secondAttachment.getName(), "second");
		assertNull(secondAttachment.getContent());
		assertNull(secondAttachment.getContentType());
		assertEquals(secondAttachment.getComment(), "Second Test File");
		assertEquals(secondAttachment.getSize(), 0);

		Attachment thirdAttachment = attachments.get(2);
		assertEquals(thirdAttachment.getName(), "third");
		assertNull(thirdAttachment.getContent());
		assertNull(thirdAttachment.getContentType());
		assertEquals(thirdAttachment.getComment(), "Third Test File");
		assertEquals(thirdAttachment.getSize(), 0);

		Attachment fourthAttachment = attachments.get(3);
		assertEquals(fourthAttachment.getName(), "first");
		assertNull(fourthAttachment.getContent());
		assertNull(fourthAttachment.getContentType());
		assertEquals(fourthAttachment.getComment(), "First Test File");
		assertEquals(fourthAttachment.getSize(), 0);

		Attachment fifthAttachment = attachments.get(4);
		assertEquals(fifthAttachment.getName(), "second");
		assertNull(fifthAttachment.getContent());
		assertNull(fifthAttachment.getContentType());
		assertEquals(fifthAttachment.getComment(), "Second Test File");
		assertEquals(fifthAttachment.getSize(), 0);

		Attachment sixthAttachment = attachments.get(5);
		assertEquals(sixthAttachment.getName(), "third");
		assertNull(sixthAttachment.getContent());
		assertNull(sixthAttachment.getContentType());
		assertEquals(sixthAttachment.getComment(), "Third Test File");
		assertEquals(sixthAttachment.getSize(), 0);

		Attachment seventhAttachment = attachments.get(6);
		assertEquals(seventhAttachment.getName(), "first");
		assertNull(seventhAttachment.getContent());
		assertNull(seventhAttachment.getContentType());
		assertEquals(seventhAttachment.getComment(), "First Test File");
		assertEquals(seventhAttachment.getSize(), 0);

		Attachment eightAttachment = attachments.get(7);
		assertEquals(eightAttachment.getName(), "second");
		assertNull(eightAttachment.getContent());
		assertNull(eightAttachment.getContentType());
		assertEquals(eightAttachment.getComment(), "Second Test File");
		assertEquals(eightAttachment.getSize(), 0);

		Attachment ninthAttachment = attachments.get(8);
		assertEquals(ninthAttachment.getName(), "third");
		assertNull(ninthAttachment.getContent());
		assertNull(ninthAttachment.getContentType());
		assertEquals(ninthAttachment.getComment(), "Third Test File");
		assertEquals(ninthAttachment.getSize(), 0);

		Attachment tenAttachment = attachments.get(9);
		assertEquals(tenAttachment.getName(), "first");
		assertNull(tenAttachment.getContent());
		assertNull(tenAttachment.getContentType());
		assertEquals(tenAttachment.getComment(), "First Test File");
		assertEquals(tenAttachment.getSize(), 0);

		Attachment elevenAttachment = attachments.get(10);
		assertEquals(elevenAttachment.getName(), "second");
		assertNull(elevenAttachment.getContent());
		assertNull(elevenAttachment.getContentType());
		assertEquals(elevenAttachment.getComment(), "Second Test File");
		assertEquals(elevenAttachment.getSize(), 0);

		Attachment twelveAttachment = attachments.get(11);
		assertEquals(twelveAttachment.getName(), "third");
		assertNull(twelveAttachment.getContent());
		assertNull(twelveAttachment.getContentType());
		assertEquals(twelveAttachment.getComment(), "Third Test File");
		assertEquals(twelveAttachment.getSize(), 0);

		Attachment thirteenAttachment = attachments.get(12);
		assertEquals(thirteenAttachment.getName(), "first");
		assertNull(thirteenAttachment.getContent());
		assertNull(thirteenAttachment.getContentType());
		assertEquals(thirteenAttachment.getComment(), "First Test File");
		assertEquals(thirteenAttachment.getSize(), 0);

		Attachment fourteenAttachment = attachments.get(13);
		assertEquals(fourteenAttachment.getName(), "second");
		assertNull(fourteenAttachment.getContent());
		assertNull(fourteenAttachment.getContentType());
		assertEquals(fourteenAttachment.getComment(), "Second Test File");
		assertEquals(fourteenAttachment.getSize(), 0);

		Attachment fifteenAttachment = attachments.get(14);
		assertEquals(fifteenAttachment.getName(), "third");
		assertNull(fifteenAttachment.getContent());
		assertNull(fifteenAttachment.getContentType());
		assertEquals(fifteenAttachment.getComment(), "Third Test File");
		assertEquals(fifteenAttachment.getSize(), 0);

		Attachment sixteenAttachment = attachments.get(15);
		assertEquals(sixteenAttachment.getName(), "fourth");
		assertNull(sixteenAttachment.getContent());
		assertNull(sixteenAttachment.getContentType());
		assertEquals(sixteenAttachment.getComment(), "Fourth Test File");
		assertEquals(sixteenAttachment.getSize(), 0);

		Attachment seventeenAttachment = attachments.get(16);
		assertEquals(seventeenAttachment.getName(), "fifth");
		assertNull(seventeenAttachment.getContent());
		assertNull(seventeenAttachment.getContentType());
		assertEquals(seventeenAttachment.getComment(), "Fifth Test File");
		assertEquals(seventeenAttachment.getSize(), 0);

		Attachment eighteenAttachment = attachments.get(17);
		assertEquals(eighteenAttachment.getName(), "sixth");
		assertNull(eighteenAttachment.getContent());
		assertNull(eighteenAttachment.getContentType());
		assertEquals(eighteenAttachment.getComment(), "Sixth Test File");
		assertEquals(eighteenAttachment.getSize(), 0);

		Attachment ninteenAttachment = attachments.get(18);
		assertEquals(ninteenAttachment.getName(), "seventh");
		assertNull(ninteenAttachment.getContent());
		assertNull(ninteenAttachment.getContentType());
		assertEquals(ninteenAttachment.getComment(), "Seventh Test File");
		assertEquals(ninteenAttachment.getSize(), 0);

		Attachment twentyAttachment = attachments.get(19);
		assertEquals(twentyAttachment.getName(), "eight");
		assertNull(twentyAttachment.getContent());
		assertNull(twentyAttachment.getContentType());
		assertEquals(twentyAttachment.getComment(), "Eight Test File");
		assertEquals(twentyAttachment.getSize(), 0);

		Attachment twentyoneAttachment = attachments.get(20);
		assertEquals(twentyoneAttachment.getName(), "ninth");
		assertNull(twentyoneAttachment.getContent());
		assertNull(twentyoneAttachment.getContentType());
		assertEquals(twentyoneAttachment.getComment(), "Ninth Test File");
		assertEquals(twentyoneAttachment.getSize(), 0);

		Attachment twentytwoAttachment = attachments.get(21);
		assertEquals(twentytwoAttachment.getName(), "ten");
		assertNull(twentytwoAttachment.getContent());
		assertNull(twentytwoAttachment.getContentType());
		assertEquals(twentytwoAttachment.getComment(), "Ten Test File");
		assertEquals(twentytwoAttachment.getSize(), 0);

		Attachment twentythreeAttachment = attachments.get(22);
		assertEquals(twentythreeAttachment.getName(), "eleven");
		assertNull(twentythreeAttachment.getContent());
		assertNull(twentythreeAttachment.getContentType());
		assertEquals(twentythreeAttachment.getComment(), "Eleven Test File");
		assertEquals(twentythreeAttachment.getSize(), 0);
	}

	@After
	public void stop() {
		String message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "First Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"First Test Forum"), "First Test Topic"), "Second Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Third Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Fourth Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Fifth Test Post"));
		assertTrue(message.equals("OK"));
		message = removePost(driver, new Post(new Topic(new Forum(
				"Second Test Forum"), "Fourth Test Topic"), "Sixth Test Post"));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"First Test Topic",
						Arrays.asList(new Post[] { new Post("First Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("First Test Forum"),
						"Second Test Topic",
						Arrays.asList(new Post[] { new Post("Second Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("Second Test Forum"),
						"Third Test Topic",
						Arrays.asList(new Post[] { new Post("Third Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeTopic(
				driver,
				new Topic(
						new Forum("Second Test Forum"),
						"Fourth Test Topic",
						Arrays.asList(new Post[] { new Post("Fourth Test Body") })));
		assertTrue(message.equals("OK"));
		message = removeForum(driver, new Forum("First Test Forum"),
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"),
				SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
