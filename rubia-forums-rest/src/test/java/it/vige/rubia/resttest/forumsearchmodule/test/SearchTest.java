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
package it.vige.rubia.resttest.forumsearchmodule.test;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.resttest.RestCaller;

public class SearchTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/search/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	@BeforeAll
	public static void setUp() {
	}

	@Test
	public void uiRootAccess() {
		Response response = get(url + "hasAccess", authorization);
		response.readEntity(Boolean.class);
		response.close();
	}

	@AfterAll
	public static void stop() {
	}
}
