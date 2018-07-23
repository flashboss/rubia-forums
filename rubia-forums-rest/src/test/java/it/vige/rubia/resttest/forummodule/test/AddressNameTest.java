package it.vige.rubia.resttest.forummodule.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import it.vige.rubia.resttest.RestCaller;

public class AddressNameTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/forums/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	@Test
	public void setGuestUserName() {
		Response response = get(url + "setGuestUserName/bubbusettete", authorization);
		response.close();
		response = get(url + "getGuestUserName", authorization);
		String value = response.readEntity(String.class);
		response.close();
		assertEquals("bubbusettete", value, "Guest user name sent");
		response = get(url + "setGuestUserName/guest", authorization);
		response.close();
		response = get(url + "getGuestUserName", authorization);
		value = response.readEntity(String.class);
		response.close();
		assertEquals("guest", value, "Guest user name sent");
	}
}
