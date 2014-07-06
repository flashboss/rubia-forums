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
package org.vige.rubia.selenium.forum.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.model.Links.CATEGORY_TEMPLATE_LINK;
import static org.vige.rubia.selenium.forum.model.Links.FORUM_TEMPLATE_LINK;
import static org.vige.rubia.selenium.forum.model.Links.POST_TEMPLATE_LINK;
import static org.vige.rubia.selenium.forum.model.Links.TOPIC_TEMPLATE_LINK;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

public class VerifyAttachment {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String FORUM_TABLE = "forumtablestyle";
	public static final String FORUM_SUBJECT = "tbody/tr/td[not(@class = 'forumcategory')]/h3/a";
	public static final String FORUM_LINK = "miviewtopicbody3";
	public static final String SUBJECT_LINK = "//table[contains(@class,'forumtablestyle')]/tbody/tr/td[2]/h3/a";
	public static final String ATTACHMENT_LIST = "forumAttachmentTable";
	public static final String ATTACHMENT_NAME_OUTPUT_TEXT = "tbody/tr/td[2]";
	public static final String ATTACHMENT_COMMENT_OUTPUT_TEXT = "tbody/tr[2]/td[2]";
	public static final String ATTACHMENT_SIZE_OUTPUT_TEXT = "tbody/tr[3]/td[2]";
	public static final String BODY_OUTPUT_TEXT = "forumpostcontent";

	public static List<Attachment> getAttachmentsOfTopics(WebDriver driver,
			Topic... topics) {
		List<Attachment> attachments = new ArrayList<Attachment>();
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement tableComponent = driver.findElement(className(FORUM_TABLE));
		List<WebElement> forumSubjectComponents = tableComponent
				.findElements(xpath(FORUM_SUBJECT));
		int forumComponentSize = forumSubjectComponents.size();
		for (int j = 0; j < forumComponentSize; j++) {
			home = driver.findElement(linkText(HOME_LINK));
			home.click();
			tableComponent = driver.findElement(className(FORUM_TABLE));
			forumSubjectComponents = tableComponent
					.findElements(xpath(FORUM_SUBJECT));
			WebElement forumEl = forumSubjectComponents.get(j);
			forumEl.click();
			List<WebElement> subjectComponents = driver
					.findElements(xpath(SUBJECT_LINK));
			int subjectComponentsSize = subjectComponents.size();
			for (int i = 0; i < subjectComponentsSize; i++) {
				subjectComponents = driver.findElements(xpath(SUBJECT_LINK));
				List<String> topicNames = findTopicNames(topics);
				if (topicNames.contains(subjectComponents.get(i).getText())) {
					subjectComponents = driver
							.findElements(xpath(SUBJECT_LINK));
					WebElement subjectComponent = subjectComponents.get(i);
					subjectComponent.click();
					List<WebElement> postComponents = driver
							.findElements(className(BODY_OUTPUT_TEXT));
					for (WebElement postComponent : postComponents) {
						attachments.addAll(getAttachmentsOfCurrentPost(driver,
								postComponent));
					}
					String forumLinkText = driver.findElement(id(FORUM_LINK))
							.getText();
					driver.findElement(linkText(forumLinkText)).click();
				}
			}
		}
		return attachments;
	}

	public static List<Attachment> getAttachmentsOfCurrentPost(
			WebDriver driver, WebElement postComponent) {
		List<Attachment> attachments = new ArrayList<Attachment>();
		List<WebElement> attachmentComponents = postComponent
				.findElements(className(ATTACHMENT_LIST));
		for (WebElement attachmentComponent : attachmentComponents) {
			String attachmentName = attachmentComponent.findElement(
					xpath(ATTACHMENT_NAME_OUTPUT_TEXT)).getText();
			String attachmentComment = attachmentComponent.findElement(
					xpath(ATTACHMENT_COMMENT_OUTPUT_TEXT)).getText();
			String attachmentSize = attachmentComponent.findElement(
					xpath(ATTACHMENT_SIZE_OUTPUT_TEXT)).getText();
			Attachment attachment = new Attachment();
			attachment.setComment(attachmentComment);
			attachment.setName(attachmentName);
			attachment.setSize(new Integer(attachmentSize.split(" B")[0]));
			addParents(driver, attachment);
			attachments.add(attachment);
		}
		return attachments;
	}

	private static void addParents(WebDriver driver, Attachment attachment) {
		Post post = new Post();
		Message message = new Message();
		message.setSubject(driver.findElement(POST_TEMPLATE_LINK.getValue())
				.getText());
		post.setMessage(message);
		Topic topic = new Topic();
		topic.setSubject(driver.findElement(TOPIC_TEMPLATE_LINK.getValue())
				.getText());
		post.setTopic(topic);
		Forum forum = new Forum();
		forum.setName(driver.findElement(FORUM_TEMPLATE_LINK.getValue())
				.getText());
		topic.setForum(forum);
		Category category = new Category();
		category.setTitle(driver.findElement(CATEGORY_TEMPLATE_LINK.getValue())
				.getText());
		forum.setCategory(category);
		attachment.setPost(post);

	}

	private static List<String> findTopicNames(Topic[] topics) {
		List<String> topicNames = new ArrayList<String>();
		for (Topic topic : topics)
			topicNames.add(topic.getSubject());
		return topicNames;
	}
}
