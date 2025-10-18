package com.app.coffeemanagementapplication.models;

public class ProductRating {
    public Product product;
    public float averageRating;
    public int totalFeedback;

    public ProductRating(Product product, float averageRating, int totalFeedback) {
        this.product = product;
        this.averageRating = averageRating;
        this.totalFeedback = totalFeedback;
    }
}
