package com.app.coffeemanagementapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.activities.OrderDetailActivity;
import com.app.coffeemanagementapplication.adapters.PaidOrdersAdapter;
import com.app.coffeemanagementapplication.databinding.FragmentPaidOrdersBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderStatus;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IOrderRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.OrderService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class PaidOrdersFragment extends Fragment implements PaidOrdersAdapter.OnTrackOrderClickListener {

    private FragmentPaidOrdersBinding binding;
    private IOrderRepo orderRepo;
    private IOrderItemRepo orderItemRepo;
    private IProductRepo productRepo;
    private PaidOrdersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaidOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderRepo = new OrderService(getContext());
        orderItemRepo = new OrderItemService(getContext());
        productRepo = new ProductService(getContext());

        binding.rvPaidOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPaidOrders();
    }

    private void loadPaidOrders() {
        int userId = MySharePrefers.getUserId();
        List<Order> paidOrders = orderRepo.getPayorDeliverOrdersByUserId(userId);
        adapter = new PaidOrdersAdapter(getContext(), paidOrders, orderItemRepo, productRepo, this);
        binding.rvPaidOrders.setAdapter(adapter);
    }

    @Override
    public void onTrackOrderClick(Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderId", order.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
