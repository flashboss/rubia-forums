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
package org.vige.rubia.selenium.myforums.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.properties.NotificationType;

public class ViewAllForumsUpdateForum {

	public static final String EDIT_FORUMS_LINK = getBundle("ResourceJSF")
			.getString("EditAllSubscribed");
	public static final String EDIT_LINK = getBundle("ResourceJSF").getString(
			"Edit");
	public static final String EDIT_SELECT = getBundle("ResourceJSF")
			.getString("Edit");
	public static final String EDIT_BUTTON = "forumfloatright";
	public static final String FORUM_LINK = "forumlistopen";

	public static void goTo(WebDriver driver) {
		ViewAllForums.goTo(driver);
		WebElement element = driver.findElement(linkText(EDIT_FORUMS_LINK));
		element.click();
	}

	public static String viewAllForumsUpdateForum(WebDriver driver,
			Forum forum, NotificationType notificationType) {
		goTo(driver);
		WebElement editLink = driver.findElement(linkText(forum.getName()))
				.findElement(xpath("../../.."))
				.findElement(linkText(EDIT_LINK));
		editLink.click();
		WebElement select = driver.findElement(linkText(forum.getName()))
				.findElement(xpath("../../.."))
				.findElement(className(EDIT_BUTTON))
				.findElement(xpath("select"));
		select.sendKeys(notificationType.toString());
		WebElement editButton = driver.findElement(linkText(forum.getName()))
				.findElement(xpath("../../.."))
				.findElement(className(EDIT_BUTTON))
				.findElement(xpath("input[2]"));
		editButton.click();
		WebElement resultEditPost = driver
				.findElement(linkText(forum.getName()))
				.findElement(xpath("../../.."))
				.findElement(className(FORUM_LINK));
		String message = resultEditPost.getText();
		return message.substring(0, message.indexOf("(")).trim();
	}

}
