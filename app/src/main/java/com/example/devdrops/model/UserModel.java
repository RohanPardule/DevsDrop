package com.example.devdrops.model;

public class UserModel {
    String email;
    String username;
    String userId;
    String profession;

    public UserModel(String email, String username, String userId, String profession) {
        this.email = email;
        this.username = username;
        this.userId = userId;
        this.profession = profession;
    }

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
