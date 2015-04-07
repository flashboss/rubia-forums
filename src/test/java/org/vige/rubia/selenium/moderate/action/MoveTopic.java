package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyTopic.goTo;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

public class MoveTopic {

	public static final String SPLIT_PANEL_LINK = "miviewtopicbody16";
	public static final String BUTTON_ROW = "buttonrow";
	public static final String FORUM_SELECT = "forumtablestyle";
	public static final String MESSAGE_RESULT = "successtext";

	public static void goToSplitPanel(WebDriver driver, Topic topic) {
		goTo(driver, topic);
		WebElement splitPanelLink = driver.findElement(id("miviewtopicbody16"))
				.findElement(xpath("form/ul/li[5]/a"));
		splitPanelLink.click();
	}

	private static void select(WebDriver driver, Topic topic, Forum forumDest) {
		if (topic != null) {
			List<Post> posts = topic.getPosts();
			if (posts != null)
				for (Post post : posts) {
					WebElement option = driver
							.findElements(className("forumtablestyle"))
							.get(1)
							.findElement(
									xpath("tbody/tr/td/p[contains(text(),'"
											+ post.getMessage().getText()
											+ "')]/../../td[3]/input"));
					option.click();
				}
		}
		if (forumDest != null) {
			WebElement forumSelect = driver
					.findElements(className("forumtablestyle")).get(0)
					.findElement(xpath("tbody/tr[4]/td[2]/select"));
			forumSelect.sendKeys(forumDest.getName());
			List<Topic> topics = forumDest.getTopics();
			if (topics != null && topics.size() == 1) {
				WebElement topicSelect = driver
						.findElements(className("forumtablestyle")).get(0)
						.findElement(xpath("tbody/tr[3]/td[2]/input"));
				topicSelect.clear();
				topicSelect.sendKeys(topics.get(0).getSubject());
			}
		}
	}

	public static String moveTopicSelectedUp(WebDriver driver, Topic topic,
			Forum forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(0)
				.findElement(xpath("input[1]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

	public static String moveTopicFromSelectedUp(WebDriver driver, Topic topic,
			Forum forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(0)
				.findElement(xpath("input[2]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

	public static String moveTopicSelectedDown(WebDriver driver, Topic topic,
			Forum forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(1)
				.findElement(xpath("input[1]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

	public static String moveTopicFromSelectedDown(WebDriver driver,
			Topic topic, Forum forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(1)
				.findElement(xpath("input[2]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

}
