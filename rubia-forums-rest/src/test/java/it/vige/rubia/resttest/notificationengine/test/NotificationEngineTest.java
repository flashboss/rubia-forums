/******************************************************************************
 * Vige, Home of Professional Open Source Copyright 2010, Vige, and           *
 * individual contributors by the @authors tag. See the copyright.txt in the  *
 * distribution for a full listing of individual contributors.                *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may    *
 * not use this file except in compliance with the License. You may obtain    *
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0        *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package it.vige.rubia.resttest.notificationengine.test;

import static it.vige.rubia.util.NotificationEngine.MODE_POST;
import static it.vige.rubia.util.NotificationEngine.MODE_REPLY;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.resttest.RestCaller;

public class NotificationEngineTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/notification/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	@BeforeAll
	public static void setUp() {
		get(url + "setFrom/auser", authorization);
	}

	@Test
	public void uiRootAccess() {
		get(url + "schedule/1/" + MODE_POST + "/VIEW_URL/REPLY_URL", authorization);
		get(url + "scheduleForNotification/1/" + MODE_REPLY, authorization);
	}

	@AfterAll
	public static void stop() {
		get(url + "stop", authorization);
	}
}
