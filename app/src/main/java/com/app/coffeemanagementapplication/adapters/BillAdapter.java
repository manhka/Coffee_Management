package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IProductRepo;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private Context context;
    private List<OrderItem> orderItems;
    private IProductRepo productRepo;

    public BillAdapter(Context context, List<OrderItem> orderItems, IProductRepo productRepo) {
        this.context = context;
        this.orderItems = orderItems;
        this.productRepo = productRepo;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        Product product = productRepo.getProductById(item.getProductId());

        if (product != null) {
            holder.tvProductName.setText(product.getName());
            holder.tvProductQuantity.setText("x" + item.getQuantity());
            holder.tvProductPrice.setText(CurrencyUtils.formatVNCurrency(item.getUnitPrice() * item.getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
