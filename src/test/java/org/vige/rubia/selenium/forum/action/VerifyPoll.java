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
package org.vige.rubia.selenium.forum.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.By.id;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Poll;
import org.vige.rubia.model.PollOption;

public class VerifyPoll {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString(
			"Home");
	public static final String FORUM_TABLE = "forumtablestyle";
	public static final String FORUM_SUBJECT = "tbody/tr/td[not(@class = 'forumcategory')]/h3/a";
	public static final String FORUM_LINK = "miviewtopicbody3";
	public static final String SUBJECT_LINK = "//table[contains(@class,'forumtablestyle')]/tbody/tr/td[2]/h3/a";
	public static final String FORUM_POLL_TABLE = "forumPollTable";
	public static final String QUESTION_OUTPUT_TEXT = "questionCell";
	public static final String ANSWER_OUTPUT_TEXT = "answerCell";
	public static final String VOTES_LINK = getBundle("ResourceJSF").getString(
			"View_ballot");
	public static final String RESULT_VOTES_LINK = getBundle("ResourceJSF")
			.getString("View_results");
	public static final String TOTAL_VOTES_LINK = "totalCell";
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"E MMM d, yyyy H:mm a");

	public static List<Poll> getPollsOfTopics(WebDriver driver,
			String... topicNames) {
		List<Poll> polls = new ArrayList<Poll>();
		WebElement home = driver.findElement(linkText(HOME_LINK));
		home.click();
		WebElement tableComponent = driver.findElement(className(FORUM_TABLE));
		List<WebElement> forumSubjectComponents = tableComponent
				.findElements(xpath(FORUM_SUBJECT));
		int forumComponentSize = forumSubjectComponents.size();
		for (int j = 0; j < forumComponentSize; j++) {
			home = driver.findElement(linkText(HOME_LINK));
			home.click();
			tableComponent = driver.findElement(className(FORUM_TABLE));
			forumSubjectComponents = tableComponent
					.findElements(xpath(FORUM_SUBJECT));
			WebElement forumEl = forumSubjectComponents.get(j);
			forumEl.click();
			List<WebElement> subjectComponents = driver
					.findElements(xpath(SUBJECT_LINK));
			int subjectComponentsSize = subjectComponents.size();
			for (int i = 0; i < subjectComponentsSize; i++) {
				subjectComponents = driver.findElements(xpath(SUBJECT_LINK));
				if (Arrays.asList(topicNames).contains(
						subjectComponents.get(i).getText())) {
					subjectComponents = driver
							.findElements(xpath(SUBJECT_LINK));
					WebElement subjectComponent = subjectComponents.get(i);
					subjectComponent.click();
					addPollToCurrentTopic(driver, polls);
					String forumLinkText = driver.findElement(id(FORUM_LINK))
							.getText();
					driver.findElement(linkText(forumLinkText)).click();
				}
			}
		}
		return polls;
	}

	public static void addPollToCurrentTopic(WebDriver driver, List<Poll> polls) {
		WebElement question = null;
		try {
			question = driver.findElement(className(QUESTION_OUTPUT_TEXT));
		} catch (NoSuchElementException ex) {

		}
		if (question != null) {
			Poll poll = new Poll();
			poll.setTitle(question.getText());
			List<WebElement> pollComponents = driver
					.findElements(className(ANSWER_OUTPUT_TEXT));
			List<PollOption> pollOptions = new ArrayList<PollOption>();
			for (WebElement pollComponent : pollComponents) {
				PollOption pollOption = new PollOption();
				pollOption.setQuestion(pollComponent.getText());
				pollOption.setPoll(poll);
				pollOptions.add(pollOption);
			}

			WebElement votesResultComponent = driver
					.findElement(linkText(RESULT_VOTES_LINK));
			votesResultComponent.click();
			List<WebElement> pollComponentsTr = driver.findElement(
					className(FORUM_POLL_TABLE))
					.findElements(xpath("tbody/tr"));
			pollComponents.clear();
			for (int i2 = 0; i2 < pollComponentsTr.size(); i2++) {
				if (i2 != 0 && i2 < pollComponentsTr.size() - 2)
					pollComponents.add(pollComponentsTr.get(i2).findElement(
							xpath("td")));
			}
			for (int i3 = 0; i3 < pollOptions.size(); i3++) {
				WebElement pollComponent = pollComponents.get(i3);
				PollOption pollOption = pollOptions.get(i3);
				String numberOfVotes = driver
						.findElement(className(FORUM_POLL_TABLE))
						.findElement(
								xpath("tbody/tr[td/text()='"
										+ pollComponent.getText() + "']/td[3]"))
						.getText();
				String pollOptionPosition = driver
						.findElement(className(FORUM_POLL_TABLE))
						.findElement(
								xpath("tbody/tr[td/text()='"
										+ pollComponent.getText() + "']/td[4]"))
						.getText();
				pollOption.setVotes(new Integer(numberOfVotes.substring(0,
						numberOfVotes.length() - 1)));
				pollOption.setPollOptionPosition(new Integer(pollOptionPosition
						.substring(1, pollOptionPosition.length() - 1).trim()));
			}
			WebElement votesComponent = driver
					.findElement(linkText(VOTES_LINK));
			votesComponent.click();
			poll.setOptions(pollOptions);
			polls.add(poll);
		}

	}
}
