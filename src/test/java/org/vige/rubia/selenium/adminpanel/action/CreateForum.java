package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;

public class CreateForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator CREATE_FORUM_LINK = jq("[name='newForum']");
	public static final JQueryLocator RESULT_CREATE_FORUM = jq("[id='receiptSelectionForm:noReceiptMsg']");

	public static String createForum(AjaxSelenium selenium, String forumName,
			String forumDescription, String categoryTitle) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		selenium.click(CREATE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_CREATE_FORUM);
		return message;
	}
}
