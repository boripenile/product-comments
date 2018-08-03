package com.example.app.products.account;

import java.sql.Timestamp;
import java.util.Date;


public class Account {

	private Long id;

	private String email;
	
	private String password;

	private String role = "ROLE_USER";

	private Date created;

    public Account() {

	}
	
	public Account(String email, String password, String role) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.created = new Date();
	}
	
	public Account(Long id, String email, String password, String role, Timestamp created) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
		this.created = new Date(created.getTime());
	}


	public Long getId() {
		return id;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreated() {
		return created;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role + ", created="
				+ created + "]";
	}
	
}
