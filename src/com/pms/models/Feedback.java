package com.pms.models;

public class Feedback {
    private String feedback_id;
    private ProjectStage stage;
    private String comments;
    private int rating;

    public Feedback(String feedback_id, ProjectStage stage, String comments, int rating) {
        this.feedback_id = feedback_id;
        this.stage = stage;
        this.comments = comments;
        this.rating = rating;
    }

    // Getters and Setters
    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public ProjectStage getStage() {
        return stage;
    }

    public void setStage(ProjectStage stage) {
        this.stage = stage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
