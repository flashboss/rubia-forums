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
package it.vige.rubia.liferay.settezero.auth;

import static org.jboss.security.acl.BasicACLPermission.READ;

import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.security.acl.ACLProvider;
import org.jboss.security.acl.ACLProviderImpl;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.plugins.SimpleIdentity;

import it.vige.rubia.auth.ActionContext;
import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.ForumsACLResource;
import it.vige.rubia.auth.SecurityContext;
import it.vige.rubia.auth.UIContext;

@Named("forumsACLProvider")
@Stateless
public class LiferayACLProvider implements ForumsACLProvider {

	private static final long serialVersionUID = -5490482161183021121L;

	@PersistenceContext(unitName = "forums")
	private EntityManager em;

	@javax.annotation.Resource
	private EJBContext ejbContext;

	private ACLProvider provider = new ACLProviderImpl();

	@Override
	public boolean hasAccess(SecurityContext context) {
		if (context instanceof UIContext)
			return hasAccess((UIContext) context);
		else
			return hasAccess((ActionContext) context);
	}

	public boolean hasAccess(UIContext context) {
		final String aclContextStr = context.getFragment();
		provider.setPersistenceStrategy(new ForumsJPAPersistenceStrategy(em));
		ForumsACLResource resource = null;
		resource = em.find(ForumsACLResource.class, aclContextStr);
		if (resource == null)
			resource = new ForumsACLResource(aclContextStr);
		String roleId = "";
		if (ejbContext.getCallerPrincipal().getName().equals("anonymous"))
			roleId = "guests";
		else if (ejbContext.isCallerInRole("administrator"))
			return true;
		else if (ejbContext.isCallerInRole("users") || ejbContext.isCallerInRole("user"))
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

	public boolean hasAccess(ActionContext context) {
		String className = context.getManagedBean().getClass().getName();
		final String aclContextStr = className.substring(0, className.indexOf("$Proxy$_$$_WeldSubclass")) + ":"
				+ context.getBusinessAction().getName();
		provider.setPersistenceStrategy(new ForumsJPAPersistenceStrategy(em));
		ForumsACLResource resource = null;
		resource = em.find(ForumsACLResource.class, aclContextStr);
		if (resource == null)
			resource = new ForumsACLResource(aclContextStr);
		String roleId = "";
		if (ejbContext.getCallerPrincipal().getName().equals("anonymous"))
			roleId = "guests";
		else if (ejbContext.isCallerInRole("administrator"))
			return true;
		else if (ejbContext.isCallerInRole("user"))
			roleId = "users";
		Identity identity = new SimpleIdentity(roleId);
		boolean authorized = false;
		try {
			authorized = provider.isAccessGranted(resource, identity, READ);
			if (authorized) {
				resource.add("runtimeInfo", new Object[] { context.getManagedBean() });
				resource.add("identity", context.getIdentity());
				authorized = resource.evaluate();
			}
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		return authorized;
	}

}
