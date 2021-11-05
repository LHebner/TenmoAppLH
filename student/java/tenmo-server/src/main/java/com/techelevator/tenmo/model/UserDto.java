package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {

    @JsonProperty("id")
    private long userId;
    @JsonProperty("username")
    private String userName;

    public UserDto() {}

    public UserDto(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
