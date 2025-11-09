package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.DiscountAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityChoosingDiscountBinding;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.repositories.IDiscountRepo;
import com.app.coffeemanagementapplication.services.DiscountService;

import java.util.List;

public class ChoosingDiscountActivity extends BaseActivity {
    private ActivityChoosingDiscountBinding binding;
    private List<Discount> discountList;
    private DiscountAdapter discountAdapter;
    private IDiscountRepo discountRepo;
    private int discountId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoosingDiscountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        double totalOrder = intent.getDoubleExtra("total", 0);
        Log.d("totalOrderrrrrr", String.valueOf(totalOrder));
        discountRepo = new DiscountService(this);
        discountList = discountRepo.getAllDiscounts();
        binding.btnSave.setVisibility(View.INVISIBLE);
        binding.btnSave.setEnabled(false);
        discountAdapter = new DiscountAdapter(discountList, this, totalOrder, position -> {
            discountId = discountList.get(position).getId();
            for (int i = 0; i < discountList.size(); i++) {
                discountList.get(i).setDefault(i == position);
            }
            discountAdapter.notifyDataSetChanged();
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(true);
        });
        binding.rcvDiscount.setAdapter(discountAdapter);
        binding.rcvDiscount.setLayoutManager(new LinearLayoutManager(this));
        binding.imvBtnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discountRepo.clearDefaultDiscount();
                discountRepo.setDefaultDiscount(discountId);
                MySharePrefers.setDiscountId(discountId);
                Intent intent= new Intent(ChoosingDiscountActivity.this, PaymentOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}