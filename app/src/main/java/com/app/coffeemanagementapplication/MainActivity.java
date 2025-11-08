package com.app.coffeemanagementapplication;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

// 1. ⚠️ THÊM IMPORT
import androidx.core.splashscreen.SplashScreen;
import com.app.coffeemanagementapplication.models.RoleType;
import com.app.coffeemanagementapplication.activities.LoginActivity;
import com.app.coffeemanagementapplication.activities.ProfileActivity;
// (Import các Activity đích khác của bạn nếu cần)
// import com.app.coffeemanagementapplication.activities.StaffHomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        if (!MySharePrefers.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            String roleName = MySharePrefers.getCurrentUserRole();
            RoleType role = null;
            try {
                if (roleName != null) {
                    role = RoleType.valueOf(roleName);
                }
            } catch (Exception e) {
                role = null;
            }

            navigateToHomeScreen(role);
        }
        finish();
        return;
    }

    private void navigateToHomeScreen(RoleType role) {
        Intent intent;

        if (role == null) {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            MySharePrefers.clearLoginInfo();
        } else {
            switch (role) {
                case STAFF:
                    // SỬA TÊN ACTIVITY NÀY thành Activity chính của Staff
                    // intent = new Intent(MainActivity.this, StaffHomeActivity.class);
                    // (Tạm thời vẫn trỏ đến Profile để test)
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
                case ADMIN:
                    // SỬA TÊN ACTIVITY NÀY thành Activity chính của Admin
                    // intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                    // (Tạm thời vẫn trỏ đến Profile để test)
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
                case CUSTOMER:
                default:
                    // Trỏ đến ProfileActivity như logic test hiện tại
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}