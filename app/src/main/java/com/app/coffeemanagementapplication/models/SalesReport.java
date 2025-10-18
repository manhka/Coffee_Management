package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "SalesReports",
        foreignKeys = @ForeignKey(
                entity = Users.class,
                parentColumns = "id",
                childColumns = "generatedBy"
        )
)
public class SalesReport {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String reportDate;
    public int totalOrders;
    public double totalRevenue;
    public double totalDiscount;
    public int generatedBy;
    public String lastUpdated;

    public SalesReport() {
    }

    public SalesReport(int id, String reportDate, int totalOrders, double totalRevenue, double totalDiscount, int generatedBy, String lastUpdated) {
        this.id = id;
        this.reportDate = reportDate;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.totalDiscount = totalDiscount;
        this.generatedBy = generatedBy;
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public int getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(int generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}