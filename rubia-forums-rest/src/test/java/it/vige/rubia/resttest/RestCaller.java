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
package it.vige.rubia.resttest;

import static jakarta.json.bind.JsonbBuilder.create;
import static jakarta.ws.rs.client.ClientBuilder.newClient;
import static jakarta.ws.rs.client.Entity.entity;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public abstract class RestCaller {

	private final static Client client = newClient();

	protected static Response get(String url, String authorization) {
		WebTarget target = client.target(url);
		return target.request().header("Authorization", authorization).get();
	}

	protected static Response post(String url, String authorization, Object entity) {
		Jsonb jsonb = create();
		String json = jsonb.toJson(entity);
		WebTarget target = client.target(url);
		Entity<String> restEntity = entity(json, APPLICATION_JSON);
		return target.request().header("Authorization", authorization).post(restEntity);
	}
}
