package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Forum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VerifyForum {

	public static final String MOD_TOOLS_LINK = "modtools";

	public static void goToModerate(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement moderate = driver.findElement(className(MOD_TOOLS_LINK))
				.findElement(xpath("ul/li[2]/a"));
		moderate.click();
	}
}
