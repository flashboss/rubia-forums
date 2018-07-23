package it.vige.rubia.rest.test.forummodule;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

public class GuestUserTest {

	private final static Client client = newClient();
	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	@Test
	public void setAddressName() {
		Response response = getResponse(url + "setFromAddress/bubbusettete2", authorization);
		response.close();
		response = getResponse(url + "getFromAddress", authorization);
		String value = response.readEntity(String.class);
		response.close();
		assertEquals("bubbusettete2", value, "From address name sent");
		response = getResponse(url + "setFromAddress/portal@example.com", authorization);
		response.close();
		response = getResponse(url + "getFromAddress", authorization);
		value = response.readEntity(String.class);
		response.close();
		assertEquals("portal@example.com", value, "From address name sent");
	}

	private Response getResponse(String url, String authorization) {
		WebTarget target = client.target(url);
		return target.request().header("Authorization", authorization).get();
	}
}
