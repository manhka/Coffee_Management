package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.OrderItemStatus;

import java.util.List;

@Dao
public interface IOrderItemDao {
    @Query("SELECT * FROM OrderItems where orderItemStatus = :status")
    List<OrderItem> getAllOrderItemsByStatus(String status);

    @Query("SELECT * FROM OrderItems WHERE isSelected = 1 AND orderItemStatus = :status")
    List<OrderItem> getAllSelectedOrderItemsByStatus(String status);

    @Query("SELECT * FROM OrderItems WHERE orderId = :orderId")
    List<OrderItem> getOrderItemsByOrderId(int orderId);

    @Insert
    void insertOrderItem(OrderItem orderItem);

    @Query("UPDATE OrderItems SET quantity = :quantity, isSelected = :isSelected WHERE id = :id")
    void updateOrderItem(int id, int quantity, boolean isSelected);
    @Query("UPDATE OrderItems SET orderId = :orderId, isSelected = 1 WHERE id = :id")
    void updateOrderIdOfSelectedOrderItem(int id, int orderId);
    @Query("UPDATE OrderItems SET orderItemStatus = :status WHERE isSelected=1 AND id = :id")
    void updateSelectedOrderItemStatus(int id, String status);

    @Query("DELETE FROM OrderItems WHERE id = :id")
    void deleteOrderItemById(int id);

    @Query("SELECT * FROM OrderItems WHERE orderId = :orderId AND productId = :productId")
    List<OrderItem> searchOrderItems(Integer orderId, Integer productId);

    @Query("SELECT * FROM OrderItems WHERE id = :id LIMIT 1")
    OrderItem getOrderItemById(int id);

}
