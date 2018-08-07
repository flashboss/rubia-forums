package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static org.openqa.selenium.By.linkText;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.TopicBean;

public class VerifyTopic {

	public static TopicBean verifyTopic(WebDriver driver, TopicBean topic) {
		WebElement topicElement = driver.findElement(linkText(topic.getSubject()));
		topicElement.click();
		return getTopic(driver);
	}
}
