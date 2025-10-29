package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.ProductFeedbackAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityProductFeedbackBinding;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.repositories.IUserRepo;
import com.app.coffeemanagementapplication.services.FeedbackService;
import com.app.coffeemanagementapplication.services.ProductService;
import com.app.coffeemanagementapplication.services.UserService;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductFeedbackActivity extends BaseActivity {
    private ActivityProductFeedbackBinding binding;
    private IFeedbackRepo feedbackRepo;
    private List<Feedback> feedbackList;
    private ProductFeedbackAdapter adapter;
    private IUserRepo userRepo;
    private IProductRepo productRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        feedbackRepo = new FeedbackService(this);
        userRepo= new UserService(this);
        productRepo= new ProductService(this);
        Intent intent = getIntent();
        int productId = intent.getIntExtra("productId", -1);
        Product product= productRepo.getProductById(productId);
        binding.tvProductName.setText(product.getName());
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(com.app.coffeemanagementapplication.R.drawable.ic_launcher_background)
                .into(binding.imgProduct);

        feedbackList = feedbackRepo.getFeedbackByProductId(productId);
        adapter = new ProductFeedbackAdapter(this, feedbackList,userRepo);
        binding.rcvFeedbacks.setAdapter(adapter);
        binding.rcvFeedbacks.setLayoutManager(new LinearLayoutManager(this));
        binding.imvBtnBack.setOnClickListener(v -> finish());
    }
}