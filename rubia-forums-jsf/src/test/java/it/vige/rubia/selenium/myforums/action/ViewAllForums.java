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
package it.vige.rubia.selenium.myforums.action;

import static it.vige.rubia.selenium.forum.action.VerifyForum.getForum;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.ForumBean;

public class ViewAllForums {
	public static final String MY_FORUMS_LINK = getBundle("ResourceJSF")
			.getString("My_Forums");
	public static final String MY_FORUMS_LIST = "header";
	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");

	public static void goTo(WebDriver driver) {
		WebElement home = driver.findElement(linkText(MY_FORUMS_LINK));
		home.click();
	}

	public static List<ForumBean> viewAllForums(WebDriver driver) {
		goTo(driver);
		List<WebElement> elements = driver
				.findElements(className(MY_FORUMS_LIST)).get(1)
				.findElements(xpath("../tr"));
		int elementsCount = elements.size() - 1;
		List<ForumBean> forums = new ArrayList<ForumBean>();
		for (int i = 2; i < elementsCount; i++) {
			WebElement element = driver.findElements(className(MY_FORUMS_LIST))
					.get(1).findElement(xpath("../tr[" + i + "]/td[2]/h3/a"));
			element.click();
			forums.add(getForum(driver));
			goTo(driver);
		}
		return forums;
	}
}
