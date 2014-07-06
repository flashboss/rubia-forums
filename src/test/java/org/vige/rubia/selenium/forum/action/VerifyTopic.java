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
import static org.vige.rubia.selenium.forum.action.VerifyPoll.getPollOfCurrentTopic;
import static org.vige.rubia.selenium.forum.action.VerifyPost.getPostsOfCurrentTopic;
import static org.vige.rubia.selenium.forum.model.Links.CATEGORY_TEMPLATE_LINK;
import static org.vige.rubia.selenium.forum.model.Links.FORUM_TEMPLATE_LINK;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Category;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Poster;
import org.vige.rubia.model.Topic;

public class VerifyTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String TOPIC_TABLE = "forumtablestyle";
	public static final String TOPIC_STICKY = getBundle("ResourceJSF")
			.getString("Topic_Sticky");
	public static final String SUBJECT_LINK = "tbody/tr/td[2]/h3/a";
	public static final String TYPE_SUBJECT_OUTPUT_TEXT = "tbody/tr/td[2]/h3/b";
	public static final String REPLIED_OUTPUT_TEXT = "tbody/tr[contains(@class,'Row')]/td[3]";
	public static final String VIEW_OUTPUT_TEXT = "tbody/tr[contains(@class,'Row')]/td[4]";
	public static final String LAST_POST_DATE_OUTPUT_TEXT = "tbody/tr[contains(@class,'Row')]/td[5]";
	public static final String USER_LINK = "tbody/tr/td[2]/a";
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public static List<Topic> getTopicsOfForums(WebDriver driver,
			Forum... forums) {
		List<Topic> topics = new ArrayList<Topic>();
		for (Forum forum : forums) {
			WebElement home = driver.findElement(linkText(HOME_LINK));
			home.click();
			WebElement forumEl = driver.findElement(linkText(forum.getName()));
			forumEl.click();
			List<WebElement> tableComponents = driver
					.findElements(className(TOPIC_TABLE));
			int tableComponentsSize = tableComponents.size();
			for (int i = 0; i < tableComponentsSize; i++) {
				List<WebElement> subjectComponents = driver
						.findElements(className(TOPIC_TABLE)).get(i)
						.findElements(xpath(SUBJECT_LINK));
				int subjectComponentsSize = subjectComponents.size();
				for (int i4 = 0; i4 < subjectComponentsSize; i4++) {
					WebElement topicTable = driver.findElements(
							className(TOPIC_TABLE)).get(i);
					subjectComponents = topicTable
							.findElements(xpath(SUBJECT_LINK));
					WebElement subjectComponent = subjectComponents.get(i4);
					Topic topic = new Topic();
					String subjectText = subjectComponent.getText();
					topic.setSubject(subjectText);
					List<WebElement> subjectTypeComponents = topicTable
							.findElements(xpath(TYPE_SUBJECT_OUTPUT_TEXT));
					if (subjectTypeComponents.size() == 0)
						topic.setType(0);
					else if (subjectTypeComponents.get(i4).getText().trim()
							.equals(TOPIC_STICKY))
						topic.setType(1);
					else
						topic.setType(2);
					String user = topicTable.findElements(xpath(USER_LINK))
							.get(i4).getText();
					int replies = new Integer(topicTable
							.findElements(xpath(REPLIED_OUTPUT_TEXT)).get(i4)
							.getText());
					int viewCount = new Integer(topicTable
							.findElements(xpath(VIEW_OUTPUT_TEXT)).get(i4)
							.getText());
					Date lastPostDate = null;
					try {
						lastPostDate = dateFormat
								.parse(topicTable
										.findElements(
												xpath(LAST_POST_DATE_OUTPUT_TEXT))
										.get(i4).getText().split("\n")[2]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					topic.setReplies(replies);
					topic.setViewCount(viewCount);
					topic.setLastPostDate(lastPostDate);
					Poster poster = new Poster();
					poster.setUserId(user);
					topic.setPoster(poster);
					subjectComponent.click();
					topic.setPoll(getPollOfCurrentTopic(driver));
					topic.setPosts(getPostsOfCurrentTopic(driver));
					addParents(driver, topic);
					topics.add(topic);
					driver.findElement(linkText(forum.getName())).click();
				}
			}
		}
		return topics;
	}

	private static void addParents(WebDriver driver, Topic topic) {
		Forum forum = new Forum();
		forum.setName(driver.findElement(
				linkText(driver.findElement(FORUM_TEMPLATE_LINK.getValue())
						.getText())).getText());
		topic.setForum(forum);
		Category category = new Category();
		category.setTitle(driver.findElement(CATEGORY_TEMPLATE_LINK.getValue())
				.getText());
		forum.setCategory(category);

	}
}
