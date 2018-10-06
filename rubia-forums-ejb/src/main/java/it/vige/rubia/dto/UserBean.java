package it.vige.rubia.dto;

import java.io.Serializable;

import it.vige.rubia.auth.User;

public class UserBean implements User, Serializable {

	private static final long serialVersionUID = -5607842929773721930L;

	private String id;

	private String userName;

	public UserBean() {

	}

	public UserBean(User user) {
		id = user.getId();
		userName = user.getUserName();
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
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
