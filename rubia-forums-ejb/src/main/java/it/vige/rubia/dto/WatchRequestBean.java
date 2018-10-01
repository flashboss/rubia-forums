package it.vige.rubia.dto;

import java.io.Serializable;

public class WatchRequestBean implements Serializable {

	private static final long serialVersionUID = 6418210313550988529L;

	private int indexInstance;

	private int forumId;

	private int postId;

	private int topicId;
	
	private String date;

	private UserBean user;

	public int getIndexInstance() {
		return indexInstance;
	}

	public void setIndexInstance(int indexInstance) {
		this.indexInstance = indexInstance;
	}

	public int getForumId() {
		return forumId;
	}

	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}
}
