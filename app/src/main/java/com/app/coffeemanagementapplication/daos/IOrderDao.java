package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.coffeemanagementapplication.models.Order;

import java.util.List;

@Dao
public interface IOrderDao {
    @Query("SELECT * FROM Orders")
    List<Order> getAllOrders();

    @Query("SELECT * FROM Orders WHERE id = :id LIMIT 1")
    Order getOrderById(int id);
    @Query("SELECT * FROM Orders WHERE userId = :userId")
    List<Order> getOrdersByUserId(int userId);
    @Query("SELECT * FROM Orders WHERE orderStatus = :status")
    List<Order> getOrdersByStatus(String status);
    @Query("SELECT * FROM Orders WHERE orderDate = :date")
    List<Order> getOrdersByDate(String date);

    @Insert
    long  insertOrder(Order order);

    @Update
    void updateOrder(Order order);
    @Query("DELETE FROM Orders WHERE id = :id")
    void deleteOrderById(int id);
    @Query("SELECT * FROM Orders WHERE userId = :userId AND orderStatus = :status AND orderDate = :date")
    List<Order> searchOrders(Integer userId, String status, String date);

}
