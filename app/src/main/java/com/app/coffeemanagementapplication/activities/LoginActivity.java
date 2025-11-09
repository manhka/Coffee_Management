package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.coffeemanagementapplication.MainActivity;
import com.app.coffeemanagementapplication.databinding.CustomToastBinding;
import com.app.coffeemanagementapplication.AppConstants;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.databinding.ActivityLoginBinding;
import com.app.coffeemanagementapplication.databinding.CustomToastFailBinding;
import com.app.coffeemanagementapplication.models.RoleType;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.services.UserService;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userService = new UserService(this);

        setupListeners();
    }

    private void setupListeners() {
        binding.buttonLogin.setOnClickListener(v -> loginUser());
        binding.textSignUp.setOnClickListener(v -> navigateToRegister());
    }

    private void loginUser() {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        boolean isValid = true;
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { isValid = false; }
        if (TextUtils.isEmpty(password)) {  isValid = false; }
        if (!isValid) { return; }

        try {
            Users user = userService.login(email, password);

            if (user != null) {
                MySharePrefers.saveLoginInfo(user.getId(), user.getRole().name());

                showCustomToast("Đăng nhập thành công!");
                navigateToHome();
                finish();
            } else {
                showFailToast("Email hoặc mật khẩu không đúng.");
            }
        } catch (Exception e) {
            showFailToast("Đã xảy ra lỗi khi đăng nhập.");
            Log.e("LoginActivity", "Lỗi đăng nhập: " + e.getMessage());
        }
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    // Chuyển hướng màn hình theo vai trò
    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showCustomToast(String message) {
        CustomToastBinding binding = CustomToastBinding.inflate(getLayoutInflater());
        binding.toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
    }

    private void showFailToast(String message) {
        CustomToastFailBinding binding = CustomToastFailBinding.inflate(getLayoutInflater());
        binding.toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
    }
}