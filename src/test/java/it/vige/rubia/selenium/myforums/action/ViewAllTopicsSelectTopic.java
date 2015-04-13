package it.vige.rubia.selenium.myforums.action;

import static it.vige.rubia.selenium.myforums.action.ViewAllForums.MY_FORUMS_LIST;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.selenium.profile.action.VerifyProfile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewAllTopicsSelectTopic {

	public static final String PROFILE_LINK = "../tr/td[2]/a";

	public static Poster selectProfile(WebDriver driver, Topic topic) {
		goTo(driver);
		WebElement element = driver.findElements(className(MY_FORUMS_LIST))
				.get(0).findElement(xpath(PROFILE_LINK));
		String userId = element.getText();
		element.click();
		return VerifyProfile.verifyProfile(driver, userId);
	}
}
