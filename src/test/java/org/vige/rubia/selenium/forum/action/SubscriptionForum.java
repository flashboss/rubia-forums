package org.vige.rubia.selenium.forum.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.properties.OperationType.SUBSCRIBE;
import static org.vige.rubia.selenium.Constants.OK;
import static org.vige.rubia.selenium.forum.action.VerifyForum.goTo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.properties.NotificationType;
import org.vige.rubia.properties.OperationType;

public class SubscriptionForum {

	public static final String REGISTRATION_BUTTON = "forumfloatright";
	public static final String BUTTONS = "forumformbuttonrow";
	public static final String SELECT_NOTIFICATION = "//form/select";
	public static final String FORUM_TITLE = "forumtitletext";

	public static String registerForum(WebDriver driver, Forum forum,
			NotificationType notificationType, OperationType buttonType) {
		goTo(driver, forum);
		WebElement registerButton = driver.findElement(
				className(REGISTRATION_BUTTON)).findElement(xpath("//a[3]"));
		registerButton.click();
		WebElement notificationOption = driver
				.findElement(xpath(SELECT_NOTIFICATION));
		notificationOption.sendKeys(notificationType + "");
		WebElement confirmButton = driver.findElement(className(BUTTONS))
				.findElements(className("buttonMed")).get(buttonType.ordinal());
		confirmButton.click();
		WebElement forumTitle = driver.findElement(className(FORUM_TITLE));
		return forumTitle.getText();
	}

	public static String unregisterForum(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement registerButton = driver.findElement(
				className(REGISTRATION_BUTTON)).findElement(xpath("//a[3]"));
		registerButton.click();
		return OK;
	}

	public static boolean isRegistered(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement registerButton = driver.findElement(
				className(REGISTRATION_BUTTON))
				.findElement(xpath("//a[3]/img"));
		if (registerButton.getAttribute("name").equalsIgnoreCase(
				SUBSCRIBE.name()))
			return false;
		else
			return true;
	}
}
