package com.app.coffeemanagementapplication.services;
import android.app.Application;

import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IProductDao;
import com.app.coffeemanagementapplication.daos.IUserDao;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.IUserRepo;

import java.util.Collections;
import java.util.List;

public class UserService implements IUserRepo {
    private final IUserDao userDao;

    public UserService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.userDao = db.userDao();
    }

    @Override
    public void insertUser(Users user) {
        userDao.insertUser(user);
    }

    @Override
    public void insertUsers(List<Users> users) {
        userDao.insertUsers(users);

    }

    @Override
    public void updateUser(Users user) {
        userDao.updateUser(user);

    }

    @Override
    public void deleteUser(Users user) {
        userDao.deleteUser(user);

    }

    @Override
    public List<Users> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public Users getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public Users getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public Users login(String email, String password) {
        return userDao.login(email, password);
    }
    public boolean isEmailExists(String email) {
        return userDao.countUserByEmail(email) > 0;
    }
}
