package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ItemAddressBinding;
import com.app.coffeemanagementapplication.models.ShippingAddress;

import java.util.List;

public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ShippingAddressViewHolder> {
    private List<ShippingAddress> shippingAddressList;
    private Context context;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public ShippingAddressAdapter(List<ShippingAddress> shippingAddressList, Context context, OnItemClickListener itemClickListener) {
        this.shippingAddressList = shippingAddressList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ShippingAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ShippingAddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressViewHolder holder, int position) {
        ShippingAddress shippingAddress = shippingAddressList.get(position);
        holder.binding.tvFullName.setText(shippingAddress.getFullName());
        holder.binding.tvPhone.setText(shippingAddress.getPhone());
        holder.binding.tvFullAddress.setText(shippingAddress.getAddressLine() + ", " + shippingAddress.getCity() + ", " + shippingAddress.getDistrict() + ", " + shippingAddress.getWard());
        holder.binding.tvNote.setText(shippingAddress.getNote());
        if (shippingAddress.isDefault()) {
            holder.binding.imvDefault.setImageResource(R.drawable.checked);
        } else {
            holder.binding.imvDefault.setImageResource(R.drawable.unchecked);
        }

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return shippingAddressList != null ? shippingAddressList.size() : 0;
    }

    public static class ShippingAddressViewHolder extends RecyclerView.ViewHolder {
        ItemAddressBinding binding;

        public ShippingAddressViewHolder(ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
