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

import java.util.Collection;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

public class RemoveAttachment {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String UPDATE_POST_BUTTON = "miviewtopicbody6";
	public static final String ATTACHMENT_DELETE_BUTTON = "buttonMed";

	public static String removeAttachments(WebDriver driver, Post post) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forum = driver.findElement(linkText(post.getTopic()
				.getForum().getName()));
		forum.click();
		WebElement topicEl = driver.findElement(linkText(post.getTopic()
				.getSubject()));
		topicEl.click();
		WebElement updatePostButton = driver
				.findElement(
						xpath("//tbody[contains(.,'"
								+ post.getMessage().getText() + "')]"))
				.findElement(id(UPDATE_POST_BUTTON))
				.findElement(xpath("ul/a[1]"));
		updatePostButton.click();
		Collection<Attachment> attachments = post.getAttachments();
		String message = "";
		for (Attachment attachment : attachments) {
			message = "";
			String comment = attachment.getComment();
			WebElement fileCommentInputText = driver
					.findElement(xpath("//textarea[text()='" + comment + "']"));
			WebElement attachmentDeleteButton = fileCommentInputText
					.findElement(xpath("../../..")).findElement(
							className(ATTACHMENT_DELETE_BUTTON));
			attachmentDeleteButton.click();
			try {
				driver.findElement(xpath("//textarea[text()='" + comment + "']"));
			} catch (NoSuchElementException ex) {
				message = "OK";
			}
		}
		return message;
	}

	public static String removeAllAttachments(WebDriver driver, Topic topic) {
		WebElement attachmentButton = driver.findElement(id(""));
		attachmentButton.click();
		WebElement resultAttachmentnOperation = driver
				.findElement(className(""));
		String message = resultAttachmentnOperation.getText();
		return message;
	}
}