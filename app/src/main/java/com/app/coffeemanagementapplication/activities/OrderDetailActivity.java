package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.adapters.OrderDetailAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityOrderDetailBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.OrderStatus;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IOrderRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.AddressService;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.OrderService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private IOrderRepo orderRepo;
    private IAddressRepo addressRepo;
    private IOrderItemRepo orderItemRepo;
    private IProductRepo productRepo;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        orderRepo = new OrderService(this);
        addressRepo = new AddressService(this);
        orderItemRepo = new OrderItemService(this);
        productRepo = new ProductService(this);

        int orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId != -1) {
            order = orderRepo.getOrderById(orderId);
            ShippingAddress address = addressRepo.getAddressById(order.getDeliveryAddressId());
            List<OrderItem> orderItems = orderItemRepo.getOrderItemsByOrderId(orderId);

            binding.tvAddress.setText(address.getAddressLine() + ", " + address.getWard() + ", " + address.getDistrict() + ", " + address.getCity());
            binding.tvPaymentMethod.setText(order.getPaymentMethod());

            OrderDetailAdapter adapter = new OrderDetailAdapter(this, orderItems, productRepo);
            binding.rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            binding.rvOrderItems.setAdapter(adapter);

            updateStatusCheckboxes(order.getOrderStatus());

            binding.cbOrderCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        binding.btnReceiveOrder.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnReceiveOrder.setVisibility(View.GONE);
                    }
                }
            });

            binding.btnReceiveOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order.setOrderStatus(OrderStatus.DELIVERED.getValue());
                    orderRepo.updateOrder(order);
                    finish();
                }
            });


        }
    }

    private void updateStatusCheckboxes(String status) {
        OrderStatus orderStatus = OrderStatus.fromValue(status);
        binding.cbOrderCompleted.setChecked(false);

        switch (orderStatus) {
            case DELIVERED:
            case COMPLETED:
                binding.cbOrderCompleted.setChecked(true);
                break;
            case PREPARING:
            case PAY:
                break;
        }
    }
}
