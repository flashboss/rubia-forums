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
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfCurrentPost;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Post;

public class VerifyPost {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String FORUM_TABLE = "forumtablestyle";
	public static final String FORUM_SUBJECT = "tbody/tr/td[not(@class = 'forumcategory')]/h3/a";
	public static final String FORUM_LINK = "miviewtopicbody3";
	public static final String SUBJECT_LINK = "//table[contains(@class,'forumtablestyle')]/tbody/tr/td[2]/h3/a";
	public static final String BODY_OUTPUT_TEXT = "forumpostcontent";
	public static final String POST_SUBJECT_OUTPUT_TEXT = "../../tr/td[2]/div[@id='miviewtopicbody7']/ul/li[3]";
	public static final String POST_SUBJECT_TEXT = getBundle("ResourceJSF")
			.getString("Post_subject") + ": ";
	public static final String CREATE_DATE_OUTPUT_TEXT = "../../tr/td[2]/div[@id='miviewtopicbody7']/ul/li[2]";
	public static final String CREATE_DATE_TEXT = getBundle("ResourceJSF")
			.getString("Posted") + ": ";
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"E MMM d, yyyy H:mm a");

	public static List<Post> getPostsOfTopics(WebDriver driver,
			String... topicNames) {
		List<Post> posts = new ArrayList<Post>();
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement tableComponent = driver.findElement(className(FORUM_TABLE));
		List<WebElement> forumSubjectComponents = tableComponent
				.findElements(xpath(FORUM_SUBJECT));
		int forumComponentSize = forumSubjectComponents.size();
		for (int j = 0; j < forumComponentSize; j++) {
			home = driver.findElement(linkText(HOME_LINK));
			home.click();
			tableComponent = driver.findElement(className(FORUM_TABLE));
			forumSubjectComponents = tableComponent
					.findElements(xpath(FORUM_SUBJECT));
			WebElement forumEl = forumSubjectComponents.get(j);
			forumEl.click();
			List<WebElement> subjectComponents = driver
					.findElements(xpath(SUBJECT_LINK));
			int subjectComponentsSize = subjectComponents.size();
			for (int i = 0; i < subjectComponentsSize; i++) {
				subjectComponents = driver.findElements(xpath(SUBJECT_LINK));
				if (Arrays.asList(topicNames).contains(
						subjectComponents.get(i).getText())) {
					subjectComponents = driver
							.findElements(xpath(SUBJECT_LINK));
					WebElement subjectComponent = subjectComponents.get(i);
					subjectComponent.click();
					posts.addAll(getPostsOfCurrentTopic(driver));
					String forumLinkText = driver.findElement(id(FORUM_LINK))
							.getText();
					driver.findElement(linkText(forumLinkText)).click();
				}
			}
		}
		return posts;
	}

	public static List<Post> getPostsOfCurrentTopic(WebDriver driver) {
		List<WebElement> postComponents = driver
				.findElements(className(BODY_OUTPUT_TEXT));
		List<Post> posts = new ArrayList<Post>();
		for (WebElement postComponent : postComponents) {
			Post post = new Post();
			String body = postComponent.findElement(xpath("p")).getText();
			String post_subject = postComponent
					.findElement(xpath(POST_SUBJECT_OUTPUT_TEXT)).getText()
					.split(POST_SUBJECT_TEXT)[1];
			String createDateStr = postComponent
					.findElement(xpath(CREATE_DATE_OUTPUT_TEXT)).getText()
					.split(CREATE_DATE_TEXT)[1];
			Date createDate = null;
			try {
				createDate = dateFormat.parse(createDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Message message = new Message();
			message.setSubject(post_subject);
			message.setText(body);
			post.setMessage(message);
			post.setCreateDate(createDate);
			post.setAttachments(getAttachmentsOfCurrentPost(driver, postComponent));
			posts.add(post);
		}
		return posts;
	}
}
