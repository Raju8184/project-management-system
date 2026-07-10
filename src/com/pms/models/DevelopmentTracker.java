package com.pms.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevelopmentTracker {
    private int current_index;
    private List<Map<String, String>> history;

    public DevelopmentTracker() {
        this.current_index = 0;
        this.history = new ArrayList<>();
    }

    public void advance_stage() {
        if (current_index < ProjectStage.values().length - 1) {
            current_index++;
            Map<String, String> event = new HashMap<>();
            event.put("stage", current_stage().name());
            event.put("status", "Started");
            history.add(event);
        }
    }

    public ProjectStage current_stage() {
        return ProjectStage.values()[current_index];
    }

    public int getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(int current_index) {
        this.current_index = current_index;
    }

    public List<Map<String, String>> getHistory() {
        return history;
    }
}
