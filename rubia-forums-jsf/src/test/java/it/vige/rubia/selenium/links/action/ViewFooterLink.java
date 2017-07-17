package it.vige.rubia.selenium.links.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.selenium.forum.action.Write;

public class ViewFooterLink extends Write {

	public static String FOOTER_LINK = "PoweredBy";

	public static void goTo(WebDriver driver) {
		WebElement footerLink = driver.findElement(className(FOOTER_LINK))
				.findElement(xpath("a"));
		footerLink.click();
	}

	public static String getPage(WebDriver driver) {
		goTo(driver);
		returnToHome(driver);
		return driver.getTitle();
	}
}
