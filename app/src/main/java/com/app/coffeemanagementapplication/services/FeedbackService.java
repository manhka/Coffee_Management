package com.app.coffeemanagementapplication.services;

import android.content.Context;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IFeedbackDao;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;

import java.util.Collections;
import java.util.List;

public class FeedbackService implements IFeedbackRepo {

    private final IFeedbackDao feedbackDao;

    // Constructor khởi tạo DAO
    public FeedbackService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.feedbackDao = db.feedbackDao();
    }


    @Override
    public void insertFeedback(Feedback feedback) {
        feedbackDao.insertFeedback(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackDao.getAllFeedbacks();
    }

    @Override
    public List<Feedback> getFeedbackByProductId(int productId) {
        return feedbackDao.getFeedbackByProductId(productId);
    }

    @Override
    public Float getAverageRatingByProduct(int productId) {
        return feedbackDao.getAverageRatingByProduct(productId);
    }

    @Override
    public int getFeedbackCountByProduct(int productId) {
        return feedbackDao.getFeedbackCountByProduct(productId);
    }
}
