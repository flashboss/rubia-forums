package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Forum;

public class UnlockForum {

	public static final String LOCK_FORUM = "admintools";
	public static final String RESULT_LOCK_FORUM = "forumtitletext";

	public static String lockForum(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement lockForum = driver.findElement(className(LOCK_FORUM)).findElement(xpath("ul/li[2]/a"));
		lockForum.click();
		WebElement resultLockForum = driver.findElement(className(RESULT_LOCK_FORUM))
				.findElement(xpath("//table/tbody/tr/td"));
		String message = resultLockForum.getText();
		return message;
	}

}
