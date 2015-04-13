package it.vige.rubia.gatein.treseizero.portlet.auth;

import java.util.Map;

import javax.inject.Named;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfileHandler;
import it.vige.rubia.auth.ProfileInfo;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserProfileModule;

@Named("userProfileModule")
public class GateinUserProfileModule implements UserProfileModule {

	private UserProfileHandler userProfileHandler;

	@Override
	public Object getProperty(User arg0, String arg1) throws IllegalArgumentException {
		getUserHandler();
		return null;
	}

	@Override
	public void setProperty(User arg0, String arg1, Object arg2) throws IllegalArgumentException {

	}

	@Override
	public Map<Object, Object> getProperties(User arg0) throws IllegalArgumentException {
		return null;
	}

	@Override
	public ProfileInfo getProfileInfo() {
		return null;
	}

	private UserProfileHandler getUserHandler() {
		if (userProfileHandler == null) {
			ExoContainer container = PortalContainer.getInstance();
			OrganizationService organizationService = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
			userProfileHandler = organizationService.getUserProfileHandler();
		}
		return userProfileHandler;
	}

	@Override
	public Object getPropertyFromId(String arg0, String arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
