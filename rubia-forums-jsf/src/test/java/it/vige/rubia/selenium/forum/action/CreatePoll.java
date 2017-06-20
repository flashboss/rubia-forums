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
package it.vige.rubia.selenium.forum.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;

public class CreatePoll extends Create {
	public static final String NEW_OPTION_INPUT_TEXT = "post:newOption";
	public static final String ADD_OPTION_BUTTON = "buttonMed";
	public static final String QUESTION_INPUT_TEXT = "post:question";

	public static String[] createOptions(WebDriver driver, Poll poll) {
		WebElement questionInput = driver.findElement(id(QUESTION_INPUT_TEXT));
		questionInput.sendKeys(poll.getTitle());
		sleepThread();
		List<PollOption> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver.findElement(id(NEW_OPTION_INPUT_TEXT));
				optionInput.sendKeys(options.get(i).getQuestion());
				optionButton = driver
						.findElements(className(ADD_OPTION_BUTTON)).get(i * 2);
				optionButton.click();
				sleepThread();
			}
		WebElement[] updatedElements = new WebElement[options.size()];
		for (int i = 0; i < options.size(); i++)
			updatedElements[i] = driver.findElement(xpath("//input[@value='"
					+ options.get(i).getQuestion() + "']"));
		String[] results = new String[updatedElements.length];
		for (int i = 0; i < updatedElements.length; i++)
			results[i] = updatedElements[i].getAttribute("value");
		return results;
	}
}