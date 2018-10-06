package it.vige.rubia.dto;

import java.io.Serializable;

public class UserPropertyBean implements Serializable {

	private static final long serialVersionUID = -5607142929773721930L;

	private UserBean user;

	private String key;

	private String value;

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
