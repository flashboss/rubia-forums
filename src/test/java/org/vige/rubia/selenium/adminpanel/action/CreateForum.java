package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class CreateForum {

	public static final JQueryLocator CREATE_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_CREATE_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String createForum(AjaxSelenium selenium, String forumName,
			String forumDescription, String categoryTitle) {
		selenium.click(CREATE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_CREATE_FORUM);
		return message;
	}
}
