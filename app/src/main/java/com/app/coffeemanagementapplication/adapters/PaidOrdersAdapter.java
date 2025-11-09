package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.bumptech.glide.Glide;

import java.util.List;

public class PaidOrdersAdapter extends RecyclerView.Adapter<PaidOrdersAdapter.PaidOrderViewHolder> {

    private List<Order> paidOrders;
    private Context context;
    private OnTrackOrderClickListener listener;
    private IOrderItemRepo orderItemRepo;
    private IProductRepo productRepo;

    public PaidOrdersAdapter(Context context, List<Order> paidOrders, IOrderItemRepo orderItemRepo, IProductRepo productRepo, OnTrackOrderClickListener listener) {
        this.context = context;
        this.paidOrders = paidOrders;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaidOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paid_order, parent, false);
        return new PaidOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaidOrderViewHolder holder, int position) {
        Order order = paidOrders.get(position);
        List<OrderItem> orderItems = orderItemRepo.getOrderItemsByOrderId(order.getId());

        int totalQuantity = 0;
        for (OrderItem item : orderItems) {
            totalQuantity += item.getQuantity();
        }

        if (orderItems != null && !orderItems.isEmpty()) {
            OrderItem firstItem = orderItems.get(0);
            Product product = productRepo.getProductById(firstItem.getProductId());

            if (product != null) {
                holder.txtName.setText(product.getName());
                holder.txtDescription.setText(product.getDescription());
                holder.txtPrice.setText(CurrencyUtils.formatVNCurrency(order.getTotalAmount()));
                holder.txtQuantity.setText("SL: " + totalQuantity);
                Glide.with(context)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(holder.imgProduct);
            }
        }

        holder.btnTrackOrder.setOnClickListener(v -> listener.onTrackOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return paidOrders.size();
    }

    public static class PaidOrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtPrice, txtQuantity;
        Button btnTrackOrder;

        public PaidOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
        }
    }

    public interface OnTrackOrderClickListener {
        void onTrackOrderClick(Order order);
    }
}
