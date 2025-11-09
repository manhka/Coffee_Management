package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.daos.IOrderDao;
import com.app.coffeemanagementapplication.models.Order;

import java.util.Collections;
import java.util.List;

public class OrderRepository implements IOrderRepo {
    private final IOrderDao orderDao;

    public OrderRepository(IOrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Override
    public Order getOrderById(int id) {
        return orderDao.getOrderById(id);
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderDao.getOrdersByStatus(status);
    }

    @Override
    public List<Order> getOrdersByStatusAndUserId(String status, int userId) {
        return orderDao.getOrdersByStatusAndUserId(status,userId);
    }

    @Override
    public List<Order> getOrdersByDate(String date) {
        return orderDao.getOrdersByDate(date);
    }

    @Override
    public long insertOrder(Order order) {
        return orderDao.insertOrder(order);
    }

    @Override
    public void updateOrder(Order order) {
        orderDao.updateOrder(order);
    }

    @Override
    public void deleteOrderById(int id) {
        orderDao.deleteOrderById(id);
    }

    @Override
    public List<Order> searchOrders(Integer userId, String status, String date) {
        return orderDao.searchOrders(userId, status, date);
    }
}
