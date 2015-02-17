package org.vige.rubia.selenium.myforums.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyForum.getForum;
import static org.vige.rubia.selenium.myforums.action.ViewAllForumsUpdateForum.goTo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import static org.vige.rubia.selenium.profile.action.VerifyProfile.*;

public class ViewAllForumsSelectForum {

	public static final String PROFILE_LINK = "header";

	public static Forum selectForum(WebDriver driver, Forum forum) {
		goTo(driver);
		WebElement forumLink = driver.findElement(linkText(forum.getName()));
		forumLink.click();
		return getForum(driver);
	}

	public static Poster selectProfile(WebDriver driver, Post post) {
		ViewAllForums.goTo(driver);
		WebElement profileLink = driver
				.findElements(className(PROFILE_LINK))
				.get(1)
				.findElement(
						xpath("../tr/td/a[contains(text(),'"
								+ post.getMessage().getSubject() + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		return verifyProfile(driver, userId);
	}

	public static Poster selectAllForumsProfile(WebDriver driver, Post post) {
		goTo(driver);
		WebElement profileLink = driver
				.findElements(className(PROFILE_LINK))
				.get(0)
				.findElement(
						xpath("../tr/td/a[contains(text(),'"
								+ post.getMessage().getSubject() + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		return verifyProfile(driver, userId);
	}

}
