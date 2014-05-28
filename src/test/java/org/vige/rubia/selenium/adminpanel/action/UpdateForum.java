package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;
import static java.util.ResourceBundle.getBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;
import org.jboss.test.selenium.locator.option.OptionLabelLocator;
import org.jboss.test.selenium.locator.option.OptionLocatorFactory;

public class UpdateForum {

	public static final LinkLocator ADMIN_PANEL_LINK = link(getBundle(
			"ResourceJSF").getString("Admin_panel"));
	public static final XpathLocator UPDATE_FORUM_NAME_INPUT_TEXT = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_name") + ":']/input[5]");
	public static final XpathLocator UPDATE_FORUM_DESCRIPTION_INPUT_TEXT = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_desc") + ":']/textarea");
	public static final XpathLocator SELECT_CATEGORY = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category")
			+ ":']/select");
	public static final XpathLocator UPDATE_FORUM_LINK = xp("//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category")
			+ ":']/input[6]");
	public static final JQueryLocator RESULT_UPDATE_FORUM = jq("[class='successtext']");

	public static String updateForum(AjaxSelenium selenium,
			String oldForumName, String[] newParameters) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator updateForum = xp("//td[strong/text()='" + oldForumName
				+ "']/form/a");
		selenium.click(updateForum);
		selenium.waitForPageToLoad();
		selenium.type(UPDATE_FORUM_NAME_INPUT_TEXT, newParameters[0]);
		selenium.type(UPDATE_FORUM_DESCRIPTION_INPUT_TEXT, newParameters[1]);
		OptionLabelLocator categoryOption = OptionLocatorFactory
				.optionLabel(newParameters[2]);
		selenium.select(SELECT_CATEGORY, categoryOption);
		selenium.click(UPDATE_FORUM_LINK);
		selenium.waitForPageToLoad();
		String message = selenium.getText(RESULT_UPDATE_FORUM);
		return message;
	}

}
