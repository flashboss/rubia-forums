package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Topic;

public class LockTopic {

	public static final String LOCK_TOPIC = "miviewtopicbody16";
	public static final String RESULT_LOCK_TOPIC = "forumtitletext";

	public static String lockTopic(WebDriver driver, Topic topic) {
		goTo(driver, topic);
		WebElement lockTopic = driver.findElement(id(LOCK_TOPIC)).findElement(xpath("form/ul/li[4]/a"));
		lockTopic.click();
		WebElement resultLockTopic = driver.findElement(className(RESULT_LOCK_TOPIC))
				.findElement(xpath("//table/tbody/tr/td"));
		String message = resultLockTopic.getText();
		return message;
	}

}
