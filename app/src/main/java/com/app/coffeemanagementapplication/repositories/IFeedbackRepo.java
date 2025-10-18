package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Feedback;

import java.util.List;

public interface IFeedbackRepo {
    List<Feedback> getAllFeedbacks();
    void insertFeedback(Feedback feedback);

    // các hàm liên quan đến product
    Float getAverageRatingByProduct(int productId);
    int getFeedbackCountByProduct(int productId);
    List<Feedback> getFeedbackByProduct(int productId);
}
