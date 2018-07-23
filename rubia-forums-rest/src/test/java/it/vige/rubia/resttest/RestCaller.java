package it.vige.rubia.resttest;

import static javax.json.bind.JsonbBuilder.create;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.json.bind.Jsonb;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public abstract class RestCaller {

	private final static Client client = newClient();

	protected Response get(String url, String authorization) {
		WebTarget target = client.target(url);
		return target.request().header("Authorization", authorization).get();
	}

	protected Response post(String url, String authorization, Object entity) {
		Jsonb jsonb = create();
		String json = jsonb.toJson(entity);
		WebTarget target = client.target(url);
		Entity<String> restEntity = entity(json, APPLICATION_JSON);
		return target.request().header("Authorization", authorization).post(restEntity);
	}
}
