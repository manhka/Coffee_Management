package com.app.coffeemanagementapplication.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.databinding.ItemCategoryBinding;
import com.app.coffeemanagementapplication.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categories;
    private int selectedPosition = 0;
    private OnCategoryClickListener listener;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category); // trả ra Category thật
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
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
        Category category = categories.get(position);
        holder.binding.txtCategory.setText(category.getName());

        // Highlight item được chọn
        if (position == selectedPosition) {
            holder.binding.txtCategory.setTextColor(Color.BLACK);
            holder.binding.txtCategory.setPaintFlags(
                    holder.binding.txtCategory.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
            );
        } else {
            holder.binding.txtCategory.setTextColor(Color.parseColor("#9E9E9E"));
            holder.binding.txtCategory.setPaintFlags(
                    holder.binding.txtCategory.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG)
            );
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
