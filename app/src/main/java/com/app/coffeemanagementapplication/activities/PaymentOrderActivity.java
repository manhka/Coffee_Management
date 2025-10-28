package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.adapters.PaymentOrderAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityPaymentOrderBinding;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.Payment;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IPaymentRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.AddressService;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.PaymentService;
import com.app.coffeemanagementapplication.services.ProductService;

import java.util.List;

public class PaymentOrderActivity extends BaseActivity {
    private PaymentOrderAdapter adapter;
    private IProductRepo productRepo;
    private IOrderItemRepo orderItemRepo;
    private IPaymentRepo paymentRepo;
    private IAddressRepo addressRepo;
    private List<OrderItem> orderItems;
    private ActivityPaymentOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productRepo = new ProductService(this);
        orderItemRepo = new OrderItemService(this);
        orderItems = orderItemRepo.getAllSelectedOrderItems();
        addressRepo= new AddressService(this);
        paymentRepo= new PaymentService(this);
        adapter = new PaymentOrderAdapter(orderItems, this, productRepo);
        binding.rvCartItems.setAdapter(adapter);
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.imvBtnBack.setOnClickListener(v -> finish());
        int addressId= MySharePrefers.getAddressId();
        if (addressId!=-1){
            ShippingAddress shippingAddress=addressRepo.getAddressById(addressId);
            String address= shippingAddress.getAddressLine()+", " +shippingAddress.getCity()+", " +shippingAddress.getDistrict()+", " +shippingAddress.getWard();
            String phone= shippingAddress.getPhone();
            String fullName= shippingAddress.getFullName();
            String note= shippingAddress.getNote();
            binding.tvSelectedAddress.setText(fullName+"\n"+address+"\n"+phone+"\n"+note);
        }else {
            binding.tvSelectedAddress.setText("Chọn địa chỉ giao hàng");
        }
        int paymentId= MySharePrefers.getPaymentMethodId();
        if (paymentId!=-1){
            Payment payment= paymentRepo.getPaymentMethodById(paymentId);
            binding.tvSelectedPayment.setText(String.format("%s\n%s", payment.getPaymentName(), payment.getPaymentDescription()));
        }else {
            binding.tvSelectedPayment.setText("Chọn phương thức thanh toán");
        }
        binding.llSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PaymentOrderActivity.this,ChoosingAddressActivity.class);
                startActivity(intent);
            }
        });
        binding.llSelectPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PaymentOrderActivity.this,PaymentMethodActivity.class);
                startActivity(intent);
            }
        });
    }
}