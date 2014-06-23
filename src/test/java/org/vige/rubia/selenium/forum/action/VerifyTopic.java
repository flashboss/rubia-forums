package org.vige.rubia.selenium.forum.action;

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

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;
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
			WebElement forum = driver.findElement(linkText(forumName));
			forum.click();
			List<WebElement> topicComponents = driver
					.findElements(className(TOPIC_TABLE));
			for (WebElement topicComponent : topicComponents) {
				Topic topic = new Topic();
				WebElement subjectComponent = topicComponent
						.findElement(xpath(SUBJECT_LINK));
				String subjectText = subjectComponent.getText();
				topic.setSubject(subjectText);
				WebElement subjectTypeComponent = null;
				try {
					subjectTypeComponent = topicComponent
							.findElement(xpath(TYPE_SUBJECT_OUTPUT_TEXT));
				} catch (NoSuchElementException ex) {
					topic.setType(0);
				}
				if (subjectTypeComponent != null)
					if (subjectTypeComponent.getText().trim()
							.equals(TOPIC_STICKY))
						topic.setType(1);
					else
						topic.setType(2);
				String user = topicComponent.findElement(xpath(USER_LINK))
						.getText();
				Poster poster = new Poster();
				poster.setUserId(user);
				topic.setPoster(poster);
				subjectComponent.click();
				WebElement question = null;
				try {
					question = driver
							.findElement(className(QUESTION_OUTPUT_TEXT));
				} catch (NoSuchElementException ex) {

				}
				if (question != null) {
					Poll poll = new Poll();
					poll.setTitle(question.getText());
					List<WebElement> pollComponents = driver
							.findElements(className(ANSWER_OUTPUT_TEXT));
					List<PollOption> pollOptions = new ArrayList<PollOption>();
					for (WebElement pollComponent : pollComponents) {
						PollOption pollOption = new PollOption();
						pollOption.setQuestion(pollComponent.getText());
						pollOption.setPoll(poll);
						pollOptions.add(pollOption);
					}

					WebElement votesResultComponent = driver
							.findElement(linkText(RESULT_VOTES_LINK));
					votesResultComponent.click();
					List<WebElement> pollComponentsTr = driver.findElement(
							className(FORUM_POLL_TABLE)).findElements(
							xpath("tbody/tr"));
					pollComponents.clear();
					for (int i = 0; i < pollComponentsTr.size(); i++) {
						if (i != 0 && i < pollComponentsTr.size() - 2)
							pollComponents.add(pollComponentsTr.get(i)
									.findElement(xpath("td")));
					}
					for (int i = 0; i < pollComponents.size(); i++) {
						WebElement pollComponent = pollComponents.get(i);
						PollOption pollOption = pollOptions.get(i);
						String numberOfVotes = driver
								.findElement(className(FORUM_POLL_TABLE))
								.findElement(
										xpath("tbody/tr[td/text()='"
												+ pollComponent.getText()
												+ "']/td[3]")).getText();
						String pollOptionPosition = driver
								.findElement(className(FORUM_POLL_TABLE))
								.findElement(
										xpath("tbody/tr[td/text()='"
												+ pollComponent.getText()
												+ "']/td[4]")).getText();
						pollOption.setVotes(new Integer(numberOfVotes
								.substring(0, numberOfVotes.length() - 1)));
						pollOption
								.setPollOptionPosition(new Integer(
										pollOptionPosition
												.substring(
														1,
														pollOptionPosition
																.length() - 1)
												.trim()));
					}
					WebElement votesComponent = driver
							.findElement(linkText(VOTES_LINK));
					votesComponent.click();
					poll.setOptions(pollOptions);
					topic.setPoll(poll);
				}
				List<WebElement> postComponents = driver
						.findElements(className(BODY_OUTPUT_TEXT));
				List<Post> posts = new ArrayList<Post>();
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
					} catch (ParseException e) {
						// TODO Auto-generated catch block
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
								.findElement(xpath(ATTACHMENT_NAME_OUTPUT_TEXT))
								.getText();
						String attachmentComment = attachmentComponent
								.findElement(
										xpath(ATTACHMENT_COMMENT_OUTPUT_TEXT))
								.getText();
						String attachmentSize = attachmentComponent
								.findElement(xpath(ATTACHMENT_SIZE_OUTPUT_TEXT))
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
				topic.setPosts(posts);
				topics.add(topic);
			}
		}
		return topics;
	}
}
