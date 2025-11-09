package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.databinding.ItemStaffOrderBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffOrderAdapter extends RecyclerView.Adapter<StaffOrderAdapter.StaffOrderViewHolder> {

    private final Context context;
    private final List<Order> orders;
    private final OrderRepository orderRepository;
    private OnOrderClickListener orderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.orderClickListener = listener;
    }

    public StaffOrderAdapter(Context context, List<Order> orders, OrderRepository orderRepository) {
        this.context = context;
        this.orders = orders;
        this.orderRepository = orderRepository;
    }

    @NonNull
    @Override
    public StaffOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStaffOrderBinding binding = ItemStaffOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new StaffOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffOrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set order ID
        holder.binding.txtOrderId.setText("#ORD" + String.format("%03d", order.getId()));

        // Set order status
        setOrderStatus(holder, order.getOrderStatus());

        // Set customer name (you may need to fetch from Users table)
        holder.binding.txtCustomerName.setText("Khách hàng #" + order.getUserId());

        // Set order time
        holder.binding.txtOrderTime.setText(formatDate(order.getOrderDate()));

        // Set payment method
        String paymentMethod = getPaymentMethodText(order.getPaymentMethod());
        holder.binding.txtPaymentMethod.setText(paymentMethod);

        // Set total price
        holder.binding.txtTotalPrice.setText("Tổng tiền: " + CurrencyUtils.formatVNCurrency(order.getTotalAmount()));

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (orderClickListener != null) {
                orderClickListener.onOrderClick(order);
            }
        });
    }

    private void setOrderStatus(StaffOrderViewHolder holder, String status) {
        int backgroundColor;
        int textColor;
        String statusText;

        switch (status) {
            case "PENDING":
                backgroundColor = 0xFFFFF3CD;
                textColor = 0xFF856404;
                statusText = "Chờ xác nhận";
                break;
            case "PREPARING":
                backgroundColor = 0xFFE2E3E5;
                textColor = 0xFF383D41;
                statusText = "Đang chuẩn bị";
                break;
            case "COMPLETED":
                backgroundColor = 0xFFD4EDDA;
                textColor = 0xFF155724;
                statusText = "Hoàn thành";
                break;
            case "DELIVERED":
                backgroundColor = 0xFFC3E6CB;
                textColor = 0xFF155724;
                statusText = "Đã giao";
                break;
            default:
                backgroundColor = 0xFFF8D7DA;
                textColor = 0xFF721C24;
                statusText = "Đã hủy";
                break;
        }

        holder.binding.txtOrderStatus.setBackgroundColor(backgroundColor);
        holder.binding.txtOrderStatus.setTextColor(textColor);
        holder.binding.txtOrderStatus.setText(statusText);
    }

    private String getPaymentMethodText(String method) {
        switch (method) {
            case "CASH":
                return "Tiền mặt";
            case "QR":
                return "Mã QR";
            case "E_WALLET":
                return "Ví điện tử";
            default:
                return method;
        }
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    static class StaffOrderViewHolder extends RecyclerView.ViewHolder {
        ItemStaffOrderBinding binding;

        public StaffOrderViewHolder(@NonNull ItemStaffOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
