package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Product;

import java.util.List;

public interface IProductRepo {
    List<Product> getAllProducts();

    Product getProductById(int id);

    void insertProduct(Product product);

    void updateProduct(Product product);

    void deleteProductById(int id);
    List<Product> searchProducts(String name, Integer categoryId);

}
