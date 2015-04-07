package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.linkText;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Topic;

public class VerifyTopic {

	public static Topic verifyTopic(WebDriver driver, Topic topic) {
		WebElement topicElement = driver.findElement(linkText(topic
				.getSubject()));
		topicElement.click();
		return getTopic(driver);
	}
}
