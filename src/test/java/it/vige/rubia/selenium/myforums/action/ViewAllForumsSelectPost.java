package it.vige.rubia.selenium.myforums.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static it.vige.rubia.selenium.myforums.action.ViewAllForums.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewAllForumsSelectPost {

	public static final String POST_LINK = "header";
	public static final String PROFILE_LINK = "";

	public static Topic selectPost(WebDriver driver, Post post) {
		goTo(driver);
		WebElement postLink = driver
				.findElements(className(POST_LINK))
				.get(1)
				.findElement(
						xpath("../tr/td/a[contains(text(),'"
								+ post.getMessage().getSubject() + "')]"));
		postLink.click();
		return getTopic(driver);
	}

	public static Topic selectAllForumsPost(WebDriver driver, Post post) {
		ViewAllForumsUpdateForum.goTo(driver);
		WebElement postLink = driver
				.findElements(className(POST_LINK))
				.get(0)
				.findElement(
						xpath("../tr/td/a[contains(text(),'"
								+ post.getMessage().getSubject() + "')]"));
		postLink.click();
		return getTopic(driver);
	}

	public static Poster selectProfile(WebDriver driver, Post post) {
		return null;
	}

}
