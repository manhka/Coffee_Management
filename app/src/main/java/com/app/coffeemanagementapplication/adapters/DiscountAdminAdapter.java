package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.Discount;

import java.util.List;

public class DiscountAdminAdapter extends RecyclerView.Adapter<DiscountAdminAdapter.DiscountViewHolder> {

    public interface OnItemClickListener {
        void onEditClick(Discount discount);
        void onDeleteClick(Discount discount);
    }

    private List<Discount> discountList;
    private Context context;
    private OnItemClickListener listener;

    public DiscountAdminAdapter(List<Discount> discountList, Context context, OnItemClickListener listener) {
        this.discountList = discountList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_discount_admin, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.txtName.setText(discount.getName());
        holder.txtCode.setText("Code: " + discount.getCode());
        holder.txtDescription.setText(discount.getDescription());
        holder.txtDate.setText("Start: " + discount.getStartDate() + " | End: " + discount.getEndDate());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(discount));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(discount));
    }

    @Override
    public int getItemCount() {
        return discountList != null ? discountList.size() : 0;
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCode, txtDescription, txtDate;
        Button btnEdit, btnDelete;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtDiscountName);
            txtCode = itemView.findViewById(R.id.txtDiscountCode);
            txtDescription = itemView.findViewById(R.id.txtDiscountDescription);
            txtDate = itemView.findViewById(R.id.txtDiscountDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
