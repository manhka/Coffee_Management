package com.app.coffeemanagementapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.OrderPagerAdapter;
import com.app.coffeemanagementapplication.databinding.FragmentHistoryBinding;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public HistoryFragment() {
        // Required empty public constructor
    }


    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OrderPagerAdapter adapter = new OrderPagerAdapter(this);
        binding.viewPagerOrders.setAdapter(adapter);

        new TabLayoutMediator(binding.tabOrderStatus, binding.viewPagerOrders,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Chưa thanh toán"); break;
                        case 1: tab.setText("Đã thanh toán"); break;
                        case 2: tab.setText("Hoàn thành"); break;
                    }
                }).attach();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}