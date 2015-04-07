package org.vige.rubia.selenium.moderate.action;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.properties.OperationType.CONFIRM;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Topic;
import org.vige.rubia.properties.OperationType;

public class RemoveForum {

	public static final String REMOVE_LINK = "tfoot";
	public static final String REMOVE_FORUM_LINK = "buttonMed";
	public static final String SELECT_TYPE = "form";
	public static final String RESULT_REMOVE_FORUM = "javax_faces_developmentstage_messages";

	public static String removeForum(WebDriver driver,
			OperationType removeType, Forum forum) {
		if (forum != null)
			for (Topic topic : forum.getTopics()) {
				WebElement topicToSelect = driver.findElement(
						linkText(topic.getSubject())).findElement(
						xpath("../../td[5]/input"));
				topicToSelect.click();
			}
		WebElement removeForum = driver.findElement(tagName(REMOVE_LINK))
				.findElement(xpath("tr/td/input[1]"));
		removeForum.click();
		String message = "";
		WebElement resultRemoveForum = null;
		try {
			resultRemoveForum = driver
					.findElement(id(RESULT_REMOVE_FORUM));
			message = resultRemoveForum.getText();
		} catch (NoSuchElementException ex) {
			if (removeType == CONFIRM) {
				WebElement option = driver.findElements(tagName(SELECT_TYPE))
						.get(1).findElement(xpath("input[3]"));
				option.click();
				resultRemoveForum = driver
						.findElement(id(RESULT_REMOVE_FORUM));
				message = resultRemoveForum.getText();
			} else {
				WebElement option = driver.findElements(tagName(SELECT_TYPE))
						.get(1).findElement(xpath("input[4]"));
				option.click();
			}
			return message;

		}
		return message;
	}

}
