package com.app.coffeemanagementapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.activities.PaymentOrderActivity;
import com.app.coffeemanagementapplication.adapters.PendingOrderAdapter;
import com.app.coffeemanagementapplication.databinding.FragmentPendingOrdersBinding;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.OrderItemStatus;
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productRepo = new ProductService(requireContext());
        orderItemRepo = new OrderItemService(requireContext());

        setupRecyclerView();

        // Gán sự kiện click một lần duy nhất
        binding.btnContinues.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PaymentOrderActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContinueButton();
    }

    private void updateContinueButton() {
        List<OrderItem> checkedOrderItems = orderItemRepo.getAllSelectedOrderItemsByStatus(OrderItemStatus.PENDING.getValue());

        if (checkedOrderItems.isEmpty()) {
            binding.btnContinues.setEnabled(false);
            binding.btnContinues.setAlpha(0.5f);
        } else {
            binding.btnContinues.setEnabled(true);
            binding.btnContinues.setAlpha(1.0f);
        }
    }

    private void setupRecyclerView() {
        orderItems = orderItemRepo.getAllOrderItemsByStatus(OrderItemStatus.PENDING.getValue());
        adapter = new PendingOrderAdapter(requireContext(), orderItems, productRepo);
        binding.rcvPendingOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvPendingOrders.setAdapter(adapter);

        // Khi tick/untick sản phẩm
        adapter.setOnOrderItemUpdateListener(item -> {
            orderItemRepo.updateOrderItem(item.getId(), item.getQuantity(), item.isSelected());
            updateTotalPrice();
        });

        // Khi thay đổi tổng tiền (callback từ adapter)
        adapter.setOnCartChangeListener(totalStr -> {
            binding.txtTotalPrice.setText(totalStr);
        });

        // Khi xóa sản phẩm
        adapter.setOnDeleteClickListener(item -> {
            orderItemRepo.deleteOrderItemById(item.getId());
            adapter.removeItem(item);
            updateTotalPrice();
        });
        adapter.setOnSelectionChangedListener(() -> {
            updateContinueButton();
        });
        //  Cập nhật tổng tiền ngay khi vào màn
        updateTotalPrice();
    }

    /**
     * Tính tổng tiền dựa trên dữ liệu mới nhất trong DB
     */
    private void updateTotalPrice() {
        double total = calculateTotalFromDb();
        binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
    }

    /**
     * Đọc lại toàn bộ dữ liệu từ DB và tính tổng (đảm bảo chính xác)
     */
    private double calculateTotalFromDb() {
        double total = 0;
        List<OrderItem> items = orderItemRepo.getAllOrderItemsByStatus(OrderItemStatus.PENDING.getValue());
        for (OrderItem item : items) {
            if (item.isSelected()) {
                Product product = productRepo.getProductById(item.getProductId());
                if (product != null) {
                    total += product.getPrice() * item.getQuantity();
                }
            }
        }
        return total;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
