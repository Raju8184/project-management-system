package com.pms.models;

import com.pms.Employee;
import java.util.Date;

public class Assignment {
    private String assignment_id;
    private String role;
    private Date assigned_date;
    private Employee employee;

    public Assignment(String assignment_id, String role, Date assigned_date, Employee employee) {
        this.assignment_id = assignment_id;
        this.role = role;
        this.assigned_date = assigned_date;
        this.employee = employee;
    }

    public String getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(String assignment_id) {
        this.assignment_id = assignment_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getAssigned_date() {
        return assigned_date;
    }

    public void setAssigned_date(Date assigned_date) {
        this.assigned_date = assigned_date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
