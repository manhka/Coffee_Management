package com.app.coffeemanagementapplication.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ActivityUpdateOrderStatusBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.repositories.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateOrderStatusActivity extends BaseActivity {

    private ActivityUpdateOrderStatusBinding binding;
    private OrderRepository orderRepository;
    private int orderId;
    private Order currentOrder;
    private String selectedStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateOrderStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId == -1) {
            finish();
            return;
        }

        initRepository();
        setupListeners();
        loadOrderStatus();
    }

    private void initRepository() {
        orderRepository = new OrderRepository(
                DatabaseClient.getInstance(this).getAppDatabase().orderDao()
        );
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnCancel.setOnClickListener(v -> finish());

        // Setup radio button listeners
        setupStatusOptionListener(binding.layoutConfirmed, binding.radioConfirmed, "CONFIRMED");
        setupStatusOptionListener(binding.layoutPreparing, binding.radioPreparing, "PREPARING");
        setupStatusOptionListener(binding.layoutCompleted, binding.radioCompleted, "COMPLETED");
        setupStatusOptionListener(binding.layoutDelivered, binding.radioDelivered, "DELIVERED");

        binding.btnConfirmUpdate.setOnClickListener(v -> {
            if (selectedStatus != null) {
                updateOrderStatus();
            }
        });
    }

    private void setupStatusOptionListener(View layout, RadioButton radioButton, String status) {
        layout.setOnClickListener(v -> {
            selectedStatus = status;
            radioButton.setChecked(true);
            clearOtherRadioButtons(radioButton);
            binding.noteSection.setVisibility(View.VISIBLE);
            enableConfirmButton();
        });

        radioButton.setOnClickListener(v -> {
            selectedStatus = status;
            clearOtherRadioButtons(radioButton);
            binding.noteSection.setVisibility(View.VISIBLE);
            enableConfirmButton();
        });
    }

    private void clearOtherRadioButtons(RadioButton selectedButton) {
        if (selectedButton != binding.radioConfirmed) binding.radioConfirmed.setChecked(false);
        if (selectedButton != binding.radioPreparing) binding.radioPreparing.setChecked(false);
        if (selectedButton != binding.radioCompleted) binding.radioCompleted.setChecked(false);
        if (selectedButton != binding.radioDelivered) binding.radioDelivered.setChecked(false);
    }

    private void enableConfirmButton() {
        binding.btnConfirmUpdate.setEnabled(true);
        binding.btnConfirmUpdate.setAlpha(1.0f);
    }

    private void loadOrderStatus() {
        new Thread(() -> {
            currentOrder = orderRepository.getOrderById(orderId);
            if (currentOrder == null) {
                runOnUiThread(this::finish);
                return;
            }

            runOnUiThread(() -> {
                binding.txtHeaderOrderId.setText("Mã đơn: #ORD" + String.format("%03d", currentOrder.getId()));
                updateCurrentStatusDisplay(currentOrder.getOrderStatus());
            });
        }).start();
    }

    private void updateCurrentStatusDisplay(String status) {
        int backgroundColor;
        String statusText;

        switch (status) {
            case "PENDING":
                backgroundColor = 0xFFFFF3CD;
                statusText = "⏳ Chờ xác nhận";
                break;
            case "PREPARING":
                backgroundColor = 0xFFE2E3E5;
                statusText = "👨‍🍳 Đang chuẩn bị";
                break;
            case "COMPLETED":
                backgroundColor = 0xFFD4EDDA;
                statusText = "✨ Hoàn thành";
                break;
            case "DELIVERED":
                backgroundColor = 0xFFC3E6CB;
                statusText = "🚚 Đã giao";
                break;
            default:
                backgroundColor = 0xFFF8D7DA;
                statusText = "❌ Đã hủy";
                break;
        }

        binding.txtCurrentStatus.setBackgroundColor(backgroundColor);
        binding.txtCurrentStatus.setText(statusText);
        
        String currentTime = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(new Date());
        binding.txtStatusTime.setText("Cập nhật lúc: " + currentTime);
    }

    private void updateOrderStatus() {
        if (currentOrder == null || selectedStatus == null) return;

        new Thread(() -> {
            currentOrder.setOrderStatus(selectedStatus);
            orderRepository.updateOrder(currentOrder);

            runOnUiThread(() -> showSuccessDialog());
        }).start();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
        TextView btnClose = dialog.findViewById(R.id.btnClose);

        txtMessage.setText("Trạng thái đơn hàng đã được cập nhật");

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();

        // Auto close after 2 seconds
        new android.os.Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                finish();
            }
        }, 2000);
    }
}
