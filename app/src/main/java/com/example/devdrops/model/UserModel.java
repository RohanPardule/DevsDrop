package com.example.devdrops.model;

public class UserModel {
    String email;
    String username;
    String userId;
    String profession;
    String profile;
    int followersCount;

    public UserModel(String email, String username, String userId, String profession, int followersCount) {
        this.email = email;
        this.username = username;
        this.userId = userId;
        this.profession = profession;
        this.followersCount=followersCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public UserModel() {
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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
