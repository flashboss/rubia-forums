/*
 * Vige, Home of Professional Open Source
 * Copyright 2010, Vige, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.name;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Category;

public class CreateCategory {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");
	public static final String CREATE_CATEGORY_LINK = "newCategory";
	public static final String CREATE_CATEGORY_TITLE_INPUT_TEXT = "addCategoryForm:Category";
	public static final String CREATE_CATEGORY_BUTTON = "addCategoryForm:editinline";
	public static final String RESULT_CREATE_CATEGORY = "successtext";

	public static String createCategory(WebDriver driver, Category category) {
		WebElement adminPanelLink = startAdminPanel(driver);
		adminPanelLink.click();
		WebElement createCategoryLink = driver
				.findElement(name(CREATE_CATEGORY_LINK));
		createCategoryLink.click();
		WebElement createCategoryTitleInputType = driver
				.findElement(id(CREATE_CATEGORY_TITLE_INPUT_TEXT));
		createCategoryTitleInputType.sendKeys(category.getTitle());
		WebElement createCategoryButton = driver
				.findElement(id(CREATE_CATEGORY_BUTTON));
		createCategoryButton.click();
		WebElement resultCreateCategory = driver
				.findElement(className(RESULT_CREATE_CATEGORY));
		String message = resultCreateCategory.getText();
		return message;
	}

	private static WebElement startAdminPanel(WebDriver driver) {
		WebElement adminPanelLink = null;
		try {
			adminPanelLink = driver.findElement(linkText(ADMIN_PANEL_LINK));
		} catch (Exception ex) {
		}
		if (adminPanelLink == null)
			return startAdminPanel(driver);
		else
			return adminPanelLink;
	}
}
