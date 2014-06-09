package org.vige.rubia.selenium.forum.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RemoveTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String REMOVE_TOPIC_BUTTON = "newTopic";
	public static final String CONFIRM_REMOVE_TOPIC_BUTTON = "to_implement";
	public static final String RESULT_REMOVE_TOPIC = "successtext";

	public static String removeTopic(WebDriver driver, String forumName,
			String subject, Operation operation) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forum = driver.findElement(linkText(forumName));
		forum.click();
		WebElement topic = driver.findElement(linkText(subject));
		topic.click();
		WebElement removeTopicButton = driver
				.findElement(linkText(REMOVE_TOPIC_BUTTON));
		removeTopicButton.click();
		WebElement confirmRemoveTopicButton = driver
				.findElement(linkText(CONFIRM_REMOVE_TOPIC_BUTTON));
		confirmRemoveTopicButton.click();
		WebElement resultCreateTopic = driver
				.findElement(className(RESULT_REMOVE_TOPIC));
		String message = resultCreateTopic.getText();
		return message;
	}

}
