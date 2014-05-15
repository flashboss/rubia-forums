package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class MoveForum {

	public static final JQueryLocator MOVE_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_MOVE_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String moveForum(AjaxSelenium selenium, String forumName, Move move) {
		selenium.click(MOVE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_MOVE_FORUM);
		return message;
	}

}
