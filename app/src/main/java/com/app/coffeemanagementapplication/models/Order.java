package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Orders",
        foreignKeys = {
                @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "userId"),
                @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "staffId"),
                @ForeignKey(entity = Discount.class, parentColumns = "id", childColumns = "discountId"),
                @ForeignKey(entity = ShippingAddress.class, parentColumns = "id", childColumns = "deliveryAddressId")
        }
)
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private Integer staffId;
    private Integer discountId;
    private String orderDate;
    private double totalAmount;
    private String paymentMethod; // 'CASH', 'QR', 'E_WALLET'
    private String orderStatus; // 'PENDING', 'PREPARING', 'COMPLETED', 'DELIVERED'
    private String note;
    private Integer deliveryAddressId;

    public Order() {
    }

    public Order(int id, int userId, Integer staffId, Integer discountId, String orderDate, double totalAmount, String paymentMethod, String orderStatus, String note, Integer deliveryAddressId) {
        this.id = id;
        this.userId = userId;
        this.staffId = staffId;
        this.discountId = discountId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.note = note;
        this.deliveryAddressId = deliveryAddressId;
    }

    public Order(int userId, Integer staffId, Integer discountId, String orderDate, double totalAmount, String paymentMethod, String orderStatus, String note, Integer deliveryAddressId) {
        this.userId = userId;
        this.staffId = staffId;
        this.discountId = discountId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.note = note;
        this.deliveryAddressId = deliveryAddressId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Integer deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }
}