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
import com.app.coffeemanagementapplication.models.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder> {

    public interface OnItemClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    private List<Product> productList;
    private Context context;
    private OnItemClickListener listener;

    public ProductAdminAdapter(List<Product> productList, Context context, OnItemClickListener listener) {
        this.productList = productList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(CurrencyUtils.formatVNCurrency(product.getPrice()));
        holder.txtDescription.setText(product.getDescription());
        holder.txtStatus.setText(product.isAvailable() ? "Còn hàng" : "Hết hàng");
        holder.txtStatus.setTextColor(product.isAvailable() ? 
            context.getResources().getColor(android.R.color.holo_green_dark) : 
            context.getResources().getColor(android.R.color.holo_red_dark));

        // Load image
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_product_placeholder);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(product));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(product));
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtDescription, txtStatus;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtDescription = itemView.findViewById(R.id.txtProductDescription);
            txtStatus = itemView.findViewById(R.id.txtProductStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
