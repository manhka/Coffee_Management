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
import com.app.coffeemanagementapplication.databinding.ItemCusDiscountBinding;
import com.app.coffeemanagementapplication.models.Discount;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {
    private List<Discount> discountList;
    private Context context;
    private OnItemClickListener itemClickListener;
    private double totalOrder = 0;
    private int selectedPosition = -1;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public DiscountAdapter(List<Discount> discountList, Context context, double totalOrder, OnItemClickListener itemClickListener) {
        this.discountList = discountList;
        this.context = context;
        this.totalOrder = totalOrder;

        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCusDiscountBinding binding = ItemCusDiscountBinding.inflate(LayoutInflater.from(context), parent, false);
        return new DiscountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.binding.txtDiscountName.setText(discount.getName());
        holder.binding.txtDiscount.setText(discount.getCode());
        holder.binding.txtDiscountDescription.setText(discount.getDescription());
        holder.binding.txtStartDate.setText("Bắt đầu: " + discount.getStartDate());
        holder.binding.txtEndDate.setText("Hết hạn: " + discount.getEndDate());
        boolean isEligible = totalOrder >= discount.getCondition();        // Kiểm tra điều kiện
        if (isEligible) {
            holder.binding.cardDiscount.setEnabled(true);
            holder.binding.cardDiscount.setAlpha(1f);
            holder.binding.cardDiscount.setClickable(true);
            holder.binding.txtDiscountHint.setVisibility(View.GONE);
        } else {
            holder.binding.cardDiscount.setEnabled(false);
            holder.binding.cardDiscount.setAlpha(0.5f);
            holder.binding.cardDiscount.setClickable(false);
            holder.binding.txtDiscountHint.setVisibility(View.VISIBLE);
            holder.binding.txtDiscountHint.setText(
                    "Mua thêm " + CurrencyUtils.formatVNCurrency(discount.getCondition() - totalOrder)
                            + " để áp dụng mã này"
            );
        }

        // Hiển thị icon chọn/bỏ chọn
        if (isEligible) {
            if (position == selectedPosition) {
                // Đang được chọn
                holder.binding.imvIsSelected.setVisibility(View.VISIBLE);
                holder.binding.imvIsSelected.setImageResource(R.drawable.checked);
            } else {
                // Chưa chọn nhưng đủ điều kiện → hiển thị unchecked
                if (discount.isDefault()){
                    holder.binding.imvIsSelected.setVisibility(View.VISIBLE);
                    holder.binding.imvIsSelected.setImageResource(R.drawable.checked);
                }else {
                    holder.binding.imvIsSelected.setVisibility(View.VISIBLE);
                    holder.binding.imvIsSelected.setImageResource(R.drawable.unchecked);
                }
            }
        } else {
            // Không đủ điều kiện → ẩn luôn icon
            holder.binding.imvIsSelected.setVisibility(View.GONE);
        }

        //  Xử lý click
        holder.itemView.setOnClickListener(v -> {
            if (isEligible) {
                selectedPosition = position;
                notifyDataSetChanged(); // cập nhật toàn bộ danh sách
                itemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return discountList != null ? discountList.size() : 0;
    }


    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        ItemCusDiscountBinding binding;

        public DiscountViewHolder(ItemCusDiscountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
