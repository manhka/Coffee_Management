package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.MyApplication;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.activities.FeedbackActivity;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.bumptech.glide.Glide;

import java.util.List;

public class CompletedOrdersAdapter extends RecyclerView.Adapter<CompletedOrdersAdapter.CompletedOrderViewHolder> {

    private List<Order> completedOrders;
    private Context context;
    private IOrderItemRepo orderItemRepo;
    private IProductRepo productRepo;

    public CompletedOrdersAdapter(Context context, List<Order> completedOrders, IOrderItemRepo orderItemRepo, IProductRepo productRepo) {
        this.context = context;
        this.completedOrders = completedOrders;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
    }

    @NonNull
    @Override
    public CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_completed_order, parent, false);
        return new CompletedOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderViewHolder holder, int position) {
        Order order = completedOrders.get(position);
        List<OrderItem> orderItems = orderItemRepo.getOrderItemsByOrderId(order.getId());

        if (orderItems != null && !orderItems.isEmpty()) {
            OrderItem firstItem = orderItems.get(0);
            Product product = productRepo.getProductById(firstItem.getProductId());

            if (product != null) {
                holder.txtName.setText(product.getName());
                holder.txtDescription.setText(product.getDescription());
                holder.txtPrice.setText(CurrencyUtils.formatVNCurrency(product.getPrice()));
                Glide.with(context)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(holder.imgProduct);

                // Set OnClickListener to open FeedbackActivity without extras
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, FeedbackActivity.class);
                    context.startActivity(intent);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return completedOrders.size();
    }

    public static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDescription, txtPrice;

        public CompletedOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}
