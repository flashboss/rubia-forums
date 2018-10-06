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
package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import it.vige.rubia.auth.UserModule;
import it.vige.rubia.dto.UserBean;

@Path("/user/")
public class RestUserModule {

	@EJB
	private UserModule userModule;

	@GET
	@Path("findUserByUserName/{userName}")
	@Produces(APPLICATION_JSON)
	public UserBean findUserByUserName(@PathParam("userName") String arg0) throws IllegalArgumentException {
		return new UserBean(userModule.findUserByUserName(arg0));
	}

	@GET
	@Path("findUserById/{id}")
	@Produces(APPLICATION_JSON)
	public UserBean findUserById(@PathParam("id") String arg0) throws IllegalArgumentException {
		return new UserBean(userModule.findUserById(arg0));
	}

	@GET
	@Path("isGuest")
	public Boolean isGuest() {
		return userModule.isGuest();
	}

}
