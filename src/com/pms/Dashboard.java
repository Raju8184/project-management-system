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
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

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

        JButton deleteProjBtn = new JButton("Delete Project");
        deleteProjBtn.setForeground(Color.RED);
        deleteProjBtn.addActionListener(e -> {
            int selected = projectTable.getSelectedRow();
            if (selected != -1) {
                company.getProjects().remove(selected);
                refreshTables();
            }
        });

        projectActionPanel.add(new JButton("Refresh Data") {
            {
                addActionListener(ev -> refreshTables());
            }
        });
        projectActionPanel.add(advanceStageBtn);
        projectActionPanel.add(deleteProjBtn);
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

        JPanel clientActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteClientBtn = new JButton("Delete Client");
        deleteClientBtn.setForeground(Color.RED);
        deleteClientBtn.addActionListener(e -> {
            int selected = clientTable.getSelectedRow();
            if (selected != -1) {
                company.getClients().remove(selected);
                refreshTables();
            }
        });
        clientActionPanel.add(deleteClientBtn);
        clientPanel.add(clientActionPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Clients", clientPanel);

        // 3. Employees Tab
        JPanel empPanel = new JPanel(new BorderLayout(10, 10));
        empPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] empCols = { "Employee ID", "Name", "Designation", "Assigned Projects" };
        employeeModel = new DefaultTableModel(empCols, 0);
        employeeTable = new JTable(employeeModel);
        employeeTable.setRowHeight(25);
        empPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel empActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton assignEmpBtn = new JButton("+ Assign to Project");
        assignEmpBtn.addActionListener(e -> {
            int selected = employeeTable.getSelectedRow();
            if (selected != -1) {
                Employee emp = company.getEmployees().get(selected);
                if (company.getProjects().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No projects exist yet! Register a project first.");
                    return;
                }
                JComboBox<String> projBox = new JComboBox<>();
                for (Project p : company.getProjects())
                    projBox.addItem(p.getName());

                JTextField roleField = new JTextField();
                Object[] message = { "Select Target Project:", projBox, "Assign Role (e.g., Lead Dev):", roleField };

                int option = JOptionPane.showConfirmDialog(this, message, "Assigning " + emp.getName(),
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    Project selectedProj = company.getProjects().get(projBox.getSelectedIndex());
                    selectedProj.assign_employee(new Assignment("A" + (selectedProj.getAssignments().size() + 1),
                            roleField.getText(), new Date(), emp));
                    refreshTables();
                    JOptionPane.showMessageDialog(this, "Assignment created successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an Employee from the table to assign them.");
            }
        });

        JButton deleteEmpBtn = new JButton("Fire Employee (Delete)");
        deleteEmpBtn.setForeground(Color.RED);
        deleteEmpBtn.addActionListener(e -> {
            int selected = employeeTable.getSelectedRow();
            if (selected != -1) {
                company.getEmployees().remove(selected);
                refreshTables();
            }
        });

        empActionPanel.add(assignEmpBtn);
        empActionPanel.add(deleteEmpBtn);
        empPanel.add(empActionPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Employees", empPanel);

        // 4. Quick Action Tab
        JPanel actionPanelContainer = new JPanel(new BorderLayout());
        JPanel actionPanel = new JPanel(new GridLayout(12, 4, 15, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Create Project Section
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
        actionPanel.add(new JLabel(""));
        actionPanel.add(new JLabel(""));

        // Create Client Section
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

        // Hire Employee Section
        JPanel empActionPanel2 = new JPanel(new GridLayout(4, 2, 10, 10));
        empActionPanel2.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JLabel eTitle = new JLabel("--- Hire New Employee ---");
        eTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        empActionPanel2.add(eTitle);
        empActionPanel2.add(new JLabel(""));
        empActionPanel2.add(new JLabel("Employee Name:"));
        JTextField eNameField = new JTextField();
        empActionPanel2.add(eNameField);
        empActionPanel2.add(new JLabel("Designation:"));
        JTextField eDesField = new JTextField();
        empActionPanel2.add(eDesField);
        JButton addEmpBtn = new JButton("Hire Employee");
        addEmpBtn.addActionListener(e -> {
            if (!eNameField.getText().isEmpty()) {
                company.hire_employee(new Employee("EMP" + (company.getEmployees().size() + 1), eNameField.getText(),
                        eDesField.getText()));
                refreshTables();
                JOptionPane.showMessageDialog(this, "Employee Hired Successfully!");
            }
        });
        empActionPanel2.add(new JLabel(""));
        empActionPanel2.add(addEmpBtn);

        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.add(actionPanel, BorderLayout.NORTH);
        combinedPanel.add(empActionPanel2, BorderLayout.CENTER);
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
            // Dynamically calculate how many projects this employee is actively assigned to
            int activeProjects = 0;
            for (Project p : company.getProjects()) {
                for (Assignment a : p.getAssignments()) {
                    if (a.getEmployee() != null && a.getEmployee().getEmployee_id().equals(e.getEmployee_id())) {
                        activeProjects++;
                    }
                }
            }
            employeeModel.addRow(new Object[] { e.getEmployee_id(), e.getName(), e.getDesignation(),
                    activeProjects + " Active Projects" });
        }
    }

    public static void main(String[] args) {
        Company comp = new Company("Global Tech");

        Client c1 = new Client("C001", "Bob Smith", "bob@example.com", "Acme LLC");
        comp.onboard_client(c1);

        Project p1 = new WebDevelopmentProject("PRJ001", "Acme Website Rewrite", 25000, new Date());
        p1.advance_stage();
        p1.advance_stage();
        comp.register_project(p1);

        Employee e1 = new Employee("E001", "Alice Johnson", "Senior Designer");
        comp.hire_employee(e1);

        // Link them with an Assignment
        p1.assign_employee(new Assignment("A1", "Lead Designer", new Date(), e1));

        SwingUtilities.invokeLater(() -> {
            new Dashboard(comp).setVisible(true);
        });
    }
}
