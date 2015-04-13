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
package it.vige.rubia.selenium.search.action;

import static it.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfCurrentPostInPageNoParent;
import static it.vige.rubia.selenium.forum.action.VerifyPost.FORUM_TABLE;
import static it.vige.rubia.selenium.profile.action.VerifyProfile.verifyProfile;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.search.SearchCriteria;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewPagePostSearch extends ViewPageSearch {

	public static final String POST_VIEW = "rf-dt-b";

	public static final String POST_POSTER = "tr/td/a";

	public static final String POST_SUBJECT = "tr/td[2]/div/ul/li[2]";

	public static final String POST_TEXT = "tr[2]/td[2]/p";

	public static final String POST_CREATED_DATE = "tr/td[2]/div/ul/li";

	public static DateFormat dateFormat = new SimpleDateFormat(
			"EEE MMM d, yyyy HH:mm aaa");

	public static List<Post> searchPost(WebDriver driver,
			SearchCriteria arguments) {
		addKeys(driver, arguments);
		return getPosts(driver, arguments);
	}

	private static Post getPost(WebDriver driver, WebElement element) {
		Post post = new Post();
		post.setPoster(new Poster(element.findElement(xpath(POST_POSTER))
				.getText()));
		String createdDate = element
				.findElement(xpath(POST_CREATED_DATE))
				.getText()
				.replace(getBundle("ResourceJSF").getString("Posted") + ": ",
						"");
		try {
			Date date = dateFormat.parse(createdDate);
			post.setCreateDate(date);
		} catch (ParseException e) {
		}
		Message message = new Message();
		message.setSubject(element
				.findElement(xpath(POST_SUBJECT))
				.getText()
				.replace(
						getBundle("ResourceJSF").getString("Post_subject")
								+ ": ", ""));
		message.setText(element.findElement(xpath(POST_TEXT)).getText());
		post.setMessage(message);
		List<Attachment> attachments = getAttachmentsOfCurrentPostInPageNoParent(
				driver, post);
		post.setAttachments(attachments);
		return post;
	}

	public static List<Post> getPosts(WebDriver driver, SearchCriteria arguments) {
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
		WebElement messageResult = getMessageResult(driver);
		if (messageResult != null
				&& (messageResult.getText().contains("Value is required") || messageResult
						.getText().equals(
								getBundle("ResourceJSF").getString(
										"Search_posts_not_found"))))
			return null;
		else {
			List<Post> posts = new ArrayList<Post>();
			List<WebElement> elements = driver
					.findElements(className(POST_VIEW));
			for (WebElement element : elements)
				posts.add(getPost(driver, element));
			return posts;
		}
	}

	public static Poster getPosterFromLink(WebDriver driver, Post post) {
		WebElement profileLink = driver
				.findElements(className(FORUM_TABLE))
				.get(0)
				.findElement(
						xpath("tbody/tr/td/p[contains(text(),'"
								+ post.getMessage().getText() + "')]"))
				.findElement(xpath("../../../tr/td/a"));
		String userId = profileLink.getText();
		profileLink.click();
		Poster poster = verifyProfile(driver, userId);
		return poster;
	}

	public static Poster getPosterFromButton(WebDriver driver, Post post) {
		WebElement profileLink = driver
				.findElements(className(FORUM_TABLE))
				.get(0)
				.findElement(
						xpath("tbody/tr/td/p[contains(text(),'"
								+ post.getMessage().getText() + "')]"))
				.findElement(xpath("../../../tr/td"));
		String userId = profileLink.getText();
		WebElement button = driver
				.findElements(className(FORUM_TABLE))
				.get(0)
				.findElement(
						xpath("tbody/tr/td/p[contains(text(),'"
								+ post.getMessage().getText() + "')]"))
				.findElement(xpath("../../../tr[3]/td[2]/ul/li/a"));
		button.click();
		Poster poster = verifyProfile(driver, userId);
		return poster;
	}

}
