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

import static it.vige.rubia.selenium.myforums.action.ViewAllForums.MY_FORUMS_LIST;
import static it.vige.rubia.selenium.myforums.action.ViewAllForumsUpdateForum.EDIT_BUTTON;
import static it.vige.rubia.selenium.myforums.action.ViewAllTopics.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Topic;
import it.vige.rubia.properties.NotificationType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewAllTopicsUpdateTopic {

	public static String viewAllTopicsUpdateTopic(WebDriver driver,
			Topic topic, NotificationType notificationType) {
		goTo(driver);
		WebElement editLink = driver
				.findElements(className(MY_FORUMS_LIST))
				.get(0)
				.findElement(
						xpath("../tr/td/a[contains(text(),'"
								+ topic.getSubject() + "')]/../../td[4]/a"));
		editLink.click();
		WebElement select = driver.findElement(linkText(topic.getSubject()))
				.findElement(xpath("../../../.."))
				.findElement(className(EDIT_BUTTON))
				.findElement(xpath("select"));
		select.sendKeys(notificationType.toString());
		WebElement editButton = driver
				.findElement(linkText(topic.getSubject()))
				.findElement(xpath("../../../.."))
				.findElement(className(EDIT_BUTTON))
				.findElement(xpath("input[2]"));
		editButton.click();
		WebElement resultEditPost = null;
		String message = "";
		resultEditPost = driver
				.findElements(className(MY_FORUMS_LIST))
				.get(0)
				.findElement(
						xpath("../tr/td/a[contains(text(),'"
								+ topic.getSubject() + "')]/../../td[4]"));
		message = resultEditPost.getText();
		return message.substring(0, message.indexOf("(")).trim();
	}

}
