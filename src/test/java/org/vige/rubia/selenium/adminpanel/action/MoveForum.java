package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;

public class MoveForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator MOVE_FORUM_DOWN_LINK = jq("[name='arrowdown']");
	public static final JQueryLocator MOVE_FORUM_UP_LINK = jq("[name='arrowup']");

	public static String moveForum(AjaxSelenium selenium, String forumName,
			Move move) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator moveForum = xp("//tr[td/strong/text()='" + forumName
				+ "']/td[2]/form/ul/li[3]/a/img");
		selenium.click(moveForum);
		selenium.waitForPageToLoad();
		selenium.click(move == Move.UP ? MOVE_FORUM_UP_LINK
				: MOVE_FORUM_DOWN_LINK);
		selenium.waitForPageToLoad();
		String movedForumName = moveForum.getAsString();
		return movedForumName;
	}

}
