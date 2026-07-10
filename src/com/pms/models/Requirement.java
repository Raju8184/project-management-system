package com.pms.models;

public class Requirement {
    private String requirement_id;
    private String description;
    private String priority;
    private boolean is_fulfilled;

    public Requirement(String requirement_id, String description, String priority) {
        this.requirement_id = requirement_id;
        this.description = description;
        this.priority = priority;
        this.is_fulfilled = false;
    }

    public void mark_fulfilled() {
        this.is_fulfilled = true;
    }

    // Getters and Setters
    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isIs_fulfilled() {
        return is_fulfilled;
    }

    public void setIs_fulfilled(boolean is_fulfilled) {
        this.is_fulfilled = is_fulfilled;
    }
}
