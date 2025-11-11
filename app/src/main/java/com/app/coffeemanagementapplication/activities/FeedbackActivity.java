package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IOrderRepo;
import com.app.coffeemanagementapplication.services.FeedbackService;
import com.app.coffeemanagementapplication.services.OrderItemService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackActivity extends BaseActivity {

    private Button btnSubmitFeedback;
    private RatingBar ratingBarFeedback;
    private EditText etFeedbackComment;
    private IFeedbackRepo feedbackRepo;
    private IOrderItemRepo orderItemRepo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        orderItemRepo = new OrderItemService(this);
        feedbackRepo = new FeedbackService(this);
        // Ánh xạ view
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        ratingBarFeedback = findViewById(R.id.ratingBarFeedback);
        etFeedbackComment = findViewById(R.id.etFeedbackComment);
        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy số sao
                float rating = ratingBarFeedback.getRating();

                // Lấy nội dung comment
                String comment = etFeedbackComment.getText().toString().trim();

                // Kiểm tra hợp lệ
                if (rating == 0) {
                    Toast.makeText(FeedbackActivity.this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (comment.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Vui lòng nhập nhận xét của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = getIntent();
                int orderId = intent.getIntExtra("orderId", 0);
                List<OrderItem> orderItems = orderItemRepo.getOrderItemsByOrderId(orderId);
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                for (OrderItem orderItem : orderItems) {
                    feedbackRepo.insertFeedback(new Feedback(null, MySharePrefers.getUserId(), orderItem.getProductId(), (int) rating, comment, formattedDate));
                }
                Intent intent1 = new Intent(FeedbackActivity.this, CustomerHomeActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
