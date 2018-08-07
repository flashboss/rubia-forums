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
package it.vige.rubia.selenium.adminpanel.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import it.vige.rubia.dto.ForumBean;

public class CreateForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF").getString("Admin_panel");
	public static final String CREATE_FORUM_NAME_INPUT_TEXT = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_name") + ":']/input[4]";
	public static final String CREATE_FORUM_DESCRIPTION_INPUT_TEXT = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_desc") + ":']/textarea";
	public static final String SELECT_CATEGORY = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category") + ":']/select";
	public static final String CREATE_FORUM_BUTTON = "//form[label/text()='"
			+ getBundle("ResourceJSF").getString("Forum_category") + ":']/input[5]";
	public static final String RESULT_CREATE_FORUM = "successtext";

	public static String createForum(WebDriver driver, ForumBean forum) {
		WebElement adminPanelLink = driver.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		String formId = driver.findElement(xpath("//td[strong/text()='" + forum.getCategory().getTitle() + "']/form"))
				.getAttribute("id");
		WebElement createForum = driver.findElement(id(formId)).findElement(xpath("div/a/img"));
		createForum.click();
		WebElement createForumNameInputText = driver.findElement(xpath(CREATE_FORUM_NAME_INPUT_TEXT));
		createForumNameInputText.sendKeys(forum.getName());
		WebElement createForumDescriptionInputText = driver.findElement(xpath(CREATE_FORUM_DESCRIPTION_INPUT_TEXT));
		createForumDescriptionInputText.sendKeys(forum.getDescription());
		Select categoryOption = new Select(driver.findElement(xpath(SELECT_CATEGORY)));
		categoryOption.selectByVisibleText(forum.getCategory().getTitle());
		WebElement createForumButton = driver.findElement(xpath(CREATE_FORUM_BUTTON));
		createForumButton.click();
		WebElement resultCreateForum = driver.findElement(className(RESULT_CREATE_FORUM));
		String message = resultCreateForum.getText();
		return message;
	}
}
