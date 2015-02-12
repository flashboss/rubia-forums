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
import static org.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static org.vige.rubia.selenium.myforums.action.ViewAllForums.MY_FORUMS_LIST;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Topic;

public class ViewAllTopics {

	public static final String MY_TOPICS_LINK = getBundle("ResourceJSF")
			.getString("ViewAllSubscribed");

	public static void goTo(WebDriver driver) {
		ViewAllForums.goTo(driver);
		WebElement element = driver.findElement(linkText(MY_TOPICS_LINK));
		element.click();
	}

	public static List<Topic> viewAllTopics(WebDriver driver) {
		goTo(driver);
		List<WebElement> elements = driver.findElement(
				className(MY_FORUMS_LIST)).findElements(xpath("../tr"));
		int elementsCount = elements.size() + 1;
		List<Topic> topics = new ArrayList<Topic>();
		for (int i = 2; i < elementsCount; i++) {
			WebElement element = driver.findElements(className(MY_FORUMS_LIST))
					.get(0).findElement(xpath("../tr[" + i + "]/td[2]/h3/a"));
			element.click();
			topics.add(getTopic(driver));
			goTo(driver);
		}
		return topics;
	}
}
