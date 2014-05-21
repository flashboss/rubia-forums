package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;

public class RemoveCategory {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final XpathLocator REMOVE_CATEGORY_LINK = xp("//input[type='submit']");
	public static final JQueryLocator RESULT_REMOVE_CATEGORY = jq("[class='successtext']");

	public static String removeCategory(AjaxSelenium selenium,
			String categoryTitle, String removeType) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator removeCategory = xp("//tr[td/strong/text()='"
				+ categoryTitle + "']/td[2]/form/ul/li[3]/a/img");
		selenium.click(removeCategory);
		selenium.waitForPageToLoad();
		selenium.click(REMOVE_CATEGORY_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_REMOVE_CATEGORY);
		return message;
	}
}
