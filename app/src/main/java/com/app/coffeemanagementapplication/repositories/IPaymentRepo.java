package com.app.coffeemanagementapplication.repositories;

import androidx.room.Insert;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.Payment;

import java.util.List;

public interface IPaymentRepo {
    List<Payment> getAllPaymentMethods();

    Payment getPaymentMethodById(int id);

    void insertPayment(Payment payment);
    void clearDefaultPaymentMethod();

    void setDefaultPaymentMethod(int paymentId);
}
