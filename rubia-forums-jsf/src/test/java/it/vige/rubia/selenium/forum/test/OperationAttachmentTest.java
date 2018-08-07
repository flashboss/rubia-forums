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
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.CREATED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_0_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.REMOVED_FORUM_1_MESSAGE;
import static it.vige.rubia.selenium.adminpanel.test.AdminPanelForumTest.SELECT_FORUM_TYPE;
import static it.vige.rubia.selenium.forum.action.CreatePost.createPost;
import static it.vige.rubia.selenium.forum.action.CreateTopic.createTopic;
import static it.vige.rubia.selenium.forum.action.RemoveAttachment.removeAttachments;
import static it.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static it.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfTopics;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import it.vige.rubia.selenium.adminpanel.test.AdminPanelCategoryTest;

@RunWith(Arquillian.class)
@RunAsClient
public class OperationAttachmentTest {

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
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "First Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("First Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "Second Test Post",
						asList(new AttachmentBean("first", "First Test File"),
								new AttachmentBean("second", "Second Test File"),
								new AttachmentBean("third", "Third Test File"))));
		assertTrue(message.equals("Second Test Post"));
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
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
						asList(new AttachmentBean("seventh", "Seventh Test File"),
								new AttachmentBean("eight", "Eight Test File"),
								new AttachmentBean("ninth", "Ninth Test File"))));
		assertTrue(message.equals("Third Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Fourth Test Post",
						asList(new AttachmentBean("ten", "Ten Test File"),
								new AttachmentBean("eleven", "Eleven Test File"),
								new AttachmentBean("twelve", "Twelve Test File"))));
		assertTrue(message.equals("Fourth Test Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"),
						"Fifth Test with Truncation over 25 characters Post",
						asList(new AttachmentBean("thirteen", "Thirteen Test File"),
								new AttachmentBean("fourteen", "Fourteen Test File"),
								new AttachmentBean("fifteen", "Fifteen Test File"))));
		assertTrue(message.equals("Fifth Test with Truncation over 25 characters Post"));
		message = createPost(driver,
				new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Sixth Test Post",
						asList(new AttachmentBean("sixteen", "Sixteen Test File"),
								new AttachmentBean("seventeen", "Seventeen Test File"),
								new AttachmentBean("eighteen", "Eighteen Test File"))));
		assertTrue(message.equals("Sixth Test Post"));
	}

	@Test
	public void verifyCreatedAttachments() {
		List<AttachmentBean> attachments = getAttachmentsOfTopics(driver, new TopicBean("First Test Topic"),
				new TopicBean("Second Test Topic"), new TopicBean("Third Test Topic"),
				new TopicBean("Fourth Test Topic"));
		assertEquals(30, attachments.size());

		AttachmentBean firstAttachment = attachments.get(0);
		assertTrue(firstAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(firstAttachment.getContent()).equals("first"));
		assertNull(firstAttachment.getContentType());
		assertEquals("First Test File", firstAttachment.getComment());
		assertEquals(5, firstAttachment.getSize());

		AttachmentBean secondAttachment = attachments.get(1);
		assertTrue(secondAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(secondAttachment.getContent()).equals("second"));
		assertNull(secondAttachment.getContentType());
		assertEquals("Second Test File", secondAttachment.getComment());
		assertEquals(6, secondAttachment.getSize());

		AttachmentBean thirdAttachment = attachments.get(2);
		assertTrue(thirdAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(thirdAttachment.getContent()).equals("third"));
		assertNull(thirdAttachment.getContentType());
		assertEquals("Third Test File", thirdAttachment.getComment());
		assertEquals(5, thirdAttachment.getSize());

		AttachmentBean fourthAttachment = attachments.get(3);
		assertTrue(fourthAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(fourthAttachment.getContent()).equals("first"));
		assertNull(fourthAttachment.getContentType());
		assertEquals("First Test File", fourthAttachment.getComment());
		assertEquals(5, fourthAttachment.getSize());

		AttachmentBean fifthAttachment = attachments.get(4);
		assertTrue(fifthAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(fifthAttachment.getContent()).equals("second"));
		assertNull(fifthAttachment.getContentType());
		assertEquals("Second Test File", fifthAttachment.getComment());
		assertEquals(6, fifthAttachment.getSize());

		AttachmentBean sixthAttachment = attachments.get(5);
		assertTrue(sixthAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(sixthAttachment.getContent()).equals("third"));
		assertNull(sixthAttachment.getContentType());
		assertEquals("Third Test File", sixthAttachment.getComment());
		assertEquals(5, sixthAttachment.getSize());

		AttachmentBean seventhAttachment = attachments.get(6);
		assertTrue(seventhAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(seventhAttachment.getContent()).equals("first"));
		assertNull(seventhAttachment.getContentType());
		assertEquals("First Test File", seventhAttachment.getComment());
		assertEquals(5, seventhAttachment.getSize());

		AttachmentBean eightAttachment = attachments.get(7);
		assertTrue(eightAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(eightAttachment.getContent()).equals("second"));
		assertNull(eightAttachment.getContentType());
		assertEquals("Second Test File", eightAttachment.getComment());
		assertEquals(6, eightAttachment.getSize());

		AttachmentBean ninthAttachment = attachments.get(8);
		assertTrue(ninthAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(ninthAttachment.getContent()).equals("third"));
		assertNull(ninthAttachment.getContentType());
		assertEquals("Third Test File", ninthAttachment.getComment());
		assertEquals(5, ninthAttachment.getSize());

		AttachmentBean tenAttachment = attachments.get(9);
		assertTrue(tenAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(tenAttachment.getContent()).equals("first"));
		assertNull(tenAttachment.getContentType());
		assertEquals("First Test File", tenAttachment.getComment());
		assertEquals(5, tenAttachment.getSize());

		AttachmentBean elevenAttachment = attachments.get(10);
		assertTrue(elevenAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(elevenAttachment.getContent()).equals("second"));
		assertNull(elevenAttachment.getContentType());
		assertEquals("Second Test File", elevenAttachment.getComment());
		assertEquals(6, elevenAttachment.getSize());

		AttachmentBean twelveAttachment = attachments.get(11);
		assertTrue(twelveAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(twelveAttachment.getContent()).equals("third"));
		assertNull(twelveAttachment.getContentType());
		assertEquals("Third Test File", twelveAttachment.getComment());
		assertEquals(5, twelveAttachment.getSize());

		AttachmentBean thirteenAttachment = attachments.get(12);
		assertTrue(thirteenAttachment.getName().matches("first(.*).txt"));
		assertNotNull(new String(thirteenAttachment.getContent()).equals("first"));
		assertNull(thirteenAttachment.getContentType());
		assertEquals(thirteenAttachment.getComment(), "First Test File");
		assertEquals(thirteenAttachment.getSize(), 5);

		AttachmentBean fourteenAttachment = attachments.get(13);
		assertTrue(fourteenAttachment.getName().matches("second(.*).txt"));
		assertNotNull(new String(fourteenAttachment.getContent()).equals("second"));
		assertNull(fourteenAttachment.getContentType());
		assertEquals("Second Test File", fourteenAttachment.getComment());
		assertEquals(6, fourteenAttachment.getSize());

		AttachmentBean fifteenAttachment = attachments.get(14);
		assertTrue(fifteenAttachment.getName().matches("third(.*).txt"));
		assertNotNull(new String(fifteenAttachment.getContent()).equals("third"));
		assertNull(fifteenAttachment.getContentType());
		assertEquals("Third Test File", fifteenAttachment.getComment());
		assertEquals(5, fifteenAttachment.getSize());

		AttachmentBean sixteenAttachment = attachments.get(15);
		assertTrue(sixteenAttachment.getName().matches("fourth(.*).txt"));
		assertNotNull(new String(sixteenAttachment.getContent()).equals("fourth"));
		assertNull(sixteenAttachment.getContentType());
		assertEquals("Fourth Test File", sixteenAttachment.getComment());
		assertEquals(6, sixteenAttachment.getSize());

		AttachmentBean seventeenAttachment = attachments.get(16);
		assertTrue(seventeenAttachment.getName().matches("fifth(.*).txt"));
		assertNotNull(new String(seventeenAttachment.getContent()).equals("fifth"));
		assertNull(seventeenAttachment.getContentType());
		assertEquals("Fifth Test with Truncation over 25 characters File", seventeenAttachment.getComment());
		assertEquals(5, seventeenAttachment.getSize());

		AttachmentBean eighteenAttachment = attachments.get(17);
		assertTrue(eighteenAttachment.getName().matches("sixth(.*).txt"));
		assertNotNull(new String(eighteenAttachment.getContent()).equals("sixth"));
		assertNull(eighteenAttachment.getContentType());
		assertEquals("Sixth Test File", eighteenAttachment.getComment());
		assertEquals(5, eighteenAttachment.getSize());

		AttachmentBean ninteenAttachment = attachments.get(18);
		assertTrue(ninteenAttachment.getName().matches("seventh(.*).txt"));
		assertNotNull(new String(ninteenAttachment.getContent()).equals("seventh"));
		assertNull(ninteenAttachment.getContentType());
		assertEquals("Seventh Test File", ninteenAttachment.getComment());
		assertEquals(7, ninteenAttachment.getSize());

		AttachmentBean twentyAttachment = attachments.get(19);
		assertTrue(twentyAttachment.getName().matches("eight(.*).txt"));
		assertNotNull(new String(twentyAttachment.getContent()).equals("eight"));
		assertNull(twentyAttachment.getContentType());
		assertEquals("Eight Test File", twentyAttachment.getComment());
		assertEquals(5, twentyAttachment.getSize());

		AttachmentBean twentyoneAttachment = attachments.get(20);
		assertTrue(twentyoneAttachment.getName().matches("ninth(.*).txt"));
		assertNotNull(new String(twentyoneAttachment.getContent()).equals("ninth"));
		assertNull(twentyoneAttachment.getContentType());
		assertEquals("Ninth Test File", twentyoneAttachment.getComment());
		assertEquals(5, twentyoneAttachment.getSize());

		AttachmentBean twentytwoAttachment = attachments.get(21);
		assertTrue(twentytwoAttachment.getName().matches("ten(.*).txt"));
		assertNotNull(new String(twentytwoAttachment.getContent()).equals("ten"));
		assertNull(twentytwoAttachment.getContentType());
		assertEquals("Ten Test File", twentytwoAttachment.getComment());
		assertEquals(3, twentytwoAttachment.getSize());

		AttachmentBean twentythreeAttachment = attachments.get(22);
		assertTrue(twentythreeAttachment.getName().matches("eleven(.*).txt"));
		assertNotNull(new String(twentythreeAttachment.getContent()).equals("eleven"));
		assertNull(twentythreeAttachment.getContentType());
		assertEquals("Eleven Test File", twentythreeAttachment.getComment());
		assertEquals(6, twentythreeAttachment.getSize());

		AttachmentBean twentytfourAttachment = attachments.get(23);
		assertTrue(twentytfourAttachment.getName().matches("twelve(.*).txt"));
		assertNotNull(new String(twentytfourAttachment.getContent()).equals("twelve"));
		assertNull(twentytfourAttachment.getContentType());
		assertEquals("Twelve Test File", twentytfourAttachment.getComment());
		assertEquals(6, twentytfourAttachment.getSize());

		AttachmentBean twentytfiveAttachment = attachments.get(24);
		assertTrue(twentytfiveAttachment.getName().matches("thirteen(.*).txt"));
		assertNotNull(new String(twentytfiveAttachment.getContent()).equals("thirteen"));
		assertNull(twentytfiveAttachment.getContentType());
		assertEquals("Thirteen Test File", twentytfiveAttachment.getComment());
		assertEquals(8, twentytfiveAttachment.getSize());

		AttachmentBean twentysixAttachment = attachments.get(25);
		assertTrue(twentysixAttachment.getName().matches("fourteen(.*).txt"));
		assertNotNull(new String(twentysixAttachment.getContent()).equals("fourteen"));
		assertNull(twentysixAttachment.getContentType());
		assertEquals("Fourteen Test File", twentysixAttachment.getComment());
		assertEquals(8, twentysixAttachment.getSize());

		AttachmentBean twentysevenAttachment = attachments.get(26);
		assertTrue(twentysevenAttachment.getName().matches("fifteen(.*).txt"));
		assertNotNull(new String(twentysevenAttachment.getContent()).equals("fifteen"));
		assertNull(twentysevenAttachment.getContentType());
		assertEquals("Fifteen Test File", twentysevenAttachment.getComment());
		assertEquals(7, twentysevenAttachment.getSize());

		AttachmentBean twentyeightAttachment = attachments.get(27);
		assertTrue(twentyeightAttachment.getName().matches("sixteen(.*).txt"));
		assertNotNull(new String(twentyeightAttachment.getContent()).equals("sixteen"));
		assertNull(twentyeightAttachment.getContentType());
		assertEquals("Sixteen Test File", twentyeightAttachment.getComment());
		assertEquals(7, twentyeightAttachment.getSize());

		AttachmentBean twentynineAttachment = attachments.get(28);
		assertTrue(twentynineAttachment.getName().matches("seventeen(.*).txt"));
		assertNotNull(new String(twentynineAttachment.getContent()).equals("seventeen"));
		assertNull(twentynineAttachment.getContentType());
		assertEquals("Seventeen Test File", twentynineAttachment.getComment());
		assertEquals(9, twentynineAttachment.getSize());

		AttachmentBean thirtyAttachment = attachments.get(29);
		assertTrue(thirtyAttachment.getName().matches("eighteen(.*).txt"));
		assertNotNull(new String(thirtyAttachment.getContent()).equals("eighteen"));
		assertNull(thirtyAttachment.getContentType());
		assertEquals("Eighteen Test File", thirtyAttachment.getComment());
		assertEquals(8, thirtyAttachment.getSize());
	}

	@AfterClass
	public static void stop() {
		PostBean post = new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"),
				"First Test Body",
				asList(new AttachmentBean("first", "First Test File"), new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File")));
		String message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new PostBean(new TopicBean(new ForumBean("First Test Forum"), "Second Test Topic"), "Second Test Body",
				asList(new AttachmentBean("first", "First Test File"), new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Third Test Topic"), "Third Test Body",
				asList(new AttachmentBean("first", "First Test File"), new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Fourth Test Body",
				asList(new AttachmentBean("fourth", "Fourth Test File"),
						new AttachmentBean("fifth", "Fifth Test with Truncation over 25 characters File"),
						new AttachmentBean("sixth", "Sixth Test File")));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		post = new PostBean(new TopicBean(new ForumBean("First Test Forum"), "First Test Topic"), "First Test Post",
				asList(new AttachmentBean[] { new AttachmentBean("first", "First Test File"),
						new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		post.getTopic().setSubject(RE + "First Test Topic");
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new PostBean(new TopicBean(new ForumBean("First Test Forum"), RE + "First Test Topic"),
				"Second Test Post",
				asList(new AttachmentBean[] { new AttachmentBean("first", "First Test File"),
						new AttachmentBean("second", "Second Test File"),
						new AttachmentBean("third", "Third Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new PostBean(new TopicBean(new ForumBean("Second Test Forum"), "Fourth Test Topic"), "Third Test Post",
				asList(new AttachmentBean[] { new AttachmentBean("seventh", "Seventh Test File"),
						new AttachmentBean("eight", "Eight Test File"),
						new AttachmentBean("ninth", "Ninth Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		post.getTopic().setSubject(RE + "Fourth Test Topic");
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new PostBean(new TopicBean(new ForumBean("Second Test Forum"), RE + "Fourth Test Topic"),
				"Fourth Test Post",
				asList(new AttachmentBean[] { new AttachmentBean("ten", "Ten Test File"),
						new AttachmentBean("eleven", "Eleven Test File"),
						new AttachmentBean("twelve", "Twelve Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new PostBean(new TopicBean(new ForumBean("Second Test Forum"), RE + "Fourth Test Topic"),
				"Fifth Test with Truncation over 25 characters Post",
				asList(new AttachmentBean[] { new AttachmentBean("thirteen", "Thirteen Test File"),
						new AttachmentBean("fourteen", "Fourteen Test File"),
						new AttachmentBean("fifteen", "Fifteen Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		post = new PostBean(new TopicBean(new ForumBean("Second Test Forum"), RE + "Fourth Test Topic"),
				"Sixth Test Post",
				asList(new AttachmentBean[] { new AttachmentBean("sixteen", "Sixteen Test File"),
						new AttachmentBean("seventeen", "Seventeen Test File"),
						new AttachmentBean("eighteen", "Eighteen Test File") }));
		message = removeAttachments(driver, post);
		assertTrue(message.equals(OK));
		message = removePost(driver, post);
		assertTrue(message.equals(OK));
		message = removeForum(driver, new ForumBean("First Test Forum"), "Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, new ForumBean("Second Test Forum"), SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, new CategoryBean("First Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, new CategoryBean("Second Test Category"),
				AdminPanelCategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}
}
