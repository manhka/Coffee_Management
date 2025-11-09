package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.coffeemanagementapplication.AppConstants;
import com.app.coffeemanagementapplication.activities.LoginActivity;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.databinding.ActivityProfileBinding;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.services.AddressService;
import com.app.coffeemanagementapplication.services.UserService;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private UserService userService;
    private AddressService addressService;
    private int currentUserId = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userService = new UserService(getApplication());
        addressService = new AddressService(getApplication());

        currentUserId = MySharePrefers.getCurrentUserId();

        if (currentUserId == -1) {
            navigateToLogin();
            return;
        }

        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUserId != -1) {
            loadUserProfile();
            loadDefaultAddress();
        }
    }


    private void setupListeners() {
        binding.buttonEditProfile.setOnClickListener(v -> navigateToEditProfile());
        binding.buttonLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserProfile() {
        if (binding == null) return;

        try {
            Users user = userService.getUserById(currentUserId);

            if (binding == null) return;

            if (user != null) {
                binding.textViewFullName.setText(user.getFullName() != null ? user.getFullName() : "N/A");
                binding.textViewEmail.setText(user.getEmail());
                binding.textViewPhone.setText(user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "Chưa cập nhật");
            } else {
                Toast.makeText(this, "Không thể tải thông tin.", Toast.LENGTH_SHORT).show();
                logoutUser();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải thông tin người dùng.", Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity", "Error loading user profile", e);
        }
    }

    private void loadDefaultAddress() {
        if (binding == null) return;
        try {
            ShippingAddress defaultAddress = addressService.getDefaultAddressByUserId(currentUserId);
            if (binding == null) return;

            if (defaultAddress != null) {
                String fullAddress = defaultAddress.getAddressLine() + ", " +
                        defaultAddress.getWard() + ", " +
                        defaultAddress.getDistrict() + ", " +
                        defaultAddress.getCity();
                binding.textViewDefaultAddress.setText(fullAddress);
            } else {
                binding.textViewDefaultAddress.setText("Chưa có địa chỉ");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải địa chỉ.", Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity", "Error loading default address", e);
        }
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    private void logoutUser() {
        MySharePrefers.clearLoginInfo();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}