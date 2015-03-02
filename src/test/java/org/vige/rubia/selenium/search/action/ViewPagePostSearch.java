package org.vige.rubia.selenium.search.action;

import static java.util.ResourceBundle.getBundle;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;
import static org.vige.rubia.selenium.forum.action.VerifyAttachment.getAttachmentsOfCurrentPostInPageNoParent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.vige.rubia.model.Attachment;
import org.vige.rubia.model.Message;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Poster;
import org.vige.rubia.search.SearchCriteria;

public class ViewPagePostSearch extends ViewPageSearch {

	public static final String POST_VIEW = "rf-dt-b";

	public static final String POST_POSTER = "tr/td/a";

	public static final String POST_SUBJECT = "tr/td[2]/div/ul/li[2]";

	public static final String POST_TEXT = "tr[2]/td[2]/p";

	public static final String POST_CREATED_DATE = "tr/td[2]/div/ul/li";

	public static DateFormat dateFormat = new SimpleDateFormat(
			"EEE MMM d, yyyy HH:mm aaa");

	public static List<Post> searchPost(WebDriver driver,
			SearchCriteria arguments) {
		addKeys(driver, arguments);
		return getPosts(driver, arguments);
	}

	private static Post getPost(WebDriver driver, WebElement element) {
		Post post = new Post();
		post.setPoster(new Poster(element.findElement(xpath(POST_POSTER))
				.getText()));
		String createdDate = element
				.findElement(xpath(POST_CREATED_DATE))
				.getText()
				.replace(getBundle("ResourceJSF").getString("Posted") + ": ",
						"");
		try {
			Date date = dateFormat.parse(createdDate);
			post.setCreateDate(date);
		} catch (ParseException e) {
		}
		Message message = new Message();
		message.setSubject(element
				.findElement(xpath(POST_SUBJECT))
				.getText()
				.replace(
						getBundle("ResourceJSF").getString("Post_subject")
								+ ": ", ""));
		message.setText(element.findElement(xpath(POST_TEXT)).getText());
		post.setMessage(message);
		List<Attachment> attachments = getAttachmentsOfCurrentPostInPageNoParent(
				driver, post);
		post.setAttachments(attachments);
		return post;
	}

	public static List<Post> getPosts(WebDriver driver, SearchCriteria arguments) {
		WebElement button = driver.findElements(className(BUTTON)).get(0);
		button.click();
		WebElement messageResult = getMessageResult(driver);
		if (messageResult != null
				&& (messageResult.getText().contains("Value is required") || messageResult
						.getText().equals(
								getBundle("ResourceJSF").getString(
										"Search_posts_not_found"))))
			return null;
		else {
			List<Post> posts = new ArrayList<Post>();
			List<WebElement> elements = driver
					.findElements(className(POST_VIEW));
			for (WebElement element : elements)
				posts.add(getPost(driver, element));
			return posts;
		}
	}

}
