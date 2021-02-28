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
package it.vige.rubia.resttest.aclprovider.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.picketlink.idm.model.basic.User;

import it.vige.rubia.auth.UIContext;
import it.vige.rubia.model.Forum;
import it.vige.rubia.resttest.RestCaller;
import it.vige.rubia.wildfly.auth.JBossUser;

public class UIAccessTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/acl/";

	@Test
	public void uiRootAccess() {
		UIContext uiContext = new UIContext();
		uiContext.setFragment("acl://accessAdminTool");
		Response response = post(url + "hasAccess", "Basic cm9vdDpndG4=", uiContext);
		boolean value = response.readEntity(Boolean.class);
		response.close();
		assertEquals(true, value, "Has ui root access");
	}

	@Test
	public void uiDemoAccess() {
		UIContext uiContext = new UIContext();
		uiContext.setFragment("acl://accessAdminTool");
		Response response = post(url + "hasAccess", "Basic ZGVtbzpndG4=", uiContext);
		boolean value = response.readEntity(Boolean.class);
		response.close();
		assertEquals(false, value, "Has not ui demo access");
	}

	@Test
	public void uiDemoAccessAddTopic() {
		UIContext uiContext = new UIContext();
		uiContext.setFragment("acl://newTopic");
		User user = new User("demo");
		uiContext.setIdentity(new JBossUser(user));
		Forum forum = new Forum();
		uiContext.setContextData(new Object[] { forum });
		Response response = post(url + "hasAccess", "Basic ZGVtbzpndG4=", uiContext);
		boolean value = response.readEntity(Boolean.class);
		response.close();
		assertEquals(true, value, "Has ui demo access");
	}
}
