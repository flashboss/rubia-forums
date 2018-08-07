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
package it.vige.rubia.selenium.forum.action;

import static it.vige.rubia.selenium.forum.action.VerifyPoll.getPollOfCurrentTopic;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;

public class UpdatePoll extends Write {
	public static final String UPDATE_TOPIC_BUTTON = "miviewtopicbody6";
	public static final String OPTIONS_TO_VOTE_LIST = "radioCell";
	public static final String VOTE_BUTTON = "buttonMed";
	public static final String QUESTION_INPUT_TEXT = "post:question";
	public static final String OPTION_INPUT_TEXT = "post:option_";
	public static final String OPTION_ADD_INPUT_TEXT = "post:newOption";
	public static final String UPDATE_OPTION_BUTTON = "post:UpdateOption_";
	public static final String DAYS_INPUT_TEXT = "post:pollDuration";
	public static final String SUBMIT_TOPIC_BUTTON = "post:Submit";
	public static final String ADD_OPTION_BUTTON = getBundle("ResourceJSF").getString("Add_option");
	public static final String DELETE_OPTION_BUTTON = getBundle("ResourceJSF").getString("Delete");

	public static PollBean addOptions(WebDriver driver, PollBean poll) {
		WebElement updateTopicButton = driver.findElements(xpath("//tbody")).get(2).findElement(id(UPDATE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[1]"));
		updateTopicButton.click();
		List<PollOptionBean> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver.findElement(id(OPTION_ADD_INPUT_TEXT));
				sleepThread();
				optionInput.sendKeys(options.get(i).getQuestion());
				sleepThread();
				String translatedOption = ADD_OPTION_BUTTON;
				if (translatedOption.contains("\'"))
					translatedOption = translatedOption.substring(0, translatedOption.indexOf("\'"));
				optionButton = driver.findElement(xpath("//input[starts-with(@value,'" + translatedOption + "')]"));
				optionButton.click();
				sleepThread();
			}
		WebElement submitTopicButton = driver.findElement(id(SUBMIT_TOPIC_BUTTON));
		submitTopicButton.click();
		PollBean updatedPoll = getPollOfCurrentTopic(driver);
		return updatedPoll;

	}

	public static PollBean vote(WebDriver driver, PollBean poll, int indexVote) {
		List<WebElement> optionsToVoteList = driver.findElements(className(OPTIONS_TO_VOTE_LIST));
		optionsToVoteList.get(indexVote).findElement(xpath("input")).click();
		WebElement voteButton = driver.findElement(className(VOTE_BUTTON));
		voteButton.click();
		PollBean updatedPoll = getPollOfCurrentTopic(driver);
		return updatedPoll;

	}

	public static PollBean deleteOptions(WebDriver driver, PollBean poll) {
		WebElement updateTopicButton = driver.findElements(xpath("//tbody")).get(2).findElement(id(UPDATE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[1]"));
		updateTopicButton.click();
		List<PollOptionBean> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionButton = null;
				optionButton = driver.findElement(xpath("//input[@value='" + options.get(i).getQuestion()
						+ "']/../input[@value='" + DELETE_OPTION_BUTTON + "']"));
				optionButton.click();
			}
		WebElement submitTopicButton = driver.findElement(id(SUBMIT_TOPIC_BUTTON));
		submitTopicButton.click();
		PollBean updatedPoll = getPollOfCurrentTopic(driver);
		return updatedPoll;

	}

	public static PollBean updateOptions(WebDriver driver, PollBean poll) {
		WebElement updateTopicButton = driver.findElements(xpath("//tbody")).get(2).findElement(id(UPDATE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[1]"));
		updateTopicButton.click();
		List<PollOptionBean> options = poll.getOptions();
		if (options != null)
			for (int i = 0; i < options.size(); i++) {
				WebElement optionInput = null;
				WebElement optionButton = null;
				optionInput = driver.findElement(id(OPTION_INPUT_TEXT + (i + 1)));
				sleepThread();
				optionInput.clear();
				sleepThread();
				optionInput.sendKeys(options.get(i).getQuestion());
				sleepThread();
				optionButton = driver.findElement(id(UPDATE_OPTION_BUTTON + (i + 1)));
				optionButton.click();
			}
		WebElement submitTopicButton = driver.findElement(id(SUBMIT_TOPIC_BUTTON));
		submitTopicButton.click();
		sleepThread();
		PollBean updatedPoll = getPollOfCurrentTopic(driver);
		return updatedPoll;

	}

	public static PollBean updatePoll(WebDriver driver, PollBean poll) {
		WebElement updateTopicButton = driver.findElements(xpath("//tbody")).get(2).findElement(id(UPDATE_TOPIC_BUTTON))
				.findElement(xpath("ul/a[1]"));
		updateTopicButton.click();
		WebElement questionInput = null;
		questionInput = driver.findElement(id(QUESTION_INPUT_TEXT));
		sleepThread();
		questionInput.clear();
		sleepThread();
		questionInput.sendKeys(poll.getTitle());
		sleepThread();
		WebElement daysInput = driver.findElement(id(DAYS_INPUT_TEXT));
		daysInput.clear();
		daysInput.sendKeys(poll.getLength() + "");
		WebElement submitTopicButton = driver.findElement(id(SUBMIT_TOPIC_BUTTON));
		submitTopicButton.click();
		PollBean updatedPoll = getPollOfCurrentTopic(driver);
		return updatedPoll;

	}
}