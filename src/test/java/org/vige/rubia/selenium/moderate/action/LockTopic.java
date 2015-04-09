package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.goTo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Topic;

public class LockTopic {

	public static final String LOCK_TOPIC = "miviewtopicbody16";
	public static final String RESULT_LOCK_TOPIC = "javax_faces_developmentstage_messages";

	public static String lockTopic(WebDriver driver, Topic topic) {
		goTo(driver, topic);
		WebElement lockTopic = driver.findElement(id(LOCK_TOPIC)).findElement(
				xpath("form/ul/li[4]/a"));
		lockTopic.click();
		WebElement resultLockTopic = driver.findElement(id(RESULT_LOCK_TOPIC));
		String message = resultLockTopic.getText();
		return message;
	}

}
