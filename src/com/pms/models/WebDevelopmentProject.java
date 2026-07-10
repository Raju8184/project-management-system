package com.pms.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebDevelopmentProject extends Project {

    public WebDevelopmentProject(String project_id, String name, double budget, Date deadline) {
        super(project_id, name, budget, deadline);
    }

    @Override
    public double calculate_billing() {
        return this.budget;
    }

    @Override
    public List<String> get_deliverables() {
        List<String> deliverables = new ArrayList<>();
        deliverables.add("Source Code");
        deliverables.add("Deployment Scripts");
        deliverables.add("API Documentation");
        return deliverables;
    }
}
