package com.app.coffeemanagementapplication.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.databinding.ItemCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<String> items;
    private int selectedPosition = 0;
    public CategoryAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String text = items.get(position);
        holder.binding.txtCategory.setText(text);
        if (position == selectedPosition) {
            // Được chọn → màu đen, có gạch chân
            holder.binding.txtCategory.setTextColor(Color.parseColor("#000000"));
            holder.binding.txtCategory.setPaintFlags(
                    holder.binding.txtCategory.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
            );
        } else {
            // Không chọn → màu xám, không gạch chân
            holder.binding.txtCategory.setTextColor(Color.parseColor("#9E9E9E"));
            holder.binding.txtCategory.setPaintFlags(
                    holder.binding.txtCategory.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG)
            );
        }
        // 🔹 Sự kiện click để chọn item
        holder.binding.getRoot().setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}