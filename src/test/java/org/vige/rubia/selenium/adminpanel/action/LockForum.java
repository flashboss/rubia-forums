package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;

import static org.openqa.selenium.By.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LockForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String RESULT_LOCK_FORUM = "successtext";

	public static String lockForum(WebDriver driver, String forumName) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement lockForum = driver
				.findElement(xpath("//tr[td/strong/text()='" + forumName
						+ "']/td[2]/form/ul/li[4]/a/img"));
		lockForum.click();
		WebElement resultLockForum = driver
				.findElement(className("successtext"));
		String message = resultLockForum.getText();
		return message;
	}

}
