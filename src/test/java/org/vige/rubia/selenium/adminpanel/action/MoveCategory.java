package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;

public class MoveCategory {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));

	public static Map<String, Integer> moveCategory(AjaxSelenium selenium,
			String categoryTitle, Move move) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator moveCategory = xp("//tr[td/strong/text()='"
				+ categoryTitle + "']/td[2]/form/ul/li["
				+ (move == Move.UP ? "1" : "2") + "]/a/img");
		int firstPosition = findPosition(selenium, categoryTitle);
		selenium.click(moveCategory);
		selenium.waitForPageToLoad();
		int newPosition = findPosition(selenium, categoryTitle);
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("firstPosition", firstPosition);
		result.put("newPosition", newPosition);
		return result;
	}

	private static int findPosition(AjaxSelenium selenium, String categoryTitle) {
		XpathLocator moveCategory = xp("//tr[td/strong/text()='"
				+ categoryTitle + "']");
		return selenium.getElementIndex(moveCategory);
	}
}
