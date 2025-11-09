package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.coffeemanagementapplication.databinding.CustomToastBinding;
import com.app.coffeemanagementapplication.databinding.CustomToastFailBinding;
import com.app.coffeemanagementapplication.databinding.ActivityRegisterBinding;
import com.app.coffeemanagementapplication.models.RoleType;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userService = new UserService(getApplication());
        setupListeners();
    }

    private void setupListeners() {
        binding.buttonRegister.setOnClickListener(v -> registerUser());
        binding.textSignIn.setOnClickListener(v -> navigateToLogin());
    }

    // Validation
    private void registerUser() {
        String fullName = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        boolean isValid = true;
        if (TextUtils.isEmpty(fullName)) {
            binding.layoutInputName.setError("Họ và tên không được để trống");
            isValid = false;
        } else {
            binding.layoutInputName.setError(null);
        }

        String emailError = validateEmail(email);
        if (emailError != null) {
            binding.layoutInputEmail.setError(emailError);
            isValid = false;
        } else {
            binding.layoutInputEmail.setError(null);
        }

        String passwordError = validatePassword(password);
        if (passwordError != null) {
            binding.layoutInputPassword.setError(passwordError);
            isValid = false;
        } else {
            binding.layoutInputPassword.setError(null);
        }

        if (!isValid) {
            return;
        }

        try {
            boolean exists = userService.isEmailExists(email);

            if (exists) {
                binding.layoutInputEmail.setError("Email đã tồn tại");
                showFailToast("Email đã được sử dụng.");
            } else {
                binding.layoutInputEmail.setError(null);

                Users newUser = new Users(fullName, email, password, RoleType.CUSTOMER, "");

                userService.insertUser(newUser);

                showCustomToast();
                navigateToLogin();
                finish();
            }
        } catch (Exception e) {
            showFailToast("Đã xảy ra lỗi khi đăng ký.");
            Log.e("RegisterActivity", "Error registering user", e);
        }
    }

    // Validation trường hợp riêng cho email
    private String validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "Email không được để trống";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ";
        }

        String lowerCaseEmail = email.toLowerCase(Locale.ROOT);

        if (lowerCaseEmail.endsWith("@gmail.co")) {
            return "Email không hợp lệ";
        }

        if (lowerCaseEmail.endsWith("@gmial.com") || lowerCaseEmail.endsWith("@gamil.com")) {
            return "Email không hợp lệ";
        }
        return null;
    }

    // Validation trường hợp riêng cho mật khẩu
    private String validatePassword(String password) {

        if (TextUtils.isEmpty(password)) {
            return "Mật khẩu không được để trống";
        }

        List<String> errors = new ArrayList<>();

        String specialChars = "!@#$%^&*()_+-=[]{}|;':,./<>?";
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        if (password.length() < 8) {
            errors.add("ít nhất 8 kí tự");
        }

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.contains(String.valueOf(c))) {
                hasSpecial = true;
            }
        }
        if (!hasUpper) {
            errors.add("1 chữ hoa");
        }
        if (!hasDigit) {
            errors.add("1 chữ số");
        }
        if (!hasSpecial) {
            errors.add("1 kí tự đặc biệt");
        }
        if (errors.isEmpty()) {
            return null;
        } else {
            return "Mật khẩu phải bao gồm ít nhất: " + TextUtils.join(", ", errors);
        }
    }

    // Toast cho trường hợp thành công
    private void showCustomToast() {
        CustomToastBinding binding = CustomToastBinding.inflate(getLayoutInflater());
        binding.toastText.setText("Đăng ký thành công!");

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
    }

    // Toast cho trường hợp thất bại
    private void showFailToast(String message) {
        CustomToastFailBinding binding = CustomToastFailBinding.inflate(getLayoutInflater());
        binding.toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
    }

    // Điều hướng về trang Đăng Nhập
    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}