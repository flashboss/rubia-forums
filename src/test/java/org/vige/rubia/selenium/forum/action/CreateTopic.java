package org.vige.rubia.selenium.forum.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.io.File;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
		WebElement createTopic = driver.findElement(xpath(CREATE_TOPIC_LINK));
		createTopic.click();
		WebElement subjectInput = driver.findElement(id(SUBJECT_INPUT_TEXT));
		subjectInput.sendKeys(subject);
		driver.switchTo().frame(driver.findElement(xpath(BODY_INPUT_TEXT)));
		WebElement bodytInput = driver.findElement(cssSelector("body"));
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].innerHTML = '" + body + "'", bodytInput);
		driver.switchTo().defaultContent();
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
		createOptions(driver, options);
		WebElement daysInput = driver.findElement(id(DAYS_INPUT_TEXT));
		daysInput.clear();
		daysInput.sendKeys(days + "");
		addAttachments(driver, attachments);
		WebElement operationButton = driver.findElement(id(operation.name()));
		operationButton.click();
		WebElement resultCreateTopic = driver
				.findElement(className(SUCCESS_OPERATION));
		String updatedForum = resultCreateTopic.getText();
		return updatedForum;
	}

	public static String[] updateOptions(WebDriver driver, String[] options) {
		if (options != null)
			for (int i = 0; i < options.length; i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver
						.findElement(id(OPTION_INPUT_TEXT + (i + 1)));
				optionInput.sendKeys(options[i]);
				optionButton = driver
						.findElement(className(UPDATE_OPTION_BUTTON + (i + 1)));
				optionButton.click();
			}
		WebElement[] updatedElements = new WebElement[options.length];
		for (int i = 0; i < options.length; i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options[i] + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;

	}

	public static String[] deleteOptions(WebDriver driver, String[] options) {
		if (options != null)
			for (int i = 0; i < options.length; i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver
						.findElement(id(OPTION_INPUT_TEXT + (i + 1)));
				optionInput.sendKeys(options[i]);
				optionButton = driver.findElement(className(RESET_OPTION_BUTTON
						+ (i + 1)));
				optionButton.click();
			}
		WebElement[] updatedElements = new WebElement[options.length];
		for (int i = 0; i < options.length; i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options[i] + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;

	}

	public static String[] createOptions(WebDriver driver, String[] options) {
		if (options != null)
			for (int i = 0; i < options.length; i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver.findElement(id(NEW_OPTION_INPUT_TEXT));
				optionInput.sendKeys(options[i]);
				optionButton = driver
						.findElements(className(ADD_OPTION_BUTTON)).get(i * 2);
				optionButton.click();
			}
		WebElement[] updatedElements = new WebElement[options.length];
		for (int i = 0; i < options.length; i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options[i] + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;
	}

	public static String addAttachments(WebDriver driver,
			Map<File, String> attachments) {
		if (attachments != null)
			for (File attachment : attachments.keySet()) {
				String comment = attachments.get(attachment);
				WebElement attachmentInput = driver
						.findElement(id(TO_IMPLEMENT));
				attachmentInput.sendKeys(attachment.getAbsolutePath());
				WebElement commentInput = driver.findElement(id(TO_IMPLEMENT));
				commentInput.sendKeys(comment);
			}
		WebElement attachmentButton = driver.findElement(id(""));
		attachmentButton.click();
		WebElement resultAttachmentnOperation = driver
				.findElement(className(""));
		String message = resultAttachmentnOperation.getText();
		return message;
	}

	public static String deleteAttachments(WebDriver driver,
			Map<File, String> attachments) {
		if (attachments != null)
			for (File attachment : attachments.keySet()) {
				String comment = attachments.get(attachment);
				WebElement attachmentInput = driver
						.findElement(id(TO_IMPLEMENT));
				attachmentInput.sendKeys(attachment.getAbsolutePath());
				WebElement commentInput = driver.findElement(id(TO_IMPLEMENT));
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