package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "OrderItems",
        foreignKeys = {
                @ForeignKey(
                        entity = Order.class,
                        parentColumns = "id",
                        childColumns = "orderId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class OrderItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int orderId;
    private int productId;
    private int quantity;

    private double unitPrice;
    private double subtotal;

    private String temperature; // "Nóng" hoặc "Lạnh"
    private String size;        // "Nhỏ", "Vừa", "Lớn"
    private String sugar;       // "Bình Thường",  "Ít Đường"
    private String ice;         // "Bình Thường", "Ít Đá"

    // Ghi chú thêm của khách (nếu có)
    private String note;

    // 🔹 Constructors
    public OrderItem() {}

    public OrderItem(int orderId, int productId, int quantity,
                     double unitPrice, double subtotal,
                     String temperature, String size, String sugar, String ice, String note) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.temperature = temperature;
        this.size = size;
        this.sugar = sugar;
        this.ice = ice;
        this.note = note;
    }

    // 🔹 Getter & Setter
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

    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getSugar() {
        return sugar;
    }
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getIce() {
        return ice;
    }
    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
