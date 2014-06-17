package org.vige.rubia.selenium.adminpanel.action;

import static org.vige.rubia.selenium.adminpanel.action.Move.UP;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.By.id;

import java.util.HashMap;
import java.util.Map;

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
		String formId = driver.findElement(
				xpath("//tr[td/strong/text()='" + forumName + "']/td[2]/form"))
				.getAttribute("id");
		WebElement moveForum = driver.findElement(id(formId)).findElement(
				xpath("ul/li['" + (move == UP ? 1 : 2) + "']/a/img"));
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
