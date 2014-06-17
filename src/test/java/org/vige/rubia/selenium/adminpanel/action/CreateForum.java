package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String CREATE_FORUM_NAME_INPUT_TEXT = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_name") + ":']/input[4]";
	public static final String CREATE_FORUM_DESCRIPTION_INPUT_TEXT = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_desc") + ":']/textarea";
	public static final String SELECT_CATEGORY = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category")
			+ ":']/select";
	public static final String CREATE_FORUM_BUTTON = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category")
			+ ":']/input[5]";
	public static final String RESULT_CREATE_FORUM = "successtext";

	public static String createForum(WebDriver driver, String forumName,
			String forumDescription, String categoryTitle) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		String formId = driver.findElement(
				xpath("//td[strong/text()='" + categoryTitle + "']/form"))
				.getAttribute("id");
		WebElement createForum = driver.findElement(id(formId)).findElement(
				xpath("div/a/img"));
		createForum.click();
		WebElement createForumNameInputText = driver
				.findElement(xpath(CREATE_FORUM_NAME_INPUT_TEXT));
		createForumNameInputText.sendKeys(forumName);
		WebElement createForumDescriptionInputText = driver
				.findElement(xpath(CREATE_FORUM_DESCRIPTION_INPUT_TEXT));
		createForumDescriptionInputText.sendKeys(forumDescription);
		WebElement categoryOption = driver.findElement(xpath(SELECT_CATEGORY));
		categoryOption.sendKeys(categoryTitle);
		WebElement createForumButton = driver
				.findElement(xpath(CREATE_FORUM_BUTTON));
		createForumButton.click();
		WebElement resultCreateForum = driver
				.findElement(className(RESULT_CREATE_FORUM));
		String message = resultCreateForum.getText();
		return message;
	}
}
