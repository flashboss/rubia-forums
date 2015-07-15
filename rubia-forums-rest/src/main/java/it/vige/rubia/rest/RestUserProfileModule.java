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
