package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Discounts")
public class Discount {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String code;
    public String description;
    public double value;
    public String startDate;
    public String endDate;
    public boolean isActive;

    public Discount() {
    }

    public Discount(int id, String code, String description, double value, String startDate, String endDate, boolean isActive) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}