package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;

public class MoveForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));

	public static Map<String, Integer> moveForum(AjaxSelenium selenium,
			String forumName, Move move) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator moveForum = xp("//tr[td/strong/text()='" + forumName
				+ "']/td[2]/form/ul/li[" + (move == Move.UP ? "1" : "2")
				+ "]/a/img");
		int firstPosition = findPosition(selenium, forumName);
		selenium.click(moveForum);
		selenium.waitForPageToLoad();
		int newPosition = findPosition(selenium, forumName);
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("firstPosition", firstPosition);
		result.put("newPosition", newPosition);
		return result;
	}

	private static int findPosition(AjaxSelenium selenium, String forumName) {
		XpathLocator moveForum = xp("//tr[td/strong/text()='" + forumName
				+ "']");
		return selenium.getElementIndex(moveForum);
	}

}
