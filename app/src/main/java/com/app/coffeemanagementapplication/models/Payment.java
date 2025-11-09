package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Payments",
        foreignKeys = @ForeignKey(
                entity = Order.class,
                parentColumns = "id",
                childColumns = "orderId"
        )
)
public class Payment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Integer orderId;
    private String paymentName;
    private int imageUrl;
    private String paymentDescription;
    private boolean isDefault;

    public Payment() {
    }

    public Payment(int id, Integer orderId, String paymentName, int imageUrl, String paymentDescription, boolean isDefault) {
        this.id = id;
        this.orderId = orderId;
        this.paymentName = paymentName;
        this.imageUrl = imageUrl;
        this.paymentDescription = paymentDescription;
        this.isDefault = isDefault;
    }

    public Payment(Integer orderId, String paymentName, int imageUrl, String paymentDescription, boolean isDefault) {
        this.orderId = orderId;
        this.paymentName = paymentName;
        this.imageUrl = imageUrl;
        this.paymentDescription = paymentDescription;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
