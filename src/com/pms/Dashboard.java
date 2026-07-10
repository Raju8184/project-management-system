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

        // Remove ALL hardcoded colors so it uses native OS rendering correctly!
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

        // 1. Projects Tab (Added Advance Stage feature per requirements)
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

        JPanel actionPanel = new JPanel(new GridLayout(9, 2, 10, 15));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 50, 80));

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

        JButton addProjectBtn = new JButton("Create Project");
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
                JOptionPane.showMessageDialog(this, "Project Automatically Registered!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Entry. Please check numbers.");
            }
        });
        actionPanel.add(new JLabel(""));
        actionPanel.add(addProjectBtn);

        // Create Employee Section
        JLabel eTitle = new JLabel("--- Hire New Employee ---");
        eTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        actionPanel.add(eTitle);
        actionPanel.add(new JLabel(""));

        actionPanel.add(new JLabel("Employee Name:"));
        JTextField eNameField = new JTextField();
        actionPanel.add(eNameField);

        actionPanel.add(new JLabel("Designation:"));
        JTextField eDesField = new JTextField();
        actionPanel.add(eDesField);

        JButton addEmpBtn = new JButton("Hire Employee");
        addEmpBtn.addActionListener(e -> {
            String eName = eNameField.getText();
            String eDes = eDesField.getText();
            if (!eName.isEmpty() && !eDes.isEmpty()) {
                Employee emp = new Employee("EMP" + (company.getEmployees().size() + 1), eName, eDes);
                company.hire_employee(emp);
                refreshTables();
                JOptionPane.showMessageDialog(this, "Employee Hired Successfully!");
            }
        });
        actionPanel.add(new JLabel(""));
        actionPanel.add(addEmpBtn);

        actionPanelContainer.add(actionPanel, BorderLayout.CENTER);
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

        Client c = new Client("C001", "Bob Smith", "bob@example.com", "Acme LLC");
        comp.onboard_client(c);
        Project p = new WebDevelopmentProject("PRJ001", "Acme Website Rewrite", 25000, new Date());
        c.add_project(p);
        comp.register_project(p);
        comp.hire_employee(new Employee("E001", "Alice Johnson", "Senior Designer"));
        comp.hire_employee(new Employee("E002", "Mark Spencer", "Backend Engineer"));

        SwingUtilities.invokeLater(() -> {
            new Dashboard(comp).setVisible(true);
        });
    }
}
