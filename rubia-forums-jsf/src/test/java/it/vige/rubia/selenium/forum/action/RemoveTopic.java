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

import static it.vige.rubia.selenium.Constants.OK;
import static it.vige.rubia.selenium.forum.action.RemovePost.removePost;
import static java.util.ResourceBundle.getBundle;

import org.openqa.selenium.WebDriver;

import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;

public class RemoveTopic {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String REMOVE_TOPIC_BUTTON = "miviewtopicbody6";
	public static final String CONFIRM_REMOVE_TOPIC_BUTTON = "//input[@type='submit']";

	public static String removeTopic(WebDriver driver, TopicBean topic) {
		String result = "";
		for (PostBean post : topic.getPosts()) {
			post.setTopic(topic);
			result = removePost(driver, post);
			if (!result.equals(OK))
				return "";
		}
		return result;
	}
}
