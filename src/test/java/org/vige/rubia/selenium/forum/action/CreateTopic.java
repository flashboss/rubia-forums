package org.vige.rubia.selenium.forum.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;

public class CreateTopic {

	public static final JQueryLocator CREATE_FORUM_LINK = jq("[href^='/magazzino/search/search_receipt']");
	public static final JQueryLocator RESULT_CREATE_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String createTopic(AjaxSelenium selenium, String subject,
			String description, String type, String question, String[] options, String days, byte[] attachments) {
		selenium.click(CREATE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_CREATE_FORUM);
		return message;
	}
}
