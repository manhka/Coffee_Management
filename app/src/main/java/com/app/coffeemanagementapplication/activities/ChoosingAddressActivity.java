package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.ShippingAddressAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityChoosingAddressBinding;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;
import com.app.coffeemanagementapplication.services.AddressService;

import java.util.List;

public class ChoosingAddressActivity extends BaseActivity {
    private ActivityChoosingAddressBinding binding;
    private ShippingAddressAdapter adapter;
    private IAddressRepo addressRepo;
    private List<ShippingAddress> shippingAddressList;
    private int currentShippingAddressId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoosingAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addressRepo = new AddressService(this);
        shippingAddressList = addressRepo.getAddressesByUser(1);
        binding.btnSave.setVisibility(View.INVISIBLE);
        binding.btnSave.setEnabled(false);

        adapter = new ShippingAddressAdapter(shippingAddressList, this, position -> {
            currentShippingAddressId = shippingAddressList.get(position).getId();

            for (int i = 0; i < shippingAddressList.size(); i++) {
                shippingAddressList.get(i).setDefault(i == position);
            }
            adapter.notifyDataSetChanged();
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(true);
        });

        binding.rcvAddresses.setAdapter(adapter);
        binding.rcvAddresses.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressRepo.clearDefaultAddress(1);
                addressRepo.setDefaultAddress(currentShippingAddressId, 1);
                MySharePrefers.setAddressId(currentShippingAddressId);
                Intent intent= new Intent(ChoosingAddressActivity.this, PaymentOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosingAddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
        binding.imvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosingAddressActivity.this, PaymentOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}