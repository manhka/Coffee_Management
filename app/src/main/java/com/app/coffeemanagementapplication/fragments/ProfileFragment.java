package com.app.coffeemanagementapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.activities.EditProfileActivity;
import com.app.coffeemanagementapplication.activities.LoginActivity;
import com.app.coffeemanagementapplication.databinding.FragmentProfileBinding;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.services.AddressService;
import com.app.coffeemanagementapplication.services.UserService;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserService userService;
    private AddressService addressService;
    private int currentUserId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        userService = new UserService(requireContext().getApplicationContext());
        addressService = new AddressService(requireContext().getApplicationContext());

        currentUserId = MySharePrefers.getCurrentUserId();

        if(currentUserId == -1){
            navigateToLogin();
        }else{
            setupListeners();
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(currentUserId != -1){
            loadUserProfile();
            loadDefaultAddress();
        }
        binding.imvBtnBack.setOnClickListener(v -> requireActivity().finish());
    }

    private void setupListeners(){
        binding.buttonEditProfile.setOnClickListener(v -> navigateToEditProfile());
        binding.buttonLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserProfile(){
        try{
            Users user = userService.getUserById(currentUserId);
            if(user != null){
                binding.textViewFullName.setText(user.getFullName());
                binding.textViewEmail.setText(user.getEmail());
                binding.textViewPhone.setText(
                        user.getPhone() != null && !user.getPhone().isEmpty()
                                ? user.getPhone() : "Chưa cập nhật"
                );
            }else{
                Toast.makeText(getContext(), "Không thể tải thông tin!", Toast.LENGTH_SHORT).show();
                logoutUser();
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Lỗi tải thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDefaultAddress(){
        try{
            ShippingAddress defaultAddress = addressService.getDefaultAddressByUserId(currentUserId);

            if(defaultAddress != null){
                String fullAddress = defaultAddress.getAddressLine() + ", " +
                        defaultAddress.getWard() + ", " +
                        defaultAddress.getDistrict() + ", " +
                        defaultAddress.getCity();
                binding.textViewDefaultAddress.setText(fullAddress);
            }else{
                binding.textViewDefaultAddress.setText("Chưa có địa chỉ");
            }

        }catch (Exception e){
            Toast.makeText(getContext(), "Lỗi tải địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToEditProfile(){
        Intent intent = new Intent(requireContext(), EditProfileActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    private void logoutUser(){
        MySharePrefers.clearLoginInfo();
        navigateToLogin();
    }

    private void navigateToLogin(){
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

}