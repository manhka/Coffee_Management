package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Discount;
import java.util.List;

public interface IDiscountRepo {
    List<Discount> getAllDiscounts();
    void insertDiscount(Discount discount);
    void updateDiscount(Discount discount);
    void deleteDiscount(Discount discount);
    Discount getDiscountById(int id);
    void clearDefaultDiscount();
    void setDefaultDiscount(int discountId);
}
