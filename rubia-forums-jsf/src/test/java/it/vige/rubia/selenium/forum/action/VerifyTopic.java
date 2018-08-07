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

import static it.vige.rubia.dto.TopicType.ADVICE;
import static it.vige.rubia.dto.TopicType.IMPORTANT;
import static it.vige.rubia.dto.TopicType.NORMAL;
import static it.vige.rubia.selenium.forum.action.VerifyPoll.getPollOfCurrentTopic;
import static it.vige.rubia.selenium.forum.action.VerifyPost.getPostsOfCurrentTopic;
import static it.vige.rubia.selenium.forum.model.Links.CATEGORY_TEMPLATE_LINK;
import static it.vige.rubia.selenium.forum.model.Links.FORUM_TEMPLATE_LINK;
import static it.vige.rubia.selenium.forum.model.Links.TOPIC_TEMPLATE_LINK;
import static it.vige.rubia.selenium.profile.action.VerifyProfile.verifyProfile;
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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;

public class VerifyTopic {

	private static Logger log = getLogger(VerifyTopic.class);

	public static final String TOPIC_TABLE = "forumtablestyle";
	public static final String TOPIC_STICKY = getBundle("ResourceJSF").getString("Topic_Sticky");
	public static final String SUBJECT_LINK = "tbody/tr/td[2]/h3/a";
	public static final String SUBJECT_IN_TOPIC_LINK = "tbody/tr/td[2]/p";
	public static final String TYPE_SUBJECT_OUTPUT_TEXT = "tbody/tr/td[2]/h3/b";
	public static final String REPLIED_OUTPUT_TEXT = "tbody/tr[contains(@role,'row')]/td[3] | tbody/tr[contains(@class,'Row')]/td[3]";
	public static final String VIEW_OUTPUT_TEXT = "tbody/tr[contains(@role,'row')]/td[4] | tbody/tr[contains(@class,'Row')]/td[4]";
	public static final String LAST_POST_DATE_OUTPUT_TEXT = "tbody/tr[contains(@role,'row')]/td[5] | tbody/tr[contains(@class,'Row')]/td[5]";
	public static final String LAST_POST_DATE_IN_TOPIC_OUTPUT_TEXT = "tbody/tr/td[2]";
	public static final String USER_LINK = "tbody/tr/td[2]/a";
	public static final String USER_IN_TOPIC_LINK = "tbody/tr/td[1]/a";
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static ResourceBundle bundle = getBundle("ResourceJSF");
	public static final String PROFILE_LINK = "header";

	public static TopicBean getTopic(WebDriver driver) {
		TopicBean topic = new TopicBean();
		WebElement topicTable = driver.findElements(className(TOPIC_TABLE)).get(1);
		WebElement subjectComponent = topicTable.findElement(TOPIC_TEMPLATE_LINK.getValue());
		String subjectText = subjectComponent.getText();
		topic.setSubject(subjectText);
		String user = topicTable.findElement(xpath(USER_IN_TOPIC_LINK)).getText();
		Date lastPostDate = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy HH:mm a");
			String posted = bundle.getString("Posted");
			String permanentLink = topicTable.findElement(xpath(LAST_POST_DATE_IN_TOPIC_OUTPUT_TEXT)).getText();
			lastPostDate = dateFormat.parse(permanentLink.substring(permanentLink.indexOf(posted) + posted.length() + 1,
					permanentLink.indexOf(bundle.getString("Post_subject"))).trim());
		} catch (ParseException e) {
			log.error(e);
		}
		topic.setLastPostDate(lastPostDate);
		PosterBean poster = new PosterBean();
		poster.setUserId(user);
		topic.setPoster(poster);
		subjectComponent.click();
		topic.setPoll(getPollOfCurrentTopic(driver));
		topic.setPosts(getPostsOfCurrentTopic(driver));
		addParents(driver, topic);
		return topic;
	}

	public static TopicBean getTopic(WebDriver driver, int i4, WebElement topicTable) {
		TopicBean topic = new TopicBean();
		List<WebElement> subjectComponents = topicTable.findElements(xpath(SUBJECT_LINK));
		WebElement subjectComponent = subjectComponents.get(i4);
		String subjectText = subjectComponent.getText();
		topic.setSubject(subjectText);
		List<WebElement> subjectTypeComponents = topicTable.findElements(xpath(TYPE_SUBJECT_OUTPUT_TEXT));
		if (subjectTypeComponents.size() == 0)
			topic.setType(NORMAL);
		else if (subjectTypeComponents.get(i4).getText().trim().equals(TOPIC_STICKY))
			topic.setType(IMPORTANT);
		else
			topic.setType(ADVICE);
		String[] allRows = topicTable.findElements(xpath(USER_LINK)).get(i4).findElement(xpath("..")).getText()
				.split(bundle.getString("By"));
		String user = allRows[allRows.length - 1].trim();
		int replies = parseInt(topicTable.findElements(xpath(REPLIED_OUTPUT_TEXT)).get(i4).getText());
		int viewCount = parseInt(topicTable.findElements(xpath(VIEW_OUTPUT_TEXT)).get(i4).getText());
		Date lastPostDate = null;
		try {
			lastPostDate = dateFormat
					.parse(topicTable.findElements(xpath(LAST_POST_DATE_OUTPUT_TEXT)).get(i4).getText().split("\n")[2]);
		} catch (ParseException e) {
			log.error(e);
		}
		topic.setReplies(replies);
		topic.setViewCount(viewCount);
		topic.setLastPostDate(lastPostDate);
		PosterBean poster = new PosterBean();
		poster.setUserId(user);
		topic.setPoster(poster);
		subjectComponent.click();
		topic.setPoll(getPollOfCurrentTopic(driver));
		topic.setPosts(getPostsOfCurrentTopic(driver));
		addParents(driver, topic);
		return topic;
	}

	public static List<TopicBean> getTopicsOfForums(WebDriver driver, ForumBean... forums) {
		List<TopicBean> topics = new ArrayList<TopicBean>();
		for (ForumBean forum : forums) {
			VerifyForum.goTo(driver, forum);
			List<WebElement> tableComponents = driver.findElements(className(TOPIC_TABLE));
			int tableComponentsSize = tableComponents.size();
			for (int i = 0; i < tableComponentsSize; i++) {
				List<WebElement> subjectComponents = driver.findElements(className(TOPIC_TABLE)).get(i)
						.findElements(xpath(SUBJECT_LINK));
				int subjectComponentsSize = subjectComponents.size();
				for (int i4 = 0; i4 < subjectComponentsSize; i4++) {
					WebElement topicTable = driver.findElements(className(TOPIC_TABLE)).get(i);
					TopicBean topic = getTopic(driver, i4, topicTable);
					topics.add(topic);
					driver.findElement(linkText(forum.getName())).click();
				}
			}
		}
		return topics;
	}

	public static void goTo(WebDriver driver, TopicBean topic) {
		VerifyForum.goTo(driver, topic.getForum());
		WebElement topicEl = driver.findElement(linkText(topic.getSubject()));
		topicEl.click();
	}

	public static PosterBean getPoster(WebDriver driver, ForumBean forum) {
		WebElement profileLink = driver.findElement(linkText(forum.getName()))
				.findElement(xpath("../../../td[5]/a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		PosterBean poster = verifyProfile(driver, userId);
		return poster;
	}

	public static PosterBean getPoster(WebDriver driver, TopicBean topic) {
		WebElement profileLink = driver.findElements(className(PROFILE_LINK)).get(0)
				.findElement(xpath("../tr/td/h3/a[contains(text(),'" + topic.getSubject() + "')]"))
				.findElement(xpath("../../a"));
		String userId = profileLink.getText();
		profileLink.click();
		PosterBean poster = verifyProfile(driver, userId);
		return poster;
	}

	public static PosterBean getPosterLastPost(WebDriver driver, TopicBean topic) {
		WebElement profileLink = driver.findElements(className(PROFILE_LINK)).get(0)
				.findElement(xpath("../tr/td/a[contains(text(),'" + topic.getSubject() + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		PosterBean poster = verifyProfile(driver, userId);
		return poster;
	}

	private static void addParents(WebDriver driver, TopicBean topic) {
		ForumBean forum = new ForumBean();
		forum.setName(
				driver.findElement(linkText(driver.findElement(FORUM_TEMPLATE_LINK.getValue()).getText())).getText());
		topic.setForum(forum);
		CategoryBean category = new CategoryBean();
		category.setTitle(driver.findElement(CATEGORY_TEMPLATE_LINK.getValue()).getText());
		forum.setCategory(category);

	}
}
