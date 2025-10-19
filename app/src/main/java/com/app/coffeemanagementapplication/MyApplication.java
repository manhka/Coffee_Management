package com.app.coffeemanagementapplication;


import android.app.Application;

import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.ICategoryRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.CategoryService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo repo
        ICategoryRepo categoryRepo = new CategoryService(this);
        IProductRepo productRepo = new ProductService(this);

        // Chỉ insert nếu DB trống (tránh nhân đôi)
        List<Category> existingCategories = categoryRepo.getAllCategories();
        if (existingCategories == null || existingCategories.isEmpty()) {
            seedCategories(categoryRepo);
        }

        List<Product> existingProducts = productRepo.getAllProducts();
        if (existingProducts == null || existingProducts.isEmpty()) {
            seedProducts(productRepo);
        }
    }

    // 🟫 Seed Category
    private void seedCategories(ICategoryRepo categoryRepo) {
        categoryRepo.insertCategory(new Category(1, "Cafe", "Các loại cafe", "", ""));
        categoryRepo.insertCategory(new Category(2, "Trà Sữa", "Trà sữa các vị", "", ""));
        categoryRepo.insertCategory(new Category(3, "Sinh Tố", "Sinh tố trái cây", "", ""));
        categoryRepo.insertCategory(new Category(4, "Bánh", "Bánh ngọt, bánh mì", "", ""));
    }

    // 🟫 Seed Product
    private void seedProducts(IProductRepo productRepo) {
        productRepo.insertProduct(new Product(1, 1, "Cà phê sữa đá", "Cà phê truyền thống Việt Nam", 25000,
                "https://cdn.pixabay.com/photo/2020/03/28/14/38/egg-coffee-4977310_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(2, 1, "Espresso", "Cà phê Ý đậm vị", 30000,
                "https://cdn.pixabay.com/photo/2022/11/07/04/58/dalgona-coffee-7575608_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(3, 2, "Trà sữa trân châu", "Thức uống ngọt ngào", 35000,
                "https://cdn.pixabay.com/photo/2020/03/05/12/44/orange-4904390_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(4, 3, "Sinh tố xoài", "Sinh tố tươi mát", 40000,
                "https://cdn.pixabay.com/photo/2020/02/03/07/18/drink-4814956_1280.jpg", true, "", ""));
        productRepo.insertProduct(new Product(5, 4, "Bánh donut", "Bánh ngọt thơm ngon", 20000,
                "https://cdn.pixabay.com/photo/2024/06/16/16/34/celery-8833800_1280.jpg", true, "", ""));
    }
}
