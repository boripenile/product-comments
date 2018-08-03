package com.example.app.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Snippet {

	private String videoId;
	
	private TopLevelComment topLevelComment;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public TopLevelComment getTopLevelComment() {
		return topLevelComment;
	}

	public void setTopLevelComment(TopLevelComment topLevelComment) {
		this.topLevelComment = topLevelComment;
	}

	@Override
	public String toString() {
		return "Snippet [videoId=" + videoId + ", topLevelComment=" + topLevelComment + "]";
	}
	
	
}
