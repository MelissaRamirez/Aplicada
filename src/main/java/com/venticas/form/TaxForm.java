package com.venticas.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TaxForm {

	private int id;
	@NotNull
	@Size(min = 5, max = 50)
	private String type;
	@NotNull
	@Min(1)
	private float percentage;

	public TaxForm() {

	}

	public TaxForm(int id, String type, float percentage) {

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
