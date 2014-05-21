package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;

public class RemoveForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final XpathLocator REMOVE_FORUM_LINK = xp("//input[type='submit']");
	public static final JQueryLocator RESULT_REMOVE_FORUM = jq("[class='successtext']");

	public static String removeForum(AjaxSelenium selenium, String forumName,
			String removeType) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator removeForum = xp("//tr[td/strong/text()='" + forumName
				+ "']/td[2]/form/ul/li[3]/a/img");
		selenium.click(removeForum);
		selenium.waitForPageToLoad();
		selenium.click(REMOVE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_REMOVE_FORUM);
		return message;
	}
}
