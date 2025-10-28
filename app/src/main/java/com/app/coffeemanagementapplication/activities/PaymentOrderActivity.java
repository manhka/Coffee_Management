package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.adapters.PaymentOrderAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityPaymentOrderBinding;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class PaymentOrderActivity extends BaseActivity {
    private PaymentOrderAdapter adapter;
    private IProductRepo productRepo;
    private IOrderItemRepo orderItemRepo;
    private List<OrderItem> orderItems;
    private ActivityPaymentOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productRepo = new ProductService(this);
        orderItemRepo = new OrderItemService(this);
        orderItems = orderItemRepo.getAllSelectedOrderItems();
        adapter = new PaymentOrderAdapter(orderItems, this, productRepo);
        binding.rvCartItems.setAdapter(adapter);
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.imvBtnBack.setOnClickListener(v -> finish());
        binding.llSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PaymentOrderActivity.this,ChoosingAddressActivity.class);
                startActivity(intent);
            }
        });
    }
}