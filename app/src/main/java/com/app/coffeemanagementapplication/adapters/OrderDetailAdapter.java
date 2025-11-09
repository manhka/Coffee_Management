package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private Context context;
    private List<OrderItem> orderItems;
    private IProductRepo productRepo;

    public OrderDetailAdapter(Context context, List<OrderItem> orderItems, IProductRepo productRepo) {
        this.context = context;
        this.orderItems = orderItems;
        this.productRepo = productRepo;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail_product, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        Product product = productRepo.getProductById(item.getProductId());

        if (product != null) {
            holder.txtName.setText(product.getName());
            holder.txtQuantity.setText("Số lượng: " + item.getQuantity());
            Glide.with(context)
                    .load(product.getImageUrl())
                    .into(holder.imgProduct);
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtQuantity;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }
    }
}
