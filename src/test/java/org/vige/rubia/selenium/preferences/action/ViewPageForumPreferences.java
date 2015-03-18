package org.vige.rubia.selenium.preferences.action;

import static org.openqa.selenium.By.className;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.ui.action.PreferenceController;

public class ViewPageForumPreferences extends ViewPagePreferences {

	public static final String FORUM_VIEWS = "forumtablestyle";

	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public static void submit(WebDriver driver, PreferenceController arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
	}

	public static void reset(WebDriver driver, PreferenceController arguments) {
		addKeys(driver, arguments);
		List<WebElement> buttons = driver.findElements(className(BUTTON));
		WebElement reset = buttons.get(1);
		reset.click();
		WebElement submit = buttons.get(0);
		submit.click();
	}

}
