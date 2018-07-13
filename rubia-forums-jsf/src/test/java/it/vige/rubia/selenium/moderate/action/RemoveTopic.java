package it.vige.rubia.selenium.moderate.action;

import static it.vige.rubia.properties.OperationType.CONFIRM;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;
import it.vige.rubia.model.Topic;
import it.vige.rubia.properties.OperationType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RemoveTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String REMOVE_TOPIC_BUTTON = "miviewtopicbody16";
	public static final String CONFIRM_REMOVE_TOPIC_BUTTON = "//input[@type='submit']";
	public static final String SELECT_TYPE = "form";
	public static final String RESULT_REMOVE_TOPIC = "forumtitletext";

	public static String removeTopic(WebDriver driver, OperationType removeType, Topic topic) {
		String result = "";
		goTo(driver, topic);
		WebElement removeButton = driver.findElement(id(REMOVE_TOPIC_BUTTON)).findElement(xpath("form/ul/li[2]/a"));
		removeButton.click();
		if (removeType == CONFIRM) {
			WebElement option = driver.findElements(tagName(SELECT_TYPE)).get(1).findElement(xpath("input[4]"));
			option.click();
			WebElement resultRemoveTopic = driver.findElement(className(RESULT_REMOVE_TOPIC))
					.findElement(xpath("//table/tbody/tr/td"));
			result = resultRemoveTopic.getText();
		} else {
			WebElement option = driver.findElements(tagName(SELECT_TYPE)).get(1).findElement(xpath("input[5]"));
			option.click();
		}
		return result;
	}

}
