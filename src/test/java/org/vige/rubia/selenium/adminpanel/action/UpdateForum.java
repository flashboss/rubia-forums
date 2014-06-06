package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.By.className;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UpdateForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String UPDATE_FORUM_NAME_INPUT_TEXT = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_name") + ":']/input[5]";
	public static final String UPDATE_FORUM_DESCRIPTION_INPUT_TEXT = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_desc") + ":']/textarea";
	public static final String SELECT_CATEGORY = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category")
			+ ":']/select";
	public static final String UPDATE_FORUM_LINK = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category")
			+ ":']/input[6]";
	public static final String RESULT_UPDATE_FORUM = "successtext";

	public static String updateForum(WebDriver driver, String oldForumName,
			String[] newParameters) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement updateForum = driver
				.findElement(xpath("//td[strong/text()='" + oldForumName
						+ "']/form/a"));
		updateForum.click();
		WebElement updateForumNameInputText = driver
				.findElement(xpath(UPDATE_FORUM_NAME_INPUT_TEXT));
		updateForumNameInputText.clear();
		updateForumNameInputText.sendKeys(newParameters[0]);
		WebElement updateForumDescriptionInputText = driver
				.findElement(xpath(UPDATE_FORUM_DESCRIPTION_INPUT_TEXT));
		updateForumDescriptionInputText.clear();
		updateForumDescriptionInputText.sendKeys(newParameters[1]);
		WebElement categoryOption = driver.findElement(xpath(SELECT_CATEGORY));
		categoryOption.sendKeys(newParameters[2]);
		WebElement updateForumLink = driver
				.findElement(xpath(UPDATE_FORUM_LINK));
		updateForumLink.click();
		WebElement resultUpdateForum = driver
				.findElement(className(RESULT_UPDATE_FORUM));
		String message = resultUpdateForum.getText();
		return message;
	}
}
