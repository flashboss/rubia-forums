package it.vige.rubia.rest.test.forummodule;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

public class AddressNameTest {

	private Client client = newClient();
	private String url = "http://localhost:8080/rubia-forums-rest/services/forums/";

	@Test
	public void setGuestUserName() {
		Response response = getResponse(url + "setGuestUserName/bubbusettete");
		response.close();
		response = getResponse(url + "getGuestUserName");
		String value = response.readEntity(String.class);
		response.close();
		assertEquals("bubbusettete", value, "Guest user name sent");
		response = getResponse(url + "setGuestUserName/guest");
		response.close();
		response = getResponse(url + "getGuestUserName");
		value = response.readEntity(String.class);
		response.close();
		assertEquals("guest", value, "Guest user name sent");
	}

	private Response getResponse(String url) {
		WebTarget target = client.target(url);
		return target.request().header("Authorization", "Basic cm9vdDpndG4=").get();

	}
}
