package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.properties.OperationType.CONFIRM;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.properties.OperationType;

public class RemoveForum {

	public static final String REMOVE_LINK = "buttonMed";
	public static final String REMOVE_FORUM_LINK = "buttonMed";
	public static final String SELECT_TYPE = "form";
	public static final String RESULT_REMOVE_FORUM = "forumtitletext";

	public static String removeForum(WebDriver driver, OperationType removeType, ForumBean forum) {
		if (forum != null)
			for (TopicBean topic : forum.getTopics()) {
				WebElement topicToSelect = driver.findElement(linkText(topic.getSubject()))
						.findElement(xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement removeForum = driver.findElements(className(REMOVE_LINK)).get(0);
		removeForum.click();
		String message = "";
		WebElement resultRemoveForum = null;
		try {
			resultRemoveForum = driver.findElement(className(RESULT_REMOVE_FORUM))
					.findElement(xpath("//table/tbody/tr/td"));
			message = resultRemoveForum.getText();
		} catch (NoSuchElementException ex) {
			if (removeType == CONFIRM) {
				WebElement option = driver.findElements(tagName(SELECT_TYPE)).get(1).findElement(xpath("input[3]"));
				option.click();
				resultRemoveForum = driver.findElement(className(RESULT_REMOVE_FORUM))
						.findElement(xpath("//table/tbody/tr/td"));
				message = resultRemoveForum.getText();
			} else {
				WebElement option = driver.findElements(tagName(SELECT_TYPE)).get(1).findElement(xpath("input[4]"));
				option.click();
			}
			return message;

		}
		return message;
	}

}
