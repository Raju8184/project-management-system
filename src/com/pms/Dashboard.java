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

    public Dashboard(Company company) {
        this.company = company;
        setTitle("Project Management System Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Core Layout
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Projects Tab
        JPanel projectPanel = new JPanel(new BorderLayout());
        String[] projCols = { "ID", "Name", "Type", "Budget", "Stage" };
        projectModel = new DefaultTableModel(projCols, 0);
        projectTable = new JTable(projectModel);
        projectPanel.add(new JScrollPane(projectTable), BorderLayout.CENTER);
        JButton refreshProjBtn = new JButton("Refresh Data");
        refreshProjBtn.addActionListener(e -> refreshTables());
        projectPanel.add(refreshProjBtn, BorderLayout.SOUTH);
        tabbedPane.add("Projects", projectPanel);

        // 2. Clients Tab
        JPanel clientPanel = new JPanel(new BorderLayout());
        String[] clientCols = { "ID", "Name", "Company", "Email" };
        clientModel = new DefaultTableModel(clientCols, 0);
        clientTable = new JTable(clientModel);
        clientPanel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        tabbedPane.add("Clients", clientPanel);

        // 3. Quick Action Tab
        JPanel actionPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        actionPanel.add(new JLabel("New Project Name:"));
        JTextField pNameField = new JTextField();
        actionPanel.add(pNameField);

        actionPanel.add(new JLabel("Budget:"));
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
                            java.util.Arrays.asList("iOS", "Android"));
                } else {
                    p = new DataScienceProject("PRJ" + (company.getProjects().size() + 1), name, budget, new Date(),
                            10.0f);
                }
                company.register_project(p);
                refreshTables();
                JOptionPane.showMessageDialog(this, "Project Added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Entry");
            }
        });

        actionPanel.add(new JLabel("")); // spacer
        actionPanel.add(addProjectBtn);

        tabbedPane.add("Quick Create", actionPanel);

        // Add tabs
        add(tabbedPane);

        // Load data initially
        refreshTables();
    }

    private void refreshTables() {
        projectModel.setRowCount(0);
        for (Project p : company.getProjects()) {
            projectModel.addRow(new Object[] {
                    p.getProject_id(),
                    p.getName(),
                    p.getClass().getSimpleName(),
                    p.getBudget(),
                    p.getTracker().current_stage()
            });
        }

        clientModel.setRowCount(0);
        for (Client c : company.getClients()) {
            clientModel.addRow(new Object[] {
                    c.getClient_id(),
                    c.getName(),
                    c.getCompany_name(),
                    c.getContact_email()
            });
        }
    }

    public static void main(String[] args) {
        Company comp = new Company("Global Tech");

        // Populate some sample data for visual test
        Client c = new Client("C001", "Bob Smith", "bob@example.com", "Acme");
        comp.onboard_client(c);
        Project p = new WebDevelopmentProject("PRJ0", "Acme Website", 25000, new Date());
        c.add_project(p);
        comp.register_project(p);

        SwingUtilities.invokeLater(() -> {
            new Dashboard(comp).setVisible(true);
        });
    }
}
