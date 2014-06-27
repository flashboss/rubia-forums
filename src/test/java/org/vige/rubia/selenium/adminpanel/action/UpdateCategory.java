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
