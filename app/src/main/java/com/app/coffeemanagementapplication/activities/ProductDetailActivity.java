package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ActivityProductDetailBinding;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.FeedbackService;
import com.app.coffeemanagementapplication.services.ProductService;
import com.bumptech.glide.Glide;

public class ProductDetailActivity extends BaseActivity {
    private ActivityProductDetailBinding binding;
    private IProductRepo productRepo;
    private IFeedbackRepo feedbackRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        int productId = intent.getIntExtra("productId", -1);
        Toast.makeText(ProductDetailActivity.this, "Product ID: " + productId, Toast.LENGTH_SHORT).show();
//)
        productRepo = new ProductService(this);
        Product product = productRepo.getProductById(productId);
        binding.txtProductName.setText(product.getName());
        binding.txtProductDescription.setText(product.getDescription());
        binding.txtProductPrice.setText(String.format("$%.2f", product.getPrice()));
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(com.app.coffeemanagementapplication.R.drawable.ic_launcher_background)
                .into(binding.imvProductImage);
        feedbackRepo = new FeedbackService(this);
        float avg = feedbackRepo.getAverageRatingByProduct(productId);
        int count = feedbackRepo.getFeedbackCountByProduct(productId);
        binding.txtRatingValue.setText(String.format("%.1f", avg));
        binding.txtRatingCount.setText("(" + count + ")");
    }

}