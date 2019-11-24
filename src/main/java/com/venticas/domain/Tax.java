package com.venticas.domain;

public class Tax {
	private int id;
	private String type;
	private float percentage;

	public Tax() {

	}

	public Tax(int id, String type, float percentage) {

		this.id = id;
		this.type = type;
		this.percentage = percentage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

}
