package org.vige.rubia.selenium.forum.model;

import org.openqa.selenium.By;
import static org.openqa.selenium.By.*;

public enum Links {
	CATEGORY_TEMPLATE_LINK(xpath("//li[@class='first']/ul/li/a")), FORUM_TEMPLATE_LINK(
			xpath("//li[@class='first']/ul/li/ul/li/a")), TOPIC_TEMPLATE_LINK(
			xpath("//li[@class='first']/ul/li/ul/li/ul/li/a")), POST_TEMPLATE_LINK(
			xpath("//li[@class='first']/ul/li/ul/li/ul/li/a"));

	private By value;

	private Links(By value) {
		this.setValue(value);
	}

	public By getValue() {
		return value;
	}

	public void setValue(By value) {
		this.value = value;
	}
}
