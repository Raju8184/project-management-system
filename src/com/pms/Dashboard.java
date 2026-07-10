package com.pms;

import com.pms.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class Dashboard extends JFrame {
    private Company company;

    private JTable projectTable;
    private DefaultTableModel projectModel;

    private JTable clientTable;
    private DefaultTableModel clientModel;

    private JTable employeeTable;
    private DefaultTableModel employeeModel;

    public Dashboard(Company company) {
        this.company = company;
        setTitle("Project Management System Dashboard");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        // Header Title
        JLabel title = new JLabel("Company Dashboard: " + company.getName(), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Projects Tab
        JPanel projectPanel = new JPanel(new BorderLayout(10, 10));
        projectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] projCols = { "Project ID", "Name", "Type", "Budget", "Current Stage" };
        projectModel = new DefaultTableModel(projCols, 0);
        projectTable = new JTable(projectModel);
        projectTable.setRowHeight(25);
        projectPanel.add(new JScrollPane(projectTable), BorderLayout.CENTER);

        JPanel projectActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton advanceStageBtn = new JButton("Advance Selected Project Stage");
        advanceStageBtn.addActionListener(e -> {
            int selected = projectTable.getSelectedRow();
            if (selected != -1) {
                Project p = company.getProjects().get(selected);
                p.advance_stage();
                refreshTables();
                JOptionPane.showMessageDialog(this, "Project advanced to next stage!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project from the table first.");
            }
        });

        JButton refreshProjBtn = new JButton("Refresh Data");
        refreshProjBtn.addActionListener(e -> refreshTables());

        projectActionPanel.add(refreshProjBtn);
        projectActionPanel.add(advanceStageBtn);
        projectPanel.add(projectActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Projects", projectPanel);

        // 2. Clients Tab
        JPanel clientPanel = new JPanel(new BorderLayout(10, 10));
        clientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] clientCols = { "Client ID", "Full Name", "Organization", "Email" };
        clientModel = new DefaultTableModel(clientCols, 0);
        clientTable = new JTable(clientModel);
        clientTable.setRowHeight(25);
        clientPanel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        tabbedPane.addTab("Clients", clientPanel);

        // 3. Employees Tab
        JPanel empPanel = new JPanel(new BorderLayout(10, 10));
        empPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] empCols = { "Employee ID", "Name", "Designation", "Current Projects" };
        employeeModel = new DefaultTableModel(empCols, 0);
        employeeTable = new JTable(employeeModel);
        employeeTable.setRowHeight(25);
        empPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        tabbedPane.addTab("Employees", empPanel);

        // 4. Quick Action Tab
        JPanel actionPanelContainer = new JPanel(new BorderLayout());

        // Increased GridLayout grid limit to fit Clients properly
        JPanel actionPanel = new JPanel(new GridLayout(12, 4, 15, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // --- Column 1: Create Project & Client ---
        JLabel pTitle = new JLabel("--- Create New Project ---");
        pTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        actionPanel.add(pTitle);
        actionPanel.add(new JLabel(""));

        actionPanel.add(new JLabel("New Project Name:"));
        JTextField pNameField = new JTextField();
        actionPanel.add(pNameField);

        actionPanel.add(new JLabel("Budget ($):"));
        JTextField pBudgetField = new JTextField();
        actionPanel.add(pBudgetField);

        actionPanel.add(new JLabel("Project Type:"));
        JComboBox<String> pTypeBox = new JComboBox<>(new String[] { "Web", "Mobile", "Data Science" });
        actionPanel.add(pTypeBox);

        JButton addProjectBtn = new JButton("Register Project");
        addProjectBtn.addActionListener(e -> {
            try {
                String name = pNameField.getText();
                double budget = Double.parseDouble(pBudgetField.getText());
                String type = (String) pTypeBox.getSelectedItem();
                Project p = null;
                if ("Web".equals(type)) {
                    p = new WebDevelopmentProject("PRJ" + (company.getProjects().size() + 1), name, budget, new Date());
                } else if ("Mobile".equals(type)) {
                    p = new MobileAppProject("PRJ" + (company.getProjects().size() + 1), name, budget, new Date(),
                            java.util.Arrays.asList("iOS"));
                } else {
                    p = new DataScienceProject("PRJ" + (company.getProjects().size() + 1), name, budget, new Date(),
                            5.0f);
                }
                company.register_project(p);
                refreshTables();
                JOptionPane.showMessageDialog(this, "Project Registered!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Entry.");
            }
        });
        actionPanel.add(new JLabel(""));
        actionPanel.add(addProjectBtn);

        // Divider
        actionPanel.add(new JLabel(""));
        actionPanel.add(new JLabel(""));

        JLabel cTitle = new JLabel("--- Onboard New Client ---");
        cTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        actionPanel.add(cTitle);
        actionPanel.add(new JLabel(""));

        actionPanel.add(new JLabel("Client Full Name:"));
        JTextField cNameField = new JTextField();
        actionPanel.add(cNameField);

        actionPanel.add(new JLabel("Organization:"));
        JTextField cOrgField = new JTextField();
        actionPanel.add(cOrgField);

        actionPanel.add(new JLabel("Email Address:"));
        JTextField cEmailField = new JTextField();
        actionPanel.add(cEmailField);

        JButton addClientBtn = new JButton("Onboard Client");
        addClientBtn.addActionListener(e -> {
            if (!cNameField.getText().isEmpty()) {
                company.onboard_client(new Client("C00" + (company.getClients().size() + 1), cNameField.getText(),
                        cEmailField.getText(), cOrgField.getText()));
                refreshTables();
                JOptionPane.showMessageDialog(this, "Client Onboarded!");
            }
        });
        actionPanel.add(new JLabel(""));
        actionPanel.add(addClientBtn);

        // --- Hire Employee Section Below ---
        JPanel empActionPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        empActionPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel eTitle = new JLabel("--- Hire New Employee ---");
        eTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        empActionPanel.add(eTitle);
        empActionPanel.add(new JLabel(""));

        empActionPanel.add(new JLabel("Employee Name:"));
        JTextField eNameField = new JTextField();
        empActionPanel.add(eNameField);

        empActionPanel.add(new JLabel("Designation:"));
        JTextField eDesField = new JTextField();
        empActionPanel.add(eDesField);

        JButton addEmpBtn = new JButton("Hire Employee");
        addEmpBtn.addActionListener(e -> {
            if (!eNameField.getText().isEmpty()) {
                company.hire_employee(new Employee("EMP" + (company.getEmployees().size() + 1), eNameField.getText(),
                        eDesField.getText()));
                refreshTables();
                JOptionPane.showMessageDialog(this, "Employee Hired Successfully!");
            }
        });
        empActionPanel.add(new JLabel(""));
        empActionPanel.add(addEmpBtn);

        // Assembly
        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.add(actionPanel, BorderLayout.NORTH);
        combinedPanel.add(empActionPanel, BorderLayout.CENTER);

        actionPanelContainer.add(new JScrollPane(combinedPanel), BorderLayout.CENTER);
        tabbedPane.addTab("Quick Management", actionPanelContainer);

        add(tabbedPane, BorderLayout.CENTER);
        refreshTables();
    }

    private void refreshTables() {
        projectModel.setRowCount(0);
        for (Project p : company.getProjects()) {
            projectModel.addRow(new Object[] {
                    p.getProject_id(), p.getName(), p.getClass().getSimpleName(),
                    "$" + String.format("%.2f", p.getBudget()), p.getTracker().current_stage()
            });
        }

        clientModel.setRowCount(0);
        for (Client c : company.getClients()) {
            clientModel
                    .addRow(new Object[] { c.getClient_id(), c.getName(), c.getCompany_name(), c.getContact_email() });
        }

        employeeModel.setRowCount(0);
        for (Employee e : company.getEmployees()) {
            employeeModel.addRow(new Object[] { e.getEmployee_id(), e.getName(), e.getDesignation(), "0 Active" });
        }
    }

    public static void main(String[] args) {
        Company comp = new Company("Global Tech");

        // Massive Sample Data Population for a beautiful demo

        // Clients
        Client c1 = new Client("C001", "Bob Smith", "bob@example.com", "Acme LLC");
        Client c2 = new Client("C002", "Tony Stark", "stark@starkind.com", "Stark Industries");
        Client c3 = new Client("C003", "Bruce Wayne", "wayne@enterprises.com", "Wayne Corp");
        comp.onboard_client(c1);
        comp.onboard_client(c2);
        comp.onboard_client(c3);

        // Projects
        Project p1 = new WebDevelopmentProject("PRJ001", "Acme Website Rewrite", 25000, new Date());
        Project p2 = new MobileAppProject("PRJ002", "Stark Security App", 120000, new Date(),
                java.util.Arrays.asList("iOS"));
        Project p3 = new DataScienceProject("PRJ003", "Gotham Predictor AI", 85000, new Date(), 50.0f);
        Project p4 = new WebDevelopmentProject("PRJ004", "Employee Internal Portal", 15000, new Date());

        p1.advance_stage();
        p1.advance_stage(); // Make it DEVELOPMENT
        p2.advance_stage(); // Make it DESIGN

        comp.register_project(p1);
        comp.register_project(p2);
        comp.register_project(p3);
        comp.register_project(p4);

        // Employees
        comp.hire_employee(new Employee("E001", "Alice Johnson", "Senior Designer"));
        comp.hire_employee(new Employee("E002", "Mark Spencer", "Backend Engineer"));
        comp.hire_employee(new Employee("E003", "Sarah Connor", "Data Scientist"));
        comp.hire_employee(new Employee("E004", "James Bond", "Mobile Developer"));

        SwingUtilities.invokeLater(() -> {
            new Dashboard(comp).setVisible(true);
        });
    }
}
