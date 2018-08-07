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

import static it.vige.rubia.selenium.Constants.OK;
import static it.vige.rubia.selenium.myforums.action.ViewAllForums.MY_FORUMS_LIST;
import static it.vige.rubia.selenium.myforums.action.ViewAllForums.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.ForumBean;

public class ViewAllForumsRemoveForum {

	public static String viewAllForumsRemoveForum(WebDriver driver, ForumBean forum) {
		goTo(driver);
		WebElement element = driver
				.findElements(className(MY_FORUMS_LIST))
				.get(1)
				.findElement(
						xpath("../tr/td/h3/a[contains(text(),'"
								+ forum.getName()
								+ "')]/../../../td[6]/div/ul/li/a"));
		element.click();
		WebElement resultRemovePost = null;
		String message = "";
		try {
			resultRemovePost = driver
					.findElements(className(MY_FORUMS_LIST))
					.get(1)
					.findElement(
							xpath("../tr/td/h3/a[contains(text(),'"
									+ forum.getName()
									+ "')]/../../../td[6]/div/ul/li/a"));
			message = resultRemovePost.getText();
		} catch (NoSuchElementException ex) {
			message = OK;
		}
		return message;
	}

	public static String viewAllEditForumsRemoveForum(WebDriver driver,
			ForumBean forum) {
		ViewAllForumsUpdateForum.goTo(driver);
		WebElement element = driver
				.findElements(className(MY_FORUMS_LIST))
				.get(0)
				.findElement(
						xpath("../tr/td/h3/a[contains(text(),'"
								+ forum.getName()
								+ "')]/../../../td[5]/div/ul/li/a"));
		element.click();
		WebElement resultRemovePost = null;
		String message = "";
		try {
			resultRemovePost = driver
					.findElements(className(MY_FORUMS_LIST))
					.get(0)
					.findElement(
							xpath("../tr/td/h3/a[contains(text(),'"
									+ forum.getName()
									+ "')]/../../../td[5]/div/ul/li/a"));
			message = resultRemovePost.getText();
		} catch (NoSuchElementException ex) {
			message = OK;
		}
		return message;
	}

}
