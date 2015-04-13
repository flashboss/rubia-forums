package it.vige.rubia.gatein.treottodue.portlet.auth;

import static javax.faces.context.FacesContext.getCurrentInstance;
import static org.exoplatform.container.ExoContainerContext.getCurrentContainer;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Named;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserHandler;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;

@Named("userModule")
public class GateinUserModule implements UserModule, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471050522969443933L;

	private UserHandler userHandler;

	@Override
	public User findUserByUserName(String arg0) throws IllegalArgumentException {
		User user = null;
		try {
			if (userHandler == null)
				userHandler = getUserHandler();
			org.exoplatform.services.organization.User newUser = getUserHandler()
					.findUserByName(arg0);
			user = new GateinUser(newUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	@Override
	public User findUserById(String arg0) {
		User user = null;
		try {
			org.exoplatform.services.organization.User newUser = getUserHandler()
					.findUserByName(arg0);
			user = new GateinUser(newUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	private UserHandler getUserHandler() {
		if (userHandler == null) {
			ExoContainer container = PortalContainer.getInstance();
			OrganizationService organizationService = (OrganizationService) container
					.getComponentInstanceOfType(OrganizationService.class);
			userHandler = organizationService.getUserHandler();
		}
		return userHandler;
	}

	@Override
	public boolean isGuest() {
		Collection<?> groups = null;
		OrganizationService orgService = getOrganizationService();
		try {
			String userId = getCurrentInstance().getExternalContext()
					.getRemoteUser();
			if (userId == null)
				return true;
			groups = orgService.getGroupHandler().findGroupsOfUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (groups != null)
			if (groups.size() == 1)
				return ((Group) groups.toArray()[0]).getGroupName().equals(
						getGuestGroupName());
		return false;
	}

	private OrganizationService getOrganizationService() {
		ExoContainer container = getCurrentContainer();
		OrganizationService orgService = (OrganizationService) container
				.getComponentInstanceOfType(OrganizationService.class);
		return orgService;
	}

	private static UserACL getUserACL() {
		ExoContainer container = getCurrentContainer();
		UserACL acl = (UserACL) container
				.getComponentInstanceOfType(UserACL.class);
		return acl;
	}

	private String getGuestGroupName() {
		OrganizationService orgService = getOrganizationService();
		Group group = null;
		try {
			group = orgService.getGroupHandler().findGroupById(
					getUserACL().getGuestsGroup());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (group != null)
			return group.getGroupName();
		return "";
	}
}
