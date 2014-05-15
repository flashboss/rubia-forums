package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class MoveCategory {

	public static final JQueryLocator MOVE_CATEGORY_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_MOVE_CATEGORY = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String moveCategory(AjaxSelenium selenium,
			String categoryTitle, Move move) {
		selenium.click(MOVE_CATEGORY_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_MOVE_CATEGORY);
		return message;
	}
}
