package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.id;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static java.util.ResourceBundle.getBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.IdLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;

public class CreateCategory {

	public static final LinkLocator ADMIN_PANEL_LINK = link(getBundle(
			"ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator CREATE_CATEGORY_LINK = jq("[name='newCategory']");
	public static final IdLocator CREATE_CATEGORY_TITLE_INPUT_TEXT = id("addCategoryForm:Category");
	public static final IdLocator CREATE_CATEGORY_BUTTON = id("addCategoryForm:editinline");
	public static final JQueryLocator RESULT_CREATE_CATEGORY = jq("[class='successtext']");

	public static String createCategory(AjaxSelenium selenium,
			String categoryTitle) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		selenium.click(CREATE_CATEGORY_LINK);
		selenium.waitForPageToLoad();
		selenium.type(CREATE_CATEGORY_TITLE_INPUT_TEXT, categoryTitle);
		selenium.click(CREATE_CATEGORY_BUTTON);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_CREATE_CATEGORY);
		return message;
	}
}
