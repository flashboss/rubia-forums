package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;

public class LockForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator RESULT_LOCK_FORUM = jq("[class='successtext']");

	public static String lockForum(AjaxSelenium selenium, String forumName) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator lockForum = xp("//tr[td/strong/text()='" + forumName
				+ "']/td[2]/form/ul/li[4]/a/img");
		selenium.click(lockForum);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_LOCK_FORUM);
		return message;
	}

}
