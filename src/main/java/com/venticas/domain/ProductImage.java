package com.venticas.domain;

public class ProductImage {
	private int id;
	private String filePath;
	private String webFilePath;
	
	public ProductImage() {}

	public ProductImage(int id, String filePath) {
		this.id = id;
		this.filePath = filePath;
		this.webFilePath = filePath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getWebFilePath() {
		this.webFilePath = this.filePath.replace("\\", "/");
		return webFilePath.replace("uploaded-img/", "");
	}

	public void setWebFilePath(String webFilePath) {
		this.webFilePath = webFilePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
