package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.security.acl.ACLPermission;
import org.jboss.security.acl.ACLPersistenceStrategy;
import org.jboss.security.acl.ACLProvider;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;

@Path("/acl/")
public class RestACLProvider implements ACLProvider {

	@EJB
	private ACLProvider aclProvider;

	@POST
	@Path("getEntitlements")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public <T> Set<T> getEntitlements(Class<T> arg0, Resource arg1, Identity arg2) throws AuthorizationException {
		return aclProvider.getEntitlements(arg0, arg1, arg2);
	}

	@Override
	@GET
	@Path("getPersistenceStrategy")
	@Consumes(APPLICATION_JSON)
	public ACLPersistenceStrategy getPersistenceStrategy() {
		return aclProvider.getPersistenceStrategy();
	}

	@POST
	@Path("initialize")
	@Consumes(APPLICATION_JSON)
	@Override
	public void initialize(Map<String, Object> arg0, Map<String, Object> arg1) {
		aclProvider.initialize(arg0, arg1);
	}

	@POST
	@Path("isAccessGranted")
	@Consumes(APPLICATION_JSON)
	@Override
	public boolean isAccessGranted(Resource arg0, Identity arg1, ACLPermission arg2) throws AuthorizationException {
		return aclProvider.isAccessGranted(arg0, arg1, arg2);
	}

	@POST
	@Path("setPersistenceStrategy")
	@Consumes(APPLICATION_JSON)
	@Override
	public void setPersistenceStrategy(ACLPersistenceStrategy arg0) {
		aclProvider.setPersistenceStrategy(arg0);
	}

	@GET
	@Path("tearDown")
	@Override
	public boolean tearDown() {
		return aclProvider.tearDown();
	}

}
