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
import org.vige.rubia.model.Topic;

public class RemoveTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String REMOVE_TOPIC_BUTTON = "miviewtopicbody6";
	public static final String CONFIRM_REMOVE_TOPIC_BUTTON = "//input[@type='submit']";

	public static String removeTopic(WebDriver driver, Topic topic) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forum = driver.findElement(linkText(topic.getForum()
				.getName()));
		forum.click();
		WebElement topicEl = driver.findElement(linkText(topic.getSubject()));
		topicEl.click();
		WebElement removeTopicButton = driver
				.findElement(
						xpath("//tbody[contains(.,'"
								+ topic.getPosts().get(0).getMessage()
										.getText() + "')]"))
				.findElement(id(REMOVE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[2]"));
		removeTopicButton.click();
		WebElement confirmRemoveTopicButton = driver
				.findElement(xpath(CONFIRM_REMOVE_TOPIC_BUTTON));
		confirmRemoveTopicButton.click();
		WebElement resultRemoveTopic = null;
		String message = "";
		try {
			resultRemoveTopic = driver
					.findElement(linkText(topic.getSubject()));
			message = resultRemoveTopic.getText();
		} catch (NoSuchElementException ex) {
			message = "OK";
		}
		return message;
	}
}
