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
package it.vige.rubia.selenium.forum.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static it.vige.rubia.selenium.forum.model.Links.CATEGORY_TEMPLATE_LINK;
import static it.vige.rubia.selenium.forum.model.Links.FORUM_TEMPLATE_LINK;
import static it.vige.rubia.selenium.forum.model.Links.POST_TEMPLATE_LINK;
import static it.vige.rubia.selenium.forum.model.Links.TOPIC_TEMPLATE_LINK;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;

public class VerifyAttachment {

	public final static String download_url = getProperty("java.io.tmpdir") + "rubia_downloaded_files";

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String FORUM_TABLE = "forumtablestyle";
	public static final String FORUM_SUBJECT = "tbody/tr/td[not(@class = 'forumcategory')]/h3/a";
	public static final String FORUM_LINK = "miviewtopicbody3";
	public static final String SUBJECT_LINK = "//table[contains(@class,'forumtablestyle')]/tbody/tr/td[2]/h3/a";
	public static final String ATTACHMENT_LIST = "forumAttachmentTable";
	public static final String ATTACHMENT_NAME_OUTPUT_TEXT = "tbody/tr/td[2]";
	public static final String ATTACHMENT_COMMENT_OUTPUT_TEXT = "tbody/tr[2]/td[2]";
	public static final String ATTACHMENT_SIZE_OUTPUT_TEXT = "tbody/tr[3]/td[2]";
	public static final String ATTACHMENT_DOWNLOAD_LINK = "tbody/tr[2]/td[3]/a";
	public static final String BODY_OUTPUT_TEXT = "forumpostcontent";

	public static List<AttachmentBean> getAttachmentsOfTopics(WebDriver driver, TopicBean... topics) {
		List<AttachmentBean> attachments = new LinkedList<AttachmentBean>();
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement tableComponent = driver.findElement(className(FORUM_TABLE));
		List<WebElement> forumSubjectComponents = tableComponent.findElements(xpath(FORUM_SUBJECT));
		int forumComponentSize = forumSubjectComponents.size();
		for (int j = 0; j < forumComponentSize; j++) {
			home = driver.findElement(linkText(HOME_LINK));
			home.click();
			tableComponent = driver.findElement(className(FORUM_TABLE));
			forumSubjectComponents = tableComponent.findElements(xpath(FORUM_SUBJECT));
			WebElement forumEl = forumSubjectComponents.get(j);
			forumEl.click();
			List<WebElement> subjectComponents = driver.findElements(xpath(SUBJECT_LINK));
			int subjectComponentsSize = subjectComponents.size();
			for (int i = 0; i < subjectComponentsSize; i++) {
				subjectComponents = driver.findElements(xpath(SUBJECT_LINK));
				List<String> topicNames = findTopicNames(topics);
				if (topicNames.contains(subjectComponents.get(i).getText())) {
					subjectComponents = driver.findElements(xpath(SUBJECT_LINK));
					WebElement subjectComponent = subjectComponents.get(i);
					subjectComponent.click();
					List<WebElement> postComponents = driver.findElements(className(BODY_OUTPUT_TEXT));
					for (WebElement postComponent : postComponents) {
						attachments.addAll(getAttachmentsOfCurrentPost(driver, postComponent));
					}
					String forumLinkText = driver.findElement(id(FORUM_LINK)).getText();
					driver.findElement(linkText(forumLinkText)).click();
				}
			}
		}
		return attachments;
	}

	public static List<AttachmentBean> getAttachmentsOfCurrentPost(WebDriver driver, PostBean post) {
		goTo(driver, post.getTopic());
		return getAttachmentsOfCurrentPostInPage(driver, post);
	}

	public static List<AttachmentBean> getAttachmentsOfCurrentPostInPage(WebDriver driver, PostBean post) {
		WebElement postComponent = driver
				.findElement(xpath("//td[contains(p/text(),'" + post.getMessage().getText() + "')]"));
		return getAttachmentsOfCurrentPost(driver, postComponent);
	}

	private static List<AttachmentBean> getAttachmentsOfCurrentPost(WebDriver driver, WebElement postComponent) {
		List<AttachmentBean> attachments = new LinkedList<AttachmentBean>();
		List<WebElement> attachmentComponents = postComponent.findElements(className(ATTACHMENT_LIST));
		for (WebElement attachmentComponent : attachmentComponents) {
			AttachmentBean attachment = getAttachment(attachmentComponent);
			addParents(driver, attachment);
			attachments.add(attachment);
		}
		return attachments;
	}

	public static List<AttachmentBean> getAttachmentsOfCurrentPostInPageNoParent(WebDriver driver, PostBean post) {
		WebElement postComponent = driver
				.findElement(xpath("//td[contains(p/text(),'" + post.getMessage().getText() + "')]"));
		return getAttachmentsOfCurrentPostNoParent(driver, postComponent);
	}

	private static List<AttachmentBean> getAttachmentsOfCurrentPostNoParent(WebDriver driver, WebElement postComponent) {
		List<AttachmentBean> attachments = new LinkedList<AttachmentBean>();
		List<WebElement> attachmentComponents = postComponent.findElements(className(ATTACHMENT_LIST));
		for (WebElement attachmentComponent : attachmentComponents) {
			AttachmentBean attachment = getAttachment(attachmentComponent);
			attachments.add(attachment);
		}
		return attachments;
	}

	private static AttachmentBean getAttachment(WebElement attachmentComponent) {
		String attachmentName = attachmentComponent.findElement(xpath(ATTACHMENT_NAME_OUTPUT_TEXT)).getText();
		String attachmentComment = attachmentComponent.findElement(xpath(ATTACHMENT_COMMENT_OUTPUT_TEXT)).getText();
		String attachmentSize = attachmentComponent.findElement(xpath(ATTACHMENT_SIZE_OUTPUT_TEXT)).getText();
		attachmentComponent.findElement(xpath(ATTACHMENT_DOWNLOAD_LINK)).click();
		File file = new File(download_url + "/" + attachmentName);
		int attachmentSizeValue = parseInt(attachmentSize.split(" B")[0]);
		byte[] content = new byte[attachmentSizeValue];
		AttachmentBean attachment = new AttachmentBean();
		try {
			writeFile(content, file);
		} catch (IOException e) {
			return attachment;
		}
		attachment.setComment(attachmentComment);
		attachment.setName(attachmentName);
		attachment.setSize(attachmentSizeValue);
		attachment.setContent(content);
		return attachment;
	}

	private static void writeFile(byte[] content, File file) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(content);
		} catch (IOException e) {
			writeFile(content, file);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}

	}

	private static void addParents(WebDriver driver, AttachmentBean attachment) {
		PostBean post = new PostBean();
		MessageBean message = new MessageBean();
		message.setSubject(driver.findElement(POST_TEMPLATE_LINK.getValue()).getText());
		post.setMessage(message);
		TopicBean topic = new TopicBean();
		topic.setSubject(driver.findElement(TOPIC_TEMPLATE_LINK.getValue()).getText());
		post.setTopic(topic);
		ForumBean forum = new ForumBean();
		forum.setName(driver.findElement(FORUM_TEMPLATE_LINK.getValue()).getText());
		topic.setForum(forum);
		CategoryBean category = new CategoryBean();
		category.setTitle(driver.findElement(CATEGORY_TEMPLATE_LINK.getValue()).getText());
		forum.setCategory(category);
		attachment.setPost(post);

	}

	private static List<String> findTopicNames(TopicBean[] topics) {
		List<String> topicNames = new ArrayList<String>();
		for (TopicBean topic : topics)
			topicNames.add(topic.getSubject());
		return topicNames;
	}
}
