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

import static it.vige.rubia.selenium.forum.action.VerifyPost.getPostsOfCurrentTopic;
import static it.vige.rubia.selenium.profile.action.VerifyProfile.verifyProfile;
import static it.vige.rubia.ui.ForumUtil.truncate;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.model.Message;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.search.SearchCriteria;
import it.vige.rubia.selenium.forum.action.VerifyTopic;

public class ViewPageTopicSearch extends ViewPageSearch {

	public static final String TOPIC_VIEW = "rf-dt-b";

	public static final String TOPIC_SUBJECT = "tr/td[2]/h3/a";

	public static final String TOPIC_POSTER = "tr/td[2]/a";

	public static final String LAST_POST_SUBJECT = "tr/td[5]/a";

	public static final String LAST_POST_POSTER = "tr/td[5]/a[2]";

	public static final String LAST_POST_CREATED_DATE = "tr/td[5]";

	public static final String TOPIC_REPLIES = "tr/td[3]";

	public static final String TOPIC_VIEWS = "tr/td[4]";

	public static String PROFILE_LINK = "rf-dt-fst-r";

	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static List<Topic> searchTopic(WebDriver driver, SearchCriteria arguments) {
		addKeys(driver, arguments);
		return getTopics(driver, arguments);
	}

	public static List<Topic> getTopics(WebDriver driver, SearchCriteria arguments) {
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
		WebElement messageResult = getMessageResult(driver);
		if (messageResult != null && (messageResult.getText().contains("Value is required")
				|| messageResult.getText().equals(getBundle("ResourceJSF").getString("Search_topics_not_found"))))
			return null;
		else {
			List<Topic> topics = new ArrayList<Topic>();
			List<WebElement> elements = driver.findElements(className(TOPIC_VIEW));
			for (WebElement element : elements)
				topics.add(getTopic(driver, element));
			return topics;
		}
	}

	public static Topic getTopic(WebDriver driver, Topic topic) {
		WebElement topicElement = driver.findElement(linkText(topic.getSubject()));
		topicElement.click();
		return VerifyTopic.getTopic(driver);
	}

	private static Topic getTopic(WebDriver driver, WebElement element) {
		Topic topic = new Topic();
		topic.setSubject(element.findElement(xpath(TOPIC_SUBJECT)).getText());
		topic.setPoster(new Poster(element.findElement(xpath(TOPIC_POSTER)).getText()));
		Post lastPost = new Post();
		Message message = new Message();
		message.setSubject(element.findElement(xpath(LAST_POST_SUBJECT)).getText());
		lastPost.setMessage(message);
		lastPost.setPoster(new Poster(element.findElement(xpath(LAST_POST_POSTER)).getText()));
		String createdDate = element.findElement(xpath(LAST_POST_CREATED_DATE)).getText().split("\n")[2];
		try {
			Date date = dateFormat.parse(createdDate);
			lastPost.setCreateDate(date);
			topic.setLastPostDate(date);
		} catch (ParseException e) {
		}
		topic.getPosts().add(lastPost);
		topic.setReplies(new Integer(element.findElement(xpath(TOPIC_REPLIES)).getText()));
		topic.setViewCount(new Integer(element.findElement(xpath(TOPIC_VIEWS)).getText()));
		return topic;
	}

	public static Poster getPosterLastPost(WebDriver driver, Topic topic) {
		WebElement profileLink = driver.findElement(className(PROFILE_LINK))
				.findElement(xpath("td[5]/a[contains(text(),'" + truncate(topic.getSubject(), 25) + "')]"))
				.findElement(xpath("../a[2]"));
		String userId = profileLink.getText();
		profileLink.click();
		Poster poster = verifyProfile(driver, userId);
		return poster;
	}

	public static Poster getPoster(WebDriver driver, Topic topic) {
		WebElement profileLink = driver.findElement(linkText(topic.getSubject())).findElement(xpath("../../a"));
		String userId = profileLink.getText();
		profileLink.click();
		Poster poster = verifyProfile(driver, userId);
		return poster;
	}

	public static Post getLastPostOfCurrentForum(WebDriver driver, Topic topic) {
		WebElement postComponent = driver.findElement(className(PROFILE_LINK))
				.findElement(xpath("td[5]/a[contains(text(),'" + truncate(topic.getSubject(), 25) + "')]"))
				.findElement(xpath("../a"));
		postComponent.click();
		List<Post> posts = getPostsOfCurrentTopic(driver);
		return posts.get(posts.size() - 1);
	}

}
