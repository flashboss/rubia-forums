package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;
import org.jboss.test.selenium.locator.option.OptionLabelLocator;
import org.jboss.test.selenium.locator.option.OptionLocatorFactory;

public class CreateForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(getBundle("ResourceJSF").getString("Admin_panel"));
	public static final XpathLocator CREATE_FORUM_NAME_INPUT_TEXT = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_name")
			+ ":']/input[4]");
	public static final XpathLocator CREATE_FORUM_DESCRIPTION_INPUT_TEXT = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_desc")
			+ ":']/textarea");
	public static final XpathLocator SELECT_CATEGORY = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString(
					"Forum_category") + ":']/select");
	public static final XpathLocator CREATE_FORUM_BUTTON = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString(
					"Forum_category") + ":']/input[5]");
	public static final JQueryLocator RESULT_CREATE_FORUM = jq("[class='successtext']");

	public static String createForum(AjaxSelenium selenium, String forumName,
			String forumDescription, String categoryTitle) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator createForum = xp("//td[strong/text()='" + categoryTitle
				+ "']/form/div/a/img");
		selenium.click(createForum);
		selenium.waitForPageToLoad();
		selenium.type(CREATE_FORUM_NAME_INPUT_TEXT, forumName);
		selenium.type(CREATE_FORUM_DESCRIPTION_INPUT_TEXT, forumDescription);
		OptionLabelLocator categoryOption = OptionLocatorFactory
				.optionLabel(categoryTitle);
		selenium.select(SELECT_CATEGORY, categoryOption);
		selenium.click(CREATE_FORUM_BUTTON);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_CREATE_FORUM);
		return message;
	}
}
