package com.example.app.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TopLevelComment {

	private String kind;
	
	private SnippetDetail snippet;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public SnippetDetail getSnippet() {
		return snippet;
	}

	public void setSnippet(SnippetDetail snippet) {
		this.snippet = snippet;
	}

	@Override
	public String toString() {
		return "TopLevelComment [kind=" + kind + ", snippet=" + snippet + "]";
	}
	
	
}
