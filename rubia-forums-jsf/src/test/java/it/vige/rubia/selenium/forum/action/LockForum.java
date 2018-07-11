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

import static it.vige.rubia.selenium.forum.action.VerifyForum.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Forum;

public class LockForum {

	public static final String LOCK_FORUM = "admintools";
	public static final String RESULT_LOCK_FORUM = "actionbuttons";

	public static String lockForum(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement lockForum = driver.findElement(className(LOCK_FORUM)).findElement(xpath("ul/li[2]/a"));
		lockForum.click();
		WebElement resultLockForum = driver.findElement(className(RESULT_LOCK_FORUM)).findElement(tagName("img"));
		String message = resultLockForum.getAttribute("alt");
		return message;
	}

}
