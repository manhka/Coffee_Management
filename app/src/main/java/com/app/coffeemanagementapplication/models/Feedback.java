package com.app.coffeemanagementapplication.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

    @Entity(
            tableName = "Feedbacks",
            foreignKeys = {
                    @ForeignKey(entity = Order.class, parentColumns = "id", childColumns = "orderId"),
                    @ForeignKey(entity = Users.class, parentColumns = "id", childColumns = "userId")
            }
    )
    public class Feedback {
        @PrimaryKey(autoGenerate = true)
        public int id;

        public int orderId;
        public int userId;
        public int rating;
        public String comment;
        public String createdAt;

    public Feedback() {
    }

    public Feedback(int id, int orderId, int userId, int rating, String comment, String createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}