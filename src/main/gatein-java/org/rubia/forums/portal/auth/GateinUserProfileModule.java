package org.rubia.forums.portal.auth;

import java.util.Map;

import javax.inject.Named;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfileHandler;
import org.rubia.forums.auth.ProfileInfo;
import org.rubia.forums.auth.User;
import org.rubia.forums.auth.UserProfileModule;

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
