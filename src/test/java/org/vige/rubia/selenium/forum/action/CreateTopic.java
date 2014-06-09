package org.vige.rubia.selenium.forum.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.Operation.SUBMIT;

import java.io.File;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String CREATE_TOPIC_LINK = "//div[@class='actionbuttons']/ul/li/a";
	public static final String SUBJECT_INPUT_TEXT = "post:SubjectInputText";
	public static final String BODY_INPUT_TEXT = "post:message:inp";
	public static final String QUESTION_INPUT_TEXT = "post:question";
	public static final String NEW_OPTION_INPUT_TEXT = "post:newOption";
	public static final String DAYS_INPUT_TEXT = "post:pollDuration";
	public static final String TO_IMPLEMENT = "to_implements";
	public final static String SUCCESS_OPERATION = "forumtitletext";

	public static String createTopic(WebDriver driver, String forumName,
			String subject, String body, TopicType topicType, String question,
			String[] options, int days, Map<File, String> attachments,
			Operation operation) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forum = driver.findElement(linkText(forumName));
		forum.click();
		WebElement createForum = driver.findElement(xpath(CREATE_TOPIC_LINK));
		createForum.click();
		WebElement subjectInput = driver.findElement(id(SUBJECT_INPUT_TEXT));
		subjectInput.sendKeys(subject);
		WebElement bodytInput = driver.findElement(id(BODY_INPUT_TEXT));
		bodytInput.sendKeys(body);
		WebElement topicTypeInput = null;
		switch (topicType) {
		case NORMAL:
			topicTypeInput = driver.findElements(
					xpath("//input[@type='radio']")).get(0);
			break;
		case IMPORTANT:
			topicTypeInput = driver.findElements(
					xpath("//input[@type='radio'][1]")).get(1);
			break;
		case ADVICE:
			topicTypeInput = driver.findElements(
					xpath("//input[@type='radio'][2]")).get(2);
			break;
		}
		topicTypeInput.click();
		WebElement questionInput = driver.findElement(id(QUESTION_INPUT_TEXT));
		questionInput.sendKeys(question);
		createOptions(driver, options, SUBMIT);
		WebElement daysInput = driver.findElement(id(DAYS_INPUT_TEXT));
		daysInput.sendKeys(days + "");
		addAttachments(driver, attachments, SUBMIT);
		WebElement operationButton = driver.findElement(id(operation.name()));
		operationButton.click();
		WebElement resultCreateTopic = driver
				.findElement(className(SUCCESS_OPERATION));
		String updatedForum = resultCreateTopic.getText();
		return updatedForum;
	}

	public static String createOptions(WebDriver driver, String[] options,
			Operation operation) {
		if (options != null)
			for (String option : options) {
				WebElement optionInput = driver
						.findElement(id(NEW_OPTION_INPUT_TEXT));
				optionInput.sendKeys(option);
			}
		WebElement optionButton = driver.findElement(id(operation.name()));
		optionButton.click();
		WebElement resultOptionOperation = driver.findElement(className(""));
		String message = resultOptionOperation.getText();
		return message;
	}

	public static String addAttachments(WebDriver driver,
			Map<File, String> attachments, Operation operation) {
		if (attachments != null)
			for (File attachment : attachments.keySet()) {
				String comment = attachments.get(attachment);
				WebElement attachmentInput = driver
						.findElement(id(TO_IMPLEMENT));
				attachmentInput.sendKeys(attachment.getAbsolutePath());
				WebElement commentInput = driver.findElement(id(TO_IMPLEMENT));
				commentInput.sendKeys(comment);
			}
		WebElement attachmentButton = driver.findElement(id(operation.name()));
		attachmentButton.click();
		WebElement resultAttachmentnOperation = driver
				.findElement(className(""));
		String message = resultAttachmentnOperation.getText();
		return message;
	}
}