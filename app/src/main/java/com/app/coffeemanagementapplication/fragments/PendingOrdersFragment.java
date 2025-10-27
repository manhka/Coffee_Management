package com.app.coffeemanagementapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.adapters.PendingOrderAdapter;
import com.app.coffeemanagementapplication.databinding.FragmentPendingOrdersBinding;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersFragment extends Fragment {

    private FragmentPendingOrdersBinding binding;
    private PendingOrderAdapter adapter;
    private List<OrderItem> orderItems = new ArrayList<>();
    private IProductRepo productRepo;
    private IOrderItemRepo orderItemRepo;

    public PendingOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingOrdersBinding.inflate(inflater, container, false);

        productRepo = new ProductService(requireContext());
        orderItemRepo = new OrderItemService(requireContext());

        setupRecyclerView();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        orderItems = getPendingOrderItems();

        adapter = new PendingOrderAdapter(requireContext(), orderItems, productRepo);
        binding.rcvPendingOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvPendingOrders.setAdapter(adapter);

        //  Khi tick hoặc bỏ tick sản phẩm => cập nhật tổng
        adapter.setOnCartChangeListener(total -> {
            binding.txtTotalPrice.setText("Tổng: " + CurrencyUtils.formatVNCurrency(total));
        });

        //  Khi tăng/giảm số lượng
        adapter.setOnQuantityChangeListener((item, newQuantity) -> {
            // chỉ cập nhật tổng nếu item đó đang được chọn
            if (item.isSelected()) {
                double total = calculateTotal();
                binding.txtTotalPrice.setText("Tổng: " + CurrencyUtils.formatVNCurrency(total));
            }
        });
        adapter.setOnDeleteClickListener(item -> {
            orderItemRepo.deleteOrderItemById(item.getId());
            adapter.removeItem(item);
            double total = calculateTotal();
            binding.txtTotalPrice.setText("Tổng: " + CurrencyUtils.formatVNCurrency(total));
        });
    }

    private double calculateTotal() {
        double total = 0;
        for (OrderItem item : orderItems) {
            if (item.isSelected()) {
                Product p = productRepo.getProductById(item.getProductId());
                if (p != null) {
                    total += p.getPrice() * item.getQuantity();
                }
            }
        }
        return total;
    }

    private List<OrderItem> getPendingOrderItems() {
        return orderItemRepo.getAllOrderItems();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
