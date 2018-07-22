package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.SecurityContext;

@Path("/acl/")
public class RestACLProvider implements ForumsACLProvider {

	private static final long serialVersionUID = 8785387436799420309L;
	
	@EJB
	private ForumsACLProvider aclProvider;

	@POST
	@Path("hasAccess")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public boolean hasAccess(SecurityContext context) {
		return aclProvider.hasAccess(context);
	}

}
