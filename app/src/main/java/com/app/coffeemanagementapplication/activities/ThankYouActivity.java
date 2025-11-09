package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.databinding.ActivityThankYouBinding;
import com.bumptech.glide.Glide;

public class ThankYouActivity extends BaseActivity {

    private ActivityThankYouBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThankYouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productImageUrl = intent.getStringExtra("productImageUrl");
        int quantity = intent.getIntExtra("quantity", 0);
        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

        // Hiển thị hình ảnh sản phẩm
        Glide.with(this)
                .load(productImageUrl)
                .into(binding.imgThankYouProduct);

        // Hiển thị thông tin đơn hàng với định dạng tiền tệ
        String orderDetails = "Sản phẩm: " + productName + "\n" +
                "Số lượng: " + quantity + "\n" +
                "Tổng tiền: " + CurrencyUtils.formatVNCurrency(totalPrice);
        binding.tvOrderInfo.setText(orderDetails);

        binding.btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouActivity.this, CustomerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}