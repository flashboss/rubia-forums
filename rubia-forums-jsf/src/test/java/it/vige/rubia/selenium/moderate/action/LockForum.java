package it.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Topic;

public class LockForum {

	public static final String LOCK_FORUM = "buttonMed";
	public static final String RESULT_LOCK_FORUM = "forumtitletext";

	public static String lockForum(WebDriver driver, Forum forum) {
		if (forum != null)
			for (Topic topic : forum.getTopics()) {
				WebElement topicToSelect = driver.findElement(linkText(topic.getSubject()))
						.findElement(xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement lockForum = driver.findElements(className(LOCK_FORUM)).get(2);
		lockForum.click();
		WebElement resultLockForum = driver.findElement(className(RESULT_LOCK_FORUM))
				.findElement(xpath("//table/tbody/tr/td"));
		String message = resultLockForum.getText();
		return message;
	}

	public static String unlockForum(WebDriver driver, Forum forum) {
		if (forum != null)
			for (Topic topic : forum.getTopics()) {
				WebElement topicToSelect = driver.findElement(linkText(topic.getSubject()))
						.findElement(xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement unlockForum = driver.findElements(className(LOCK_FORUM)).get(3);
		unlockForum.click();
		WebElement resultLockForum = driver.findElement(className(RESULT_LOCK_FORUM))
				.findElement(xpath("//table/tbody/tr/td"));
		String message = resultLockForum.getText();
		return message;
	}

}
