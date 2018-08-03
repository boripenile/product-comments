package com.example.app.products.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value= {"userName", "date", "starRating", "comment", "link"}, 
	alphabetic=true)
public class CommentDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value="userName")
	private String userName;
	
	@JsonProperty(value="date")
	private String date;
	
	@JsonProperty(value="starRating")
	private String starRating;
	
	@JsonProperty(value="comment")
	private String comment;
	
	@JsonProperty(value="link")
	private String link;

	
	public CommentDTO(String userName, String date, String starRating, String comment, String link) {
		this.userName = userName;
		if (date.contains("on")) {
			String d = date.replace("on ", "");
			this.date = d;
		} else {
			this.date = date;
		}
		this.starRating = !starRating.isEmpty() ? starRating.substring(0, 1) : "";
		this.comment = comment;
		this.link = link;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStarRating() {
		return starRating;
	}

	public void setStarRating(String starRating) {
		this.starRating = starRating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "CommentDTO [userName=" + userName + ", date=" + date + ", starRating=" + starRating + ", comment="
				+ comment + ", link=" + link + "]";
	}
	
	
}
