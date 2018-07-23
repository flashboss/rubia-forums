package it.vige.rubia.rest.test.forummodule;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

public class GuestUserTest {

	private Client client = newClient();
	private String url = "http://localhost:8080/rubia-forums-rest/services/forums/";

	@Test
	public void setAddressName() {
		Response response = getResponse(url + "setFromAddress/bubbusettete2");
		response.close();
		response = getResponse(url + "getFromAddress");
		String value = response.readEntity(String.class);
		response.close();
		assertEquals("bubbusettete2", value, "From address name sent");
		response = getResponse(url + "setFromAddress/portal@example.com");
		response.close();
		response = getResponse(url + "getFromAddress");
		value = response.readEntity(String.class);
		response.close();
		assertEquals("portal@example.com", value, "From address name sent");
	}

	private Response getResponse(String url) {
		WebTarget target = client.target(url);
		return target.request().header("Authorization", "Basic cm9vdDpndG4=").get();

	}
}
