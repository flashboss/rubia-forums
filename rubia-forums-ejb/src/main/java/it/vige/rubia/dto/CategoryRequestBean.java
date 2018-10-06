package it.vige.rubia.dto;

import java.io.Serializable;

public class CategoryRequestBean implements Serializable {

	private static final long serialVersionUID = 6018210313550988529L;

	private CategoryBean category;

	private int limit;

	public CategoryBean getCategory() {
		return category;
	}

	public void setCategory(CategoryBean category) {
		this.category = category;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
