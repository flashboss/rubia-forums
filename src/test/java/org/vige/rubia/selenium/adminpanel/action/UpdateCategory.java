package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;
import static java.util.ResourceBundle.getBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;

public class UpdateCategory {

	public static final LinkLocator ADMIN_PANEL_LINK = link(getBundle(
			"ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator UPDATE_CATEGORY_NAME_INPUT_TEXT = jq("[type='text']");
	public static final JQueryLocator UPDATE_CATEGORY_BUTTON = jq("[type='submit']");
	public static final JQueryLocator RESULT_UPDATE_CATEGORY = jq("[class='successtext']");

	public static String updateCategory(AjaxSelenium selenium,
			String oldCategoryTitle, String newCategoryTitle) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		selenium.click(xp("//td[strong/text()='" + oldCategoryTitle
				+ "']/form[2]/a"));
		selenium.waitForPageToLoad();
		selenium.type(UPDATE_CATEGORY_NAME_INPUT_TEXT, newCategoryTitle);
		selenium.click(UPDATE_CATEGORY_BUTTON);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_UPDATE_CATEGORY);
		return message;
	}

}
