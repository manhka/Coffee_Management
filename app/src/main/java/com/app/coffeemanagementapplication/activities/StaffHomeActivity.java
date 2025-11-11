package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.adapters.StaffOrderAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityStaffHomeBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.repositories.OrderRepository;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class StaffHomeActivity extends BaseActivity {

    private ActivityStaffHomeBinding binding;
    private StaffOrderAdapter adapter;
    private OrderRepository orderRepository;
    private List<Order> allOrders;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaffHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRepository();
        setupRecyclerView();
        setupFilterButtons();
        loadOrders();
        binding.btnOpenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(StaffHomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRepository() {
        orderRepository = new OrderRepository(
                DatabaseClient.getInstance(this).getAppDatabase().orderDao()
        );
    }

    private void setupRecyclerView() {
        allOrders = new ArrayList<>();
        adapter = new StaffOrderAdapter(this, allOrders, orderRepository);
        
        binding.rcvOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvOrders.setAdapter(adapter);

        // Set click listener
        adapter.setOnOrderClickListener(order -> {
            Intent intent = new Intent(this, OrderDetailStaffActivity.class);
            intent.putExtra("ORDER_ID", order.getId());
            startActivity(intent);
        });
    }

    private void setupFilterButtons() {
        // Set initial active state
        setActiveFilter(binding.btnFilterAll);

        binding.btnFilterAll.setOnClickListener(v -> {
            setActiveFilter((MaterialButton) v);
            filterOrders("all");
        });

        binding.btnFilterPending.setOnClickListener(v -> {
            setActiveFilter((MaterialButton) v);
            filterOrders("PAY");
        });

        binding.btnFilterPreparing.setOnClickListener(v -> {
            setActiveFilter((MaterialButton) v);
            filterOrders("PREPARING");
        });

        binding.btnFilterCompleted.setOnClickListener(v -> {
            setActiveFilter((MaterialButton) v);
            filterOrders("COMPLETED");
        });
    }

    private void setActiveFilter(MaterialButton activeButton) {
        // Reset all buttons
        resetFilterButton(binding.btnFilterAll);
        resetFilterButton(binding.btnFilterPending);
        resetFilterButton(binding.btnFilterPreparing);
        resetFilterButton(binding.btnFilterCompleted);

        // Set active button
        activeButton.setBackgroundColor(getResources().getColor(com.app.coffeemanagementapplication.R.color.brown, null));
        activeButton.setTextColor(getResources().getColor(com.app.coffeemanagementapplication.R.color.white, null));
    }

    private void resetFilterButton(MaterialButton button) {
        button.setBackgroundColor(getResources().getColor(com.app.coffeemanagementapplication.R.color.white, null));
        button.setTextColor(getResources().getColor(com.app.coffeemanagementapplication.R.color.brown, null));
    }

    private void loadOrders() {
        new Thread(() -> {
            List<Order> orders = orderRepository.getAllOrders();
            runOnUiThread(() -> {
                allOrders.clear();
                allOrders.addAll(orders);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void filterOrders(String status) {
        currentFilter = status;
        new Thread(() -> {
            List<Order> filteredOrders;
            if (status.equals("all")) {
                filteredOrders = orderRepository.getAllOrders();
            } else {
                filteredOrders = orderRepository.getOrdersByStatus(status);
            }
            
            runOnUiThread(() -> {
                allOrders.clear();
                allOrders.addAll(filteredOrders);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
