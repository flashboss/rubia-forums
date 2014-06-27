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

import static org.vige.rubia.selenium.adminpanel.action.Move.UP;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.By.id;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MoveForum {

	public static final String ADMIN_PANEL_LINK = getBundle("ResourceJSF")
			.getString("Admin_panel");

	public static Map<String, Integer> moveForum(WebDriver driver,
			String forumName, Move move) {
		WebElement adminPanelLink = driver
				.findElement(linkText(ADMIN_PANEL_LINK));
		adminPanelLink.click();
		String formId = driver.findElement(
				xpath("//tr[td/strong/text()='" + forumName + "']/td[2]/form"))
				.getAttribute("id");
		WebElement moveForum = driver.findElement(id(formId)).findElement(
				xpath("ul/li['" + (move == UP ? 1 : 2) + "']/a/img"));
		int firstPosition = findPosition(driver, forumName);
		moveForum.click();
		int newPosition = findPosition(driver, forumName);
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("firstPosition", firstPosition);
		result.put("newPosition", newPosition);
		return result;
	}

	private static int findPosition(WebDriver driver, String forumName) {
		WebElement moveForum = driver
				.findElement(xpath("//tr[td/strong/text()='" + forumName + "']"));
		return moveForum.getLocation().getY();
	}
}
