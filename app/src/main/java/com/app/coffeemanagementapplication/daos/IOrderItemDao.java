package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.coffeemanagementapplication.models.OrderItem;

import java.util.List;

@Dao
public interface IOrderItemDao {
    @Query("SELECT * FROM OrderItems")
    List<OrderItem> getAllOrderItems();

    @Query("SELECT * FROM OrderItems WHERE orderId = :orderId")
    List<OrderItem> getOrderItemsByOrderId(int orderId);
    @Insert
    void insertOrderItem(OrderItem orderItem);

    @Update
    void updateOrderItem(OrderItem orderItem);
    @Query("DELETE FROM OrderItems WHERE id = :id")
    void deleteOrderItemById(int id);
    @Query("SELECT * FROM OrderItems WHERE orderId = :orderId AND productId = :productId")
    List<OrderItem> searchOrderItems(Integer orderId, Integer productId);
    @Query("SELECT * FROM OrderItems WHERE id = :id LIMIT 1")
    OrderItem getOrderItemById(int id);

}
