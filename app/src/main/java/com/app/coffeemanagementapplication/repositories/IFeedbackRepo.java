package com.app.coffeemanagementapplication.repositories;

import com.app.coffeemanagementapplication.models.Feedback;

import java.util.List;

public interface IFeedbackRepo {

    void insertFeedback(Feedback feedback);


    List<Feedback> getAllFeedbacks();


    List<Feedback> getFeedbackByProductId(int productId);


    Float getAverageRatingByProduct(int productId);


    int getFeedbackCountByProduct(int productId);
}
