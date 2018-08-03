package com.example.app.products.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FacebookCommentDTO {

	private String kind;
	
	private String nextPageToken;
	
	private List<Item> items = new ArrayList<Item>();

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	@Override
	public String toString() {
		return "FacebookComment [kind=" + kind + ", items=" + items + "]";
	}
	
	
}
