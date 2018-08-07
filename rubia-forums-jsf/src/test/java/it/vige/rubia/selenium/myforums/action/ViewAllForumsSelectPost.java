package it.vige.rubia.selenium.myforums.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllForums.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;

public class ViewAllForumsSelectPost {

	public static final String POST_LINK = "header";
	public static final String PROFILE_LINK = "";

	public static TopicBean selectPost(WebDriver driver, PostBean post) {
		goTo(driver);
		WebElement postLink = driver.findElements(className(POST_LINK)).get(1)
				.findElement(xpath("../tr/td/a[contains(text(),'" + post.getMessage().getSubject() + "')]"));
		postLink.click();
		return getTopic(driver);
	}

	public static TopicBean selectAllForumsPost(WebDriver driver, PostBean post) {
		ViewAllForumsUpdateForum.goTo(driver);
		WebElement postLink = driver.findElements(className(POST_LINK)).get(0)
				.findElement(xpath("../tr/td/a[contains(text(),'" + post.getMessage().getSubject() + "')]"));
		postLink.click();
		return getTopic(driver);
	}

	public static PosterBean selectProfile(WebDriver driver, PostBean post) {
		return null;
	}

}
