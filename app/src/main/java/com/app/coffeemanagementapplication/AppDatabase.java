package com.app.coffeemanagementapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.app.coffeemanagementapplication.daos.IAddressDao;
import com.app.coffeemanagementapplication.daos.ICategoryDao;
import com.app.coffeemanagementapplication.daos.IDiscountDao;
import com.app.coffeemanagementapplication.daos.IFeedbackDao;
import com.app.coffeemanagementapplication.daos.IOrderDao;
import com.app.coffeemanagementapplication.daos.IOrderItemDao;
import com.app.coffeemanagementapplication.daos.IPaymentDao;
import com.app.coffeemanagementapplication.daos.IProductDao;
import com.app.coffeemanagementapplication.daos.IUserDao;
import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Payment;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.models.Users;

@Database(entities = {Payment.class, Product.class, Category.class, Order.class, Feedback.class, Users.class, OrderItem.class, Discount.class, ShippingAddress.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase  {
    public abstract IProductDao productDao();
    public abstract IFeedbackDao feedbackDao();
    public abstract IOrderDao orderDao();
    public abstract ICategoryDao categoryDao();

    public abstract IOrderItemDao orderItemDao();
    public abstract IAddressDao addressDao();
    public abstract IUserDao userDao();
    public abstract IPaymentDao paymentDao();
    public abstract IDiscountDao discountDao();



}
