package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RemoveCategory {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String REMOVE_CATEGORY_LINK = "buttonMed";
	public static final String SELECT_TYPE = "//form/select";
	public static final String RESULT_REMOVE_CATEGORY = "successtext";

	public static String removeCategory(WebDriver driver, String categoryTitle,
			String removeType) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement removeCategory = driver
				.findElement(xpath("//tr[td/strong/text()='" + categoryTitle
						+ "']/td[2]/form/ul/li[3]/a/img"));
		removeCategory.click();
		WebElement categoryOption = driver.findElement(xpath(SELECT_TYPE));
		categoryOption.sendKeys(removeType);
		WebElement removeCategoryLink = driver
				.findElement(className(REMOVE_CATEGORY_LINK));
		removeCategoryLink.click();
		WebElement resultRemoveCategory = driver
				.findElement(className(RESULT_REMOVE_CATEGORY));
		String message = resultRemoveCategory.getText();
		return message;
	}
}
