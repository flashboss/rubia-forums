package it.vige.rubia.selenium.forum.action;

import static java.lang.Thread.sleep;

public class Create {
	protected static final String BODY_INPUT_TEXT = "//iframe[contains(@title,'post:message:inp')]";

	protected static void sleepThread() {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
