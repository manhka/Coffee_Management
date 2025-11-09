package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Discounts")
public class Discount {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    private String code;
    private String description;
    private double condition; // đơn bao nhiêu tiền thì được áp dụng mã này
    private double value; // giảm bao nhiêu tiền cho đơn này ví dụ giảm 10k cho đơn 100k
    private String hint;
    private String startDate;

    private String endDate;
    private boolean isActive;
    private boolean isDefault;

    public Discount() {
    }



    public Discount(int id, String name, String code, String description, double condition, double value, String hint, String startDate, String endDate, boolean isActive, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.condition = condition;
        this.value = value;
        this.hint = hint;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.isDefault = isDefault;
    }



    public Discount(String name, String code, String description, double condition, double value, String hint, String startDate, String endDate, boolean isActive, boolean isDefault) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.condition = condition;
        this.value = value;
        this.hint = hint;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getCondition() {
        return condition;
    }

    public void setCondition(double condition) {
        this.condition = condition;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}