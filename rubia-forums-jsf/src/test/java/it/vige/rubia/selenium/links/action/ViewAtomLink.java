package it.vige.rubia.selenium.links.action;

import static it.vige.rubia.Constants.BY;
import static it.vige.rubia.Constants.RE;
import static it.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfCurrentPostInPage;
import static it.vige.rubia.selenium.forum.action.VerifyPoll.getPollOfCurrentTopic;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.selenium.forum.action.VerifyForum;
import it.vige.rubia.selenium.forum.action.VerifyTopic;
import it.vige.rubia.selenium.forum.action.Write;

public class ViewAtomLink extends Write {

	public static String ATOM_LINK = "ATOM";
	public static String FEED_TITLE_TEXT = "feedTitleText";
	public static String FEED_SUBTITLE_TEXT = "feedSubtitleText";
	public static String ENTRY_LINK = "entry";
	public static String LAST_UPDATED = "lastUpdated";
	public static String FEED_ENTRY_CONTENT = "feedEntryContent";

	public static void goTo(WebDriver driver) {
		WebElement footerLink = driver.findElement(linkText(ATOM_LINK));
		footerLink.click();
	}

	public static ForumBean getPage(WebDriver driver, ForumBean forum) {
		VerifyForum.goTo(driver, forum);
		goTo(driver);
		ForumBean result = new ForumBean();
		result.setName(driver.findElement(id(FEED_TITLE_TEXT)).getText().split(": ")[1]);
		result.setCategory(
				new CategoryBean(driver.findElement(id(FEED_SUBTITLE_TEXT)).getText().split(" in category ")[1]));
		Map<String, TopicBean> topics = new HashMap<String, TopicBean>();
		int entriesSize = driver.findElements(className(ENTRY_LINK)).size();
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
		for (int i = 0; i < entriesSize; i++) {
			WebElement entry = driver.findElements(className(ENTRY_LINK)).get(i);
			String[] entryText = entry.getText().split(BY);
			String lastUpdated = entry.findElement(className(LAST_UPDATED)).getText();
			String topicTitle = entryText[0].replace(RE, "");
			TopicBean topic = topics.get(topicTitle);
			if (topic == null) {
				topic = new TopicBean(topicTitle);
				topics.put(topicTitle, topic);
			}
			PostBean post = new PostBean(entry.findElement(className(FEED_ENTRY_CONTENT)).getText());
			post.setPoster(new PosterBean(entryText[1].split("\n")[0]));
			try {
				post.setCreateDate(dateFormat.parse(lastUpdated));
			} catch (ParseException e) {
			}
			post.getMessage().setSubject(entryText[0]);
			topic.getPosts().add(post);
			WebElement entryLink = driver.findElement(linkText(entry.getText().split("\n")[0]));
			entryLink.click();
			List<AttachmentBean> attachments = getAttachmentsOfCurrentPostInPage(driver, post);
			post.setAttachments(attachments);
			if (topic.getPoll() == null) {
				PollBean poll = getPollOfCurrentTopic(driver);
				topic.setPoll(poll);
			}
			VerifyForum.goTo(driver, forum);
			goTo(driver);
		}
		result.setTopics(new ArrayList<TopicBean>(topics.values()));
		returnToHome(driver);
		return result;
	}

	public static TopicBean getPage(WebDriver driver, TopicBean topic) {
		VerifyTopic.goTo(driver, topic);
		goTo(driver);
		TopicBean result = new TopicBean();
		result.setSubject(driver.findElement(id(FEED_TITLE_TEXT)).getText().split(": ")[1]);
		String[] splittedText = driver.findElement(id(FEED_SUBTITLE_TEXT)).getText()
				.split(" in topic | in forum | in category ");
		result.setForum(new ForumBean(splittedText[2]));
		result.getForum().setCategory(new CategoryBean(splittedText[3]));
		List<PostBean> posts = new ArrayList<PostBean>();
		int entriesSize = driver.findElements(className(ENTRY_LINK)).size();
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
		for (int i = 0; i < entriesSize; i++) {
			WebElement entry = driver.findElements(className(ENTRY_LINK)).get(i);
			String[] entryText = entry.getText().split(BY);
			String lastUpdated = entry.findElement(className(LAST_UPDATED)).getText();
			PostBean post = new PostBean(entryText[0]);
			post.setPoster(new PosterBean(entryText[1].split("\n")[0]));
			try {
				post.setCreateDate(dateFormat.parse(lastUpdated));
			} catch (ParseException e) {
			}
			post.setMessage(new MessageBean(entry.findElement(className(FEED_ENTRY_CONTENT)).getText()));
			post.getMessage().setSubject(entryText[0]);
			posts.add(post);
			WebElement entryLink = driver.findElement(linkText(entry.getText().split("\n")[0]));
			entryLink.click();
			List<AttachmentBean> attachments = getAttachmentsOfCurrentPostInPage(driver, post);
			post.setAttachments(attachments);
			if (topic.getPoll() == null) {
				PollBean poll = getPollOfCurrentTopic(driver);
				result.setPoll(poll);
			}
			VerifyTopic.goTo(driver, result);
			goTo(driver);
		}
		result.setPosts(posts);
		returnToHome(driver);
		return result;
	}
}
