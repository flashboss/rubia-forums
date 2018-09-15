package it.vige.rubia.dto;

import java.io.Serializable;

import it.vige.rubia.dto.TopicBean;

public class TopicRequestBean implements Serializable {

	private static final long serialVersionUID = 6018210313550988529L;

	private TopicBean topic;

	private int start;

	private int perPage;

	public TopicBean getTopic() {
		return topic;
	}

	public void setTopic(TopicBean topic) {
		this.topic = topic;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}
}
