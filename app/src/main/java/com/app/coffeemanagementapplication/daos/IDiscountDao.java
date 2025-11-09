package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.Discount;

import java.util.List;

@Dao
public interface IDiscountDao {
    @Query("SELECT * FROM Discounts")
    List<Discount> getAllDiscounts();

    @Insert
    void insertDiscount(Discount discount);

    @Query("SELECT * FROM Discounts WHERE id = :id LIMIT 1")
    Discount getDiscountById(int id);

    @Query("UPDATE Discounts SET isDefault = 0")
    void clearDefaultDiscount();

    @Query("UPDATE Discounts SET isDefault = 1 WHERE id = :discountId")
    void setDefaultDiscount(int discountId);

}
