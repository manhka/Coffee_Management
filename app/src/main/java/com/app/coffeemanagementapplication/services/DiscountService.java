package com.app.coffeemanagementapplication.services;

import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IDiscountDao;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.repositories.IDiscountRepo;

import java.util.Collections;
import java.util.List;

public class DiscountService implements IDiscountRepo {
    private final IDiscountDao discountDao;

    public DiscountService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.discountDao = db.discountDao();
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountDao.getAllDiscounts();
    }

    @Override
    public void insertDiscount(Discount discount) {
        discountDao.insertDiscount(discount);
    }

    @Override
    public Discount getDiscountById(int id) {
        return discountDao.getDiscountById(id);
    }

    @Override
    public void clearDefaultDiscount() {
        discountDao.clearDefaultDiscount();
    }

    @Override
    public void setDefaultDiscount(int discountId) {
        discountDao.setDefaultDiscount(discountId);
    }
}
