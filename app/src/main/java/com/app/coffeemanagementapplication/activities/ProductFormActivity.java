package com.app.coffeemanagementapplication.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.services.CategoryService;
import com.app.coffeemanagementapplication.services.ProductService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductFormActivity extends AppCompatActivity {

    private EditText etName, etDescription, etPrice, etImageUrl;
    private Spinner spinnerCategory;
    private CheckBox cbAvailable;
    private MaterialButton btnSave;
    private ProductService productService;
    private CategoryService categoryService;
    private Product product;
    private MaterialToolbar toolbar;
    private List<Category> categoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Khởi tạo views
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etImageUrl = findViewById(R.id.etImageUrl);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        cbAvailable = findViewById(R.id.cbAvailable);
        btnSave = findViewById(R.id.btnSave);

        productService = new ProductService(this);
        categoryService = new CategoryService(this);

        // Load categories
        loadCategories();

        int productId = getIntent().getIntExtra("productId", -1);

        if (productId != -1) {
            toolbar.setTitle("Sửa Sản Phẩm");
            product = productService.getProductById(productId);
            if (product != null) {
                etName.setText(product.getName());
                etDescription.setText(product.getDescription());
                etPrice.setText(String.valueOf(product.getPrice()));
                etImageUrl.setText(product.getImageUrl());
                cbAvailable.setChecked(product.isAvailable());

                // Set selected category
                if (product.getCategoryId() != null) {
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).getId() == product.getCategoryId()) {
                            spinnerCategory.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            toolbar.setTitle("Thêm Sản Phẩm");
            product = null;
            cbAvailable.setChecked(true); // Mặc định là còn hàng
        }

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void loadCategories() {
        categoryList = categoryService.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        boolean isAvailable = cbAvailable.isChecked();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer categoryId = null;
        if (spinnerCategory.getSelectedItemPosition() >= 0 && categoryList.size() > 0) {
            categoryId = categoryList.get(spinnerCategory.getSelectedItemPosition()).getId();
        }

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (product == null) {
            // Thêm mới
            product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);
            product.setCategoryId(categoryId);
            product.setAvailable(isAvailable);
            product.setCreatedAt(currentTime);
            product.setUpdatedAt(currentTime);
            productService.insertProduct(product);
            Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            // Cập nhật
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);
            product.setCategoryId(categoryId);
            product.setAvailable(isAvailable);
            product.setUpdatedAt(currentTime);
            productService.updateProduct(product);
            Toast.makeText(this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
