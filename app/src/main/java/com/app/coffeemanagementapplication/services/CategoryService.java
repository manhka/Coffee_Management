package com.app.coffeemanagementapplication.services;

import com.app.coffeemanagementapplication.daos.ICategoryDao;
import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.repositories.ICategoryRepo;

import java.util.Collections;
import java.util.List;

public class CategoryService implements ICategoryRepo {
    ICategoryDao categoryDao;

    public CategoryService(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> getAllCategories() {
        return Collections.emptyList();
    }

    @Override
    public Category getCategoryById(int id) {
        return null;
    }

    @Override
    public Category getCategoryByName(String name) {
        return null;
    }

    @Override
    public void insertCategory(Category category) {

    }

    @Override
    public void updateCategory(Category category) {

    }

    @Override
    public void deleteCategory(Category category) {

    }
}
