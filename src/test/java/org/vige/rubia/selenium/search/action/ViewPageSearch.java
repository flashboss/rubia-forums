/*
 * Vige, Home of Professional Open Source
 * Copyright 2010, Vige, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vige.rubia.selenium.search.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.search.DisplayAs.POSTS;
import static org.vige.rubia.search.Searching.TITLE_MSG;
import static org.vige.rubia.search.SortOrder.ASC;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

	protected static WebElement getMessageResult(WebDriver driver) {
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
		return messageResult;
	}

	public static void reset(WebDriver driver, SearchCriteria arguments) {
		addKeys(driver, arguments);
		WebElement button = driver.findElements(className(BUTTON)).get(1);
		button.click();
	}

	protected static void addKeys(WebDriver driver, SearchCriteria arguments) {
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
			if (arguments.getDisplayAs().equals(POSTS.name()))
				element = table
						.findElement(xpath("tbody/tr[10]/td[2]/table/tbody/tr[1]/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[10]/td[2]/table/tbody/tr[2]/td/input"));
			element.click();
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
			if (arguments.getSearching().equals(TITLE_MSG.name()))
				element = table
						.findElement(xpath("tbody/tr[8]/td[2]/table/tbody/tr[1]/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[8]/td[2]/table/tbody/tr[2]/td/input"));
			element.click();
		}
		if (arguments.getSortBy() != null) {
			element = table.findElement(xpath("tbody/tr[9]/td[2]/select"));
			element.sendKeys(arguments.getSortBy());
		}
		if (arguments.getSortOrder() != null) {
			if (arguments.getSortOrder().equals(ASC.name()))
				element = table
						.findElement(xpath("tbody/tr[9]/td[2]/table/tbody/tr[1]/td/input"));
			else
				element = table
						.findElement(xpath("tbody/tr[9]/td[2]/table/tbody/tr[2]/td/input"));
			element.click();
		}
		if (arguments.getTimePeriod() != null) {
			element = table.findElement(xpath("tbody/tr[7]/td[2]/select"));
			element.sendKeys(arguments.getTimePeriod());
		}
	}

}
