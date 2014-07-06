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
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

public class CreateTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String CREATE_TOPIC_LINK = "//div[@class='actionbuttons']/ul/li/a";
	public static final String SUBJECT_INPUT_TEXT = "post:SubjectInputText";
	public static final String BODY_INPUT_TEXT = "//iframe[contains(@title,'post:message:inp')]";
	public static final String QUESTION_INPUT_TEXT = "post:question";
	public static final String NEW_OPTION_INPUT_TEXT = "post:newOption";
	public static final String OPTION_INPUT_TEXT = "post:option_";
	public static final String UPDATE_OPTION_BUTTON = "post:UpdateOption_";
	public static final String RESET_OPTION_BUTTON = "post:UpdateOption_";
	public static final String ADD_OPTION_BUTTON = "buttonMed";
	public static final String DAYS_INPUT_TEXT = "post:pollDuration";
	public static final String FILE_CHOOSE_BUTTON = "rf-fu-inp";
	public static final String FILE_COMMENT_INPUT_TEXT = "Posttextarea";
	public static final String RESULT_ATTACHMENT_LIST = "rf-fu-itm";
	public static final String SUBMIT_BUTTON = "post:Submit";

	public static String createTopic(WebDriver driver, Topic topic) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forumEl = driver.findElement(linkText(topic.getForum()
				.getName()));
		forumEl.click();
		WebElement createTopic = driver.findElement(xpath(CREATE_TOPIC_LINK));
		createTopic.click();
		WebElement subjectInput = driver.findElement(id(SUBJECT_INPUT_TEXT));
		subjectInput.sendKeys(topic.getSubject());
		driver.switchTo().frame(driver.findElement(xpath(BODY_INPUT_TEXT)));
		WebElement bodytInput = driver.findElement(cssSelector("body"));
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].innerHTML = '"
						+ topic.getPosts().get(0).getMessage().getText() + "'",
				bodytInput);
		driver.switchTo().defaultContent();
		WebElement topicTypeInput = null;
		topicTypeInput = driver.findElements(xpath("//input[@type='radio']"))
				.get(topic.getType());
		topicTypeInput.click();
		WebElement questionInput = driver.findElement(id(QUESTION_INPUT_TEXT));
		questionInput.sendKeys(topic.getPoll().getTitle());
		createOptions(driver, topic.getPoll());
		WebElement daysInput = driver.findElement(id(DAYS_INPUT_TEXT));
		daysInput.clear();
		daysInput.sendKeys(topic.getPoll().getLength() + "");
		addAttachments(driver, topic.getPosts().get(0));
		WebElement operationButton = driver.findElement(id(SUBMIT_BUTTON));
		operationButton.click();
		WebElement resultCreateTopic = driver.findElement(linkText(topic
				.getSubject()));
		String updatedForum = resultCreateTopic.getText();
		return updatedForum;
	}

	public static String[] updateOptions(WebDriver driver, Poll poll) {
		List<PollOption> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver
						.findElement(id(OPTION_INPUT_TEXT + (i + 1)));
				optionInput.sendKeys(options.get(i).getQuestion());
				optionButton = driver
						.findElement(className(UPDATE_OPTION_BUTTON + (i + 1)));
				optionButton.click();
			}
		WebElement[] updatedElements = new WebElement[options.size()];
		for (int i = 0; i < options.size(); i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options.get(i).getQuestion() + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;

	}

	public static String[] deleteOptions(WebDriver driver, Poll poll) {
		List<PollOption> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver
						.findElement(id(OPTION_INPUT_TEXT + (i + 1)));
				optionInput.sendKeys(options.get(i).getQuestion());
				optionButton = driver.findElement(className(RESET_OPTION_BUTTON
						+ (i + 1)));
				optionButton.click();
			}
		WebElement[] updatedElements = new WebElement[options.size()];
		for (int i = 0; i < options.size(); i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options.get(i).getQuestion() + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;

	}

	public static String[] createOptions(WebDriver driver, Poll poll) {
		List<PollOption> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver.findElement(id(NEW_OPTION_INPUT_TEXT));
				optionInput.sendKeys(options.get(i).getQuestion());
				optionButton = driver
						.findElements(className(ADD_OPTION_BUTTON)).get(i * 2);
				optionButton.click();
			}
		WebElement[] updatedElements = new WebElement[options.size()];
		for (int i = 0; i < options.size(); i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options.get(i).getQuestion() + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;
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