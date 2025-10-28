package com.app.coffeemanagementapplication.services;

import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IPaymentDao;
import com.app.coffeemanagementapplication.daos.IProductDao;
import com.app.coffeemanagementapplication.models.Payment;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;
import com.app.coffeemanagementapplication.repositories.IPaymentRepo;

import java.util.Collections;
import java.util.List;

public class PaymentService implements IPaymentRepo {
    private final IPaymentDao paymentDao;

    public PaymentService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.paymentDao = db.paymentDao();
    }


    @Override
    public List<Payment> getAllPaymentMethods() {
        return paymentDao.getAllPaymentMethods();
    }

    @Override
    public Payment getPaymentMethodById(int id) {
        return paymentDao.getPaymentMethodById(id);
    }

    @Override
    public void insertPayment(Payment payment) {
paymentDao.insertPayment(payment);
    }

    @Override
    public void clearDefaultPaymentMethod() {
paymentDao.clearDefaultPaymentMethod();
    }

    @Override
    public void setDefaultPaymentMethod(int paymentId) {
paymentDao.setDefaultPaymentMethod(paymentId);
    }
}
