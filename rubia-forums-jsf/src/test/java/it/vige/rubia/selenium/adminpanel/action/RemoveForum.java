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
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import it.vige.rubia.model.Forum;

public class RemoveForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF").getString("Admin_panel");
	public static final String REMOVE_FORUM_LINK = "buttonMed";
	public static final String SELECT_TYPE = "//form/select";
	public static final String RESULT_REMOVE_FORUM = "successtext";

	public static String removeForum(WebDriver driver, Forum forum, String removeType) {
		WebElement adminPanelLink = driver.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		WebElement removeForum = driver
				.findElement(xpath("//tr[td/strong/text()='" + forum.getName() + "']/td[2]/form/ul/li[3]/a/img"));
		removeForum.click();
		WebElement option = driver.findElement(xpath(SELECT_TYPE));
		Select select = new Select(option);
		select.selectByVisibleText(removeType);
		WebElement removeForumLink = driver.findElement(className(REMOVE_FORUM_LINK));
		removeForumLink.click();
		WebElement resultRemoveForum = driver.findElement(className(RESULT_REMOVE_FORUM));
		String message = resultRemoveForum.getText();
		return message;
	}
}
