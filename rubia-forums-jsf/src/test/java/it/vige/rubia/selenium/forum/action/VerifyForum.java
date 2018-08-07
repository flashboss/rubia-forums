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

import static it.vige.rubia.selenium.forum.model.Links.CATEGORY_TEMPLATE_LINK;
import static java.lang.Integer.parseInt;
import static java.util.ResourceBundle.getBundle;
import static org.jboss.logging.Logger.getLogger;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.auth.User;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.selenium.forum.model.TestUser;

public class VerifyForum {

	private static Logger log = getLogger(VerifyForum.class);

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String NO_POSTS = getBundle("ResourceJSF").getString("No_Posts");
	public static final String FORUM_TABLE = "forumtablestyle";
	public static final String FORUM_NAME = "forumtitletext";
	public static final String FORUM_TR = "tbody/tr";
	public static final String FORUM_NAME_LINK = "td[2]/h3/a";
	public static final String DESCRIPTION_OUTPUT_TEXT = "td[2]";
	public static final String TOPICS_COUNT_OUTPUT_TEXT = "td[3]";
	public static final String POSTS_COUNT_OUTPUT_TEXT = "td[4]";
	public static final String LAST_POST = "td[5]";
	public static final String LAST_POST_MESSAGE_LINK = "a";
	public static final String LAST_POST_USER_LINK = "a[2]";
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static ForumBean getForum(WebDriver driver) {
		ForumBean forum = new ForumBean();
		WebElement forumNameComponent = driver.findElement(className(FORUM_NAME));
		String forumNameText = forumNameComponent.getText();
		addParents(driver, forum);
		forum.setName(forumNameText);
		return forum;
	}

	public static ForumBean getForum(WebDriver driver, WebElement trComponent) {
		ForumBean forum = new ForumBean();
		WebElement forumNameComponent = trComponent.findElement(xpath(FORUM_NAME_LINK));
		String forumNameText = forumNameComponent.getText();
		addParents(driver, forum);
		forum.setName(forumNameText);
		forum.setDescription(trComponent.findElement(xpath(DESCRIPTION_OUTPUT_TEXT)).getText().split("\n")[1]);
		WebElement lastPostElement = trComponent.findElement(xpath(LAST_POST));
		if (!lastPostElement.getText().equals(NO_POSTS)) {
			PostBean lastPost = new PostBean();
			try {
				lastPost.setCreateDate(dateFormat.parse(lastPostElement.getText().split("\n")[2]));
			} catch (ParseException e) {
				log.error(e);
			}
			String userIdLastPost = lastPostElement.findElement(xpath(LAST_POST_USER_LINK)).getText();
			PosterBean poster = new PosterBean();
			poster.setUserId(userIdLastPost);
			lastPost.setPoster(poster);
			User user = new TestUser();
			user.setId(userIdLastPost);
			user.setUserName(userIdLastPost);
			lastPost.setUser(user);
			MessageBean message = new MessageBean();
			message.setSubject(lastPostElement.findElement(xpath(LAST_POST_MESSAGE_LINK)).getText());
			lastPost.setMessage(message);
			forum.setLastPost(lastPost);
		}
		forum.setTopicCount(parseInt(trComponent.findElement(xpath(TOPICS_COUNT_OUTPUT_TEXT)).getText()));
		forum.setPostCount(parseInt(trComponent.findElement(xpath(POSTS_COUNT_OUTPUT_TEXT)).getText()));
		return forum;
	}

	public static List<ForumBean> getForumsOfCategories(WebDriver driver, CategoryBean... categories) {
		List<ForumBean> forums = new ArrayList<ForumBean>();
		for (CategoryBean category : categories) {
			WebElement home = driver.findElement(linkText(HOME_LINK));
			home.click();
			WebElement categoryEl = driver.findElement(linkText(category.getTitle()));
			categoryEl.click();
			WebElement tableComponent = driver.findElement(className(FORUM_TABLE));
			List<WebElement> trComponents = tableComponent.findElements(xpath(FORUM_TR));
			int trComponentSize = trComponents.size();
			for (int i = 2; i < trComponentSize; i++) {
				tableComponent = driver.findElement(className(FORUM_TABLE));
				trComponents = tableComponent.findElements(xpath(FORUM_TR));
				WebElement trComponent = trComponents.get(i);
				ForumBean forum = getForum(driver, trComponent);
				forums.add(forum);
				driver.findElement(linkText(category.getTitle())).click();
			}
		}
		return forums;
	}

	public static void goTo(WebDriver driver, ForumBean forum) {
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement forumEl = driver.findElement(linkText(forum.getName()));
		forumEl.click();
	}

	private static void addParents(WebDriver driver, ForumBean forum) {
		CategoryBean category = new CategoryBean();
		category.setTitle(driver.findElement(linkText(driver.findElement(CATEGORY_TEMPLATE_LINK.getValue()).getText()))
				.getText());
		forum.setCategory(category);
	}
}
