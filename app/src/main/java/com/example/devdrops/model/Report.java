package com.example.devdrops.model;

// Report.java
public class Report {
    private String postId;
    private String reason;

    public Report() {
        // Default constructor required for Firebase
    }

    public Report(String postId, String reason) {
        this.postId = postId;
        this.reason = reason;
    }

    // Getters and setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
