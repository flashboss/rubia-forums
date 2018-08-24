package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.properties.OperationType.CANCEL;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.By.xpath;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.properties.OperationType;

public class MoveTopic {

	public static final String MOVE_TOPIC = "miviewtopicbody16";
	public static final String SPLIT_PANEL_LINK = "miviewtopicbody16";
	public static final String BUTTON_ROW = "buttonrow";
	public static final String BUTTON_CONFIRM = "buttonmed";
	public static final String FORUM_SELECT = "forumtablestyle";
	public static final String MESSAGE_RESULT = "successtext";
	public static final String MOVE_TOPIC_SELECT = "forum_to_id";
	public static final String RESULT_MOVE_TOPIC = "forumtitletext";

	public static void goToSplitPanel(WebDriver driver, TopicBean topic) {
		goTo(driver, topic);
		WebElement splitPanelLink = driver.findElement(id(SPLIT_PANEL_LINK)).findElement(xpath("form/ul/li[5]/a"));
		splitPanelLink.click();
	}

	private static void select(WebDriver driver, TopicBean topic, ForumBean forumDest) {
		if (topic != null) {
			List<PostBean> posts = topic.getPosts();
			if (posts != null)
				for (PostBean post : posts) {
					WebElement option = driver.findElements(className("forumtablestyle")).get(1).findElement(xpath(
							"tbody/tr/td/p[contains(text(),'" + post.getMessage().getText() + "')]/../../td[3]/input"));
					option.click();
				}
		}
		if (forumDest != null) {
			Select forumSelect = new Select(driver.findElements(className("forumtablestyle")).get(0)
					.findElement(xpath("tbody/tr[4]/td[2]/select")));
			forumSelect.selectByVisibleText(forumDest.getName());
			List<TopicBean> topics = forumDest.getTopics();
			if (topics != null && topics.size() == 1) {
				WebElement topicSelect = driver.findElements(className("forumtablestyle")).get(0)
						.findElement(xpath("tbody/tr[3]/td[2]/input"));
				topicSelect.clear();
				topicSelect.sendKeys(topics.get(0).getSubject());
			}
		}
	}

	public static String moveTopic(WebDriver driver, ForumBean forumDest, OperationType operationType) {
		WebElement moveTopic = driver.findElement(id(MOVE_TOPIC)).findElement(xpath("form/ul/li[3]/a"));
		moveTopic.click();
		if (forumDest != null) {
			WebElement forumSelect = driver.findElement(name(MOVE_TOPIC_SELECT));
			Select select = new Select(forumSelect);
			select.selectByVisibleText(forumDest.getName());
		}
		WebElement button = null;
		if (operationType.equals(CANCEL))
			button = driver.findElements(className(BUTTON_CONFIRM)).get(1);
		else
			button = driver.findElements(className(BUTTON_CONFIRM)).get(0);
		button.click();
		String result = "";
		WebElement message = driver.findElement(className(RESULT_MOVE_TOPIC)).findElement(xpath("//table/tbody/tr/td"));
		result = message.getText();
		return result;
	}

	public static String moveTopicSelectedUp(WebDriver driver, TopicBean topic, ForumBean forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(0).findElement(xpath("input[1]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

	public static String moveTopicFromSelectedUp(WebDriver driver, TopicBean topic, ForumBean forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(0).findElement(xpath("input[2]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

	public static String moveTopicSelectedDown(WebDriver driver, TopicBean topic, ForumBean forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(1).findElement(xpath("input[1]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

	public static String moveTopicFromSelectedDown(WebDriver driver, TopicBean topic, ForumBean forumDest) {
		select(driver, topic, forumDest);
		WebElement button = driver.findElements(className(BUTTON_ROW)).get(1).findElement(xpath("input[2]"));
		button.click();
		WebElement message = driver.findElement(className(MESSAGE_RESULT));
		return message.getText();
	}

}
