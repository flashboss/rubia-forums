package it.vige.rubia.jbossportal.seiunouno.portlet.auth;

import it.vige.rubia.auth.User;

public class GateinUser implements User {

	private String id;
	private String userName;

	public GateinUser(org.exoplatform.services.organization.User user) {
		this.userName = user.getUserName();
		this.id = user.getUserName();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}
