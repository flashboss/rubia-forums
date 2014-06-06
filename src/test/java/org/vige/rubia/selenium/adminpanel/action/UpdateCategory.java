package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UpdateCategory {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String UPDATE_CATEGORY_NAME_INPUT_TEXT = "//input[@type='text']";
	public static final String UPDATE_CATEGORY_BUTTON = "//input[@type='submit']";
	public static final String RESULT_UPDATE_CATEGORY = "successtext";

	public static String updateCategory(WebDriver driver,
			String oldCategoryTitle, String newCategoryTitle) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement updateCategoryLink = driver
				.findElement(xpath("//td[strong/text()='" + oldCategoryTitle
						+ "']/form[2]/a"));
		updateCategoryLink.click();
		WebElement updateCategoryNameInputText = driver
				.findElement(xpath(UPDATE_CATEGORY_NAME_INPUT_TEXT));
		updateCategoryNameInputText.clear();
		updateCategoryNameInputText.sendKeys(newCategoryTitle);
		WebElement updateCategoryButton = driver
				.findElement(xpath(UPDATE_CATEGORY_BUTTON));
		updateCategoryButton.click();
		WebElement resultUpdateCategory = driver
				.findElement(className(RESULT_UPDATE_CATEGORY));
		String message = resultUpdateCategory.getText();
		return message;
	}

}
