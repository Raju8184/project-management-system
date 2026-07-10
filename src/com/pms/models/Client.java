package com.pms.models;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String client_id;
    private String name;
    private String contact_email;
    private String company_name;
    private List<Project> projects;

    public Client(String client_id, String name, String contact_email, String company_name) {
        this.client_id = client_id;
        this.name = name;
        this.contact_email = contact_email;
        this.company_name = company_name;
        this.projects = new ArrayList<>();
    }

    public void add_project(Project project) {
        this.projects.add(project);
    }

    // Getters and Setters
    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
