package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RemoveForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String REMOVE_FORUM_LINK = "buttonMed";
	public static final String SELECT_TYPE = "//form/select";
	public static final String RESULT_REMOVE_FORUM = "successtext";

	public static String removeForum(WebDriver driver, String forumName,
			String removeType) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement removeForum = driver
				.findElement(xpath("//tr[td/strong/text()='" + forumName
						+ "']/td[2]/form/ul/li[3]/a/img"));
		removeForum.click();
		WebElement categoryOption = driver.findElement(xpath(SELECT_TYPE));
		categoryOption.sendKeys(removeType);
		WebElement removeForumLink = driver
				.findElement(className(REMOVE_FORUM_LINK));
		removeForumLink.click();
		WebElement resultRemoveForum = driver
				.findElement(className(RESULT_REMOVE_FORUM));
		String message = resultRemoveForum.getText();
		return message;
	}
}
