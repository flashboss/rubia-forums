package it.vige.rubia.rest.test.aclprovider;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.picketlink.idm.model.basic.User;

import it.vige.rubia.auth.UIContext;
import it.vige.rubia.model.Forum;
import it.vige.rubia.wildfly.auth.JBossUser;

public class UIAccessTest {

	private final static Client client = newClient();
	private final static String url = "http://localhost:8080/rubia-forums-rest/services/acl/";

	@Test
	public void uiRootAccess() {
		UIContext uiContext = new UIContext();
		uiContext.setFragment("acl://accessAdminTool");
		Response response = getResponse(url + "hasAccess", "Basic cm9vdDpndG4=", uiContext);
		boolean value = response.readEntity(Boolean.class);
		response.close();
		assertEquals(true, value, "Has ui root access");
	}

	@Test
	public void uiDemoAccess() {
		UIContext uiContext = new UIContext();
		uiContext.setFragment("acl://accessAdminTool");
		Response response = getResponse(url + "hasAccess", "Basic ZGVtbzpndG4=", uiContext);
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
		Response response = getResponse(url + "hasAccess", "Basic ZGVtbzpndG4=", uiContext);
		boolean value = response.readEntity(Boolean.class);
		response.close();
		assertEquals(true, value, "Has ui demo access");
	}

	private Response getResponse(String url, String authorization, UIContext uiContext) {
		Jsonb jsonb = JsonbBuilder.create();
		String json = jsonb.toJson(uiContext);
		WebTarget target = client.target(url);
		Entity<String> uiContextEntity = entity(json, APPLICATION_JSON);
		return target.request().header("Authorization", authorization).post(uiContextEntity);
	}
}
