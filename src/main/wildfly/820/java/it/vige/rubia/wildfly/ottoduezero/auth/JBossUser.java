package it.vige.rubia.wildfly.ottoduezero.auth;

import it.vige.rubia.auth.User;

public class JBossUser implements User {

	private String id;
	private String userName;
	
	public JBossUser(org.picketlink.idm.model.basic.User user) {
		this.userName = user.getId();
		this.id = user.getId();
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
