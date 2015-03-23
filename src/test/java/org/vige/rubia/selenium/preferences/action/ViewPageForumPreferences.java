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
package org.vige.rubia.selenium.preferences.action;

import static org.openqa.selenium.By.className;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.ui.action.PreferenceController;

public class ViewPageForumPreferences extends ViewPagePreferences {

	public static final String FORUM_VIEWS = "forumtablestyle";

	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public static void submit(WebDriver driver, PreferenceController arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
	}

	public static void reset(WebDriver driver, PreferenceController arguments) {
		addKeys(driver, arguments);
		List<WebElement> buttons = driver.findElements(className(BUTTON));
		WebElement reset = buttons.get(1);
		reset.click();
		WebElement submit = buttons.get(0);
		submit.click();
	}

}
