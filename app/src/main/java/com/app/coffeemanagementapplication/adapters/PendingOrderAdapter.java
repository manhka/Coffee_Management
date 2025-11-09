package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.CartItemBinding;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.bumptech.glide.Glide;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderItems;
    private final IProductRepo productRepo;
    private OnSelectionChangedListener selectionChangedListener;
    public interface OnSelectionChangedListener {
        void onSelectionChanged();
    }
    private OnCartChangeListener cartChangeListener;
    private OnDeleteClickListener deleteClickListener;
    private OnOrderItemUpdateListener orderItemUpdateListener;

    // ====================== Interfaces ======================
    public interface OnCartChangeListener {
        void onCartUpdated(String total);
    }

    public interface OnDeleteClickListener {
        void onDelete(OrderItem item);
    }

    /** Callback dùng để cập nhật DB qua Repository */
    public interface OnOrderItemUpdateListener {
        void onUpdateOrderItem(OrderItem item);
    }

    // ====================== Setters ======================
    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void setOnOrderItemUpdateListener(OnOrderItemUpdateListener listener) {
        this.orderItemUpdateListener = listener;
    }
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }
    // ====================== Constructor ======================
    public PendingOrderAdapter(Context context, List<OrderItem> orderItems, IProductRepo productRepo) {
        this.context = context;
        this.orderItems = orderItems;
        this.productRepo = productRepo;
    }

    // ====================== ViewHolder ======================
    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        Product product = productRepo.getProductById(orderItem.getProductId());
        if (product == null) return;

        // ----- Gán dữ liệu sản phẩm -----
        holder.binding.txtName.setText(product.getName());
        holder.binding.txtDescription.setText(product.getDescription());
        holder.binding.txtPrice.setText(CurrencyUtils.formatVNCurrency(product.getPrice()));
        holder.binding.txtQuantity.setText(String.valueOf(orderItem.getQuantity()));

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.binding.imgProduct);

        // ----- Checkbox chọn sản phẩm -----
        holder.binding.cbSelect.setOnCheckedChangeListener(null); // tránh trigger cũ
        holder.binding.cbSelect.setChecked(orderItem.isSelected());
        holder.binding.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            orderItem.setSelected(isChecked);
            notifyItemChanged(holder.getAdapterPosition());
            updateTotalPrice();

            // Gọi callback cập nhật DB
            if (orderItemUpdateListener != null) {
                orderItemUpdateListener.onUpdateOrderItem(orderItem);
            }
            if (selectionChangedListener!= null){
                selectionChangedListener.onSelectionChanged();
            }
        });

        // ----- Nút tăng số lượng -----
        holder.binding.txtPlus.setOnClickListener(v -> {
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            holder.binding.txtQuantity.setText(String.valueOf(orderItem.getQuantity()));

            if (orderItem.isSelected()) updateTotalPrice();

            //  Gọi callback cập nhật DB
            if (orderItemUpdateListener != null) {
                orderItemUpdateListener.onUpdateOrderItem(orderItem);
            }
        });

        // ----- Nút giảm số lượng -----
        holder.binding.txtMinus.setOnClickListener(v -> {
            if (orderItem.getQuantity() > 1) {
                orderItem.setQuantity(orderItem.getQuantity() - 1);
                holder.binding.txtQuantity.setText(String.valueOf(orderItem.getQuantity()));

                if (orderItem.isSelected()) updateTotalPrice();

                //  Gọi callback cập nhật DB
                if (orderItemUpdateListener != null) {
                    orderItemUpdateListener.onUpdateOrderItem(orderItem);
                }
            }
        });

        // ----- Nút xóa -----
        holder.binding.btnDelete.setOnClickListener(view -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDelete(orderItem);
            }
        });
        if (position == orderItems.size() - 1) {
            updateTotalPrice();
        }
    }

    // ====================== Helper ======================
    private void updateTotalPrice() {
        if (cartChangeListener == null) return;

        double total = 0;
        for (OrderItem item : orderItems) {
            Product product = productRepo.getProductById(item.getProductId());
            if (product != null && item.isSelected()) {
                total +=product.getPrice() * (double)item.getQuantity();
            }
        }
        cartChangeListener.onCartUpdated(CurrencyUtils.formatVNCurrency(total));
    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    static class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding binding;

        public PendingOrderViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void removeItem(OrderItem item) {
        int position = orderItems.indexOf(item);
        if (position != -1) {
            orderItems.remove(position);
            notifyItemRemoved(position);
        }
    }
}
