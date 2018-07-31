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
package it.vige.rubia.resttest.forummodule.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import it.vige.rubia.resttest.RestCaller;

public class GuestUserTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	@Test
	public void setAddressName() {
		Response response = get(url + "setFromAddress/bubbusettete2", authorization);
		response.close();
		response = get(url + "getFromAddress", authorization);
		String value = response.readEntity(String.class);
		response.close();
		assertEquals("bubbusettete2", value, "From address name sent");
		response = get(url + "setFromAddress/portal@example.com", authorization);
		response.close();
		response = get(url + "getFromAddress", authorization);
		value = response.readEntity(String.class);
		response.close();
		assertEquals("portal@example.com", value, "From address name sent");
	}
}
