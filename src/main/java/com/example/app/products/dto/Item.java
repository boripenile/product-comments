package com.example.app.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Item {

	private String id;
	
	private String kind;
	
	private Snippet snippet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Snippet getSnippet() {
		return snippet;
	}

	public void setSnippet(Snippet snippet) {
		this.snippet = snippet;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", kind=" + kind + ", snippet=" + snippet + "]";
	}
	
	
}
