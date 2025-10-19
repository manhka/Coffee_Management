package com.app.coffeemanagementapplication.models;

public class ProductRating {
    private Product product;
    private float averageRating;
    private int totalFeedback;

    public ProductRating(Product product, float averageRating, int totalFeedback) {
        this.product = product;
        this.averageRating = averageRating;
        this.totalFeedback = totalFeedback;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalFeedback(int totalFeedback) {
        this.totalFeedback = totalFeedback;
    }
}
