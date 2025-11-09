package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.coffeemanagementapplication.models.Product;

import java.util.List;

@Dao
public interface IProductDao {
    @Query("SELECT * FROM Products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM Products WHERE id = :id LIMIT 1")
    Product getProductById(int id);

    @Query("SELECT * FROM Products WHERE name LIKE :name")
    List<Product> getProductsByName(String name);

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Query("DELETE FROM Products WHERE id = :id")
    void deleteProductById(int id);
    @Query("SELECT * FROM Products " +
            "WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:categoryId IS NULL OR categoryId = :categoryId) " +
            "ORDER BY price ASC")
    List<Product> searchProducts(String name, Integer categoryId);
}
