package it.vige.rubia.selenium.forum.action;

import static it.vige.rubia.selenium.Constants.HOME_URL;
import static java.lang.Thread.sleep;

import org.openqa.selenium.WebDriver;

public class Write {
	protected static final String BODY_INPUT_TEXT = "//iframe[contains(@title,'post:message:inp')]";

	protected static void sleepThread() {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void returnToHome(WebDriver driver) {
		driver.get(HOME_URL);
		sleepThread();
	}

}
