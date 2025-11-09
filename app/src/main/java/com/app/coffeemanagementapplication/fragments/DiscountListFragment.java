package com.app.coffeemanagementapplication.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.activities.DiscountFormActivity;
import com.app.coffeemanagementapplication.adapters.DiscountAdminAdapter;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.services.DiscountService;

import java.util.List;

public class DiscountListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DiscountAdminAdapter adapter;
    private DiscountService discountService;
    private List<Discount> discountList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discount_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerDiscount);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        discountService = new DiscountService(getContext());

        loadDiscounts();
        view.findViewById(R.id.btnAddDiscount).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DiscountFormActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.btnAddDiscount).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DiscountFormActivity.class);
            startActivity(intent);
        });
    }

    private void loadDiscounts() {
        discountList = discountService.getAllDiscounts();

        adapter = new DiscountAdminAdapter(discountList, getContext(), new DiscountAdminAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Discount discount) {
                Intent intent = new Intent(getContext(), DiscountFormActivity.class);
                intent.putExtra("discountId", discount.getId());
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(Discount discount) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Delete discount " + discount.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            discountService.deleteDiscount(discount);
                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            loadDiscounts();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDiscounts(); // Reload khi quay lại fragment
    }
}
