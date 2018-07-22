package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import it.vige.rubia.auth.ActionContext;
import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.SecurityContext;
import it.vige.rubia.auth.UIContext;

@Path("/acl/")
public class RestACLProvider {

	@EJB
	private ForumsACLProvider aclProvider;

	@POST
	@Path("hasActionAccess")
	@Consumes(APPLICATION_JSON)
	@Produces(TEXT_PLAIN)
	public boolean hasAccess(RestContext context) {
		SecurityContext securityContext = null;
		if (context.getContextType().equals("action")) {
			securityContext = new ActionContext();
		} else
			securityContext = new UIContext();
		return aclProvider.hasAccess(securityContext);
	}

}
