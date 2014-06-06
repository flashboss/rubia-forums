package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;

import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.By.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MoveCategory {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");

	public static Map<String, Integer> moveCategory(WebDriver driver,
			String categoryTitle, Move move) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement moveCategory = driver
				.findElement(xpath("//tr[td/strong/text()='" + categoryTitle
						+ "']/td[2]/form/ul/li["
						+ (move == Move.UP ? "1" : "2") + "]/a/img"));
		int firstPosition = findPosition(driver, categoryTitle);
		moveCategory.click();
		int newPosition = findPosition(driver, categoryTitle);
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("firstPosition", firstPosition);
		result.put("newPosition", newPosition);
		return result;
	}

	private static int findPosition(WebDriver driver, String categoryTitle) {
		WebElement moveCategory = driver
				.findElement(xpath("//tr[td/strong/text()='" + categoryTitle
						+ "']"));
		return moveCategory.getLocation().getY();
	}
}
