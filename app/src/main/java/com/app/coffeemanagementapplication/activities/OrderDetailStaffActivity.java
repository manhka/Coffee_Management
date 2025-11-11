package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ActivityOrderDetailStaffBinding;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailStaffActivity extends BaseActivity {

    private ActivityOrderDetailStaffBinding binding;
    private OrderRepository orderRepository;
    private int orderId;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailStaffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId == -1) {
            finish();
            return;
        }

        initRepository();
        setupListeners();
        loadOrderDetails();
    }

    private void initRepository() {
        orderRepository = new OrderRepository(
                DatabaseClient.getInstance(this).getAppDatabase().orderDao()
        );
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnGoBack.setOnClickListener(v -> finish());
        
        binding.btnUpdateStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateOrderStatusActivity.class);
            intent.putExtra("ORDER_ID", orderId);
            startActivity(intent);
        });
    }

    private void loadOrderDetails() {
        new Thread(() -> {
            currentOrder = orderRepository.getOrderById(orderId);
            if (currentOrder == null) {
                runOnUiThread(this::finish);
                return;
            }

            // Load related data
            Users customer = DatabaseClient.getInstance(this).getAppDatabase()
                    .userDao().getUserById(currentOrder.getUserId());
            
            ShippingAddress address = null;
            if (currentOrder.getDeliveryAddressId() != null) {
                address = DatabaseClient.getInstance(this).getAppDatabase()
                        .addressDao().getAddressById(currentOrder.getDeliveryAddressId());
            }

            List<OrderItem> orderItems = DatabaseClient.getInstance(this).getAppDatabase()
                    .orderItemDao().getOrderItemsByOrderId(orderId);

            ShippingAddress finalAddress = address;
            runOnUiThread(() -> displayOrderDetails(currentOrder, customer, finalAddress, orderItems));
        }).start();
    }

    private void displayOrderDetails(Order order, Users customer, ShippingAddress address, List<OrderItem> orderItems) {
        // Header
        binding.txtHeaderOrderId.setText("Mã đơn: #ORD" + String.format("%03d", order.getId()));

        // Status Banner
        updateStatusBanner(order.getOrderStatus());

        // Customer Info
        if (customer != null) {
            binding.txtCustomerName.setText(customer.getFullName());
            binding.txtCustomerPhone.setText(customer.getPhone() != null ? customer.getPhone() : "Chưa có");
        }

        if (address != null) {
            String fullAddress = address.getAddressLine() + ", " + 
                                address.getWard() + ", " + 
                                address.getDistrict() + ", " + 
                                address.getCity();
            binding.txtDeliveryAddress.setText(fullAddress);
        } else {
            binding.txtDeliveryAddress.setText("Không có địa chỉ giao hàng");
        }

        // Order Info
        binding.txtOrderTime.setText(formatDate(order.getOrderDate()));
        binding.txtPaymentMethod.setText(getPaymentMethodText(order.getPaymentMethod()));

        // Products
        displayProducts(orderItems);

        // Payment Summary
        double subtotal = order.getTotalAmount();
        double discount = 0; // TODO: Calculate from discount
        double total = subtotal - discount;

        binding.txtSubtotal.setText(CurrencyUtils.formatVNCurrency(subtotal));
        binding.txtDiscount.setText("-" + CurrencyUtils.formatVNCurrency(discount));
        binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
    }

    private void updateStatusBanner(String status) {
        int backgroundColor;
        String statusTitle;
        String statusDesc;

        if (status.equalsIgnoreCase("PAY")) {
            backgroundColor = 0xFFFFF3CD;
            statusTitle = "⏳ Chờ xác nhận";
            statusDesc = "Đơn hàng đang chờ được xác nhận";
        } else if (status.equalsIgnoreCase("PREPARING")) {
            backgroundColor = 0xFFE2E3E5;
            statusTitle = "👨‍🍳 Đang chuẩn bị";
            statusDesc = "Đơn hàng đang được pha chế";
        } else if (status.equalsIgnoreCase("COMPLETED") || status.equalsIgnoreCase("COMPLETE")) {
            backgroundColor = 0xFFD4EDDA;
            statusTitle = "✨ Hoàn thành";
            statusDesc = "Đơn hàng đã hoàn thành";
        } else if (status.equalsIgnoreCase("DELIVERED")) {
            backgroundColor = 0xFFC3E6CB;
            statusTitle = "🚚 Đã giao";
            statusDesc = "Đơn hàng đã được giao thành công";
        } else {
            backgroundColor = 0xFFFFF3CD;
            statusTitle = "⏳ Chờ xác nhận";
            statusDesc = "Đơn hàng đang chờ được xác nhận";
        }

        binding.statusBanner.setBackgroundColor(backgroundColor);
        binding.txtStatusTitle.setText(statusTitle);
        binding.txtStatusDesc.setText(statusDesc);
    }

    private void displayProducts(List<OrderItem> orderItems) {
        binding.productsContainer.removeAllViews();

        for (OrderItem item : orderItems) {
            Product product = DatabaseClient.getInstance(this).getAppDatabase()
                    .productDao().getProductById(item.getProductId());
            
            if (product != null) {
                View productView = createProductItemView(item, product);
                binding.productsContainer.addView(productView);
            }
        }
    }

    private View createProductItemView(OrderItem item, Product product) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_order_product_detail, binding.productsContainer, false);

        TextView txtProductName = view.findViewById(R.id.txtProductName);
        TextView txtProductPrice = view.findViewById(R.id.txtProductPrice);
        TextView txtProductOptions = view.findViewById(R.id.txtProductOptions);
        TextView txtProductQuantity = view.findViewById(R.id.txtProductQuantity);

        txtProductName.setText(product.getName());
        txtProductPrice.setText(CurrencyUtils.formatVNCurrency(item.getUnitPrice()));
        
        // Build options string
        StringBuilder options = new StringBuilder();
        if (item.getTemperature() != null) options.append("🌡️ ").append(item.getTemperature()).append(" | ");
        if (item.getSize() != null) options.append("📏 ").append(item.getSize()).append(" | ");
        if (item.getSugar() != null) options.append("🍬 ").append(item.getSugar()).append(" | ");
        if (item.getIce() != null) options.append("🧊 ").append(item.getIce());
        
        txtProductOptions.setText(options.toString());
        txtProductQuantity.setText("Số lượng: x" + item.getQuantity());

        return view;
    }

    private String getPaymentMethodText(String method) {
        switch (method) {
            case "CASH":
                return "Tiền mặt";
            case "QR":
                return "Mã QR";
            case "E_WALLET":
                return "Ví điện tử";
            default:
                return method;
        }
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderId != -1) {
            loadOrderDetails();
        }
    }
}
