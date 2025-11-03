package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.activities.ProductDetailActivity;
import com.app.coffeemanagementapplication.databinding.ItemProductBinding;
import com.app.coffeemanagementapplication.models.ProductRating;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private  List<ProductRating> productRatings;

    public ProductAdapter(Context context, List<ProductRating> productRatings) {
        this.context = context;
        this.productRatings = productRatings;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductRating pr = productRatings.get(position);
        holder.binding.txtName.setText(pr.getProduct().getName());
        holder.binding.txtDescription.setText(pr.getProduct().getDescription());
        holder.binding.txtPrice.setText(CurrencyUtils.formatVNCurrency(pr.getProduct().getPrice()));
        holder.binding.txtRatingValue.setText(String.format("Rating: %.1f (%d)", pr.getAverageRating(), pr.getTotalFeedback()));
        holder.binding.ratingBar.setRating((float) pr.getAverageRating());
        Glide.with(context)
                .load(pr.getProduct().getImageUrl())
                .placeholder(com.app.coffeemanagementapplication.R.drawable.ic_launcher_background)
                .into(holder.binding.imgProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId", pr.getProduct().getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productRatings.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateList(List<ProductRating> newList) {
        this.productRatings = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}