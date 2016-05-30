package it.vige.rubia.selenium.links.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewFooterLink {

	public static String FOOTER_LINK = "PoweredBy";

	public static void goTo(WebDriver driver) {
		WebElement footerLink = driver.findElement(className(FOOTER_LINK)).findElement(xpath("a"));
		footerLink.click();
	}

	public static String getPage(WebDriver driver) {
		goTo(driver);
		return driver.getTitle();
	}
}
