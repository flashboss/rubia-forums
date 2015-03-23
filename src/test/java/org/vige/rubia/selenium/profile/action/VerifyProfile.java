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
package org.vige.rubia.selenium.profile.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Poster;

public class VerifyProfile {

	public static String POST_COUNT = "forumtablestyle";

	public static Poster verifyProfile(WebDriver driver, String userId) {
		Poster poster = new Poster(userId);
		WebElement postCount = driver.findElement(className(POST_COUNT))
				.findElement(xpath("tbody/tr[3]/td[2]"));
		int count = new Integer(postCount.getText().trim());
		for (int i = 0; i < count; i++)
			poster.incrementPostCount();
		return poster;
	}
}
