package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	@JsonProperty("id")
	private Integer userId;
	@JsonProperty("username")
	private String userName;


	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer id) {
		this.userId = id;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
