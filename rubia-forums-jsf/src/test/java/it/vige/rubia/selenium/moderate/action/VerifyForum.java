package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.ForumBean;

public class VerifyForum {

	public static final String MOD_TOOLS_LINK = "modtools";

	public static void goToModerate(WebDriver driver, ForumBean forum) {
		goTo(driver, forum);
		WebElement moderate = driver.findElement(className(MOD_TOOLS_LINK)).findElement(xpath("ul/li[2]/a"));
		moderate.click();
	}
}
