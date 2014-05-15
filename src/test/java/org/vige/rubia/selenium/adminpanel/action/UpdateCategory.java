package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class UpdateCategory {

	public static final JQueryLocator UPDATE_CATEGORY_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_UPDATE_CATEGORY = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String updateCategory(AjaxSelenium selenium,
			String oldCategoryTitle, String newCategoryTitle) {
		selenium.click(UPDATE_CATEGORY_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_UPDATE_CATEGORY);
		return message;
	}

}
