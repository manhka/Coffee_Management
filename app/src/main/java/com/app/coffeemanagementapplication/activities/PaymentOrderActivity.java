package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.adapters.PaymentOrderAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityPaymentOrderBinding;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.OrderItemStatus;
import com.app.coffeemanagementapplication.models.OrderStatus;
import com.app.coffeemanagementapplication.models.Payment;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;
import com.app.coffeemanagementapplication.repositories.IDiscountRepo;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IOrderRepo;
import com.app.coffeemanagementapplication.repositories.IPaymentRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.AddressService;
import com.app.coffeemanagementapplication.services.DiscountService;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.OrderService;
import com.app.coffeemanagementapplication.services.PaymentService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class PaymentOrderActivity extends BaseActivity {
    private PaymentOrderAdapter adapter;
    private IProductRepo productRepo;
    private IOrderRepo orderRepo;
    private IOrderItemRepo orderItemRepo;
    private IPaymentRepo paymentRepo;
    private IAddressRepo addressRepo;
    private IDiscountRepo discountRepo;
    private List<OrderItem> selectedPendingOrderItem;
    private ActivityPaymentOrderBinding binding;
    private double totalOrder = 0;
    private double discountValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.nestedScrollView, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Thêm padding dưới tương ứng với chiều cao bàn phím hoặc thanh điều hướng
            v.setPadding(0, 0, 0, Math.max(imeInsets.bottom, navInsets.bottom));

            return insets;
        });

// Tự động scroll đến ô input đang focus khi bàn phím mở
        binding.nestedScrollView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            View focusedView = getCurrentFocus();
            if (focusedView != null) {
                Rect rect = new Rect();
                focusedView.getWindowVisibleDisplayFrame(rect);

                int screenHeight = binding.nestedScrollView.getRootView().getHeight();
                int keypadHeight = screenHeight - rect.bottom;

                // Nếu bàn phím chiếm >15% chiều cao màn hình => đang mở
                if (keypadHeight > screenHeight * 0.15) {
                    binding.nestedScrollView.post(() -> {
                        binding.nestedScrollView.smoothScrollTo(0, focusedView.getBottom());
                    });
                }
            }
        });
        productRepo = new ProductService(this);
        orderRepo = new OrderService(this);
        orderItemRepo = new OrderItemService(this);
        discountRepo = new DiscountService(this);
        selectedPendingOrderItem = orderItemRepo.getAllSelectedOrderItemsByStatus(OrderItemStatus.PENDING.getValue());
        addressRepo = new AddressService(this);
        paymentRepo = new PaymentService(this);
        adapter = new PaymentOrderAdapter(selectedPendingOrderItem, this, productRepo);
        binding.rvCartItems.setAdapter(adapter);
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        for (OrderItem orderItem : selectedPendingOrderItem) {
            totalOrder += orderItem.getUnitPrice() * orderItem.getQuantity();
        }
        binding.imvBtnBack.setOnClickListener(v -> {
            finish();
        });
        int addressId = MySharePrefers.getAddressId();
        if (addressId != -1) {
            ShippingAddress shippingAddress = addressRepo.getAddressById(addressId);
            String address = shippingAddress.getAddressLine() + ", " + shippingAddress.getCity() + ", " + shippingAddress.getDistrict() + ", " + shippingAddress.getWard();
            String phone = shippingAddress.getPhone();
            String fullName = shippingAddress.getFullName();
            String note = shippingAddress.getNote();
            binding.tvSelectedAddress.setText(fullName + "\n" + address + "\n" + phone + "\n" + note);
        } else {
            binding.tvSelectedAddress.setText("Chọn địa chỉ giao hàng");
        }
        String paymentMethod;
        int paymentId = MySharePrefers.getPaymentMethodId();
        if (paymentId != -1) {
            Payment payment = paymentRepo.getPaymentMethodById(paymentId);
            paymentMethod = String.format("%s\n%s", payment.getPaymentName(), payment.getPaymentDescription());
            binding.tvSelectedPayment.setText(paymentMethod);
        } else {
            paymentMethod = "";
            binding.tvSelectedPayment.setText("Chọn phương thức thanh toán");
        }
        int discountId = MySharePrefers.getDiscountId();
        if (discountId != -1) {
            Discount discount = discountRepo.getDiscountById(discountId);
            discountValue = discount.getValue();
            binding.tvSelectedDiscount.setText("Mã giảm giá: " + discount.getName() + "\n" + discount.getDescription());

        } else {
            binding.tvSelectedDiscount.setText("Chọn mã giảm giá");
        }
        binding.llSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentOrderActivity.this, ChoosingAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.llSelectPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentOrderActivity.this, PaymentMethodActivity.class);
                startActivity(intent);
            }
        });
        binding.llSelectDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentOrderActivity.this, ChoosingDiscountActivity.class);
                intent.putExtra("total", totalOrder);
                startActivity(intent);
            }
        });
        double totalMoney = totalOrder - discountValue;
        binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(totalMoney));

        if (addressId != -1 && paymentId != -1) {
            binding.btnPayOrder.setEnabled(true);
            binding.btnPayOrder.setAlpha(1.0f);
        } else {
            binding.btnPayOrder.setEnabled(false);
            binding.btnPayOrder.setAlpha(0.5f);
        }

        binding.btnPayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order();
                if (MySharePrefers.getUserId() != -1) {
                    order.setUserId(MySharePrefers.getUserId());
                }
                if (paymentId != -1) {
                    order.setPaymentMethod(paymentMethod);

                }
                if (addressId != -1) {
                    order.setDeliveryAddressId(addressId);

                }
                if (discountId != -1) {
                    order.setDiscountId(discountId);
                }
                order.setOrderStatus(OrderStatus.PENDING.getValue());
                order.setNote(binding.edtNote.getText().toString());
                order.setOrderDate(String.valueOf(System.currentTimeMillis()));
                order.setTotalAmount(totalMoney);
                long orderId = orderRepo.insertOrder(order);
                order.setId((int) orderId);
                for (OrderItem orderItem : selectedPendingOrderItem) {
                    orderItemRepo.updateOrderIdOfSelectedOrderItem(orderItem.getId(), order.getId());
                    orderItemRepo.updateSelectedOrderItemStatus(orderItem.getId(), OrderItemStatus.PAY.getValue());
                }
                MySharePrefers.removeKey("getDiscountId");
                Intent intent = new Intent(PaymentOrderActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}