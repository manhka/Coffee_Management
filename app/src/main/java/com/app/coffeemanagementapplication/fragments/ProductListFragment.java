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
import com.app.coffeemanagementapplication.activities.ProductFormActivity;
import com.app.coffeemanagementapplication.adapters.ProductAdminAdapter;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class ProductListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdminAdapter adapter;
    private ProductService productService;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productService = new ProductService(getContext());

        loadProducts();

        view.findViewById(R.id.btnAddProduct).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProductFormActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts() {
        productList = productService.getAllProducts();

        adapter = new ProductAdminAdapter(productList, getContext(), new ProductAdminAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Product product) {
                Intent intent = new Intent(getContext(), ProductFormActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Product product) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Xóa sản phẩm " + product.getName() + "?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            productService.deleteProductById(product.getId());
                            Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                            loadProducts();
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts(); // Reload khi quay lại fragment
    }
}
