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

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;

public class RemovePoll {
	public static final String OPTION_INPUT_TEXT = "post:option_";
	public static final String RESET_OPTION_BUTTON = "post:UpdateOption_";

	public static String[] deleteOptions(WebDriver driver, Poll poll) {
		List<PollOption> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver
						.findElement(id(OPTION_INPUT_TEXT + (i + 1)));
				optionInput.sendKeys(options.get(i).getQuestion());
				optionButton = driver.findElement(className(RESET_OPTION_BUTTON
						+ (i + 1)));
				optionButton.click();
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