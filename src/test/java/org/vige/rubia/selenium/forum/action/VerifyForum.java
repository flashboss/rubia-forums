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

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.model.Links.CATEGORY_TEMPLATE_LINK;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.auth.User;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import org.vige.rubia.selenium.forum.model.TestUser;

public class VerifyForum {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String NO_POSTS = getBundle("ResourceJSF").getString(
			"No_Posts");
	public static final String FORUM_TABLE = "forumtablestyle";
	public static final String FORUM_TR = "tbody/tr";
	public static final String FORUM_NAME_LINK = "td[2]/h3/a";
	public static final String DESCRIPTION_OUTPUT_TEXT = "td[2]";
	public static final String TOPICS_COUNT_OUTPUT_TEXT = "td[3]";
	public static final String POSTS_COUNT_OUTPUT_TEXT = "td[4]";
	public static final String LAST_POST = "td[5]";
	public static final String LAST_POST_MESSAGE_LINK = "a";
	public static final String LAST_POST_CREATE_DATE_OUTPUT_TEXT = "br[2]";
	public static final String LAST_POST_USER_LINK = "br/a";
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public static boolean isRegistered(WebDriver driver, Forum forum) {
		return true;
	}

	public static List<Forum> getForumsOfCategories(WebDriver driver,
			Category... categories) {
		List<Forum> forums = new ArrayList<Forum>();
		for (Category category : categories) {
			WebElement home = driver.findElement(linkText(HOME_LINK));
			home.click();
			WebElement categoryEl = driver.findElement(linkText(category
					.getTitle()));
			categoryEl.click();
			WebElement tableComponent = driver
					.findElement(className(FORUM_TABLE));
			List<WebElement> trComponents = tableComponent
					.findElements(xpath(FORUM_TR));
			int trComponentSize = trComponents.size();
			for (int i = 2; i < trComponentSize; i++) {
				tableComponent = driver.findElement(className(FORUM_TABLE));
				trComponents = tableComponent.findElements(xpath(FORUM_TR));
				WebElement forumNameComponent = trComponents.get(i)
						.findElement(xpath(FORUM_NAME_LINK));
				Forum forum = new Forum();
				addParents(driver, forum);
				String forumNameText = forumNameComponent.getText();
				forum.setName(forumNameText);
				forum.setDescription(trComponents.get(i)
						.findElement(xpath(DESCRIPTION_OUTPUT_TEXT)).getText()
						.split("\n")[1]);
				WebElement lastPostElement = trComponents.get(i).findElement(
						xpath(LAST_POST));
				if (!lastPostElement.getText().equals(NO_POSTS)) {
					Post lastPost = new Post();
					try {
						lastPost.setCreateDate(dateFormat
								.parse(lastPostElement
										.findElement(
												xpath(LAST_POST_CREATE_DATE_OUTPUT_TEXT))
										.getText()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String userIdLastPost = lastPostElement.findElement(
							xpath(LAST_POST_USER_LINK)).getText();
					Poster poster = new Poster();
					poster.setUserId(userIdLastPost);
					lastPost.setPoster(poster);
					User user = new TestUser();
					user.setId(userIdLastPost);
					user.setUserName(userIdLastPost);
					lastPost.setUser(user);
					Message message = new Message();
					message.setSubject(lastPostElement.findElement(
							xpath(LAST_POST_MESSAGE_LINK)).getText());
					lastPost.setMessage(message);
					forum.setLastPost(lastPost);
				}
				forum.setTopicCount(new Integer(trComponents.get(i)
						.findElement(xpath(TOPICS_COUNT_OUTPUT_TEXT)).getText()));
				forum.setPostCount(new Integer(trComponents.get(i)
						.findElement(xpath(POSTS_COUNT_OUTPUT_TEXT)).getText()));
				forums.add(forum);
				driver.findElement(linkText(category.getTitle())).click();
			}
		}
		return forums;
	}

	public static void goTo(WebDriver driver, Forum forum) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forumEl = driver.findElement(linkText(forum.getName()));
		forumEl.click();
	}

	private static void addParents(WebDriver driver, Forum forum) {
		Category category = new Category();
		category.setTitle(driver.findElement(
				linkText(driver.findElement(CATEGORY_TEMPLATE_LINK.getValue())
						.getText())).getText());
		forum.setCategory(category);
	}
}
