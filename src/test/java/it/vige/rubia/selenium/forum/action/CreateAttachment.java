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

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateAttachment {

	public static final String FILE_CHOOSE_BUTTON = "rf-fu-inp";
	public static final String FILE_COMMENT_INPUT_TEXT = "Posttextarea";
	public static final String RESULT_ATTACHMENT_LIST = "rf-fu-itm";
	public static final String UPDATE_BUTTON = "post:Submit";

	public static String[] addAttachmentsAndSave(WebDriver driver, Post post) {
		String[] results = addAttachments(driver, post);
		WebElement updateButton = driver.findElement(id(UPDATE_BUTTON));
		updateButton.click();
		return results;
	}
	
	public static String[] addAttachments(WebDriver driver, Post post) {
		Collection<Attachment> attachments = post.getAttachments();
		if (attachments != null) {
			int i = 0;
			int oldCommentsCount = driver.findElements(
					className(FILE_COMMENT_INPUT_TEXT)).size();
			for (Attachment attachment : attachments) {

				File file;
				try {
					String oldName = attachment.getName();
					file = File.createTempFile(oldName, ".txt");
					OutputStream out = new FileOutputStream(file);
					attachment.setContent(oldName.getBytes());
					out.write(attachment.getContent());
					out.close();
					attachment.setName(file.getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String comment = attachment.getComment();
				WebElement attachmentInput = driver
						.findElement(className(FILE_CHOOSE_BUTTON));
				attachmentInput.sendKeys(attachment.getName());
				WebElement commentInput = addComment(driver, oldCommentsCount
						+ i);
				i++;
				commentInput.sendKeys(comment);
			}
		}
		List<WebElement> attachmentResultList = driver
				.findElements(className(RESULT_ATTACHMENT_LIST));
		String[] result = new String[attachmentResultList.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = attachmentResultList.get(i).getText();
		return result;
	}

	public static WebElement addComment(WebDriver driver, int index) {
		WebElement commentInput = null;
		try {
			commentInput = driver.findElements(
					className(FILE_COMMENT_INPUT_TEXT)).get(index);
		} catch (IndexOutOfBoundsException ex) {
		}
		if (commentInput == null)
			return addComment(driver, index);
		else
			return commentInput;
	}
}