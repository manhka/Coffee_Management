package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "ShippingAddress",
        foreignKeys = @ForeignKey(
                entity = Users.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class ShippingAddress {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String addressLine;
    private String city;
    private String district;
    private String ward;
    private String note;
    private boolean isDefault;
    private String fullName;
    private String phone;

    public ShippingAddress() {
    }

    public ShippingAddress(int id, int userId, String addressLine, String city, String district, String ward, String note, boolean isDefault, String fullName, String phone) {
        this.id = id;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.note = note;
        this.isDefault = isDefault;
        this.fullName = fullName;
        this.phone = phone;
    }

    public ShippingAddress(int userId, String addressLine, String city, String district, String ward, String note, boolean isDefault, String fullName, String phone) {
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.note = note;
        this.isDefault = isDefault;
        this.fullName = fullName;
        this.phone = phone;
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

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}