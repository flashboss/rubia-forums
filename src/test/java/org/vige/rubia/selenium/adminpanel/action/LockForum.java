package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class LockForum {

	public static final JQueryLocator LOCK_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_LOCK_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String lockForum(AjaxSelenium selenium, String forumName) {
		selenium.click(LOCK_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_LOCK_FORUM);
		return message;
	}

}
