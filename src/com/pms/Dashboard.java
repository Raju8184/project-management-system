package com.pms;

import com.pms.models.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Date;

public class Dashboard extends JFrame {

    // ── Colour palette ──────────────────────────────────────────────────────
    private static final Color BG_DARK = new Color(24, 26, 37);
    private static final Color BG_PANEL = new Color(34, 37, 54);
    private static final Color BG_CARD = new Color(44, 48, 69);
    private static final Color ACCENT = new Color(99, 179, 237); // sky-blue
    private static final Color ACCENT2 = new Color(72, 187, 120); // green
    private static final Color ACCENT3 = new Color(246, 173, 85); // amber
    private static final Color DANGER = new Color(252, 129, 129); // red
    private static final Color TEXT_MAIN = new Color(237, 242, 247);
    private static final Color TEXT_MUTED = new Color(150, 160, 185);
    private static final Color ROW_ALT = new Color(50, 55, 78);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 13);

    private Company company;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private DefaultTableModel projectModel, clientModel, employeeModel;

    // ── Constructor ─────────────────────────────────────────────────────────
    public Dashboard(Company company) {
        this.company = company;
        setTitle("PMS — Project Management System");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        refreshTables();
    }

    // ── Sidebar ─────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_PANEL);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo / App name
        JLabel logo = new JLabel("PMS", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.BOLD, 32));
        logo.setForeground(ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Project Manager", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(sub);
        sidebar.add(Box.createVerticalStrut(30));

        JSeparator sep = new JSeparator();
        sep.setForeground(BG_CARD);
        sep.setMaximumSize(new Dimension(999, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(20));

        // Nav buttons
        String[][] navItems = {
                { "🚀  Projects", "PROJECTS" },
                { "🤝  Clients", "CLIENTS" },
                { "👥  Employees", "EMPLOYEES" },
                { "📊  Assignments", "ASSIGNMENTS" },
                { "➕  Add New", "MANAGEMENT" },
        };

        ButtonGroup navGroup = new ButtonGroup();
        for (String[] item : navItems) {
            JToggleButton btn = navButton(item[0], item[1]);
            navGroup.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(8));
            if (item[1].equals("PROJECTS"))
                btn.setSelected(true);
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel info = new JLabel("<html><center>" + company.getName() + "</center></html>");
        info.setFont(new Font("SansSerif", Font.BOLD, 12));
        info.setForeground(TEXT_MUTED);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(info);

        return sidebar;
    }

    private JToggleButton navButton(String label, String card) {
        JToggleButton btn = new JToggleButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    setForeground(BG_DARK);
                } else {
                    g2.setColor(getModel().isRollover() ? BG_CARD : new Color(0, 0, 0, 0));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    setForeground(TEXT_MAIN);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_LABEL);
        btn.setMaximumSize(new Dimension(999, 42));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        btn.addActionListener(e -> cardLayout.show(contentPanel, card));
        return btn;
    }

    // ── Content area ─────────────────────────────────────────────────────────
    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_DARK);

        contentPanel.add(buildProjectsCard(), "PROJECTS");
        contentPanel.add(buildClientsCard(), "CLIENTS");
        contentPanel.add(buildEmployeesCard(), "EMPLOYEES");
        contentPanel.add(buildAssignmentsCard(), "ASSIGNMENTS");
        contentPanel.add(buildManagementCard(), "MANAGEMENT");

        cardLayout.show(contentPanel, "PROJECTS");
        return contentPanel;
    }

    // ── Projects card ────────────────────────────────────────────────────────
    private JPanel buildProjectsCard() {
        String[] cols = { "Project ID", "Name", "Type", "Budget", "Stage" };
        projectModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JPanel card = outerCard("🚀  Projects", ACCENT);

        JTable table = styledTable(projectModel, ACCENT);
        card.add(new JScrollPane(table) {
            {
                setBorder(null);
                getViewport().setBackground(BG_CARD);
            }
        }, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btns.setOpaque(false);

        JButton refresh = pill("↻ Refresh", ACCENT);
        JButton advance = pill("▶ Advance Stage", ACCENT2);
        JButton delete = pill("🗑 Delete", DANGER);

        refresh.addActionListener(e -> refreshTables());
        advance.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                toast("Select a project first.");
                return;
            }
            company.getProjects().get(sel).advance_stage();
            refreshTables();
        });
        delete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                toast("Select a project first.");
                return;
            }
            company.getProjects().remove(sel);
            refreshTables();
        });

        btns.add(refresh);
        btns.add(advance);
        btns.add(delete);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    // ── Clients card ─────────────────────────────────────────────────────────
    private JPanel buildClientsCard() {
        String[] cols = { "Client ID", "Name", "Organization", "Email" };
        clientModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JPanel card = outerCard("🤝  Clients", ACCENT2);
        JTable table = styledTable(clientModel, ACCENT2);
        card.add(new JScrollPane(table) {
            {
                setBorder(null);
                getViewport().setBackground(BG_CARD);
            }
        }, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btns.setOpaque(false);
        JButton delete = pill("🗑 Remove Client", DANGER);
        delete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                toast("Select a client first.");
                return;
            }
            company.getClients().remove(sel);
            refreshTables();
        });
        btns.add(delete);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    // ── Employees card ───────────────────────────────────────────────────────
    private JPanel buildEmployeesCard() {
        String[] cols = { "Employee ID", "Name", "Designation", "Active Projects" };
        employeeModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JPanel card = outerCard("👥  Employees", ACCENT3);
        JTable table = styledTable(employeeModel, ACCENT3);
        card.add(new JScrollPane(table) {
            {
                setBorder(null);
                getViewport().setBackground(BG_CARD);
            }
        }, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btns.setOpaque(false);

        JButton addSkill = pill("+ Add Skill", ACCENT);
        JButton assign = pill("📋 Assign to Project", ACCENT3);
        JButton fire = pill("🗑 Remove Employee", DANGER);

        addSkill.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                toast("Select an employee first.");
                return;
            }
            Employee emp = company.getEmployees().get(sel);
            String skill = JOptionPane.showInputDialog(this, "Enter skill for " + emp.getName() + ":");
            if (skill != null && !skill.isBlank()) {
                emp.getSkills().add(skill.trim());
                toast("Skill '" + skill.trim() + "' added to " + emp.getName());
            }
        });

        assign.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                toast("Select an employee first.");
                return;
            }
            if (company.getProjects().isEmpty()) {
                toast("No projects registered yet!");
                return;
            }
            Employee emp = company.getEmployees().get(sel);

            JComboBox<String> projBox = new JComboBox<>();
            company.getProjects().forEach(p -> projBox.addItem(p.getName()));
            JTextField roleField = new JTextField(15);

            JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
            form.setBackground(BG_PANEL);
            form.add(darkLabel("Employee: " + emp.getName()));
            form.add(darkLabel("Select Project:"));
            form.add(projBox);

            JPanel roleRow = new JPanel(new BorderLayout(5, 0));
            roleRow.setOpaque(false);
            roleRow.add(darkLabel("Role:"), BorderLayout.WEST);
            styleField(roleField);
            roleRow.add(roleField, BorderLayout.CENTER);
            form.add(roleRow);

            int ok = JOptionPane.showConfirmDialog(this, form,
                    "Assign " + emp.getName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok == JOptionPane.OK_OPTION) {
                Project p = company.getProjects().get(projBox.getSelectedIndex());
                p.assign_employee(new Assignment(
                        "A" + (p.getAssignments().size() + 1), roleField.getText(), new Date(), emp));
                refreshTables();
                toast(emp.getName() + " assigned to " + p.getName());
            }
        });

        fire.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) {
                toast("Select an employee first.");
                return;
            }
            company.getEmployees().remove(sel);
            refreshTables();
        });

        btns.add(addSkill);
        btns.add(assign);
        btns.add(fire);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    // ── Assignments card ─────────────────────────────────────────────────────
    private JPanel buildAssignmentsCard() {
        String[] cols = { "Employee Name", "Role", "Project", "Assigned On" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JPanel card = outerCard("📊  Assignments (Employee → Project)", ACCENT);
        JTable table = styledTable(model, ACCENT);
        card.add(new JScrollPane(table) {
            {
                setBorder(null);
                getViewport().setBackground(BG_CARD);
            }
        }, BorderLayout.CENTER);

        // Populate from live data
        Runnable populate = () -> {
            model.setRowCount(0);
            for (Project p : company.getProjects()) {
                for (Assignment a : p.getAssignments()) {
                    String empName = a.getEmployee() != null ? a.getEmployee().getName() : "(unknown)";
                    String skills = a.getEmployee() != null && !a.getEmployee().getSkills().isEmpty()
                            ? String.join(", ", a.getEmployee().getSkills())
                            : "";
                    model.addRow(new Object[] {
                            empName + (skills.isEmpty() ? "" : "  [" + skills + "]"),
                            a.getRole(),
                            p.getName() + "  (" + p.getTracker().current_stage() + ")",
                            a.getAssigned_date()
                    });
                }
            }
        };
        populate.run();

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btns.setOpaque(false);
        JButton refresh = pill("↻ Refresh", ACCENT);
        refresh.addActionListener(e -> populate.run());
        btns.add(refresh);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    // ── Management / Add New card ────────────────────────────────────────────
    private JPanel buildManagementCard() {
        JPanel outer = new JPanel(new GridLayout(1, 3, 15, 0));
        outer.setBackground(BG_DARK);
        outer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        outer.add(buildProjectForm());
        outer.add(buildClientForm());
        outer.add(buildEmployeeForm());

        return outer;
    }

    private JPanel buildProjectForm() {
        JPanel form = formCard("🚀 New Project", ACCENT);

        JTextField nameF = field(form, "Project Name");
        JTextField budgetF = field(form, "Budget ($)");
        JComboBox<String> typeBox = new JComboBox<>(new String[] { "Web", "Mobile", "Data Science" });
        styleCombo(typeBox);
        form.add(darkLabel("Project Type:"));
        form.add(typeBox);
        form.add(Box.createVerticalStrut(10));

        JButton btn = pill("Register Project", ACCENT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                double budget = Double.parseDouble(budgetF.getText().trim());
                String type = (String) typeBox.getSelectedItem();
                Project p;
                String id = "PRJ" + (company.getProjects().size() + 1);
                if ("Web".equals(type))
                    p = new WebDevelopmentProject(id, name, budget, new Date());
                else if ("Mobile".equals(type))
                    p = new MobileAppProject(id, name, budget, new Date(), Arrays.asList("iOS", "Android"));
                else
                    p = new DataScienceProject(id, name, budget, new Date(), 5.0f);
                company.register_project(p);
                refreshTables();
                nameF.setText("");
                budgetF.setText("");
                toast("Project registered!");
            } catch (Exception ex) {
                toast("Check your input values.");
            }
        });
        form.add(btn);
        return wrap(form);
    }

    private JPanel buildClientForm() {
        JPanel form = formCard("🤝 New Client", ACCENT2);

        JTextField nameF = field(form, "Full Name");
        JTextField orgF = field(form, "Organization");
        JTextField emailF = field(form, "Email Address");
        form.add(Box.createVerticalStrut(10));

        JButton btn = pill("Onboard Client", ACCENT2);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            if (nameF.getText().isBlank()) {
                toast("Name is required.");
                return;
            }
            String id = "C" + String.format("%03d", company.getClients().size() + 1);
            company.onboard_client(new Client(id, nameF.getText(), emailF.getText(), orgF.getText()));
            refreshTables();
            nameF.setText("");
            orgF.setText("");
            emailF.setText("");
            toast("Client onboarded!");
        });
        form.add(btn);
        return wrap(form);
    }

    private JPanel buildEmployeeForm() {
        JPanel form = formCard("👤 New Employee", ACCENT3);

        JTextField nameF = field(form, "Full Name");
        JTextField desF = field(form, "Designation");
        form.add(Box.createVerticalStrut(10));

        JButton btn = pill("Hire Employee", ACCENT3);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            if (nameF.getText().isBlank()) {
                toast("Name is required.");
                return;
            }
            String id = "EMP" + String.format("%03d", company.getEmployees().size() + 1);
            company.hire_employee(new Employee(id, nameF.getText(), desF.getText()));
            refreshTables();
            nameF.setText("");
            desF.setText("");
            toast("Employee hired!");
        });
        form.add(btn);
        return wrap(form);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private JPanel outerCard(String heading, Color accent) {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lbl = new JLabel(heading);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(accent);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }

    private JTable styledTable(DefaultTableModel model, Color accent) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? BG_CARD : ROW_ALT);
                c.setForeground(isRowSelected(row) ? BG_DARK : TEXT_MAIN);
                if (isRowSelected(row))
                    c.setBackground(accent);
                return c;
            }
        };
        table.setFont(FONT_BODY);
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setGridColor(BG_PANEL);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_MAIN);
        table.setSelectionBackground(accent);
        table.setSelectionForeground(BG_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL);
        header.setBackground(BG_PANEL);
        header.setForeground(accent);
        header.setBorder(null);
        header.setReorderingAllowed(false);
        return table;
    }

    private JButton pill(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_LABEL);
        btn.setForeground(BG_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        return btn;
    }

    private JPanel formCard(String title, Color accent) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(accent);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(18));
        return p;
    }

    private JTextField field(JPanel form, String placeholder) {
        JLabel lbl = darkLabel(placeholder + ":");
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));
        JTextField f = new JTextField();
        styleField(f);
        form.add(f);
        form.add(Box.createVerticalStrut(12));
        return f;
    }

    private void styleField(JTextField f) {
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_MAIN);
        f.setBackground(BG_CARD);
        f.setCaretColor(TEXT_MAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BG_CARD.brighter(), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleCombo(JComboBox<String> box) {
        box.setFont(FONT_BODY);
        box.setBackground(BG_CARD);
        box.setForeground(TEXT_MAIN);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel darkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel wrap(JPanel inner) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG_DARK);
        wrap.setBorder(BorderFactory.createEmptyBorder(20, 8, 20, 8));
        wrap.add(inner, BorderLayout.CENTER);
        return wrap;
    }

    private void toast(String msg) {
        JOptionPane.showMessageDialog(this, msg, "PMS", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Data refresh ─────────────────────────────────────────────────────────
    private void refreshTables() {
        projectModel.setRowCount(0);
        for (Project p : company.getProjects()) {
            projectModel.addRow(new Object[] {
                    p.getProject_id(), p.getName(), p.getClass().getSimpleName(),
                    "$" + String.format("%.0f", p.getBudget()), p.getTracker().current_stage()
            });
        }

        clientModel.setRowCount(0);
        for (Client c : company.getClients())
            clientModel
                    .addRow(new Object[] { c.getClient_id(), c.getName(), c.getCompany_name(), c.getContact_email() });

        employeeModel.setRowCount(0);
        for (Employee e : company.getEmployees()) {
            int active = 0;
            for (Project p : company.getProjects())
                for (Assignment a : p.getAssignments())
                    if (a.getEmployee() != null && a.getEmployee().getEmployee_id().equals(e.getEmployee_id()))
                        active++;
            employeeModel.addRow(
                    new Object[] { e.getEmployee_id(), e.getName(), e.getDesignation(), active + " project(s)" });
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        Company comp = new Company("Global Tech Corp");

        Client c1 = new Client("C001", "Acme Retail", "contact@acme.com", "Acme Inc.");
        Client c2 = new Client("C002", "Stark Industries", "tony@stark.com", "Stark Ind.");
        Client c3 = new Client("C003", "Wayne Enterprises", "wayne@forever.com", "Wayne Corp");
        comp.onboard_client(c1);
        comp.onboard_client(c2);
        comp.onboard_client(c3);

        Project p1 = new WebDevelopmentProject("PRJ001", "Acme E-Commerce Portal", 50000, new Date());
        Project p2 = new MobileAppProject("PRJ002", "Stark Health Tracker", 80000, new Date(),
                Arrays.asList("iOS", "Android"));
        Project p3 = new DataScienceProject("PRJ003", "Crime Predictor AI", 120000, new Date(), 500.5f);
        Project p4 = new WebDevelopmentProject("PRJ004", "Employee Intranet Portal", 20000, new Date());
        p1.advance_stage();
        p1.advance_stage();
        p2.advance_stage();
        comp.register_project(p1);
        comp.register_project(p2);
        comp.register_project(p3);
        comp.register_project(p4);
        c1.add_project(p1);
        c2.add_project(p2);

        Employee e1 = new Employee("E001", "Alice Johnson", "Senior Designer");
        Employee e2 = new Employee("E002", "Mark Spencer", "Backend Engineer");
        Employee e3 = new Employee("E003", "Sarah Connor", "Data Scientist");
        Employee e4 = new Employee("E004", "James Wilson", "Mobile Developer");
        comp.hire_employee(e1);
        comp.hire_employee(e2);
        comp.hire_employee(e3);
        comp.hire_employee(e4);

        p1.assign_employee(new Assignment("A1", "Lead Designer", new Date(), e1));
        p2.assign_employee(new Assignment("A2", "App Dev", new Date(), e4));
        p3.assign_employee(new Assignment("A3", "ML Engineer", new Date(), e3));

        SwingUtilities.invokeLater(() -> new Dashboard(comp).setVisible(true));
    }
}
