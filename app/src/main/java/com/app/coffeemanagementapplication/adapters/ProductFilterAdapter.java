package com.app.coffeemanagementapplication.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ItemFilterBinding;
import com.app.coffeemanagementapplication.models.ProductFilter;

import java.util.List;

public class ProductFilterAdapter extends RecyclerView.Adapter<ProductFilterAdapter.FilterViewHolder> {

    private final List<ProductFilter> filters;
    private int selectedPosition = -1; // Mặc định chọn "Tất cả"
    private OnFilterClickListener listener; // interface callback

    public ProductFilterAdapter(List<ProductFilter> filters) {
        this.filters = filters;
    }

    public interface OnFilterClickListener {
        void onFilterClick(String filterName);
    }

    public void setOnFilterClickListener(OnFilterClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterBinding binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        ProductFilter filter = filters.get(position);

        holder.binding.imgIcon.setImageResource(filter.getIconResId());
        holder.binding.tvName.setText(filter.getName());

        if (position == selectedPosition) {
            holder.binding.layoutFilter.setBackgroundResource(R.drawable.bg_filter_active);
            holder.binding.tvName.setTextColor(Color.parseColor("#000000"));
            holder.binding.imgIcon.setColorFilter(Color.parseColor("#4CAF50"));
        } else {
            holder.binding.layoutFilter.setBackgroundResource(R.drawable.bg_filter_inactive);
            holder.binding.tvName.setTextColor(Color.parseColor("#9E9E9E"));
            holder.binding.imgIcon.setColorFilter(Color.parseColor("#9E9E9E"));
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            int old = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(old);
            notifyItemChanged(selectedPosition);

            // Gọi listener ra bên ngoài
            if (listener != null) {
                listener.onFilterClick(filter.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        ItemFilterBinding binding;
        public FilterViewHolder(ItemFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
