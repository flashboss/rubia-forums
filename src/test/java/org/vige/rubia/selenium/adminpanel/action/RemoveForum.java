package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class RemoveForum {

	public static final JQueryLocator REMOVE_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_REMOVE_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String removeForum(AjaxSelenium selenium, String forumName) {
		selenium.click(REMOVE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_REMOVE_FORUM);
		return message;
	}
}
