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
package it.vige.rubia.resttest.usermodule.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.UserBean;
import it.vige.rubia.resttest.RestCaller;

public class UserTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/user/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(UserTest.class);

	private final static String DEMO_USER = "demo";
	private final static String JOHN_USER = "john";

	@Test
	public void find() {
		log.info("find user start");
		Response response = get(url + "findUserByUserName/" + DEMO_USER, authorization);
		UserBean user = response.readEntity(UserBean.class);
		assertEquals(DEMO_USER, user.getId());
		assertEquals(DEMO_USER, user.getUserName());
		response = get(url + "findUserByUserName/" + JOHN_USER, authorization);
		user = response.readEntity(UserBean.class);
		assertEquals(JOHN_USER, user.getId());
		assertEquals(JOHN_USER, user.getUserName());
		response = get(url + "findUserById/" + user.getId(), authorization);
		user = response.readEntity(UserBean.class);
		assertEquals(JOHN_USER, user.getId());
		assertEquals(JOHN_USER, user.getUserName());
		response = get(url + "isGuest", authorization);
		boolean isGuest = response.readEntity(Boolean.class);
		assertEquals(false, isGuest);
		response.close();
	}
}
