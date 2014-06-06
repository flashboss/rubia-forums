package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;

import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.By.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MoveForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");

	public static Map<String, Integer> moveForum(WebDriver driver,
			String forumName, Move move) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement moveForum = driver
				.findElement(xpath("//tr[td/strong/text()='" + forumName
						+ "']/td[2]/form/ul/li["
						+ (move == Move.UP ? "1" : "2") + "]/a/img"));
		int firstPosition = findPosition(driver, forumName);
		moveForum.click();
		int newPosition = findPosition(driver, forumName);
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("firstPosition", firstPosition);
		result.put("newPosition", newPosition);
		return result;
	}

	private static int findPosition(WebDriver driver, String forumName) {
		WebElement moveForum = driver
				.findElement(xpath("//tr[td/strong/text()='" + forumName + "']"));
		return moveForum.getLocation().getY();
	}
}
