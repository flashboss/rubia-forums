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

import static it.vige.rubia.selenium.forum.action.CreateAttachment.addAttachments;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Post;

public class CreatePost extends Write {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String REPLY_POST_BUTTON = "actionbuttons";
	public static final String SUBMIT_BUTTON = "post:Submit";
	public static final String LOCKED = "locked";

	public static String createPost(WebDriver driver, Post post) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forumEl = driver.findElement(linkText(post.getTopic().getForum().getName()));
		forumEl.click();
		sleepThread();
		WebElement topicSubject = driver.findElement(linkText(post.getTopic().getSubject()));
		topicSubject.click();
		sleepThread();
		try {
			WebElement bodyText = driver.findElement(className(REPLY_POST_BUTTON)).findElement(xpath("a[2]"));
			bodyText.click();
		} catch (NoSuchElementException e) {
			return LOCKED;
		}
		sleepThread();
		WebElement bodyInput = driver.findElement(cssSelector("div.ql-editor"));
		bodyInput.sendKeys(post.getMessage().getText());
		addAttachments(driver, post);
		WebElement operationButton = driver.findElement(id(SUBMIT_BUTTON));
		operationButton.click();
		WebElement resultCreatePost = driver.findElement(xpath(
				"//td[contains(@class,forumpostcontent)]/p[contains(text(),'" + post.getMessage().getText() + "')]"));
		String updatedPost = resultCreatePost.getText();
		return updatedPost;
	}
}