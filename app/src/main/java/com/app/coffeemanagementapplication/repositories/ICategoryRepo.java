package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Category;

import java.util.List;

public interface ICategoryRepo {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
    Category getCategoryByName(String name);
    void insertCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Category category);
}
