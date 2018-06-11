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
package it.vige.rubia.selenium.summary.action;

import static it.vige.rubia.selenium.forum.action.VerifyTopic.getTopic;
import static java.lang.Integer.parseInt;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Topic;

public class ViewSummary {

	public static final String TOPIC_TABLE = "forumtablestyle";
	public static final String TOPIC_SIZE = "note";

	public static List<Topic> viewSummary(WebDriver driver) {
		List<Topic> topics = new ArrayList<Topic>();
		List<WebElement> elements = driver.findElement(className(TOPIC_TABLE)).findElements(xpath("tbody/tr/td"));
		for (int i = 1; i < elements.size(); i++) {
			WebElement element = elements.get(i);
			Topic topic = new Topic();
			topic.setSubject(element.findElement(xpath("a")).getText());
			topics.add(topic);
		}
		return topics;
	}

	public static int viewSize(WebDriver driver) {
		WebElement element = driver.findElement(className(TOPIC_SIZE));
		return parseInt(element.getText().split(" ")[0]);
	}

	public static Topic getDetail(WebDriver driver, String subject) {
		WebElement element = driver.findElement(linkText(subject));
		element.click();
		return getTopic(driver);
	}
}
