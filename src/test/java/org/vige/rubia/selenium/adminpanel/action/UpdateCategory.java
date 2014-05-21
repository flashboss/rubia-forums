package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;

public class UpdateCategory {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator UPDATE_CATEGORY_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_UPDATE_CATEGORY = jq("[class='successtext']");

	public static String updateCategory(AjaxSelenium selenium,
			String oldCategoryTitle, String newCategoryTitle) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		selenium.click(UPDATE_CATEGORY_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_UPDATE_CATEGORY);
		return message;
	}

}
