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
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.util.Collection;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Post;

public class CreatePost {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String REPLY_POST_BUTTON = "actionbuttons";
	public static final String BODY_INPUT_TEXT = "//iframe[contains(@title,'post:message:inp')]";
	public static final String FILE_CHOOSE_BUTTON = "rf-fu-inp";
	public static final String FILE_COMMENT_INPUT_TEXT = "Posttextarea";
	public static final String RESULT_ATTACHMENT_LIST = "rf-fu-itm";
	public static final String SUBMIT_BUTTON = "post:Submit";

	public static String createPost(WebDriver driver, Post post) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forumEl = driver.findElement(linkText(post.getTopic()
				.getForum().getName()));
		forumEl.click();
		WebElement topicSubject = driver.findElement(linkText(post.getTopic()
				.getSubject()));
		topicSubject.click();
		WebElement bodyText = driver.findElement(className(REPLY_POST_BUTTON))
				.findElement(xpath("a[2]"));
		bodyText.click();
		driver.switchTo().frame(driver.findElement(xpath(BODY_INPUT_TEXT)));
		WebElement bodytInput = driver.findElement(cssSelector("body"));
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].innerHTML = '" + post.getMessage().getText()
						+ "'", bodytInput);
		driver.switchTo().defaultContent();
		addAttachments(driver, post);
		WebElement operationButton = driver.findElement(id(SUBMIT_BUTTON));
		operationButton.click();
		WebElement resultCreatePost = driver
				.findElement(xpath("//td[contains(@class,forumpostcontent)]/p[contains(text(),'"
						+ post.getMessage().getText() + "')]"));
		String updatedPost = resultCreatePost.getText();
		return updatedPost;
	}

	public static String[] addAttachments(WebDriver driver, Post post) {
		Collection<Attachment> attachments = post.getAttachments();
		if (attachments != null) {
			int i = 0;
			for (Attachment attachment : attachments) {
				String comment = attachment.getComment();
				WebElement attachmentInput = driver
						.findElement(className(FILE_CHOOSE_BUTTON));
				attachmentInput.sendKeys(attachment.getName());
				WebElement commentInput = addComment(driver, i + 2);
				i++;
				commentInput.sendKeys(comment);
			}
		}
		List<WebElement> attachmentResultList = driver
				.findElements(className(RESULT_ATTACHMENT_LIST));
		String[] result = new String[attachmentResultList.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = attachmentResultList.get(i).getText();
		return result;
	}

	public static WebElement addComment(WebDriver driver, int index) {
		WebElement commentInput = null;
		try {
			commentInput = driver.findElements(
					className(FILE_COMMENT_INPUT_TEXT)).get(index);
		} catch (IndexOutOfBoundsException ex) {
		}
		if (commentInput == null)
			return addComment(driver, index);
		else
			return commentInput;
	}

	public static String deleteAttachments(WebDriver driver, Post post) {
		Collection<Attachment> attachments = post.getAttachments();
		if (attachments != null)
			for (Attachment attachment : attachments) {
				String comment = attachment.getComment();
				WebElement attachmentInput = driver.findElement(id(""));
				attachmentInput.sendKeys(attachment.getName());
				WebElement commentInput = driver.findElement(id(""));
				commentInput.sendKeys(comment);
			}
		WebElement attachmentButton = driver.findElement(id(""));
		attachmentButton.click();
		WebElement resultAttachmentnOperation = driver
				.findElement(className(""));
		String message = resultAttachmentnOperation.getText();
		return message;
	}

	public static String deleteAllAttachments(WebDriver driver) {
		WebElement attachmentButton = driver.findElement(id(""));
		attachmentButton.click();
		WebElement resultAttachmentnOperation = driver
				.findElement(className(""));
		String message = resultAttachmentnOperation.getText();
		return message;
	}
}