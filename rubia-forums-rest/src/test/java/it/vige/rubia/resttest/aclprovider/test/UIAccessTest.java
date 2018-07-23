package it.vige.rubia.resttest.aclprovider.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

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
