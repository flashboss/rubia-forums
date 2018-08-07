package it.vige.rubia.selenium.myforums.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllForums.MY_FORUMS_LIST;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.goTo;
import static it.vige.rubia.selenium.profile.action.VerifyProfile.verifyProfile;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;

public class ViewAllTopicsSelectPost {

	public static final String POST_LINK = "../tr/td[3]";
	public static final String PROFILE_LINK = "../tr/td[3]";

	public static TopicBean selectPost(WebDriver driver, PostBean post) {
		goTo(driver);
		WebElement element = driver.findElements(className(MY_FORUMS_LIST)).get(0)
				.findElement(xpath(POST_LINK + "/a[contains(text(),'" + post.getMessage().getSubject() + "')]"));
		element.click();
		return getTopic(driver);
	}

	public static PosterBean selectProfile(WebDriver driver, PostBean post) {
		goTo(driver);
		WebElement element = driver.findElements(className(MY_FORUMS_LIST)).get(0)
				.findElement(xpath(PROFILE_LINK + "/a[contains(text(),'" + post.getMessage().getSubject() + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = element.getText();
		element.click();
		return verifyProfile(driver, userId);
	}

}
