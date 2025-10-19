package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.Feedback;

import java.util.List;

@Dao
public interface IFeedbackDao {
    @Query("SELECT AVG(f.rating) " +
            "FROM Feedbacks f " +
            "WHERE f.orderId IN (SELECT o.id FROM Orders o " +
            "JOIN OrderItems oi ON oi.orderId = o.id " +
            "WHERE oi.productId = :productId)")
    Float getAverageRatingByProduct(int productId);

    @Query("SELECT COUNT(f.id) " +
            "FROM Feedbacks f " +
            "JOIN Orders o ON f.orderId = o.id " +
            "JOIN OrderItems oi ON oi.orderId = o.id " +
            "WHERE oi.productId = :productId")
    int getFeedbackCountByProduct(int productId);

    @Query("SELECT f.* " +
            "FROM Feedbacks f " +
            "JOIN Orders o ON f.orderId = o.id " +
            "JOIN OrderItems oi ON oi.orderId = o.id " +
            "WHERE oi.productId = :productId")
    List<Feedback> getFeedbackByProductId(int productId);

    @Insert
    void insertFeedback(Feedback feedback);

    @Query("SELECT * FROM Feedbacks")
    List<Feedback> getAllFeedbacks();

}
