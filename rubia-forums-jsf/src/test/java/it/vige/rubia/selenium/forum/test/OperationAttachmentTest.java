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
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreateAttachment.addAttachments;
import static it.vige.rubia.selenium.forum.action.CreateAttachment.addAttachmentsAndSave;
import static it.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveAttachment.removeAllAttachments;
import static it.vige.rubia.selenium.forum.action.RemoveAttachment.removeAttachments;
import static it.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static it.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfCurrentPost;
import static it.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfTopics;
import static it.vige.rubia.selenium.forum.action.VerifyPost.goTo;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import it.vige.rubia.model.Topic;
import it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest;

@RunWith(Arquillian.class)
@RunAsClient
public class OperationAttachmentTest {

	@Drone
	private static WebDriver driver;

	@BeforeClass
	public static void setUp() {
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
		message = createPost(driver,
				new Post(new Topic(new Forum("First Test Forum"), "First Test Topic"), "First Test Post",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))));
		assertTrue(message.equals("First Test Post"));
		message = createPost(driver,
				new Post(new Topic(new Forum("First Test Forum"), "First Test Topic"), "Second Test Post",
						asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
								new Attachment("third", "Third Test File"))));
		assertTrue(message.equals("Second Test Post"));
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
				new Poll("Third Test Question",
						asList(new PollOption[] {
								new PollOption("Fifth Test with Truncation over 25 characters Answer"),
								new PollOption("Sixth Test Answer") }),
						9)));
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
		message = createPost(driver,
				new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
						asList(new Attachment("seventh", "Seventh Test File"),
								new Attachment("eight", "Eight Test File"),
								new Attachment("ninth", "Ninth Test File"))));
		assertTrue(message.equals("Third Test Post"));
		message = createPost(driver,
				new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Fourth Test Post",
						asList(new Attachment("ten", "Ten Test File"), new Attachment("eleven", "Eleven Test File"),
								new Attachment("twelve", "Twelve Test File"))));
		assertTrue(message.equals("Fourth Test Post"));
		message = createPost(driver,
				new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"),
						"Fifth Test with Truncation over 25 characters Post",
						asList(new Attachment("thirteen", "Thirteen Test File"),
								new Attachment("fourteen", "Fourteen Test File"),
								new Attachment("fifteen", "Fifteen Test File"))));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Post"));
		message = createPost(driver,
				new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Sixth Test Post",
						asList(new Attachment("sixteen", "Sixteen Test File"),
								new Attachment("seventeen", "Seventeen Test File"),
								new Attachment("eighteen", "Eighteen Test File"))));
		assertTrue(message.equals("Sixth Test Post"));
	}

	@Test
	public void verifyRemoveAllAttachments() {
		Post post = new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
				asList(new Attachment("newseventh", "New Seventh Test File"),
						new Attachment("neweight", "New Eight Test File"),
						new Attachment("newninth", "New Ninth Test File")));
		goTo(driver, post);
		addAttachments(driver, post);
		removeAllAttachments(driver, post);
		List<Attachment> attachments = getAttachmentsOfCurrentPost(driver, post);
		assertEquals(0, attachments.size());
		post = new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
				asList(new Attachment("seventh", "Seventh Test File"), new Attachment("eight", "Eight Test File"),
						new Attachment("ninth", "Ninth Test File")));
		goTo(driver, post);
		addAttachmentsAndSave(driver, post);
		attachments = getAttachmentsOfCurrentPost(driver, post);
		assertEquals(3, attachments.size());
	}

	@Test
	public void verifyCreatedAttachments() {
		List<Attachment> attachments = getAttachmentsOfTopics(driver, new Topic("First Test Topic"),
				new Topic("Second Test Topic"), new Topic("Third Test Topic"), new Topic("Fourth Test Topic"));
		assertEquals(30, attachments.size());

		Attachment firstAttachment = attachments.get(0);
		assertTrue(firstAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(firstAttachment.getContent()).equals("first"));
		assertNull(firstAttachment.getContentType());
		assertEquals("First Test File", firstAttachment.getComment());
		assertEquals(5, firstAttachment.getSize());

		Attachment secondAttachment = attachments.get(1);
		assertTrue(secondAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(secondAttachment.getContent()).equals("second"));
		assertNull(secondAttachment.getContentType());
		assertEquals("Second Test File", secondAttachment.getComment());
		assertEquals(6, secondAttachment.getSize());

		Attachment thirdAttachment = attachments.get(2);
		assertTrue(thirdAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(thirdAttachment.getContent()).equals("third"));
		assertNull(thirdAttachment.getContentType());
		assertEquals("Third Test File", thirdAttachment.getComment());
		assertEquals(5, thirdAttachment.getSize());

		Attachment fourthAttachment = attachments.get(3);
		assertTrue(fourthAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(fourthAttachment.getContent()).equals("first"));
		assertNull(fourthAttachment.getContentType());
		assertEquals("First Test File", fourthAttachment.getComment());
		assertEquals(5, fourthAttachment.getSize());

		Attachment fifthAttachment = attachments.get(4);
		assertTrue(fifthAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(fifthAttachment.getContent()).equals("second"));
		assertNull(fifthAttachment.getContentType());
		assertEquals("Second Test File", fifthAttachment.getComment());
		assertEquals(6, fifthAttachment.getSize());

		Attachment sixthAttachment = attachments.get(5);
		assertTrue(sixthAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(sixthAttachment.getContent()).equals("third"));
		assertNull(sixthAttachment.getContentType());
		assertEquals("Third Test File", sixthAttachment.getComment());
		assertEquals(5, sixthAttachment.getSize());

		Attachment seventhAttachment = attachments.get(6);
		assertTrue(seventhAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(seventhAttachment.getContent()).equals("first"));
		assertNull(seventhAttachment.getContentType());
		assertEquals("First Test File", seventhAttachment.getComment());
		assertEquals(5, seventhAttachment.getSize());

		Attachment eightAttachment = attachments.get(7);
		assertTrue(eightAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(eightAttachment.getContent()).equals("second"));
		assertNull(eightAttachment.getContentType());
		assertEquals("Second Test File", eightAttachment.getComment());
		assertEquals(6, eightAttachment.getSize());

		Attachment ninthAttachment = attachments.get(8);
		assertTrue(ninthAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(ninthAttachment.getContent()).equals("third"));
		assertNull(ninthAttachment.getContentType());
		assertEquals("Third Test File", ninthAttachment.getComment());
		assertEquals(5, ninthAttachment.getSize());

		Attachment tenAttachment = attachments.get(9);
		assertTrue(tenAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(tenAttachment.getContent()).equals("first"));
		assertNull(tenAttachment.getContentType());
		assertEquals("First Test File", tenAttachment.getComment());
		assertEquals(5, tenAttachment.getSize());

		Attachment elevenAttachment = attachments.get(10);
		assertTrue(elevenAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(elevenAttachment.getContent()).equals("second"));
		assertNull(elevenAttachment.getContentType());
		assertEquals("Second Test File", elevenAttachment.getComment());
		assertEquals(6, elevenAttachment.getSize());

		Attachment twelveAttachment = attachments.get(11);
		assertTrue(twelveAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(twelveAttachment.getContent()).equals("third"));
		assertNull(twelveAttachment.getContentType());
		assertEquals("Third Test File", twelveAttachment.getComment());
		assertEquals(5, twelveAttachment.getSize());

		Attachment thirteenAttachment = attachments.get(12);
		assertTrue(thirteenAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(thirteenAttachment.getContent()).equals("first"));
		assertNull(thirteenAttachment.getContentType());
		assertEquals(thirteenAttachment.getComment(), "First Test File");
		assertEquals(thirteenAttachment.getSize(), 5);

		Attachment fourteenAttachment = attachments.get(13);
		assertTrue(fourteenAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(fourteenAttachment.getContent()).equals("second"));
		assertNull(fourteenAttachment.getContentType());
		assertEquals("Second Test File", fourteenAttachment.getComment());
		assertEquals(6, fourteenAttachment.getSize());

		Attachment fifteenAttachment = attachments.get(14);
		assertTrue(fifteenAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(fifteenAttachment.getContent()).equals("third"));
		assertNull(fifteenAttachment.getContentType());
		assertEquals("Third Test File", fifteenAttachment.getComment());
		assertEquals(5, fifteenAttachment.getSize());

		Attachment sixteenAttachment = attachments.get(15);
		assertTrue(sixteenAttachment.getName().matches("fourth(.*).txt"));
		assertNotNull(new String(sixteenAttachment.getContent()).equals("fourth"));
		assertNull(sixteenAttachment.getContentType());
		assertEquals("Fourth Test File", sixteenAttachment.getComment());
		assertEquals(6, sixteenAttachment.getSize());

		Attachment seventeenAttachment = attachments.get(16);
		assertTrue(seventeenAttachment.getName().matches("fifth(.*).txt"));
		assertNotNull(new String(seventeenAttachment.getContent()).equals("fifth"));
		assertNull(seventeenAttachment.getContentType());
		assertEquals("Fifth Test with Truncation over 25 characters File", seventeenAttachment.getComment());
		assertEquals(5, seventeenAttachment.getSize());

		Attachment eighteenAttachment = attachments.get(17);
		assertTrue(eighteenAttachment.getName().matches("sixth(.*).txt"));
		assertNotNull(new String(eighteenAttachment.getContent()).equals("sixth"));
		assertNull(eighteenAttachment.getContentType());
		assertEquals("Sixth Test File", eighteenAttachment.getComment());
		assertEquals(5, eighteenAttachment.getSize());

		Attachment ninteenAttachment = attachments.get(18);
		assertTrue(ninteenAttachment.getName().matches("seventh(.*).txt"));
		assertNotNull(new String(ninteenAttachment.getContent()).equals("seventh"));
		assertNull(ninteenAttachment.getContentType());
		assertEquals("Seventh Test File", ninteenAttachment.getComment());
		assertEquals(7, ninteenAttachment.getSize());

		Attachment twentyAttachment = attachments.get(19);
		assertTrue(twentyAttachment.getName().matches("eight(.*).txt"));
		assertNotNull(new String(twentyAttachment.getContent()).equals("eight"));
		assertNull(twentyAttachment.getContentType());
		assertEquals("Eight Test File", twentyAttachment.getComment());
		assertEquals(5, twentyAttachment.getSize());

		Attachment twentyoneAttachment = attachments.get(20);
		assertTrue(twentyoneAttachment.getName().matches("ninth(.*).txt"));
		assertNotNull(new String(twentyoneAttachment.getContent()).equals("ninth"));
		assertNull(twentyoneAttachment.getContentType());
		assertEquals("Ninth Test File", twentyoneAttachment.getComment());
		assertEquals(5, twentyoneAttachment.getSize());

		Attachment twentytwoAttachment = attachments.get(21);
		assertTrue(twentytwoAttachment.getName().matches("ten(.*).txt"));
		assertNotNull(new String(twentytwoAttachment.getContent()).equals("ten"));
		assertNull(twentytwoAttachment.getContentType());
		assertEquals("Ten Test File", twentytwoAttachment.getComment());
		assertEquals(3, twentytwoAttachment.getSize());

		Attachment twentythreeAttachment = attachments.get(22);
		assertTrue(twentythreeAttachment.getName().matches("eleven(.*).txt"));
		assertNotNull(new String(twentythreeAttachment.getContent()).equals("eleven"));
		assertNull(twentythreeAttachment.getContentType());
		assertEquals("Eleven Test File", twentythreeAttachment.getComment());
		assertEquals(6, twentythreeAttachment.getSize());

		Attachment twentytfourAttachment = attachments.get(23);
		assertTrue(twentytfourAttachment.getName().matches("twelve(.*).txt"));
		assertNotNull(new String(twentytfourAttachment.getContent()).equals("twelve"));
		assertNull(twentytfourAttachment.getContentType());
		assertEquals("Twelve Test File", twentytfourAttachment.getComment());
		assertEquals(6, twentytfourAttachment.getSize());

		Attachment twentytfiveAttachment = attachments.get(24);
		assertTrue(twentytfiveAttachment.getName().matches("thirteen(.*).txt"));
		assertNotNull(new String(twentytfiveAttachment.getContent()).equals("thirteen"));
		assertNull(twentytfiveAttachment.getContentType());
		assertEquals("Thirteen Test File", twentytfiveAttachment.getComment());
		assertEquals(8, twentytfiveAttachment.getSize());

		Attachment twentysixAttachment = attachments.get(25);
		assertTrue(twentysixAttachment.getName().matches("fourteen(.*).txt"));
		assertNotNull(new String(twentysixAttachment.getContent()).equals("fourteen"));
		assertNull(twentysixAttachment.getContentType());
		assertEquals("Fourteen Test File", twentysixAttachment.getComment());
		assertEquals(8, twentysixAttachment.getSize());

		Attachment twentysevenAttachment = attachments.get(26);
		assertTrue(twentysevenAttachment.getName().matches("fifteen(.*).txt"));
		assertNotNull(new String(twentysevenAttachment.getContent()).equals("fifteen"));
		assertNull(twentysevenAttachment.getContentType());
		assertEquals("Fifteen Test File", twentysevenAttachment.getComment());
		assertEquals(7, twentysevenAttachment.getSize());

		Attachment twentyeightAttachment = attachments.get(27);
		assertTrue(twentyeightAttachment.getName().matches("sixteen(.*).txt"));
		assertNotNull(new String(twentyeightAttachment.getContent()).equals("sixteen"));
		assertNull(twentyeightAttachment.getContentType());
		assertEquals("Sixteen Test File", twentyeightAttachment.getComment());
		assertEquals(7, twentyeightAttachment.getSize());

		Attachment twentynineAttachment = attachments.get(28);
		assertTrue(twentynineAttachment.getName().matches("seventeen(.*).txt"));
		assertNotNull(new String(twentynineAttachment.getContent()).equals("seventeen"));
		assertNull(twentynineAttachment.getContentType());
		assertEquals("Seventeen Test File", twentynineAttachment.getComment());
		assertEquals(9, twentynineAttachment.getSize());

		Attachment thirtyAttachment = attachments.get(29);
		assertTrue(thirtyAttachment.getName().matches("eighteen(.*).txt"));
		assertNotNull(new String(thirtyAttachment.getContent()).equals("eighteen"));
		assertNull(thirtyAttachment.getContentType());
		assertEquals("Eighteen Test File", thirtyAttachment.getComment());
		assertEquals(8, thirtyAttachment.getSize());
	}

	@AfterClass
	public static void stop() {
		Post post = new Post(new Topic(new Forum("First Test Forum"), "First Test Topic"), "First Test Body",
				asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File")));
		String message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("First Test Forum"), "Second Test Topic"), "Second Test Body",
				asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new Post(new Topic(new Forum("Second Test Forum"), "Third Test Topic"), "Third Test Body",
				asList(new Attachment("first", "First Test File"), new Attachment("second", "Second Test File"),
						new Attachment("third", "Third Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Fourth Test Body",
				asList(new Attachment("fourth", "Fourth Test File"),
						new Attachment("fifth", "Fifth Test with Truncation over 25 characters File"),
						new Attachment("sixth", "Sixth Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new Post(new Topic(new Forum("First Test Forum"), "First Test Topic"), "First Test Post",
				asList(new Attachment[] { new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"), new Attachment("third", "Third Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		post.getTopic().setSubject(RE + "First Test Topic");
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("First Test Forum"), RE + "First Test Topic"), "Second Test Post",
				asList(new Attachment[] { new Attachment("first", "First Test File"),
						new Attachment("second", "Second Test File"), new Attachment("third", "Third Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
				asList(new Attachment[] { new Attachment("seventh", "Seventh Test File"),
						new Attachment("eight", "Eight Test File"), new Attachment("ninth", "Ninth Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		post.getTopic().setSubject(RE + "Fourth Test Topic");
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"), RE + "Fourth Test Topic"), "Fourth Test Post",
				asList(new Attachment[] { new Attachment("ten", "Ten Test File"),
						new Attachment("eleven", "Eleven Test File"), new Attachment("twelve", "Twelve Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"), RE + "Fourth Test Topic"),
				"Fifth Test with Truncation over 25 characters Post",
				asList(new Attachment[] { new Attachment("thirteen", "Thirteen Test File"),
						new Attachment("fourteen", "Fourteen Test File"),
						new Attachment("fifteen", "Fifteen Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new Post(new Topic(new Forum("Second Test Forum"), RE + "Fourth Test Topic"), "Sixth Test Post",
				asList(new Attachment[] { new Attachment("sixteen", "Sixteen Test File"),
						new Attachment("seventeen", "Seventeen Test File"),
						new Attachment("eighteen", "Eighteen Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		message = removeForum(driver, new Forum("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new Forum("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new Category("First Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new Category("Second Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
