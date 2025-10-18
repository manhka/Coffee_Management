package com.app.coffeemanagementapplication.services;

import android.content.Context;

import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.ProductRating;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductRatingBuilder {
    private  IProductRepo productRepo;
    private  IFeedbackRepo feedbackRepo;

    public ProductRatingBuilder(Context context) {
        productRepo = new ProductService(context);
        feedbackRepo = new FeedbackService(context);
    }

    public List<ProductRating> buildProductRatings() {
        List<Product> products = productRepo.getAllProducts();
        List<ProductRating> productRatings = new ArrayList<>();

        for (Product p : products) {
            float avg = feedbackRepo.getAverageRatingByProduct(p.getId());
            int count = feedbackRepo.getFeedbackCountByProduct(p.getId());
            productRatings.add(new ProductRating(p, avg, count));
        }

        return productRatings;
    }
    public List<ProductRating> searchProductRatings(String name, Integer categoryId, boolean sortByRating) {
        List<Product> products = productRepo.searchProducts(name, categoryId);
        List<ProductRating> productRatings = new ArrayList<>();

        for (Product p : products) {
            float avg = feedbackRepo.getAverageRatingByProduct(p.getId());
            int total = feedbackRepo.getFeedbackCountByProduct(p.getId());
            productRatings.add(new ProductRating(p, avg, total));
        }

        // Nếu muốn sắp xếp tăng dần theo rating
        if (sortByRating) {
            productRatings.sort((a, b) -> Float.compare(b.getAverageRating(), a.getAverageRating()));
        }

        return productRatings;
    }
}
