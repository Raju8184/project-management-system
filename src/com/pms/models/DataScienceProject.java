package com.pms.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataScienceProject extends Project {
    private float dataset_size_gb;

    public DataScienceProject(String project_id, String name, double budget, Date deadline, float dataset_size_gb) {
        super(project_id, name, budget, deadline);
        this.dataset_size_gb = dataset_size_gb;
    }

    @Override
    public double calculate_billing() {
        return this.budget + (dataset_size_gb * 5.0); // Cost per GB of data
    }

    @Override
    public List<String> get_deliverables() {
        List<String> deliverables = new ArrayList<>();
        deliverables.add("Trained Model (.pkl/.h5)");
        deliverables.add("Jupyter Notebooks");
        deliverables.add("Data Analysis Report");
        return deliverables;
    }

    public float getDataset_size_gb() {
        return dataset_size_gb;
    }

    public void setDataset_size_gb(float dataset_size_gb) {
        this.dataset_size_gb = dataset_size_gb;
    }
}
