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

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.Constants.OK;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.goTo;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.PollOption;
import org.vige.rubia.model.Topic;

public class RemovePoll {
	public static final String UPDATE_TOPIC_BUTTON = "miviewtopicbody6";
	public static final String CONFIRM_UPDATE_TOPIC_BUTTON = "post:Submit";
	public static final String OPTION_INPUT_TEXT = "post:option_";
	public static final String DELETE_OPTION_BUTTON = "post:DeleteOption_1";
	public static final String QUESTION_INPUT_TEXT = "post:question";
	public static final String RESULT_REMOVE_POLL = "failuretext";

	public static String removePoll(WebDriver driver, Topic topic) {
		goTo(driver, topic);
		WebElement updateTopicButton = driver.findElements(xpath("//tbody"))
				.get(2).findElement(id(UPDATE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[1]"));
		updateTopicButton.click();
		List<PollOption> options = topic.getPoll().getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionButton = driver
						.findElement(id(DELETE_OPTION_BUTTON));
				optionButton.click();
			}
		WebElement questionText = driver.findElement(id(QUESTION_INPUT_TEXT));
		questionText.clear();
		WebElement confirmUpdateButton = driver
				.findElement(id(CONFIRM_UPDATE_TOPIC_BUTTON));
		confirmUpdateButton.click();
		String result;
		try {
			result = driver.findElement(className(RESULT_REMOVE_POLL))
					.getText();
		} catch (NoSuchElementException ex) {
			result = OK;
		}
		return result;

	}
}