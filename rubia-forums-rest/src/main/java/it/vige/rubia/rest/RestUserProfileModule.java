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

import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import it.vige.rubia.auth.ProfileInfo;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserProfileModule;

@Path("/profile/")
public class RestUserProfileModule implements UserProfileModule {

	@EJB
	private UserProfileModule userProfileModule;

	@POST
	@Path("getProperty")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Object getProperty(User arg0, String arg1) throws IllegalArgumentException {
		return userProfileModule.getProperty(arg0, arg1);
	}

	@GET
	@Path("getPropertyFromId/{id}/{id2}")
	@Produces(APPLICATION_JSON)
	@Override
	public Object getPropertyFromId(@PathParam("id") String arg0, @PathParam("id2") String arg1)
			throws IllegalArgumentException {
		return userProfileModule.getPropertyFromId(arg0, arg1);
	}

	@POST
	@Path("setProperty")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public void setProperty(User arg0, String arg1, Object arg2) throws IllegalArgumentException {
		userProfileModule.setProperty(arg0, arg1, arg2);
	}

	@POST
	@Path("getProperties")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public Map<Object, Object> getProperties(User arg0) throws IllegalArgumentException {
		return userProfileModule.getProperties(arg0);
	}

	@GET
	@Path("getProfileInfo")
	@Produces(APPLICATION_JSON)
	@Override
	public ProfileInfo getProfileInfo() {
		return userProfileModule.getProfileInfo();
	}

}
