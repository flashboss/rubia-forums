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
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;

public class MoveForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");

	public static Map<String, Integer> moveForum(WebDriver driver, Forum forum,
			Move move) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		String formId = findForum(driver, forum).findElement(
				xpath("td[2]/form")).getAttribute("id");
		int firstPosition = findForum(driver, forum).getLocation().getY();
		WebElement moveForum = driver.findElement(id(formId)).findElement(
				xpath("ul/li['" + move.getValue() + "']/a/img"));
		moveForum.click();
		int newPosition = findForum(driver, forum).getLocation().getY();
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("firstPosition", firstPosition);
		result.put("newPosition", newPosition);
		return result;
	}

	private static WebElement findForum(WebDriver driver, Forum forum) {
		List<WebElement> moveForums = driver.findElements(tagName("strong"));
		WebElement foundElement = null;
		for (WebElement moveForum : moveForums)
			if (moveForum.getText().equals(forum.getName()))
				foundElement = moveForum.findElement(xpath("../.."));
		return foundElement;
	}
}
