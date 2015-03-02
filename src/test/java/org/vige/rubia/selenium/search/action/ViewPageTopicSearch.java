package org.vige.rubia.selenium.search.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import org.vige.rubia.model.Topic;
import org.vige.rubia.search.SearchCriteria;

public class ViewPageTopicSearch extends ViewPageSearch {

	public static final String TOPIC_VIEW = "rf-dt-b";

	public static final String TOPIC_SUBJECT = "tr/td[2]/h3/a";

	public static final String TOPIC_POSTER = "tr/td[2]/a";

	public static final String LAST_POST_SUBJECT = "tr/td[5]/a";

	public static final String LAST_POST_POSTER = "tr/td[5]/a[2]";

	public static final String LAST_POST_CREATED_DATE = "tr/td[5]";

	public static final String TOPIC_REPLIES = "tr/td[3]";

	public static final String TOPIC_VIEWS = "tr/td[4]";

	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public static List<Topic> searchTopic(WebDriver driver,
			SearchCriteria arguments) {
		addKeys(driver, arguments);
		return getTopics(driver, arguments);
	}

	public static List<Topic> getTopics(WebDriver driver,
			SearchCriteria arguments) {
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
		WebElement messageResult = getMessageResult(driver);
		if (messageResult != null
				&& (messageResult.getText().contains("Value is required") || messageResult
						.getText().equals(
								getBundle("ResourceJSF").getString(
										"Search_topics_not_found"))))
			return null;
		else {
			List<Topic> topics = new ArrayList<Topic>();
			List<WebElement> elements = driver
					.findElements(className(TOPIC_VIEW));
			for (WebElement element : elements)
				topics.add(getTopic(driver, element));
			return topics;
		}
	}

	private static Topic getTopic(WebDriver driver, WebElement element) {
		Topic topic = new Topic();
		topic.setSubject(element.findElement(xpath(TOPIC_SUBJECT)).getText());
		topic.setPoster(new Poster(element.findElement(xpath(TOPIC_POSTER))
				.getText()));
		Post lastPost = new Post();
		Message message = new Message();
		message.setSubject(element.findElement(xpath(LAST_POST_SUBJECT))
				.getText());
		lastPost.setMessage(message);
		lastPost.setPoster(new Poster(element.findElement(
				xpath(LAST_POST_POSTER)).getText()));
		String createdDate = element.findElement(xpath(LAST_POST_CREATED_DATE))
				.getText().split("\n")[2];
		try {
			Date date = dateFormat.parse(createdDate);
			lastPost.setCreateDate(date);
			topic.setLastPostDate(date);
		} catch (ParseException e) {
		}
		topic.getPosts().add(lastPost);
		topic.setReplies(new Integer(element.findElement(xpath(TOPIC_REPLIES))
				.getText()));
		topic.setViewCount(new Integer(element.findElement(xpath(TOPIC_VIEWS))
				.getText()));
		return topic;
	}

}
