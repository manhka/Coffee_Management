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

    private Integer orderId;
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
    private boolean isSelected = false;

    // 🔹 Constructors
    public OrderItem() {}

    public OrderItem(int id, Integer orderId, int productId, int quantity, double unitPrice, double subtotal, String temperature, String size, String sugar, String ice, String note, boolean isSelected) {
        this.id = id;
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
        this.isSelected = isSelected;
    }

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

    public OrderItem(String note, String ice, String sugar, String size, String temperature, double subtotal, double unitPrice, int quantity, int productId, int id) {
        this.note = note;
        this.ice = ice;
        this.sugar = sugar;
        this.size = size;
        this.temperature = temperature;
        this.subtotal = subtotal;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.productId = productId;
        this.id = id;
    }

    // 🔹 Getter & Setter
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
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
