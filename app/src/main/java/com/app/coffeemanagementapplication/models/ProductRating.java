package com.app.coffeemanagementapplication.models;

public class ProductRating {
    private Product product;
    private float averageRating;
    private int totalFeedback;
    private String comment;
    private String username;
    private String createdAt;
    public ProductRating(Product product, float averageRating, int totalFeedback) {
        this.product = product;
        this.averageRating = averageRating;
        this.totalFeedback = totalFeedback;
    }

    public ProductRating(Product product, float averageRating, int totalFeedback, String comment, String username, String createdAt) {
        this.product = product;
        this.averageRating = averageRating;
        this.totalFeedback = totalFeedback;
        this.comment = comment;
        this.username = username;
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
