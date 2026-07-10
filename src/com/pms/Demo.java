package com.pms;

import com.pms.models.*;
import java.util.*;

public class Demo {
    public static void main(String[] args) {
        System.out.println("=== Starting Programmatic Hardcoded Demo ===\n");

        // 1. Setup Company
        Company company = new Company("Global Tech Corp");

        // 2. Hire Employees
        Employee pm = new Employee("E001", "Alice Smith", "Project Manager");
        pm.getSkills().addAll(Arrays.asList("Agile", "Scrum", "Communication"));
        company.hire_employee(pm);

        Employee dev = new Employee("E002", "Bob Jones", "Senior Developer");
        dev.getSkills().addAll(Arrays.asList("Java", "React", "AWS"));
        company.hire_employee(dev);

        Employee dataScientist = new Employee("E003", "Charlie Brown", "Data Scientist");
        dataScientist.getSkills().addAll(Arrays.asList("Python", "TensorFlow", "SQL"));
        company.hire_employee(dataScientist);

        // 3. Onboard Clients
        Client client1 = new Client("C001", "Acme Retail", "contact@acme.com", "Acme Inc");
        Client client2 = new Client("C002", "Stark Industries", "tony@stark.com", "Stark Ind.");
        company.onboard_client(client1);
        company.onboard_client(client2);

        // 4. Create and Register Projects (Testing polymorphic behaviors)
        WebDevelopmentProject webProj = new WebDevelopmentProject("P001", "Acme E-Commerce Portal", 50000.0,
                new Date());
        client1.add_project(webProj);
        company.register_project(webProj);

        MobileAppProject mobProj = new MobileAppProject("P002", "Stark Health Tracker", 80000.0, new Date(),
                Arrays.asList("iOS", "Android"));
        client2.add_project(mobProj);
        company.register_project(mobProj);

        DataScienceProject dsProj = new DataScienceProject("P003", "Sales Predictor AI", 120000.0, new Date(), 500.5f);
        client1.add_project(dsProj);
        company.register_project(dsProj);

        // 5. Build requirements and assignments
        Requirement req1 = new Requirement("R001", "Must support 100K concurrent users", "HIGH");
        webProj.add_requirement(req1);
        req1.mark_fulfilled();

        Assignment asg1 = new Assignment("A001", "Lead Developer", new Date());
        webProj.assign_employee(asg1);

        // 6. Test Tracker / Stages
        System.out.println("Advancing stages for " + webProj.getName() + "...");
        System.out.println("Initial Stage: " + webProj.getTracker().current_stage());
        webProj.advance_stage();
        System.out.println("New Stage: " + webProj.getTracker().current_stage());

        // 7. Feedback and Billing
        Feedback fb1 = new Feedback("F001", ProjectStage.PLANNING, "Great initial meeting.", 5);
        webProj.add_feedback(fb1);

        Billing webBilling = new Billing("B001", webProj.calculate_billing());
        webProj.setBilling(webBilling);
        webBilling.record_payment(20000.0);

        Billing mobBilling = new Billing("B002", mobProj.calculate_billing());
        mobProj.setBilling(mobBilling);

        Billing dsBilling = new Billing("B003", dsProj.calculate_billing());
        dsProj.setBilling(dsBilling);

        System.out.println("\n=== Project Billing Checks ===");
        System.out.println(webProj.getName() + " Billing (Fixed): " + webProj.calculate_billing());
        System.out.println(mobProj.getName() + " Billing (1.1x extra): " + mobProj.calculate_billing());
        System.out.println(dsProj.getName() + " Billing (Extra 5.0 per GB): " + dsProj.calculate_billing());
        System.out.println(webProj.getName() + " Invoice: " + webBilling.generate_invoice());

        // 8. Output company summary
        System.out.println();
        company.company_summary();
        System.out.println("\n=== Demo Complete ===");
    }
}
