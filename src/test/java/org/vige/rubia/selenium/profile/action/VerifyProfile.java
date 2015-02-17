package org.vige.rubia.selenium.profile.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Poster;

public class VerifyProfile {

	public static String POST_COUNT = "forumtablestyle";

	public static Poster verifyProfile(WebDriver driver, String userId) {
		Poster poster = new Poster(userId);
		WebElement postCount = driver.findElement(className(POST_COUNT))
				.findElement(xpath("tbody/tr[3]/td[2]"));
		int count = new Integer(postCount.getText().trim());
		for (int i = 0; i < count; i++)
			poster.incrementPostCount();
		return poster;
	}
}
