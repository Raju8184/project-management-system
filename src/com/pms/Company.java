package com.pms;

import com.pms.models.*;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private List<Employee> employees;
    private List<Client> clients;
    private List<Project> projects;

    public Company(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void hire_employee(Employee employee) {
        this.employees.add(employee);
    }

    public void onboard_client(Client client) {
        this.clients.add(client);
    }

    public void register_project(Project project) {
        this.projects.add(project);
    }

    public void company_summary() {
        System.out.println("=== " + name + " : Company Report ===");
        System.out.println("Employees: " + employees.size());
        for (Employee e : employees) {
            System.out.println("  - " + e.get_details());
        }
        System.out.println("Clients: " + clients.size());
        for (Client c : clients) {
            System.out.println("  - " + c.getName() + " [" + c.getCompany_name() + "], "
                    + c.getProjects().size() + " project(s)");
        }
        System.out.println("Projects: " + projects.size());
        for (Project p : projects) {
            System.out.println("  - " + p.getName() + " (" + p.getProject_id() + ")");
            System.out.println("      Budget: " + p.getBudget() + ", Stage: " + p.getTracker().current_stage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
