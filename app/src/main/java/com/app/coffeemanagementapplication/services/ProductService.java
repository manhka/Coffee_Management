package com.app.coffeemanagementapplication.services;


import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IProductDao;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IProductRepo;

import java.util.List;

    public class ProductService implements IProductRepo {

        private final IProductDao productDao;

        public ProductService(Context context) {
            AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
            this.productDao = db.productDao();
        }

        @Override
        public List<Product> getAllProducts() {
            return productDao.getAllProducts();
        }

        @Override
        public Product getProductById(int id) {
            return productDao.getProductById(id);
        }

        @Override
        public void insertProduct(Product product) {
            productDao.insertProduct(product);
        }

        @Override
        public void updateProduct(Product product) {
            productDao.updateProduct(product);
        }

        @Override
        public void deleteProductById(int id) {
            productDao.deleteProductById(id);
        }
        @Override
        public List<Product> searchProducts(String name, Integer categoryId) {
            return productDao.searchProducts(name, categoryId);
        }
    }