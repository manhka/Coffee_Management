package com.app.coffeemanagementapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.coffeemanagementapplication.fragments.CompletedOrdersFragment;
import com.app.coffeemanagementapplication.fragments.PaidOrdersFragment;
import com.app.coffeemanagementapplication.fragments.PendingOrdersFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {
    public OrderPagerAdapter(@NonNull Fragment fragment) {
        super(fragment); // Fragment cha
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new PendingOrdersFragment();
            case 1: return new PaidOrdersFragment();
            case 2: return new CompletedOrdersFragment();
            default: return new PendingOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
