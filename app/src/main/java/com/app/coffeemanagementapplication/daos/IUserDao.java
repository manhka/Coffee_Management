package com.app.coffeemanagementapplication.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.coffeemanagementapplication.models.Users;

import java.util.List;

@Dao
public interface IUserDao {

    // Thêm user mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(Users user);

    // Thêm nhiều user cùng lúc
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<Users> users);

    // Cập nhật thông tin user
    @Update
    void updateUser(Users user);

    // Xóa user
    @Delete
    void deleteUser(Users user);

    // Lấy tất cả user
    @Query("SELECT * FROM Users")
    List<Users> getAllUsers();

    // Tìm user theo ID
    @Query("SELECT * FROM Users WHERE id = :id LIMIT 1")
    Users getUserById(int id);

    // Tìm user theo email
    @Query("SELECT * FROM Users WHERE email = :email LIMIT 1")
    Users getUserByEmail(String email);

    // Kiểm tra đăng nhập
    @Query("SELECT * FROM Users WHERE email = :email AND password = :password LIMIT 1")
    Users login(String email, String password);
}

