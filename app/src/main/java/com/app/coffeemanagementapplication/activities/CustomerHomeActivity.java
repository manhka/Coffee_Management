package com.app.coffeemanagementapplication.activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.fragment.app.Fragment;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ActivityCustomerHomeBinding;
import com.app.coffeemanagementapplication.fragments.CustomerHomeFragment;
import com.app.coffeemanagementapplication.fragments.HistoryFragment;
import com.app.coffeemanagementapplication.fragments.ProfileFragment;


public class CustomerHomeActivity extends BaseActivity {

    private ActivityCustomerHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CustomerHomeFragment())
                    .commit();
        }
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == com.app.coffeemanagementapplication.R.id.nav_home) {
                selectedFragment = new CustomerHomeFragment();
            } else if (id == com.app.coffeemanagementapplication.R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            } else if (id == com.app.coffeemanagementapplication.R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(com.app.coffeemanagementapplication.R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    }



