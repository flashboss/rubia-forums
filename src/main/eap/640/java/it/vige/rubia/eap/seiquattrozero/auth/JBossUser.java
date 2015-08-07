package it.vige.rubia.eap.seiquattrozero.auth;

import it.vige.rubia.auth.User;

public class JBossUser implements User {

	private String id;
	private String userName;

	public JBossUser(org.picketlink.idm.model.basic.User user) {
		this.userName = user.getLoginName();
		this.id = user.getLoginName();
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
