package com.app.coffeemanagementapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.adapters.CompletedOrdersAdapter;
import com.app.coffeemanagementapplication.databinding.FragmentCompletedOrdersBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderStatus;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IOrderRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.OrderService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class CompletedOrdersFragment extends Fragment {

    private FragmentCompletedOrdersBinding binding;
    private IOrderRepo orderRepo;
    private IOrderItemRepo orderItemRepo;
    private IProductRepo productRepo;
    private CompletedOrdersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompletedOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderRepo = new OrderService(getContext());
        orderItemRepo = new OrderItemService(getContext());
        productRepo = new ProductService(getContext());

        binding.rvCompletedOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCompletedOrders();
    }

    private void loadCompletedOrders() {
        int userId = MySharePrefers.getUserId();
        List<Order> completedOrders = orderRepo.getOrdersByStatusAndUserId(OrderStatus.COMPLETED.getValue(), userId);

        adapter = new CompletedOrdersAdapter(getContext(), completedOrders, orderItemRepo, productRepo);
        binding.rvCompletedOrders.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
