package it.vige.rubia.jbossas.setteunodue.auth;

import it.vige.rubia.auth.User;

public class JBossUser implements User {

	private String id;
	private String userName;
	
	public JBossUser(org.picketlink.idm.model.User user) {
		this.userName = user.getKey();
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
