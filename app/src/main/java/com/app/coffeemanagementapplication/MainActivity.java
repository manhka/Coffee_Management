package com.app.coffeemanagementapplication;

import android.os.Bundle;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.core.splashscreen.SplashScreen;

import com.app.coffeemanagementapplication.activities.AdminHomeActivity;
import com.app.coffeemanagementapplication.activities.CustomerHomeActivity;
import com.app.coffeemanagementapplication.activities.StaffHomeActivity;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

                    intent = new Intent(MainActivity.this, StaffHomeActivity.class);
                    break;
                case ADMIN:

                    intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                    break;
                case CUSTOMER:
                    intent = new Intent(MainActivity.this, CustomerHomeActivity.class);
                    break;
                default:
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}