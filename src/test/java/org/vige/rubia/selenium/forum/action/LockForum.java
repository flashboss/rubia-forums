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
import static org.vige.rubia.selenium.forum.action.VerifyForum.goTo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;

public class LockForum {

	public static final String LOCK_FORUM = "admintools";
	public static final String RESULT_LOCK_FORUM = "javax_faces_developmentstage_messages";

	public static String lockForum(WebDriver driver, Forum forum) {
		goTo(driver, forum);
		WebElement lockForum = driver.findElement(className(LOCK_FORUM))
				.findElement(xpath("ul/li[2]/a"));
		lockForum.click();
		WebElement resultLockForum = driver.findElement(id(RESULT_LOCK_FORUM));
		String message = resultLockForum.getText();
		return message;
	}

}
