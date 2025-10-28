package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ActivityChoosingAddressBinding;

public class ChoosingAddressActivity extends BaseActivity {
    private ActivityChoosingAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoosingAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChoosingAddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
        binding.imvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}