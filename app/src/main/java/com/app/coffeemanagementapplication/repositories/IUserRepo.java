package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Users;

import java.util.List;

public interface IUserRepo {
    void insertUser(Users user);

    // Thêm nhiều user cùng lúc
    void insertUsers(List<Users> users);

    // Cập nhật user
    void updateUser(Users user);

    // Xóa user
    void deleteUser(Users user);

    // Lấy danh sách tất cả user
    List<Users> getAllUsers();

    // Lấy user theo ID
    Users getUserById(int id);

    // Lấy user theo email
    Users getUserByEmail(String email);

    // Kiểm tra đăng nhập (email + password)
    Users login(String email, String password);
}
