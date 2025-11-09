package com.app.coffeemanagementapplication.services;

import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IOrderDao;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.repositories.IOrderRepo;

import java.util.List;

public class OrderService implements IOrderRepo {
    private final IOrderDao orderDao;

    public OrderService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.orderDao = db.orderDao();
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
    public List<Order> getOrdersByDate(String date) {
        return orderDao.getOrdersByDate(date);
    }

    @Override
    public long  insertOrder(Order order) {
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
