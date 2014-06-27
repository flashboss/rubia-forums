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
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RemoveTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String REMOVE_TOPIC_BUTTON = "miviewtopicbody6";
	public static final String CONFIRM_REMOVE_TOPIC_BUTTON = "//input[@type='submit']";

	public static String removeTopic(WebDriver driver, String forumName,
			String subject, String body) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forum = driver.findElement(linkText(forumName));
		forum.click();
		WebElement topic = driver.findElement(linkText(subject));
		topic.click();
		WebElement removeTopicButton = driver
				.findElement(xpath("//tbody[contains(.,'" + body + "')]"))
				.findElement(id(REMOVE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[2]"));
		removeTopicButton.click();
		WebElement confirmRemoveTopicButton = driver
				.findElement(xpath(CONFIRM_REMOVE_TOPIC_BUTTON));
		confirmRemoveTopicButton.click();
		WebElement resultCreateTopic = null;
		String message = "";
		try {
			resultCreateTopic = driver.findElement(linkText(subject));
			message = resultCreateTopic.getText();
		} catch (NoSuchElementException ex) {
			message = "OK";
		}
		return message;
	}
}
