package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.PaymentMethodAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityPaymentMethodBinding;
import com.app.coffeemanagementapplication.models.Payment;
import com.app.coffeemanagementapplication.repositories.IPaymentRepo;
import com.app.coffeemanagementapplication.services.PaymentService;

import java.util.List;

public class PaymentMethodActivity extends BaseActivity {
    private ActivityPaymentMethodBinding binding;
    private List<Payment> paymentList;
    private PaymentMethodAdapter adapter;
    private IPaymentRepo paymentRepo;
    private int currentPaymentMethodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentRepo = new PaymentService(this);
        paymentList = paymentRepo.getAllPaymentMethods();
        binding.btnSave.setVisibility(View.INVISIBLE);
        binding.btnSave.setEnabled(false);

        adapter = new PaymentMethodAdapter(paymentList, this, position -> {
            currentPaymentMethodId = paymentList.get(position).getId();
            for (int i = 0; i < paymentList.size(); i++) {
                paymentList.get(i).setDefault(i == position);
            }
            adapter.notifyDataSetChanged();
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(true);
        });
        binding.rcvPaymentMethods.setAdapter(adapter);
        binding.rcvPaymentMethods.setLayoutManager(new LinearLayoutManager(this));
        binding.imvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentRepo.clearDefaultPaymentMethod();
                paymentRepo.setDefaultPaymentMethod(currentPaymentMethodId);
                MySharePrefers.setPaymentMethodId(currentPaymentMethodId);
                Intent intent= new Intent(PaymentMethodActivity.this, PaymentOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}