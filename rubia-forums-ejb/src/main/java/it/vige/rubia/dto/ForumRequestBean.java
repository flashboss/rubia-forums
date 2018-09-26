package it.vige.rubia.dto;

import java.io.Serializable;

public class ForumRequestBean implements Serializable {

	private static final long serialVersionUID = 6018210413550988529L;

	private ForumBean forum;

	private int limit;

	public ForumBean getForum() {
		return forum;
	}

	public void setForum(ForumBean forum) {
		this.forum = forum;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
