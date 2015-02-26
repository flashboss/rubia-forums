package org.vige.rubia.selenium.search.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.search.DisplayAs.POSTS;
import static org.vige.rubia.search.Searching.TITLE_MSG;
import static org.vige.rubia.search.SortOrder.ASC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Forum;
import org.vige.rubia.model.Topic;
import org.vige.rubia.search.SearchCriteria;

public class ViewPageSearch {

	public static final String SEARCH_LINK = getBundle("ResourceJSF")
			.getString("Search");

	public static final String SEARCH_FIELD = "forumtablestyle";

	public static final String BUTTON = "buttonMed";

	public static final String MESSAGE_RESULT = "javax_faces_developmentstage_messages";

	public static final String NOT_FOUND_RESULTS = "forumtablestyle";

	public static void goTo(WebDriver driver) {
		WebElement searchLink = driver.findElement(linkText(SEARCH_LINK));
		searchLink.click();
	}

	public static List<Forum> getForums(WebDriver driver,
			SearchCriteria arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
		WebElement messageResult = null;
		try {
			messageResult = driver.findElement(id(MESSAGE_RESULT));
		} catch (NoSuchElementException ex) {
		}
		if (messageResult == null) {
			try {
				messageResult = driver
						.findElement(className(NOT_FOUND_RESULTS)).findElement(
								xpath("tbody/tr/td/table/tbody/tr[2]/td"));
			} catch (NoSuchElementException ex) {
			}
		}
		if (messageResult != null
				&& (messageResult.getText().contains("Value is required"))
				|| messageResult.getText().equals(
						getBundle("ResourceJSF").getString(
								"Search_posts_not_found")))
			return null;
		else
			return new ArrayList<Forum>();
	}

	public static List<Topic> getTopics(WebDriver driver,
			SearchCriteria arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
		WebElement messageResult = null;
		try {
			messageResult = driver.findElement(id(MESSAGE_RESULT));
		} catch (NoSuchElementException ex) {
		}
		if (messageResult == null) {
			try {
				messageResult = driver
						.findElement(className(NOT_FOUND_RESULTS)).findElement(
								xpath("tbody/tr/td/table/tbody/tr[2]/td"));
			} catch (NoSuchElementException ex) {
			}
		}
		if (messageResult != null
				&& (messageResult.getText().contains("Value is required"))
				|| messageResult.getText().equals(
						getBundle("ResourceJSF").getString(
								"Search_posts_not_found")))
			return null;
		else
			return new ArrayList<Topic>();
	}

	public static Map<String, String> reset(WebDriver driver,
			SearchCriteria arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(1);
		button.click();
		return null;
	}

	private static void addKeys(WebDriver driver, SearchCriteria arguments) {
		WebElement table = driver.findElement(className(SEARCH_FIELD));
		WebElement element = null;
		if (arguments.getAuthor() != null) {
			element = table.findElement(xpath("tbody/tr[3]/td[2]/input"));
			element.sendKeys(arguments.getAuthor());
		}
		if (arguments.getCategory() != null) {
			element = table.findElement(xpath("tbody/tr[5]/td[2]/select"));
			element.sendKeys(arguments.getCategory());
		}
		if (arguments.getDisplayAs() != null) {
			if (arguments.getDisplayAs().equals(POSTS))
				element = table
						.findElement(xpath("tbody/tr[10]/td[2]/table/tbody/tr[1]/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[10]/td[2]/table/tbody/tr[2]/td/input"));
			element.sendKeys(arguments.getDisplayAs());
		}
		if (arguments.getForum() != null) {
			element = table.findElement(xpath("tbody/tr[6]/td[2]/select"));
			element.sendKeys(arguments.getForum());
		}
		if (arguments.getKeywords() != null) {
			element = table.findElement(xpath("tbody/tr[2]/td[2]/input"));
			element.sendKeys(arguments.getKeywords());
		}
		if (arguments.getSearching() != null) {
			if (arguments.getSearching().equals(TITLE_MSG))
				element = table
						.findElement(xpath("tbody/tr[8]/td[2]/table/tbody/tr[1]/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[8]/td[2]/table/tbody/tr[2]/td/input"));
			element.sendKeys(arguments.getSearching());
		}
		if (arguments.getSortBy() != null) {
			element = table.findElement(xpath("tbody/tr[9]/td[2]/select"));
			element.sendKeys(arguments.getSortBy());
		}
		if (arguments.getSortOrder() != null) {
			if (arguments.getSortOrder().equals(ASC))
				element = table
						.findElement(xpath("tbody/tr[9]/td[2]/table/tbody/tr[1]/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[9]/td[2]/table/tbody/tr[2]/td/input"));
			element.sendKeys(arguments.getSortOrder());
		}
		if (arguments.getTimePeriod() != null) {
			element = table.findElement(xpath("tbody/tr[7]/td[2]/select"));
			element.sendKeys(arguments.getTimePeriod());
		}
	}

}
