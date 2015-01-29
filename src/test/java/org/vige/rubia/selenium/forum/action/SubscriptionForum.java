package org.vige.rubia.selenium.forum.action;

import org.openqa.selenium.WebDriver;
import org.vige.rubia.model.Forum;
import org.vige.rubia.properties.NotificationType;

public class SubscriptionForum {

	public static String registerForum(WebDriver driver, Forum forum,
			NotificationType notificationType) {
		return forum.getName();
	}

	public static String unregisterForum(WebDriver driver, Forum forum) {
		return "OK";
	}
}
