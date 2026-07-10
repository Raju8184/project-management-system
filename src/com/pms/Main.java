package com.pms;

import com.pms.models.*;

import java.util.*;

public class Main {
        public static void main(String[] args) {
                try (Scanner scanner = new Scanner(System.in)) {
                        Company company = new Company("Acme Solutions");

                        while (true) {
                                System.out.println("\n--- Project Management System Menu ---");
                                System.out.println("1. Add Employee");
                                System.out.println("2. Add Client");
                                System.out.println("3. Create Project");
                                System.out.println("4. Display Company Summary");
                                System.out.println("5. Exit");
                                System.out.print("Select an option: ");

                                String choiceStr = scanner.nextLine();
                                int choice;
                                try {
                                        choice = Integer.parseInt(choiceStr);
                                } catch (NumberFormatException e) {
                                        System.out.println("Invalid option. Please enter a number.");
                                        continue;
                                }

                                switch (choice) {
                                        case 1:
                                                System.out.print("Enter Employee ID: ");
                                                String empId = scanner.nextLine();
                                                System.out.print("Enter Name: ");
                                                String empName = scanner.nextLine();
                                                System.out.print("Enter Designation: ");
                                                String designation = scanner.nextLine();

                                                Employee emp = new Employee(empId, empName, designation);
                                                company.hire_employee(emp);
                                                System.out.println("Employee added successfully.");
                                                break;
                                        case 2:
                                                System.out.print("Enter Client ID: ");
                                                String clientId = scanner.nextLine();
                                                System.out.print("Enter Name: ");
                                                String clientName = scanner.nextLine();
                                                System.out.print("Enter Contact Email: ");
                                                String email = scanner.nextLine();
                                                System.out.print("Enter Company Name: ");
                                                String clientCompany = scanner.nextLine();

                                                Client client = new Client(clientId, clientName, email, clientCompany);
                                                company.onboard_client(client);
                                                System.out.println("Client added successfully.");
                                                break;
                                        case 3:
                                                System.out.print(
                                                                "Enter Project Type (1: Web, 2: Mobile, 3: Data Science): ");
                                                String pType = scanner.nextLine();

                                                System.out.print("Enter Project ID: ");
                                                String pId = scanner.nextLine();
                                                System.out.print("Enter Project Name: ");
                                                String pName = scanner.nextLine();
                                                System.out.print("Enter Project Budget: ");
                                                double budget;
                                                try {
                                                        budget = Double.parseDouble(scanner.nextLine());
                                                } catch (NumberFormatException e) {
                                                        System.out.println(
                                                                        "Invalid budget format. Project creation failed.");
                                                        break;
                                                }

                                                Project project = null;
                                                if ("1".equals(pType)) {
                                                        project = new WebDevelopmentProject(pId, pName, budget,
                                                                        new Date());
                                                } else if ("2".equals(pType)) {
                                                        System.out.print(
                                                                        "Enter comma separated platforms (e.g., iOS,Android): ");
                                                        String plats = scanner.nextLine();
                                                        project = new MobileAppProject(pId, pName, budget, new Date(),
                                                                        Arrays.asList(plats.split(",")));
                                                } else if ("3".equals(pType)) {
                                                        System.out.print("Enter dataset size in GB: ");
                                                        float ds;
                                                        try {
                                                                ds = Float.parseFloat(scanner.nextLine());
                                                        } catch (NumberFormatException e) {
                                                                System.out.println(
                                                                                "Invalid float format. Project creation failed.");
                                                                break;
                                                        }
                                                        project = new DataScienceProject(pId, pName, budget, new Date(),
                                                                        ds);
                                                } else {
                                                        System.out.println(
                                                                        "Invalid project type. Project creation failed.");
                                                        break;
                                                }

                                                System.out.print("Assign to Client (Enter existing Client ID): ");
                                                String assignClientId = scanner.nextLine();

                                                Client foundClient = null;
                                                for (Client c : company.getClients()) {
                                                        if (c.getClient_id().equals(assignClientId)) {
                                                                foundClient = c;
                                                                break;
                                                        }
                                                }

                                                if (foundClient == null) {
                                                        System.out.println("Error: Client ID '" + assignClientId
                                                                        + "' not found. Cannot attach project to a non-existent client.");
                                                        break;
                                                }

                                                foundClient.add_project(project);
                                                company.register_project(project);
                                                System.out.println(
                                                                "Project created successfully and assigned to client "
                                                                                + foundClient.getName() + ".");
                                                break;
                                        case 4:
                                                company.company_summary();
                                                break;
                                        case 5:
                                                System.out.println("Exiting System. Goodbye!");
                                                return;
                                        default:
                                                System.out.println("Invalid choice. Try again.");
                                }
                        }
                }
        }
}
