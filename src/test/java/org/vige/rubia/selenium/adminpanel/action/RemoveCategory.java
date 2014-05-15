package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class RemoveCategory {

	public static final JQueryLocator REMOVE_CATEGORY_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_REMOVE_CATEGORY = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String removeCategory(AjaxSelenium selenium, String categoryTitle) {
		selenium.click(REMOVE_CATEGORY_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_REMOVE_CATEGORY);
		return message;
	}
}
