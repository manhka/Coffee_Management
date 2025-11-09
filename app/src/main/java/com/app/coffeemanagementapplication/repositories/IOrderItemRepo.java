package com.app.coffeemanagementapplication.repositories;

import androidx.room.Insert;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.OrderItem;

import java.util.List;

public interface IOrderItemRepo {
    List<OrderItem> getAllOrderItemsByStatusAndUserId(String status, int userId);

    List<OrderItem> getAllSelectedOrderItemsByStatusAndUserId(String status, int userId);

    List<OrderItem> getOrderItemsByOrderId(int orderId);

    int getOrderItemCountByOrderId(int orderId);

    void insertOrderItem(OrderItem orderItem);

    void updateOrderItem(int id, int quantity, boolean isSelected);
    void updateOrderIdOfSelectedOrderItem(int id, int orderId);
    void updateSelectedOrderItemStatus(int id, String status);

    void deleteOrderItemById(int id);

    List<OrderItem> searchOrderItems(Integer orderId, Integer productId);

    OrderItem getOrderItemById(int id);
}
