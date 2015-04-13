package it.vige.rubia.selenium.links.action;

import static it.vige.rubia.ui.Constants.BY;
import static it.vige.rubia.ui.Constants.RE;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.selenium.forum.action.VerifyForum;
import it.vige.rubia.selenium.forum.action.VerifyTopic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewRSSLink {

	public static String RSS_LINK = "RSS";
	public static String FEED_TITLE_TEXT = "feedTitleText";
	public static String FEED_SUBTITLE_TEXT = "feedSubtitleText";
	public static String ENTRY_LINK = "entry";
	public static String LAST_UPDATED = "lastUpdated";
	public static String FEED_ENTRY_CONTENT = "feedEntryContent";

	public static void goTo(WebDriver driver) {
		WebElement footerLink = driver.findElement(linkText(RSS_LINK));
		footerLink.click();
	}

	public static Forum getPage(WebDriver driver, Forum forum) {
		VerifyForum.goTo(driver, forum);
		goTo(driver);
		Forum result = new Forum();
		result.setName(driver.findElement(id(FEED_TITLE_TEXT)).getText()
				.split(": ")[1]);
		result.setCategory(new Category(driver
				.findElement(id(FEED_SUBTITLE_TEXT)).getText()
				.split(" in category ")[1]));
		Map<String, Topic> topics = new HashMap<String, Topic>();
		List<WebElement> entries = driver.findElements(className(ENTRY_LINK));
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
		for (WebElement entry : entries) {
			String[] entryText = entry.getText().split(BY);
			String lastUpdated = entry.findElement(className(LAST_UPDATED))
					.getText();
			String topicTitle = entryText[0].replace(RE, "");
			Topic topic = topics.get(topicTitle);
			if (topic == null) {
				topic = new Topic(topicTitle);
				topics.put(topicTitle, topic);
			}
			Post post = new Post(entry.findElement(
					className(FEED_ENTRY_CONTENT)).getText());
			post.setPoster(new Poster(entryText[1].split("\n")[0]));
			try {
				post.setCreateDate(dateFormat.parse(lastUpdated));
			} catch (ParseException e) {
			}
			post.getMessage().setSubject(entryText[0]);
			topic.getPosts().add(post);
		}
		result.setTopics(new ArrayList<Topic>(topics.values()));
		return result;
	}

	public static Topic getPage(WebDriver driver, Topic topic) {
		VerifyTopic.goTo(driver, topic);
		goTo(driver);
		Topic result = new Topic();
		result.setSubject(driver.findElement(id(FEED_TITLE_TEXT)).getText()
				.split(": ")[1]);
		String[] splittedText = driver.findElement(id(FEED_SUBTITLE_TEXT))
				.getText().split(" in topic | in forum | in category ");
		result.setForum(new Forum(splittedText[2]));
		result.getForum().setCategory(new Category(splittedText[3]));
		List<Post> posts = new ArrayList<Post>();
		List<WebElement> entries = driver.findElements(className(ENTRY_LINK));
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
		for (WebElement entry : entries) {
			String[] entryText = entry.getText().split(BY);
			String lastUpdated = entry.findElement(className(LAST_UPDATED))
					.getText();
			Post post = new Post(entryText[0]);
			post.setPoster(new Poster(entryText[1].split("\n")[0]));
			try {
				post.setCreateDate(dateFormat.parse(lastUpdated));
			} catch (ParseException e) {
			}
			post.setMessage(new Message(entry.findElement(
					className(FEED_ENTRY_CONTENT)).getText()));
			post.getMessage().setSubject(entryText[0]);
			posts.add(post);
		}
		result.setPosts(posts);
		return result;
	}
}
