package com.educorreia.fitnesstracker.models;

public class RegisterItem {
    private String type;
    private double value;
    private String createdAt;

    public RegisterItem(String type, double value, String createdAt) {
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
