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
import org.vige.rubia.model.Forum;

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

	public static String updateForum(WebDriver driver, Forum oldForum,
			Forum newForum) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement updateForum = driver
				.findElement(xpath("//td[strong/text()='" + oldForum.getName()
						+ "']/form/a"));
		updateForum.click();
		WebElement updateForumNameInputText = driver
				.findElement(xpath(UPDATE_FORUM_NAME_INPUT_TEXT));
		updateForumNameInputText.clear();
		updateForumNameInputText.sendKeys(newForum.getName());
		WebElement updateForumDescriptionInputText = driver
				.findElement(xpath(UPDATE_FORUM_DESCRIPTION_INPUT_TEXT));
		updateForumDescriptionInputText.clear();
		updateForumDescriptionInputText.sendKeys(newForum.getDescription());
		WebElement categoryOption = driver.findElement(xpath(SELECT_CATEGORY));
		categoryOption.sendKeys(newForum.getCategory().getTitle());
		WebElement updateForumLink = driver
				.findElement(xpath(UPDATE_FORUM_LINK));
		updateForumLink.click();
		WebElement resultUpdateForum = driver
				.findElement(className(RESULT_UPDATE_FORUM));
		String message = resultUpdateForum.getText();
		return message;
	}
}
