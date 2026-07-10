package com.pms.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Project {
    protected String project_id;
    protected String name;
    protected double budget;
    protected Date deadline;
    protected ProjectStatus status;
    protected List<Requirement> requirements;
    protected List<Assignment> assignments;
    protected List<Feedback> feedbacks;
    protected DevelopmentTracker tracker;
    protected Billing billing;

    public Project(String project_id, String name, double budget, Date deadline) {
        this.project_id = project_id;
        this.name = name;
        this.budget = budget;
        this.deadline = deadline;
        this.status = ProjectStatus.ONGOING;
        this.requirements = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
        this.tracker = new DevelopmentTracker();
    }

    public void add_requirement(Requirement req) {
        this.requirements.add(req);
    }

    public void assign_employee(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public void add_feedback(Feedback feedback) {
        this.feedbacks.add(feedback);
    }

    public void advance_stage() {
        this.tracker.advance_stage();
    }

    public String generate_report() {
        return "Project Report: " + name + " (Stage: " + tracker.current_stage() + ", Status: " + status + ")";
    }

    public abstract double calculate_billing();

    public abstract List<String> get_deliverables();

    // Getters and Setters
    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public DevelopmentTracker getTracker() {
        return tracker;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }
}
