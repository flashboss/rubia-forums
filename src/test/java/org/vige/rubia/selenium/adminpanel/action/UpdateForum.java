package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class UpdateForum {

	public static final JQueryLocator UPDATE_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_UPDATE_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String updateForum(AjaxSelenium selenium,
			String oldForumName, String[] newParameters) {
		selenium.click(UPDATE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_UPDATE_FORUM);
		return message;
	}

}
