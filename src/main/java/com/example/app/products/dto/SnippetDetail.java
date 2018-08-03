package com.example.app.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SnippetDetail {
	
	private String authorDisplayName;
	
	private String textDisplay;
	
	private Integer likeCount;
	
	private String publishedAt;

	public String getAuthorDisplayName() {
		return authorDisplayName;
	}

	public void setAuthorDisplayName(String authorDisplayName) {
		this.authorDisplayName = authorDisplayName;
	}

	public String getTextDisplay() {
		return textDisplay;
	}

	public void setTextDisplay(String textDisplay) {
		this.textDisplay = textDisplay;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	@Override
	public String toString() {
		return "SnippetDetail [authorDisplayName=" + authorDisplayName + ", textDisplay=" + textDisplay + ", likeCount="
				+ likeCount + ", publishedAt=" + publishedAt + "]";
	}
	
	
}
