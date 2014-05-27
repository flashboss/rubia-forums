package org.vige.rubia.selenium.adminpanel.action;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.LocatorFactory.link;
import static org.jboss.test.selenium.locator.LocatorFactory.xp;

import java.util.ResourceBundle;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LinkLocator;
import org.jboss.test.selenium.locator.XpathLocator;
import org.jboss.test.selenium.locator.option.OptionLabelLocator;
import org.jboss.test.selenium.locator.option.OptionLocatorFactory;

public class MoveCategory {

	public static final LinkLocator ADMIN_PANEL_LINK = link(ResourceBundle
			.getBundle("ResourceJSF").getString("Admin_panel"));
	public static final JQueryLocator MOVE_CATEGORY_DOWN_LINK = jq("[name='arrowdown']");
	public static final JQueryLocator MOVE_CATEGORY_UP_LINK = jq("[name='arrowup']");

	public static String moveCategory(AjaxSelenium selenium,
			String categoryTitle, Move move) {
		selenium.click(ADMIN_PANEL_LINK);
		selenium.waitForPageToLoad();
		XpathLocator removeCategory = xp("//tr[td/strong/text()='"
				+ categoryTitle + "']/td[2]/form/ul/li[3]/a/img");
		selenium.click(removeCategory);
		selenium.waitForPageToLoad();
		selenium.click(move == Move.UP ? MOVE_CATEGORY_UP_LINK
				: MOVE_CATEGORY_DOWN_LINK);
		selenium.waitForPageToLoad();
		String movedCategoryTitle = removeCategory.getAsString();
		return movedCategoryTitle;
	}
}
