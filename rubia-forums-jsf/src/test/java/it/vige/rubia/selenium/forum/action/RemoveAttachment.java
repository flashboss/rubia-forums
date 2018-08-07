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
import static it.vige.rubia.selenium.forum.action.UpdatePost.UPDATE_POST_BUTTON;
import static it.vige.rubia.selenium.forum.action.VerifyTopic.goTo;
import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import java.util.Collection;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.PostBean;

public class RemoveAttachment {

	public static final String HOME_LINK = getBundle("ResourceJSF").getString("Home");
	public static final String ATTACHMENT_DELETE_BUTTON = "buttonMed";
	public static final String UPDATE_BUTTON = "post:Submit";

	public static String removeAttachments(WebDriver driver, PostBean post) {
		goTo(driver, post.getTopic());
		WebElement updatePostButton = driver
				.findElement(xpath("//tbody[contains(.,'" + post.getMessage().getText() + "')]"))
				.findElement(id(UPDATE_POST_BUTTON)).findElement(xpath("ul/a[1]"));
		updatePostButton.click();
		Collection<AttachmentBean> attachments = post.getAttachments();
		String message = "";
		for (AttachmentBean attachment : attachments) {
			message = "";
			String comment = attachment.getComment();
			WebElement fileCommentInputText = driver.findElement(xpath("//textarea[text()='" + comment + "']"));
			WebElement attachmentDeleteButton = fileCommentInputText.findElement(xpath("../../.."))
					.findElement(className(ATTACHMENT_DELETE_BUTTON));
			attachmentDeleteButton.click();
			try {
				driver.findElement(xpath("//textarea[text()='" + comment + "']"));
			} catch (NoSuchElementException ex) {
				message = OK;
			}
		}
		WebElement updateButton = driver.findElement(id(UPDATE_BUTTON));
		updateButton.click();
		return message;
	}
}