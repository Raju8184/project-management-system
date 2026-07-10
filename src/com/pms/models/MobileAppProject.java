package com.pms.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MobileAppProject extends Project {
    private List<String> platforms;

    public MobileAppProject(String project_id, String name, double budget, Date deadline, List<String> platforms) {
        super(project_id, name, budget, deadline);
        this.platforms = platforms;
    }

    @Override
    public double calculate_billing() {
        return this.budget * 1.1; // 10% extra for mobile deployment
    }

    @Override
    public List<String> get_deliverables() {
        List<String> deliverables = new ArrayList<>();
        deliverables.add("APK/IPA Files");
        deliverables.add("Source Code");
        deliverables.add("App Store Assets");
        return deliverables;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }
}
