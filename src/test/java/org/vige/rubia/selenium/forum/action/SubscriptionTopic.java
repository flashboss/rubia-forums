package org.vige.rubia.selenium.forum.action;

import org.openqa.selenium.WebDriver;
import org.vige.rubia.model.Topic;
import org.vige.rubia.properties.NotificationType;

public class SubscriptionTopic {

	public static String registerTopic(WebDriver driver, Topic topic,
			NotificationType notificationType) {
		return topic.getSubject();
	}

	public static String unregisterTopic(WebDriver driver, Topic topic) {
		return "OK";
	}

}
