package org.vige.rubia.selenium.preferences.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.ui.action.PreferenceController;

public class ViewPagePreferences {

	public static final String PREFERENCES_LINK = getBundle("ResourceJSF")
			.getString("Preferences");

	public static final String PREFERENCES_FIELD = "forumtablestyle";

	public static final String BUTTON = "buttonMed";

	public static final String MESSAGE_RESULT = "javax_faces_developmentstage_messages";

	public static final String NOT_FOUND_RESULTS = "forumtablestyle";

	public static void goTo(WebDriver driver) {
		WebElement preferencesLink = driver
				.findElement(linkText(PREFERENCES_LINK));
		preferencesLink.click();
	}

	public static void reset(WebDriver driver, PreferenceController arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(1);
		button.click();
	}

	protected static void addKeys(WebDriver driver,
			PreferenceController arguments) {
		WebElement table = driver.findElement(className(PREFERENCES_FIELD));
		WebElement element = null;
		if (arguments.getPostOrder() != null) {
			if (arguments.getPostOrder().equals("ascending"))
				element = table
						.findElement(xpath("tbody/tr[4]/td[2]/table/tbody/tr/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[4]/td[2]/table/tbody/tr/td[2]/input"));
			element.click();
		}
		if (arguments.getPostsPerTopic() != 0) {
			switch (arguments.getPostsPerTopic()) {
			case 5:
				element = table
						.findElement(xpath("tbody/tr[6]/td[2]/table/tbody/tr/td/input"));
				break;
			case 10:
				element = table
						.findElement(xpath("tbody/tr[6]/td[2]/table/tbody/tr/td[2]/input"));
				break;
			case 15:
				element = table
						.findElement(xpath("tbody/tr[6]/td[2]/table/tbody/tr/td[3]/input"));
				break;
			case 20:
				element = table
						.findElement(xpath("tbody/tr[6]/td[2]/table/tbody/tr/td[4]/input"));
				break;
			case 25:
				element = table
						.findElement(xpath("tbody/tr[6]/td[2]/table/tbody/tr/td[5]/input"));
				break;
			case 30:
				element = table
						.findElement(xpath("tbody/tr[6]/td[2]/table/tbody/tr/td[6]/input"));
				break;
			}
			element.click();
		}
		if (arguments.getDateFormat() != null) {
			element = table.findElement(xpath("tbody/tr[7]/td[2]/input"));
			element.clear();
			element.sendKeys(arguments.getDateFormat());
		}
		if (arguments.isAlwaysAddSignature()) {
			element = table
					.findElement(xpath("tbody/tr[9]/td[2]/table/tbody/tr/td/input"));
			element.click();
		} else {
			element = table
					.findElement(xpath("tbody/tr[9]/td[2]/table/tbody/tr/td[2]/input"));
			element.click();
		}
		if (arguments.isAlwaysAllowHtml()) {
			element = table
					.findElement(xpath("tbody/tr[3]/td[2]/table/tbody/tr/td/input"));
			element.click();
		} else {
			element = table
					.findElement(xpath("tbody/tr[3]/td[2]/table/tbody/tr/td[2]/input"));
			element.click();
		}
		if (arguments.isNotifyOnReply()) {
			element = table
					.findElement(xpath("tbody/tr[2]/td[2]/table/tbody/tr/td/input"));
			element.click();
		} else {
			element = table
					.findElement(xpath("tbody/tr[2]/td[2]/table/tbody/tr/td[2]/input"));
			element.click();
		}
		if (arguments.getSignature() != null) {
			element = table.findElement(xpath("tbody/tr[10]/td[2]/textarea"));
			element.clear();
			element.sendKeys(arguments.getSignature());
		}
		if (arguments.getSummaryMode() != null) {
			element = table.findElement(xpath("tbody/tr[12]/td[2]/select"));
			element.sendKeys(arguments.getSummaryMode() + "");
		}
		if (arguments.getSummaryTopicDays() != 0) {
			element = table.findElement(xpath("tbody/tr[13]/td[2]/input"));
			element.clear();
			element.sendKeys(arguments.getSummaryTopicDays() + "");
		}
		if (arguments.getSummaryTopicLimit() != 0) {
			element = table.findElement(xpath("tbody/tr[14]/td[2]/input"));
			element.clear();
			element.sendKeys(arguments.getSummaryTopicLimit() + "");
		}
		if (arguments.getSummaryTopicReplies() != 0) {
			element = table.findElement(xpath("tbody/tr[15]/td[2]/input"));
			element.clear();
			element.sendKeys(arguments.getSummaryTopicReplies() + "");
		}
		if (arguments.getTopicsPerForum() != 0) {
			switch (arguments.getTopicsPerForum()) {
			case 5:
				element = table
						.findElement(xpath("tbody/tr[5]/td[2]/table/tbody/tr/td/input"));
				break;
			case 10:
				element = table
						.findElement(xpath("tbody/tr[5]/td[2]/table/tbody/tr/td[2]/input"));
				break;
			case 15:
				element = table
						.findElement(xpath("tbody/tr[5]/td[2]/table/tbody/tr/td[3]/input"));
				break;
			case 20:
				element = table
						.findElement(xpath("tbody/tr[5]/td[2]/table/tbody/tr/td[4]/input"));
				break;
			case 25:
				element = table
						.findElement(xpath("tbody/tr[5]/td[2]/table/tbody/tr/td[5]/input"));
				break;
			case 30:
				element = table
						.findElement(xpath("tbody/tr[5]/td[2]/table/tbody/tr/td[6]/input"));
				break;
			}
			element.click();
		}
	}

}
