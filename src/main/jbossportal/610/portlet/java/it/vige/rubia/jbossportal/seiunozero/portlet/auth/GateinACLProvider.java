package it.vige.rubia.jbossportal.seiunozero.portlet.auth;

import static org.exoplatform.container.ExoContainerContext.getCurrentContainer;
import static it.vige.rubia.auth.User.GUEST_USER;
import static org.jboss.security.acl.BasicACLPermission.READ;

import java.util.Collection;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.ForumsACLResource;
import it.vige.rubia.auth.JSFActionContext;
import it.vige.rubia.auth.JSFUIContext;
import it.vige.rubia.auth.SecurityContext;
import it.vige.rubia.auth.User;
import org.jboss.security.acl.ACLProvider;
import org.jboss.security.acl.ACLProviderImpl;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.plugins.SimpleIdentity;

@Named("forumsACLProvider")
public class GateinACLProvider implements ForumsACLProvider {

	private static final long serialVersionUID = -6012509900229501371L;

	@PersistenceContext(unitName = "forums")
	private EntityManager em;

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
		User user = (User) context.getIdentity();
		if (resource == null)
			resource = new ForumsACLResource(aclContextStr);
		boolean authorized = false;
		try {
			if (isAdministrator(user.getId()))
				return true;
			else if (user.getId().equals(GUEST_USER)) {
				Identity identity = new SimpleIdentity(getGuestGroupName());
				authorized = provider.isAccessGranted(resource, identity, READ);
				if (authorized) {
					resource.add("runtimeInfo", context.getContextData());
					resource.add("identity", context.getIdentity());
					authorized = resource.evaluate();
				}
			} else {
				Collection<?> groups = getGroups(user.getId());
				for (int i = 0; i < groups.size(); i++) {
					Object obj = groups.toArray()[i];
					Group group = (Group) obj;
					String roleId = group.getGroupName();
					Identity identity = new SimpleIdentity(roleId);
					authorized = provider.isAccessGranted(resource, identity,
							READ);
					if (authorized) {
						resource.add("runtimeInfo", context.getContextData());
						resource.add("identity", context.getIdentity());
						authorized = resource.evaluate();
					}
					if (authorized)
						break;
				}
			}
		} catch (Exception e) {
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
		User user = (User) context.getIdentity();
		if (resource == null)
			resource = new ForumsACLResource(aclContextStr);
		boolean authorized = false;
		try {
			if (isAdministrator(user.getId()))
				return true;
			else if (user.getId().equals(GUEST_USER)) {
				Identity identity = new SimpleIdentity(getGuestGroupName());
				authorized = provider.isAccessGranted(resource, identity, READ);
				if (authorized) {
					resource.add("runtimeInfo",
							new Object[] { context.getManagedBean() });
					resource.add("identity", context.getIdentity());
					authorized = resource.evaluate();
				}
			} else {
				Collection<?> groups = getGroups(user.getId());
				for (int i = 0; i < groups.size(); i++) {
					Object obj = groups.toArray()[i];
					Group group = (Group) obj;
					String roleId = group.getGroupName();
					Identity identity = new SimpleIdentity(roleId);
					authorized = provider.isAccessGranted(resource, identity,
							READ);
					if (authorized) {
						resource.add("runtimeInfo",
								new Object[] { context.getManagedBean() });
						resource.add("identity", context.getIdentity());
						authorized = resource.evaluate();
					}
					if (authorized)
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorized;
	}

	private boolean isAdministrator(String username) throws Exception {
		if (username == null) {
			return false;
		} else if (username.equals(getUserACL().getSuperUser())) {
			return true;
		} else {
			return isMemberOfGroup(username, getUserACL().getAdminGroups());
		}
	}

	private static UserACL getUserACL() {
		ExoContainer container = getCurrentContainer();
		UserACL acl = (UserACL) container
				.getComponentInstanceOfType(UserACL.class);
		return acl;
	}

	private boolean isMemberOfGroup(String username, String groupId)
			throws Exception {
		boolean ret = false;
		OrganizationService orgService = getOrganizationService();
		Collection<?> groups = orgService.getGroupHandler().findGroupsOfUser(
				username);
		for (Object group : groups) {
			if (((Group) group).getId().equals(groupId)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	private Collection<?> getGroups(String username) throws Exception {
		OrganizationService orgService = getOrganizationService();
		Collection<?> groups = orgService.getGroupHandler().findGroupsOfUser(
				username);
		return groups;
	}

	private String getGuestGroupName() throws Exception {
		OrganizationService orgService = getOrganizationService();
		Group group = orgService.getGroupHandler().findGroupById(
				getUserACL().getGuestsGroup());
		if (group != null)
			return group.getGroupName();
		return "";
	}

	private OrganizationService getOrganizationService() {
		ExoContainer container = getCurrentContainer();
		OrganizationService orgService = (OrganizationService) container
				.getComponentInstanceOfType(OrganizationService.class);
		return orgService;
	}

}
