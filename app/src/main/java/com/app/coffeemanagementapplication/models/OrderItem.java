package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "OrderItems",
        foreignKeys = {
                @ForeignKey(entity = Order.class, parentColumns = "id", childColumns = "orderId"),
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId")
        }
)
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public int productId;
    public int quantity;
    public double unitPrice;
    public double subtotal;
    public String customizationNote;

    public OrderItem() {
    }

    public OrderItem(int id, int orderId, int productId, int quantity, double unitPrice, double subtotal, String customizationNote) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.customizationNote = customizationNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getCustomizationNote() {
        return customizationNote;
    }

    public void setCustomizationNote(String customizationNote) {
        this.customizationNote = customizationNote;
    }
}