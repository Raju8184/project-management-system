package com.pms.models;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String employee_id;
    private String name;
    private String designation;
    private List<String> skills;

    public Employee(String employee_id, String name, String designation) {
        this.employee_id = employee_id;
        this.name = name;
        this.designation = designation;
        this.skills = new ArrayList<>();
    }

    public String get_details() {
        return "EmployeeID: " + employee_id + ", Name: " + name + ", Designation: " + designation;
    }

    // Getters and Setters
    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
