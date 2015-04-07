package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyForum.goTo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;

public class VerifyForum {

	public static final String MOD_TOOLS_LINK = "modtools";

	public static void goToModerate(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement moderate = driver.findElement(className(MOD_TOOLS_LINK))
				.findElement(xpath("ul/li[2]/a"));
		moderate.click();
	}
}
