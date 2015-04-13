package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static org.openqa.selenium.By.linkText;
import it.vige.rubia.model.Topic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VerifyTopic {

	public static Topic verifyTopic(WebDriver driver, Topic topic) {
		WebElement topicElement = driver.findElement(linkText(topic
				.getSubject()));
		topicElement.click();
		return getTopic(driver);
	}
}
