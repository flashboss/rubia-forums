package it.vige.rubia.selenium.forum.action;

import static it.vige.rubia.properties.OperationType.SUBSCRIBE;
import static it.vige.rubia.selenium.Constants.OK;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static it.vige.rubia.selenium.forum.model.Links.FORUM_TEMPLATE_LINK;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Topic;
import it.vige.rubia.properties.NotificationType;
import it.vige.rubia.properties.OperationType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SubscriptionTopic {

	public static final String REGISTRATION_BUTTON = "forumfloatright";
	public static final String BUTTONS = "forumformbuttonrow";
	public static final String SELECT_NOTIFICATION = "//form/select";
	public static final String TOPIC_TITLE = "//form/h4";

	public static String registerTopic(WebDriver driver, Topic topic,
			NotificationType notificationType, OperationType buttonType) {
		WebElement topicSubject = driver.findElement(linkText(topic
				.getSubject()));
		topicSubject.click();
		WebElement registerButton = driver.findElement(
				className(REGISTRATION_BUTTON)).findElement(xpath("//a[3]"));
		registerButton.click();
		WebElement notificationOption = driver
				.findElement(xpath(SELECT_NOTIFICATION));
		notificationOption.sendKeys(notificationType + "");
		String topicTitle = driver.findElement(xpath(TOPIC_TITLE)).getText();
		WebElement confirmButton = driver.findElement(className(BUTTONS))
				.findElements(className("buttonMed")).get(buttonType.ordinal());
		confirmButton.click();
		WebElement forumLink = driver.findElement(FORUM_TEMPLATE_LINK
				.getValue());
		forumLink.click();
		return topicTitle;
	}

	public static String unregisterTopic(WebDriver driver, Topic topic) {
		goTo(driver, topic);
		WebElement registerButton = driver.findElement(
				className(REGISTRATION_BUTTON)).findElement(xpath("//a[3]"));
		registerButton.click();
		return OK;
	}

	public static boolean isRegistered(WebDriver driver, Topic topic) {
		WebElement topicSubject = driver.findElement(linkText(topic
				.getSubject()));
		topicSubject.click();
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
