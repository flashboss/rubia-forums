package org.vige.rubia.selenium.forum.action;

import static org.vige.rubia.selenium.forum.action.Operation.SUBMIT;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.className;

import java.io.File;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateTopic {

	public static final String CREATE_TOPIC_LINK = "newTopic";
	public static final String TO_IMPLEMENT = "to_implements";
	public static final String RESULT_CREATE_TOPIC = "successtext";

	public static String createTopic(WebDriver driver, String forumName,
			String subject, String body, String question, String[] options,
			int days, Map<File, String> attachments, Operation operation) {
		WebElement forum = driver.findElement(linkText(forumName));
		forum.click();
		WebElement createForum = driver.findElement(id(CREATE_TOPIC_LINK));
		createForum.click();
		WebElement subjectInput = driver.findElement(id(TO_IMPLEMENT));
		subjectInput.sendKeys(subject);
		WebElement bodytInput = driver.findElement(id(TO_IMPLEMENT));
		bodytInput.sendKeys(body);
		WebElement questionInput = driver.findElement(id(TO_IMPLEMENT));
		questionInput.sendKeys(question);
		createOptions(driver, options, SUBMIT);
		WebElement daysInput = driver.findElement(id(TO_IMPLEMENT));
		daysInput.sendKeys(days + "");
		addAttachments(driver, attachments, SUBMIT);
		WebElement operationButton = driver.findElement(id(operation.name()));
		operationButton.click();
		WebElement resultCreateTopic = driver
				.findElement(className(RESULT_CREATE_TOPIC));
		String message = resultCreateTopic.getText();
		return message;
	}

	public static String createOptions(WebDriver driver, String[] options,
			Operation operation) {
		if (options != null)
			for (String option : options) {
				WebElement optionInput = driver.findElement(id(TO_IMPLEMENT));
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