package com.pms;

import com.pms.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setTitle("Project Management System Dashboard - Premium");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 244, 248));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        // Header Title
        JLabel title = new JLabel("Company Dashboard: " + company.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Core Layout
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        // 1. Projects Tab
        JPanel projectPanel = createStyledPanel();
        String[] projCols = { "Project ID", "Name", "Type", "Budget", "Current Stage" };
        projectModel = new DefaultTableModel(projCols, 0);
        projectTable = createStyledTable(projectModel);
        projectPanel.add(new JScrollPane(projectTable), BorderLayout.CENTER);
        JButton refreshProjBtn = createStyledButton("↻ Refresh All Data");
        refreshProjBtn.addActionListener(e -> refreshTables());
        projectPanel.add(refreshProjBtn, BorderLayout.SOUTH);
        tabbedPane.addTab("🚀 Projects", projectPanel);

        // 2. Clients Tab
        JPanel clientPanel = createStyledPanel();
        String[] clientCols = { "Client ID", "Full Name", "Organization", "Email" };
        clientModel = new DefaultTableModel(clientCols, 0);
        clientTable = createStyledTable(clientModel);
        clientPanel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        tabbedPane.addTab("🤝 Clients", clientPanel);

        // 3. Employees Tab (NEW)
        JPanel empPanel = createStyledPanel();
        String[] empCols = { "Employee ID", "Name", "Designation", "Current Projects" };
        employeeModel = new DefaultTableModel(empCols, 0);
        employeeTable = createStyledTable(employeeModel);
        empPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        tabbedPane.addTab("👥 Employees", empPanel);

        // 4. Quick Action Tab
        JPanel actionPanelContainer = new JPanel(new BorderLayout());
        actionPanelContainer.setBackground(Color.WHITE);

        JPanel actionPanel = new JPanel(new GridLayout(9, 2, 10, 15));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 50, 80));

        // Create Project Section
        JLabel pTitle = new JLabel("--- Create New Project ---");
        pTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pTitle.setForeground(new Color(41, 128, 185));
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

        JButton addProjectBtn = createStyledButton("Create Project");
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
                JOptionPane.showMessageDialog(this, "✅ Project Automatically Registered!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid Entry. Please check numbers.");
            }
        });
        actionPanel.add(new JLabel(""));
        actionPanel.add(addProjectBtn);

        // Create Employee Section
        JLabel eTitle = new JLabel("--- Hire New Employee ---");
        eTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        eTitle.setForeground(new Color(39, 174, 96));
        actionPanel.add(eTitle);
        actionPanel.add(new JLabel(""));

        actionPanel.add(new JLabel("Employee Name:"));
        JTextField eNameField = new JTextField();
        actionPanel.add(eNameField);

        actionPanel.add(new JLabel("Designation:"));
        JTextField eDesField = new JTextField();
        actionPanel.add(eDesField);

        JButton addEmpBtn = createStyledButton("Hire Employee", new Color(39, 174, 96));
        addEmpBtn.addActionListener(e -> {
            String eName = eNameField.getText();
            String eDes = eDesField.getText();
            if (!eName.isEmpty() && !eDes.isEmpty()) {
                Employee emp = new Employee("EMP" + (company.getEmployees().size() + 1), eName, eDes);
                company.hire_employee(emp);
                refreshTables();
                JOptionPane.showMessageDialog(this, "✅ Employee Hired Successfully!");
            }
        });
        actionPanel.add(new JLabel(""));
        actionPanel.add(addEmpBtn);

        actionPanelContainer.add(actionPanel, BorderLayout.CENTER);
        tabbedPane.addTab("⚡ Quick Management", actionPanelContainer);

        // Add tabs
        add(tabbedPane, BorderLayout.CENTER);

        // Load data initially
        refreshTables();
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(236, 240, 241));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        return table;
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(41, 128, 185));
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
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

        // Populate some sample data for visual test
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
