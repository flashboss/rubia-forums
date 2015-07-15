package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;

@Path("/user/")
public class RestUserModule implements UserModule {

	@EJB
	private UserModule userModule;

	@GET
	@Path("findUserByUserName/{userName}")
	@Produces(APPLICATION_JSON)
	@Override
	public User findUserByUserName(@PathParam("userName") String arg0) throws IllegalArgumentException {
		return userModule.findUserByUserName(arg0);
	}

	@GET
	@Path("findUserById/{id}")
	@Produces(APPLICATION_JSON)
	@Override
	public User findUserById(@PathParam("id") String arg0) throws IllegalArgumentException {
		return userModule.findUserById(arg0);
	}

	@GET
	@Path("isGuest")
	@Produces(APPLICATION_JSON)
	@Override
	public boolean isGuest() {
		return userModule.isGuest();
	}

}
