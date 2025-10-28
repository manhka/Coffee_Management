package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.app.coffeemanagementapplication.models.Payment;

import java.util.List;

@Dao
public interface IPaymentDao {
    @Query("SELECT * FROM Payments")
    List<Payment> getAllPaymentMethods();

    @Query("SELECT * FROM Payments WHERE id = :id LIMIT 1")
    Payment getPaymentMethodById(int id);

    @Insert
    void insertPayment(Payment payment);
    @Query("UPDATE Payments SET isDefault = 0")
    void clearDefaultPaymentMethod();

    @Query("UPDATE Payments SET isDefault = 1 WHERE id = :paymentId")
    void setDefaultPaymentMethod(int paymentId);
}
