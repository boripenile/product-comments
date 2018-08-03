package com.example.app.products.download;

import org.hibernate.validator.constraints.*;

public class DownloadForm {

	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @SuppressWarnings("deprecation")
	@NotBlank(message = DownloadForm.NOT_BLANK_MESSAGE)
	private String downloadUrl;

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	

}
