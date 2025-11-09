package com.app.coffeemanagementapplication.repositories;

import androidx.room.Insert;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.Discount;

import java.util.List;

public interface IDiscountRepo {
    List<Discount> getAllDiscounts();

    void insertDiscount(Discount discount);

    Discount getDiscountById(int id);
    void clearDefaultDiscount();

    void setDefaultDiscount(int discountId);
}
