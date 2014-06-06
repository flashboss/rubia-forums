package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.name;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateCategory {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String CREATE_CATEGORY_LINK = "newCategory";
	public static final String CREATE_CATEGORY_TITLE_INPUT_TEXT = "addCategoryForm:Category";
	public static final String CREATE_CATEGORY_BUTTON = "addCategoryForm:editinline";
	public static final String RESULT_CREATE_CATEGORY = "successtext";

	public static String createCategory(WebDriver driver, String categoryTitle) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement createCategoryLink = driver
				.findElement(name(CREATE_CATEGORY_LINK));
		createCategoryLink.click();
		WebElement createCategoryTitleInputType = driver
				.findElement(id(CREATE_CATEGORY_TITLE_INPUT_TEXT));
		createCategoryTitleInputType.sendKeys(categoryTitle);
		WebElement createCategoryButton = driver
				.findElement(id(CREATE_CATEGORY_BUTTON));
		createCategoryButton.click();
		WebElement resultCreateCategory = driver
				.findElement(className(RESULT_CREATE_CATEGORY));
		String message = resultCreateCategory.getText();
		return message;
	}
}
