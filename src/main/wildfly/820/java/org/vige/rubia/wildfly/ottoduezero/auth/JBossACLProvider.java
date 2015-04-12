package org.vige.rubia.wildfly.ottoduezero.auth;

import static org.jboss.security.acl.BasicACLPermission.READ;

import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vige.rubia.auth.ForumsACLProvider;
import org.vige.rubia.auth.ForumsACLResource;
import org.vige.rubia.auth.JSFActionContext;
import org.vige.rubia.auth.JSFUIContext;
import org.vige.rubia.auth.SecurityContext;
import org.jboss.security.acl.ACLProvider;
import org.jboss.security.acl.ACLProviderImpl;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.plugins.SimpleIdentity;

@Named("forumsACLProvider")
@Stateless
public class JBossACLProvider implements ForumsACLProvider {

	private static final long serialVersionUID = -5490482161183021121L;

	@PersistenceContext(unitName = "forums")
	private EntityManager em;

	@javax.annotation.Resource
	private EJBContext ejbContext;

	private ACLProvider provider = new ACLProviderImpl();

	@Override
	public boolean hasAccess(SecurityContext context) {
		if (context instanceof JSFUIContext)
			return hasAccess((JSFUIContext) context);
		else
			return hasAccess((JSFActionContext) context);
	}

	public boolean hasAccess(JSFUIContext context) {
		final String aclContextStr = context.getFragment();
		provider.setPersistenceStrategy(new ForumsJPAPersistenceStrategy(em));
		ForumsACLResource resource = null;
		resource = em.find(ForumsACLResource.class, aclContextStr);
		if (resource == null)
			resource = new ForumsACLResource(aclContextStr);
		String roleId = "";
		if (ejbContext.getCallerPrincipal().getName().equals("anonymous"))
			roleId = "guests";
		else if (ejbContext.isCallerInRole("admins")
				|| ejbContext.isCallerInRole("admin"))
			return true;
		else if (ejbContext.isCallerInRole("users")
				|| ejbContext.isCallerInRole("user"))
			roleId = "users";
		Identity identity = new SimpleIdentity(roleId);
		boolean authorized = false;
		try {
			authorized = provider.isAccessGranted(resource, identity, READ);
			if (authorized) {
				resource.add("runtimeInfo", context.getContextData());
				resource.add("identity", context.getIdentity());
				authorized = resource.evaluate();
			}
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		return authorized;
	}

	public boolean hasAccess(JSFActionContext context) {
		String className = context.getManagedBean().getClass().getName();
		final String aclContextStr = className.substring(0,
				className.indexOf("$Proxy$_$$_WeldSubclass"))
				+ ":" + context.getBusinessAction().getName();
		provider.setPersistenceStrategy(new ForumsJPAPersistenceStrategy(em));
		ForumsACLResource resource = null;
		resource = em.find(ForumsACLResource.class, aclContextStr);
		if (resource == null)
			resource = new ForumsACLResource(aclContextStr);
		String roleId = "";
		if (ejbContext.getCallerPrincipal().getName().equals("anonymous"))
			roleId = "guests";
		else if (ejbContext.isCallerInRole("admins")
				|| ejbContext.isCallerInRole("admin"))
			return true;
		else if (ejbContext.isCallerInRole("users")
				|| ejbContext.isCallerInRole("user"))
			roleId = "users";
		Identity identity = new SimpleIdentity(roleId);
		boolean authorized = false;
		try {
			authorized = provider.isAccessGranted(resource, identity, READ);
			if (authorized) {
				resource.add("runtimeInfo", new Object[]{context.getManagedBean()});
				resource.add("identity", context.getIdentity());
				authorized = resource.evaluate();
			}
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		return authorized;
	}

}
