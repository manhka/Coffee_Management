package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.databinding.ItemPaymentOrderBinding;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.bumptech.glide.Glide;

import java.util.List;

public class PaymentOrderAdapter extends RecyclerView.Adapter<PaymentOrderAdapter.PaymentOrderViewHolder> {
    private List<OrderItem> orderItems;
    private IProductRepo productRepo;
    private Context context;

    public PaymentOrderAdapter(List<OrderItem> orderItems, Context context,IProductRepo productRepo) {
        this.orderItems = orderItems;
        this.context = context;
        this.productRepo= productRepo;
    }

    @NonNull
    @Override
    public PaymentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaymentOrderBinding binding = ItemPaymentOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaymentOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentOrderViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);

            Product product= productRepo.getProductById(orderItem.getProductId());
            holder.binding.txtName.setText(product.getName());
            holder.binding.txtDescription.setText(product.getDescription());
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(com.app.coffeemanagementapplication.R.drawable.ic_launcher_background)
                    .into(holder.binding.imgProduct);
            holder.binding.txtPrice.setText(CurrencyUtils.formatVNCurrency(product.getPrice()));
            holder.binding.txtQuantity.setText(String.valueOf(orderItem.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    static class PaymentOrderViewHolder extends RecyclerView.ViewHolder {
        ItemPaymentOrderBinding binding;

        public PaymentOrderViewHolder(@NonNull ItemPaymentOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
