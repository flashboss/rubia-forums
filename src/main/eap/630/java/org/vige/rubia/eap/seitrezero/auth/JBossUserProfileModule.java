package org.vige.rubia.eap.seitrezero.auth;

import java.util.Map;

import javax.inject.Named;

import org.vige.rubia.auth.ProfileInfo;
import org.vige.rubia.auth.User;
import org.vige.rubia.auth.UserProfileModule;

@Named("userProfileModule")
public class JBossUserProfileModule implements UserProfileModule {

	@Override
	public Object getProperty(User arg0, String arg1) throws IllegalArgumentException {
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

	@Override
	public Object getPropertyFromId(String arg0, String arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
