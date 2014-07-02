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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import org.vige.rubia.model.Topic;

public class VerifyTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String POST_SUBJECT_TEXT = getBundle("ResourceJSF")
			.getString("Post_subject") + ": ";
	public static final String CREATE_DATE_TEXT = getBundle("ResourceJSF")
			.getString("Posted") + ": ";
	public static final String TOPIC_TABLE = "forumtablestyle";
	public static final String TOPIC_STICKY = getBundle("ResourceJSF")
			.getString("Topic_Sticky");
	public static final String SUBJECT_LINK = "tbody/tr/td[2]/h3/a";
	public static final String TYPE_SUBJECT_OUTPUT_TEXT = "tbody/tr/td[2]/h3/b";
	public static final String REPLIED_OUTPUT_TEXT = "tbody/tr[contains(@class,'Row')]/td[3]";
	public static final String VIEW_OUTPUT_TEXT = "tbody/tr[contains(@class,'Row')]/td[4]";
	public static final String FORUM_POLL_TABLE = "forumPollTable";
	public static final String BODY_OUTPUT_TEXT = "forumpostcontent";
	public static final String POST_SUBJECT_OUTPUT_TEXT = "../../tr/td[2]/div[@id='miviewtopicbody7']/ul/li[3]";
	public static final String QUESTION_OUTPUT_TEXT = "questionCell";
	public static final String ANSWER_OUTPUT_TEXT = "answerCell";
	public static final String CREATE_DATE_OUTPUT_TEXT = "../../tr/td[2]/div[@id='miviewtopicbody7']/ul/li[2]";
	public static final String ATTACHMENT_LIST = "forumAttachmentTable";
	public static final String ATTACHMENT_NAME_OUTPUT_TEXT = "tbody/tr/td[2]";
	public static final String ATTACHMENT_COMMENT_OUTPUT_TEXT = "tbody/tr[2]/td[2]";
	public static final String ATTACHMENT_SIZE_OUTPUT_TEXT = "tbody/tr[3]/td[2]";
	public static final String USER_LINK = "tbody/tr/td[2]/a";
	public static final String VOTES_LINK = getBundle("ResourceJSF").getString(
			"View_ballot");
	public static final String RESULT_VOTES_LINK = getBundle("ResourceJSF")
			.getString("View_results");
	public static final String TOTAL_VOTES_LINK = "totalCell";
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"E MMM d, yyyy H:mm a");

	public static List<Topic> getTopicsOfForums(WebDriver driver,
			String... forumNames) {
		List<Topic> topics = new ArrayList<Topic>();
		for (String forumName : forumNames) {
			WebElement home = driver.findElement(linkText(HOME_LINK));
			home.click();
			WebElement forumEl = driver.findElement(linkText(forumName));
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
					topic.setReplies(replies);
					topic.setViewCount(viewCount);
					Poster poster = new Poster();
					poster.setUserId(user);
					topic.setPoster(poster);
					subjectComponent.click();
					topic.setPoll(getPollOfCurrentTopic(driver));
					List<WebElement> postComponents = driver
							.findElements(className(BODY_OUTPUT_TEXT));
					List<Post> posts = new ArrayList<Post>();
					Date lastPostDate = null;
					for (WebElement postComponent : postComponents) {
						Post post = new Post();
						String body = postComponent.findElement(xpath("p"))
								.getText();
						String post_subject = postComponent
								.findElement(xpath(POST_SUBJECT_OUTPUT_TEXT))
								.getText().split(POST_SUBJECT_TEXT)[1];
						String createDateStr = postComponent
								.findElement(xpath(CREATE_DATE_OUTPUT_TEXT))
								.getText().split(CREATE_DATE_TEXT)[1];
						Date createDate = null;
						try {
							createDate = dateFormat.parse(createDateStr);
							lastPostDate = createDate;
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Message message = new Message();
						message.setSubject(post_subject);
						message.setText(body);
						post.setMessage(message);
						post.setCreateDate(createDate);
						List<WebElement> attachmentComponents = postComponent
								.findElements(className(ATTACHMENT_LIST));
						for (WebElement attachmentComponent : attachmentComponents) {
							String attachmentName = attachmentComponent
									.findElement(
											xpath(ATTACHMENT_NAME_OUTPUT_TEXT))
									.getText();
							String attachmentComment = attachmentComponent
									.findElement(
											xpath(ATTACHMENT_COMMENT_OUTPUT_TEXT))
									.getText();
							String attachmentSize = attachmentComponent
									.findElement(
											xpath(ATTACHMENT_SIZE_OUTPUT_TEXT))
									.getText();
							Attachment attachment = new Attachment();
							attachment.setComment(attachmentComment);
							attachment.setName(attachmentName);
							attachment.setSize(new Integer(attachmentSize
									.split(" B")[0]));
							post.addAttachment(attachment);
						}
						posts.add(post);
					}
					Forum forum = new Forum();
					forum.setName(driver.findElement(linkText(forumName))
							.getText());
					topic.setForum(forum);
					topic.setPosts(posts);
					topic.setLastPostDate(lastPostDate);
					topics.add(topic);
					driver.findElement(linkText(forumName)).click();
				}
			}
		}
		return topics;
	}
}
