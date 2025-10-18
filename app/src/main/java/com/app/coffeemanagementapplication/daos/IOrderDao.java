package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.Order;

import java.util.List;

@Dao
public interface IOrderDao {
    @Query("SELECT * FROM Orders")
    List<Order> getAllOrders();


}
