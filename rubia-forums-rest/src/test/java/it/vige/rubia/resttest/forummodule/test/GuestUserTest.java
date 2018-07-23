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
