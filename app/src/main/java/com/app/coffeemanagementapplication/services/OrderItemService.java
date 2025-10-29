package com.app.coffeemanagementapplication.services;

import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IOrderItemDao;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;

import java.util.Collections;
import java.util.List;

public class OrderItemService implements IOrderItemRepo {
    private final IOrderItemDao orderItemDao;

    public OrderItemService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.orderItemDao = db.orderItemDao();
    }

    @Override
    public List<OrderItem> getAllOrderItemsByStatus(String status) {
        return orderItemDao.getAllOrderItemsByStatus(status);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return orderItemDao.getOrderItemsByOrderId(orderId);
    }

    @Override
    public OrderItem getOrderItemById(int id) {
        return orderItemDao.getOrderItemById(id);
    }

    @Override
    public void insertOrderItem(OrderItem orderItem) {
        orderItemDao.insertOrderItem(orderItem);
    }

    @Override
    public void deleteOrderItemById(int id) {
        orderItemDao.deleteOrderItemById(id);
    }

    @Override
    public List<OrderItem> searchOrderItems(Integer orderId, Integer productId) {
        return orderItemDao.searchOrderItems(orderId, productId);
    }

    @Override
    public void updateOrderItem(int id, int quantity, boolean isSelected) {
        orderItemDao.updateOrderItem(id, quantity, isSelected);
    }

    @Override
    public List<OrderItem> getAllSelectedOrderItemsByStatus(String status) {
        return orderItemDao.getAllSelectedOrderItemsByStatus(status);
    }

    @Override
    public void updateSelectedOrderItemStatus(int id, String status) {
        orderItemDao.updateSelectedOrderItemStatus(id, status);
    }

    @Override
    public void updateOrderIdOfSelectedOrderItem(int id, int orderId) {
        orderItemDao.updateOrderIdOfSelectedOrderItem(id, orderId);
    }


}
