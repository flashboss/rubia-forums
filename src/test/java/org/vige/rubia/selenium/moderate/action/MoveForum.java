package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.properties.OperationType.CONFIRM;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Topic;
import org.vige.rubia.properties.OperationType;

public class MoveForum {

	public static final String MOVE_FORUM = "tfoot";
	public static final String SELECT_TYPE = "buttons";
	public static final String SELECT_INPUT = "forum_to_id";
	public static final String RESULT_MOVE_FORUM = "javax_faces_developmentstage_messages";

	public static String moveForum(WebDriver driver, OperationType moveType,
			Forum sourceForum, Forum destForum) {
		if (sourceForum != null)
			for (Topic topic : sourceForum.getTopics()) {
				WebElement topicToSelect = driver.findElement(
						linkText(topic.getSubject())).findElement(
						xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement moveForum = driver.findElement(tagName(MOVE_FORUM))
				.findElement(xpath("tr/td/input[2]"));
		moveForum.click();
		String message = "";
		WebElement resultMoveForum = null;
		try {
			resultMoveForum = driver.findElement(id(RESULT_MOVE_FORUM));
			message = resultMoveForum.getText();
		} catch (NoSuchElementException ex) {
			if (moveType == CONFIRM) {
				if (destForum != null) {
					WebElement select = driver.findElement(name(SELECT_INPUT));
					select.sendKeys(destForum.getName());
				}
				WebElement option = driver.findElement(className(SELECT_TYPE))
						.findElement(xpath("input[1]"));
				option.click();
				resultMoveForum = driver.findElement(id(RESULT_MOVE_FORUM));
				message = resultMoveForum.getText();
			} else {
				WebElement option = driver.findElement(className(SELECT_TYPE))
						.findElement(xpath("input[2]"));
				option.click();
			}
			return message;
		}
		return message;
	}

}
