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
package org.vige.rubia.selenium.forum.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.ForumInstance;

public class VerifyCategory {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String CATEGORY_ROW = "forumcategory";

	public static List<Category> getCategories(WebDriver driver,
			ForumInstance... forumInstances) {
		List<Category> categories = new ArrayList<Category>();
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		List<WebElement> categoryComponents = driver
				.findElements(className(CATEGORY_ROW));
		int categoryComponentsSize = categoryComponents.size();
		for (int i = 0; i < categoryComponentsSize; i++) {
			home = driver.findElement(linkText(HOME_LINK));
			home.click();
			Category category = new Category();
			categoryComponents = driver.findElements(className(CATEGORY_ROW));
			String categoryTitle = categoryComponents.get(i).getText();
			category.setTitle(categoryTitle);
			categories.add(category);
		}
		return categories;
	}
}
