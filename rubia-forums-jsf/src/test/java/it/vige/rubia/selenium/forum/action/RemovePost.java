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

import static it.vige.rubia.selenium.Constants.OK;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.PostBean;

public class RemovePost {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String REMOVE_POST_BUTTON = "miviewtopicbody6";
	public static final String CONFIRM_REMOVE_TOPIC_BUTTON = "//input[@type='submit']";

	public static String removePost(WebDriver driver, PostBean post) {
		goTo(driver, post.getTopic());
		WebElement removePostButton = driver
				.findElement(xpath("//tbody[contains(.,'" + post.getMessage().getText() + "')]"))
				.findElement(id(REMOVE_POST_BUTTON)).findElement(xpath("ul/a[2]"));
		removePostButton.click();
		WebElement confirmRemoveTopicButton = driver.findElement(xpath(CONFIRM_REMOVE_TOPIC_BUTTON));
		confirmRemoveTopicButton.click();
		WebElement resultRemovePost = null;
		String message = "";
		try {
			resultRemovePost = driver.findElement(xpath("//td[contains(@class,forumpostcontent)]/p[contains(text(),'"
					+ post.getMessage().getText() + "')]"));
			message = resultRemovePost.getText();
		} catch (NoSuchElementException ex) {
			message = OK;
		}
		return message;
	}
}
