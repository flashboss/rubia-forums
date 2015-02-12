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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.model.TopicType.ADVICE;
import static org.vige.rubia.model.TopicType.IMPORTANT;
import static org.vige.rubia.model.TopicType.NORMAL;
import static org.vige.rubia.selenium.Constants.OK;
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
import static org.vige.rubia.selenium.forum.action.CreateAttachment.addAttachments;
import static org.vige.rubia.selenium.forum.action.CreateAttachment.addAttachmentsAndSave;
import static org.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static org.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static org.vige.rubia.selenium.forum.action.RemoveAttachment.removeAllAttachments;
import static org.vige.rubia.selenium.forum.action.RemoveAttachment.removeAttachments;
import static org.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static org.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfCurrentPost;
import static org.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfTopics;
import static org.vige.rubia.selenium.forum.action.VerifyPost.goTo;

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
		message = createPost(
				driver,
				new Post(new Topic(new Forum("First Test Forum"),
						"First Test Topic"), "First Test Post", asList(
						new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File"))));
		assertTrue(message.equals("First Test Post"));
		message = createPost(
				driver,
				new Post(new Topic(new Forum("First Test Forum"),
						"First Test Topic"), "Second Test Post", asList(
						new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File"))));
		assertTrue(message.equals("Second Test Post"));
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
		message = createPost(
				driver,
				new Post(new Topic(new Forum("Second Test Forum"),
						"Fourth Test Topic"), "Third Test Post", asList(
						new Attachment("seventh", "Seventh Test File"),
						new Attachment("eight", "Eight Test File"),
						new Attachment("ninth", "Ninth Test File"))));
		assertTrue(message.equals("Third Test Post"));
		message = createPost(
				driver,
				new Post(new Topic(new Forum("Second Test Forum"),
						"Fourth Test Topic"), "Fourth Test Post", asList(
						new Attachment("ten", "Ten Test File"), new Attachment(
								"eleven", "Eleven Test File"), new Attachment(
								"twelve", "Twelve Test File"))));
		assertTrue(message.equals("Fourth Test Post"));
		message = createPost(
				driver,
				new Post(new Topic(new Forum("Second Test Forum"),
						"Fourth Test Topic"), "Fifth Test Post", asList(
						new Attachment("thirteen", "Thirteen Test File"),
						new Attachment("fourteen", "Fourteen Test File"),
						new Attachment("fifteen", "Fifteen Test File"))));
		assertTrue(message.equals("Fifth Test Post"));
		message = createPost(
				driver,
				new Post(new Topic(new Forum("Second Test Forum"),
						"Fourth Test Topic"), "Sixth Test Post", asList(
						new Attachment("sixteen", "Sixteen Test File"),
						new Attachment("seventeen", "Seventeen Test File"),
						new Attachment("eighteen", "Eighteen Test File"))));
		assertTrue(message.equals("Sixth Test Post"));
	}

	@Test
	public void verifyRemoveAllAttachments() {
		Post post = new Post(new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic"), "Third Test Post", asList(new Attachment(
				"newseventh", "New Seventh Test File"), new Attachment(
				"neweight", "New Eight Test File"), new Attachment("newninth",
				"New Ninth Test File")));
		goTo(driver, post);
		addAttachments(driver, post);
		removeAllAttachments(driver, post);
		List<Attachment> attachments = getAttachmentsOfCurrentPost(driver, post);
		assertEquals(attachments.size(), 0);
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic"), "Third Test Post", asList(new Attachment(
				"seventh", "Seventh Test File"), new Attachment("eight",
				"Eight Test File"), new Attachment("ninth", "Ninth Test File")));
		goTo(driver, post);
		addAttachmentsAndSave(driver, post);
		attachments = getAttachmentsOfCurrentPost(driver, post);
		assertEquals(attachments.size(), 3);
	}

	@Test
	public void verifyCreatedAttachments() {
		List<Attachment> attachments = getAttachmentsOfTopics(driver,
				new Topic("First Test Topic"), new Topic("Second Test Topic"),
				new Topic("Third Test Topic"), new Topic("Fourth Test Topic"));
		assertEquals(attachments.size(), 30);

		Attachment firstAttachment = attachments.get(0);
		assertTrue(firstAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(firstAttachment.getContent()).equals("first"));
		assertNull(firstAttachment.getContentType());
		assertEquals(firstAttachment.getComment(), "First Test File");
		assertEquals(firstAttachment.getSize(), 5);

		Attachment secondAttachment = attachments.get(1);
		assertTrue(secondAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(secondAttachment.getContent())
				.equals("second"));
		assertNull(secondAttachment.getContentType());
		assertEquals(secondAttachment.getComment(), "Second Test File");
		assertEquals(secondAttachment.getSize(), 6);

		Attachment thirdAttachment = attachments.get(2);
		assertTrue(thirdAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(thirdAttachment.getContent()).equals("third"));
		assertNull(thirdAttachment.getContentType());
		assertEquals(thirdAttachment.getComment(), "Third Test File");
		assertEquals(thirdAttachment.getSize(), 5);

		Attachment fourthAttachment = attachments.get(3);
		assertTrue(fourthAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(fourthAttachment.getContent()).equals("first"));
		assertNull(fourthAttachment.getContentType());
		assertEquals(fourthAttachment.getComment(), "First Test File");
		assertEquals(fourthAttachment.getSize(), 5);

		Attachment fifthAttachment = attachments.get(4);
		assertTrue(fifthAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(fifthAttachment.getContent()).equals("second"));
		assertNull(fifthAttachment.getContentType());
		assertEquals(fifthAttachment.getComment(), "Second Test File");
		assertEquals(fifthAttachment.getSize(), 6);

		Attachment sixthAttachment = attachments.get(5);
		assertTrue(sixthAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(sixthAttachment.getContent()).equals("third"));
		assertNull(sixthAttachment.getContentType());
		assertEquals(sixthAttachment.getComment(), "Third Test File");
		assertEquals(sixthAttachment.getSize(), 5);

		Attachment seventhAttachment = attachments.get(6);
		assertTrue(seventhAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(seventhAttachment.getContent())
				.equals("first"));
		assertNull(seventhAttachment.getContentType());
		assertEquals(seventhAttachment.getComment(), "First Test File");
		assertEquals(seventhAttachment.getSize(), 5);

		Attachment eightAttachment = attachments.get(7);
		assertTrue(eightAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(eightAttachment.getContent()).equals("second"));
		assertNull(eightAttachment.getContentType());
		assertEquals(eightAttachment.getComment(), "Second Test File");
		assertEquals(eightAttachment.getSize(), 6);

		Attachment ninthAttachment = attachments.get(8);
		assertTrue(ninthAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(ninthAttachment.getContent()).equals("third"));
		assertNull(ninthAttachment.getContentType());
		assertEquals(ninthAttachment.getComment(), "Third Test File");
		assertEquals(ninthAttachment.getSize(), 5);

		Attachment tenAttachment = attachments.get(9);
		assertTrue(tenAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(tenAttachment.getContent()).equals("first"));
		assertNull(tenAttachment.getContentType());
		assertEquals(tenAttachment.getComment(), "First Test File");
		assertEquals(tenAttachment.getSize(), 5);

		Attachment elevenAttachment = attachments.get(10);
		assertTrue(elevenAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(elevenAttachment.getContent())
				.equals("second"));
		assertNull(elevenAttachment.getContentType());
		assertEquals(elevenAttachment.getComment(), "Second Test File");
		assertEquals(elevenAttachment.getSize(), 6);

		Attachment twelveAttachment = attachments.get(11);
		assertTrue(twelveAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(twelveAttachment.getContent()).equals("third"));
		assertNull(twelveAttachment.getContentType());
		assertEquals(twelveAttachment.getComment(), "Third Test File");
		assertEquals(twelveAttachment.getSize(), 5);

		Attachment thirteenAttachment = attachments.get(12);
		assertTrue(thirteenAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(thirteenAttachment.getContent())
				.equals("first"));
		assertNull(thirteenAttachment.getContentType());
		assertEquals(thirteenAttachment.getComment(), "First Test File");
		assertEquals(thirteenAttachment.getSize(), 5);

		Attachment fourteenAttachment = attachments.get(13);
		assertTrue(fourteenAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(fourteenAttachment.getContent())
				.equals("second"));
		assertNull(fourteenAttachment.getContentType());
		assertEquals(fourteenAttachment.getComment(), "Second Test File");
		assertEquals(fourteenAttachment.getSize(), 6);

		Attachment fifteenAttachment = attachments.get(14);
		assertTrue(fifteenAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(fifteenAttachment.getContent())
				.equals("third"));
		assertNull(fifteenAttachment.getContentType());
		assertEquals(fifteenAttachment.getComment(), "Third Test File");
		assertEquals(fifteenAttachment.getSize(), 5);

		Attachment sixteenAttachment = attachments.get(15);
		assertTrue(sixteenAttachment.getName().matches("fourth(.*).txt"));
		assertNotNull(new String(sixteenAttachment.getContent())
				.equals("fourth"));
		assertNull(sixteenAttachment.getContentType());
		assertEquals(sixteenAttachment.getComment(), "Fourth Test File");
		assertEquals(sixteenAttachment.getSize(), 6);

		Attachment seventeenAttachment = attachments.get(16);
		assertTrue(seventeenAttachment.getName().matches("fifth(.*).txt"));
		assertNotNull(new String(seventeenAttachment.getContent())
				.equals("fifth"));
		assertNull(seventeenAttachment.getContentType());
		assertEquals(seventeenAttachment.getComment(), "Fifth Test File");
		assertEquals(seventeenAttachment.getSize(), 5);

		Attachment eighteenAttachment = attachments.get(17);
		assertTrue(eighteenAttachment.getName().matches("sixth(.*).txt"));
		assertNotNull(new String(eighteenAttachment.getContent())
				.equals("sixth"));
		assertNull(eighteenAttachment.getContentType());
		assertEquals(eighteenAttachment.getComment(), "Sixth Test File");
		assertEquals(eighteenAttachment.getSize(), 5);

		Attachment ninteenAttachment = attachments.get(18);
		assertTrue(ninteenAttachment.getName().matches("seventh(.*).txt"));
		assertNotNull(new String(ninteenAttachment.getContent())
				.equals("seventh"));
		assertNull(ninteenAttachment.getContentType());
		assertEquals(ninteenAttachment.getComment(), "Seventh Test File");
		assertEquals(ninteenAttachment.getSize(), 7);

		Attachment twentyAttachment = attachments.get(19);
		assertTrue(twentyAttachment.getName().matches("eight(.*).txt"));
		assertNotNull(new String(twentyAttachment.getContent()).equals("eight"));
		assertNull(twentyAttachment.getContentType());
		assertEquals(twentyAttachment.getComment(), "Eight Test File");
		assertEquals(twentyAttachment.getSize(), 5);

		Attachment twentyoneAttachment = attachments.get(20);
		assertTrue(twentyoneAttachment.getName().matches("ninth(.*).txt"));
		assertNotNull(new String(twentyoneAttachment.getContent())
				.equals("ninth"));
		assertNull(twentyoneAttachment.getContentType());
		assertEquals(twentyoneAttachment.getComment(), "Ninth Test File");
		assertEquals(twentyoneAttachment.getSize(), 5);

		Attachment twentytwoAttachment = attachments.get(21);
		assertTrue(twentytwoAttachment.getName().matches("ten(.*).txt"));
		assertNotNull(new String(twentytwoAttachment.getContent())
				.equals("ten"));
		assertNull(twentytwoAttachment.getContentType());
		assertEquals(twentytwoAttachment.getComment(), "Ten Test File");
		assertEquals(twentytwoAttachment.getSize(), 3);

		Attachment twentythreeAttachment = attachments.get(22);
		assertTrue(twentythreeAttachment.getName().matches("eleven(.*).txt"));
		assertNotNull(new String(twentythreeAttachment.getContent())
				.equals("eleven"));
		assertNull(twentythreeAttachment.getContentType());
		assertEquals(twentythreeAttachment.getComment(), "Eleven Test File");
		assertEquals(twentythreeAttachment.getSize(), 6);

		Attachment twentytfourAttachment = attachments.get(23);
		assertTrue(twentytfourAttachment.getName().matches("twelve(.*).txt"));
		assertNotNull(new String(twentytfourAttachment.getContent())
				.equals("twelve"));
		assertNull(twentytfourAttachment.getContentType());
		assertEquals(twentytfourAttachment.getComment(), "Twelve Test File");
		assertEquals(twentytfourAttachment.getSize(), 6);

		Attachment twentytfiveAttachment = attachments.get(24);
		assertTrue(twentytfiveAttachment.getName().matches("thirteen(.*).txt"));
		assertNotNull(new String(twentytfiveAttachment.getContent())
				.equals("thirteen"));
		assertNull(twentytfiveAttachment.getContentType());
		assertEquals(twentytfiveAttachment.getComment(), "Thirteen Test File");
		assertEquals(twentytfiveAttachment.getSize(), 8);

		Attachment twentysixAttachment = attachments.get(25);
		assertTrue(twentysixAttachment.getName().matches("fourteen(.*).txt"));
		assertNotNull(new String(twentysixAttachment.getContent())
				.equals("fourteen"));
		assertNull(twentysixAttachment.getContentType());
		assertEquals(twentysixAttachment.getComment(), "Fourteen Test File");
		assertEquals(twentysixAttachment.getSize(), 8);

		Attachment twentysevenAttachment = attachments.get(26);
		assertTrue(twentysevenAttachment.getName().matches("fifteen(.*).txt"));
		assertNotNull(new String(twentysevenAttachment.getContent())
				.equals("fifteen"));
		assertNull(twentysevenAttachment.getContentType());
		assertEquals(twentysevenAttachment.getComment(), "Fifteen Test File");
		assertEquals(twentysevenAttachment.getSize(), 7);

		Attachment twentyeightAttachment = attachments.get(27);
		assertTrue(twentyeightAttachment.getName().matches("sixteen(.*).txt"));
		assertNotNull(new String(twentyeightAttachment.getContent())
				.equals("sixteen"));
		assertNull(twentyeightAttachment.getContentType());
		assertEquals(twentyeightAttachment.getComment(), "Sixteen Test File");
		assertEquals(twentyeightAttachment.getSize(), 7);

		Attachment twentynineAttachment = attachments.get(28);
		assertTrue(twentynineAttachment.getName().matches("seventeen(.*).txt"));
		assertNotNull(new String(twentynineAttachment.getContent())
				.equals("seventeen"));
		assertNull(twentynineAttachment.getContentType());
		assertEquals(twentynineAttachment.getComment(), "Seventeen Test File");
		assertEquals(twentynineAttachment.getSize(), 9);

		Attachment thirtyAttachment = attachments.get(29);
		assertTrue(thirtyAttachment.getName().matches("eighteen(.*).txt"));
		assertNotNull(new String(thirtyAttachment.getContent())
				.equals("eighteen"));
		assertNull(thirtyAttachment.getContentType());
		assertEquals(thirtyAttachment.getComment(), "Eighteen Test File");
		assertEquals(thirtyAttachment.getSize(), 8);
	}

	@After
	public void stop() {
		Post post = new Post(new Topic(new Forum("First Test Forum"),
				"First Test Topic"), "First Test Body",
				asList(new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File")));
		String message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("First Test Forum"),
				"Second Test Topic"), "Second Test Body", asList(
				new Attachment("first", "First Test File"), new Attachment(
						"second", "Second Test File"), new Attachment("third",
						"Third Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Third Test Topic"), "Third Test Body",
				asList(new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic"), "Fourth Test Body", asList(
				new Attachment("fourth", "Fourth Test File"), new Attachment(
						"fifth", "Fifth Test File"), new Attachment("sixth",
						"Sixth Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new Post(new Topic(new Forum("First Test Forum"),
				"First Test Topic"), "First Test Post",
				asList(new Attachment[] {
						new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		post.getTopic().setSubject("Re: First Test Topic");
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("First Test Forum"),
				"Re: First Test Topic"), "Second Test Post",
				asList(new Attachment[] {
						new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Fourth Test Topic"), "Third Test Post",
				asList(new Attachment[] {
						new Attachment("seventh", "Seventh Test File"),
						new Attachment("eight", "Eight Test File"),
						new Attachment("ninth", "Ninth Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		post.getTopic().setSubject("Re: Fourth Test Topic");
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Re: Fourth Test Topic"), "Fourth Test Post",
				asList(new Attachment[] {
						new Attachment("ten", "Ten Test File"),
						new Attachment("eleven", "Eleven Test File"),
						new Attachment("twelve", "Twelve Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Re: Fourth Test Topic"), "Fifth Test Post",
				asList(new Attachment[] {
						new Attachment("thirteen", "Thirteen Test File"),
						new Attachment("fourteen", "Fourteen Test File"),
						new Attachment("fifteen", "Fifteen Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"),
				"Re: Fourth Test Topic"), "Sixth Test Post",
				asList(new Attachment[] {
						new Attachment("sixteen", "Sixteen Test File"),
						new Attachment("seventeen", "Seventeen Test File"),
						new Attachment("eighteen", "Eighteen Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
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
