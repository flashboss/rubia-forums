package it.vige.rubia.selenium.myforums.action;

import static it.vige.rubia.selenium.forum.action.VerifyForum.getForum;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsUpdateForum.goTo;
import static it.vige.rubia.selenium.profile.action.VerifyProfile.verifyProfile;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;

public class ViewAllForumsSelectForum {

	public static final String PROFILE_LINK = "header";

	public static ForumBean selectForum(WebDriver driver, ForumBean forum) {
		goTo(driver);
		WebElement forumLink = driver.findElement(linkText(forum.getName()));
		forumLink.click();
		return getForum(driver);
	}

	public static PosterBean selectProfile(WebDriver driver, PostBean post) {
		ViewAllForums.goTo(driver);
		WebElement profileLink = driver.findElements(className(PROFILE_LINK)).get(1)
				.findElement(xpath("../tr/td/a[contains(text(),'" + post.getMessage().getSubject() + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		return verifyProfile(driver, userId);
	}

	public static PosterBean selectAllForumsProfile(WebDriver driver, PostBean post) {
		goTo(driver);
		WebElement profileLink = driver.findElements(className(PROFILE_LINK)).get(0)
				.findElement(xpath("../tr/td/a[contains(text(),'" + post.getMessage().getSubject() + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		return verifyProfile(driver, userId);
	}

}
