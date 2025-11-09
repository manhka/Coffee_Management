package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.PaymentItemBinding;
import com.app.coffeemanagementapplication.models.Payment;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder> {
    private List<Payment> paymentList;
    private Context context;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public PaymentMethodAdapter(List<Payment> paymentList, Context context, OnItemClickListener itemClickListener) {
        this.paymentList = paymentList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PaymentMethodAdapter.PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentItemBinding binding = PaymentItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PaymentMethodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodAdapter.PaymentMethodViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.binding.txtName.setText(payment.getPaymentName());
        holder.binding.txtDescription.setText(payment.getPaymentDescription());
        holder.binding.imvPayment.setImageResource(payment.getImageUrl());
        if (payment.isDefault()) {
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
        return paymentList != null ? paymentList.size() : 0;
    }

    public static class PaymentMethodViewHolder extends RecyclerView.ViewHolder {
        PaymentItemBinding binding;

        public PaymentMethodViewHolder(PaymentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
