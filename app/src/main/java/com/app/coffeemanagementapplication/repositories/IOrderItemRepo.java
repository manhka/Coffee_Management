package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.OrderItem;

import java.util.List;

public interface IOrderItemRepo {
    List<OrderItem> getAllOrderItemsByStatus(String status);



    List<OrderItem> getOrderItemsByOrderId(int orderId);

    OrderItem getOrderItemById(int id);

    void insertOrderItem(OrderItem orderItem);


    void deleteOrderItemById(int id);


    List<OrderItem> searchOrderItems(Integer orderId, Integer productId);
    void updateOrderItem(int id, int quantity, boolean isSelected);
    List<OrderItem> getAllSelectedOrderItemsByStatus(String status);
    void updateSelectedOrderItemStatus(int id, String status);
    void updateOrderIdOfSelectedOrderItem(int id, int orderId);

}
