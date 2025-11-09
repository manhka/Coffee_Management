package com.app.coffeemanagementapplication.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.fragments.AdminDashboardFragment;
import com.app.coffeemanagementapplication.fragments.DiscountListFragment;
import com.app.coffeemanagementapplication.fragments.ProductListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        BottomNavigationView bottomNav = findViewById(R.id.adminBottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                selectedFragment = new AdminDashboardFragment();
            } else if (id == R.id.nav_discount) {
                selectedFragment = new DiscountListFragment();
            } else if (id == R.id.nav_product) {
                selectedFragment = new ProductListFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
        // Mặc định hiển thị Dashboard
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AdminDashboardFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_dashboard);
        }
    }
}
