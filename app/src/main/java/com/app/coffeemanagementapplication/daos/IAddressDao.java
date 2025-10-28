package com.app.coffeemanagementapplication.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.ShippingAddress;

import java.util.List;

@Dao
public interface IAddressDao {
    @Query("SELECT * FROM ShippingAddress WHERE id = :id")
    ShippingAddress getAddressById(int id);

    @Query("UPDATE ShippingAddress " +
            "SET addressLine = :addressLine, " +
            "city = :city, " +
            "district = :district, " +
            "ward = :ward, " +
            "note = :note ," +
            "fullName = :fullName ," +
            "phone = :phone " +
            "WHERE id = :id")
    void updateAddressById(int id,
                           String addressLine,
                           String city,
                           String district,
                           String ward,
                           String note,
                           String fullName,
                           String phone);

    @Insert
    void insertAddress(ShippingAddress address);

    @Query("SELECT * FROM ShippingAddress WHERE userId = :userId")
    List<ShippingAddress> getAddressesByUser(int userId);

    @Query("DELETE FROM ShippingAddress WHERE id = :id")
    void deleteAddress(int id);

    @Query("UPDATE ShippingAddress SET isDefault = 0 WHERE userId = :userId")
    void clearDefaultAddress(int userId);

    // Đặt 1 địa chỉ làm mặc định
    @Query("UPDATE ShippingAddress SET isDefault = 1 WHERE id = :addressId")
    void setDefaultAddress(int addressId);
}
