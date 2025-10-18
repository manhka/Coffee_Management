package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.coffeemanagementapplication.models.Category;

import java.util.List;

@Dao
public interface ICategoryDao {
    @Query("SELECT * FROM Categories")
    List<Category> getAllCategories();

    @Query("SELECT * FROM Categories WHERE id = :id")
    Category getCategoryById(int id);

    @Query("SELECT * FROM Categories WHERE name = :name")
    Category getCategoryByName(String name);

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);
}
