package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class UnlockForum {

	public static final JQueryLocator UNLOCK_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_UNLOCK_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String unlockForum(AjaxSelenium selenium, String forumName) {
		selenium.click(UNLOCK_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_UNLOCK_FORUM);
		return message;
	}

}
