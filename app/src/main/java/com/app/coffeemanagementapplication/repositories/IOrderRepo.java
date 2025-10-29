package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Order;

import java.util.List;

public interface IOrderRepo {
    List<Order> getAllOrders();

    Order getOrderById(int id);

    List<Order> getOrdersByUserId(int userId);

    List<Order> getOrdersByStatus(String status);

    List<Order> getOrdersByDate(String date);

    long  insertOrder(Order order);

    void updateOrder(Order order);

    void deleteOrderById(int id);

    List<Order> searchOrders(Integer userId, String status, String date);
}
