package com.app.coffeemanagementapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.coffeemanagementapplication.models.Feedback;

import java.util.List;

@Dao
public interface IFeedbackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeedback(Feedback feedback);


    @Query("SELECT * FROM Feedbacks ORDER BY createdAt DESC")
    List<Feedback> getAllFeedbacks();


    @Query("SELECT * FROM Feedbacks WHERE productId = :productId ORDER BY createdAt DESC")
    List<Feedback> getFeedbackByProductId(int productId);


    @Query("SELECT AVG(rating) FROM Feedbacks WHERE productId = :productId")
    Float getAverageRatingByProduct(int productId);


    @Query("SELECT COUNT(*) FROM Feedbacks WHERE productId = :productId")
    int getFeedbackCountByProduct(int productId);

}
