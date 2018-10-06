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
package it.vige.rubia.resttest.usermodule.test;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jboss.logging.Logger.getLogger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.auth.User;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.resttest.RestCaller;
import it.vige.rubia.search.ResultPage;
import it.vige.rubia.search.SearchCriteria;

public class UserTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/user/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(UserTest.class);

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
	}

	@Test
	public void findAndUpdate() {
		SearchCriteria searchCriteria = new SearchCriteria();
		Response response = post(url + "findPosts", authorization, searchCriteria);
		ResultPage<PostBean> posts = response.readEntity(new GenericType<ResultPage<PostBean>>() {
		});
		response.close();
	}

	@GET
	@Path("findUserByUserName/{userName}")
	@Produces(APPLICATION_JSON)
	public User findUserByUserName(@PathParam("userName") String arg0) throws IllegalArgumentException {
		return null;
	}

	@GET
	@Path("findUserById/{id}")
	@Produces(APPLICATION_JSON)
	public User findUserById(@PathParam("id") String arg0) throws IllegalArgumentException {
		return null;
	}

	@GET
	@Path("isGuest")
	@Produces(APPLICATION_JSON)
	public boolean isGuest() {
		return false;
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");
	}
}
