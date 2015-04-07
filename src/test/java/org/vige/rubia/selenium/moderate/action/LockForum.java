package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Topic;

public class LockForum {

	public static final String LOCK_FORUM = "tfoot";
	public static final String RESULT_LOCK_FORUM = "javax_faces_developmentstage_messages";

	public static String lockForum(WebDriver driver, Forum forum) {
		if (forum != null)
			for (Topic topic : forum.getTopics()) {
				WebElement topicToSelect = driver.findElement(
						linkText(topic.getSubject())).findElement(
						xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement lockForum = driver.findElement(tagName(LOCK_FORUM))
				.findElement(xpath("tr/td/input[3]"));
		lockForum.click();
		WebElement resultLockForum = driver.findElement(id(RESULT_LOCK_FORUM));
		String message = resultLockForum.getText();
		return message;
	}

	public static String unlockForum(WebDriver driver, Forum forum) {
		if (forum != null)
			for (Topic topic : forum.getTopics()) {
				WebElement topicToSelect = driver.findElement(
						linkText(topic.getSubject())).findElement(
						xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement unlockForum = driver.findElement(tagName(LOCK_FORUM))
				.findElement(xpath("tr/td/input[4]"));
		unlockForum.click();
		WebElement resultLockForum = driver.findElement(id(RESULT_LOCK_FORUM));
		String message = resultLockForum.getText();
		return message;
	}

}
