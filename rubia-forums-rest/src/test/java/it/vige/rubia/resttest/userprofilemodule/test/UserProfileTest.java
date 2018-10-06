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
package it.vige.rubia.resttest.userprofilemodule.test;

import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.UserPropertyBean;
import it.vige.rubia.resttest.RestCaller;

public class UserProfileTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/profile/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(UserProfileTest.class);

	@Test
	public void findAndUpdate() {
		log.debug("started user profile find and update");
		UserPropertyBean userProperty = new UserPropertyBean();
		Response response = post(url + "getProperty", authorization, userProperty);
		assertNull(response.getEntity());
		response = post(url + "getProperty", authorization, userProperty);
		assertNull(response.getEntity());
		response = get(url + "getPropertyFromId/demo/test", authorization);
		assertNull(response.getEntity());
		response = post(url + "setProperty", authorization, userProperty);
		assertNull(response.getEntity());
		response = post(url + "getProperties", authorization, userProperty);
		assertNotNull(response.getEntity());
		response = get(url + "getProfileInfo", authorization);
		assertNull(response.getEntity());
		response.close();
	}
}
