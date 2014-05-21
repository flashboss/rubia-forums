package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;

public class MoveForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator MOVE_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_MOVE_FORUM = jq("[class='successtext']");

	public static String moveForum(AjaxSelenium selenium, String forumName, Move move) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		selenium.click(MOVE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_MOVE_FORUM);
		return message;
	}

}
