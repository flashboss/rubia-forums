package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyForum.goTo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;

public class LockTopic {

	public static final String LOCK_FORUM = "admintools";
	public static final String RESULT_LOCK_FORUM = "javax_faces_developmentstage_messages";

	public static String lockForum(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement lockForum = driver.findElement(className(LOCK_FORUM))
				.findElement(xpath("ul/li[2]/a"));
		lockForum.click();
		WebElement resultLockForum = driver.findElement(id(RESULT_LOCK_FORUM));
		String message = resultLockForum.getText();
		return message;
	}

}
