package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.CurrencyUtils;
import com.app.coffeemanagementapplication.databinding.ActivityProductDetailBinding;
import com.app.coffeemanagementapplication.databinding.CustomToastBinding;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.OrderItem;
import com.app.coffeemanagementapplication.models.OrderItemStatus;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IOrderItemRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.services.FeedbackService;
import com.app.coffeemanagementapplication.services.OrderItemService;
import com.app.coffeemanagementapplication.services.ProductService;
import com.bumptech.glide.Glide;

public class ProductDetailActivity extends BaseActivity {
    private ActivityProductDetailBinding binding;
    private IProductRepo productRepo;
    private IFeedbackRepo feedbackRepo;
    private double price = 0;
    final int MIN_QUANTITY = 1;
    final int MAX_QUANTITY = 100;
    private int currentQuantity = 1;
    private int size = 1; // 1:nho , 2:vua , 3:lon
    private Product product = new Product();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        feedbackRepo = new FeedbackService(this);
        binding.imvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        int productId = intent.getIntExtra("productId", -1);
        productRepo = new ProductService(this);
        product = productRepo.getProductById(productId);
        binding.txtProductName.setText(product.getName());
        binding.txtProductDescription.setText(product.getDescription());
        binding.txtProductPrice.setText(CurrencyUtils.formatVNCurrency(product.getPrice()));
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(com.app.coffeemanagementapplication.R.drawable.ic_launcher_background)
                .into(binding.imvProductImage);
        int quantity = Integer.parseInt(binding.txtQuantity.getText().toString());
        price = product.getPrice();
        double total = quantity * product.getPrice();
        binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
        Float avgObj = feedbackRepo.getAverageRatingByProduct(productId);
        double averageRating = avgObj == null ? 0.0 : avgObj;
        int numberOfRatings = feedbackRepo.getFeedbackCountByProduct(productId);
        binding.txtRatingValue.setText(String.format("%.1f", averageRating));
        binding.txtRatingCount.setText("(" + numberOfRatings + ")");
        if (numberOfRatings==0){
            binding.layoutRating.setVisibility(View.GONE);
        }else {
            binding.layoutRating.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.txtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(binding.txtQuantity.getText().toString());
                if (quantity < MAX_QUANTITY) {
                    currentQuantity = quantity + 1;
                    binding.txtQuantity.setText(String.valueOf(currentQuantity));
                    double total = getTotalPrice();
                    binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
                }
                updateButtonState(quantity);
            }
        });

        binding.txtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(binding.txtQuantity.getText().toString());
                if (quantity > MIN_QUANTITY) {
                    currentQuantity = quantity - 1;
                    binding.txtQuantity.setText(String.valueOf(currentQuantity));
                    double total = getTotalPrice();
                    binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
                }
                updateButtonState(quantity);
            }
        });
        binding.rbSizeSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = 1;
                double total = getTotalPrice();
                binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
            }
        });
        binding.rbSizeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = 2;
                double total = getTotalPrice();
                binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
            }
        });
        binding.rbSizeLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = 3;
                double total = getTotalPrice();
                binding.txtTotalPrice.setText(CurrencyUtils.formatVNCurrency(total));
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Temperature
                int temperatureSelectedId = binding.groupDrink.getCheckedRadioButtonId();
                String temperature = null;
                if (temperatureSelectedId != -1) {
                    RadioButton temperatureSelectedRadioButton = binding.getRoot().findViewById(temperatureSelectedId);
                    temperature = temperatureSelectedRadioButton.getTag().toString();
                }

// Ice
                int iceSelectedId = binding.groupIce.getCheckedRadioButtonId();
                String ice = null;
                if (iceSelectedId != -1) {
                    RadioButton iceSelectedRadioButton = binding.getRoot().findViewById(iceSelectedId);
                    ice = iceSelectedRadioButton.getTag().toString();
                }

// Size
                int sizeSelectedId = binding.groupSize.getCheckedRadioButtonId();
                String size = null;
                if (sizeSelectedId != -1) {
                    RadioButton sizeSelectedRadioButton = binding.getRoot().findViewById(sizeSelectedId);
                    size = sizeSelectedRadioButton.getTag().toString();
                }

// Sugar
                int sugarSelectedId = binding.groupSugar.getCheckedRadioButtonId();
                String sugar = null;
                if (sugarSelectedId != -1) {
                    RadioButton sugarSelectedRadioButton = binding.getRoot().findViewById(sugarSelectedId);
                    sugar = sugarSelectedRadioButton.getTag().toString();
                }

                String note = binding.edtNote.getText().toString();
                addToCart(product, currentQuantity, size, sugar, ice, temperature, note);
                CustomToastBinding binding = CustomToastBinding.inflate(getLayoutInflater());
                binding.toastText.setText("Thêm vào giỏ hàng thành công!");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(binding.getRoot());
                toast.show();
                finish();
            }
        });

        binding.layoutRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductFeedbackActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }
        });
    }

    private void updateButtonState(int quantity) {
        binding.txtMinus.setEnabled(quantity > MIN_QUANTITY);
        binding.txtPlus.setEnabled(quantity < MAX_QUANTITY);
    }

    private double getTotalPrice() {
        double total = 0;
        if (size == 1) {
            total = currentQuantity * price;
        } else if (size == 2) {
            total = currentQuantity * price + (currentQuantity * price * 0.3);
        } else if (size == 3) {
            total = currentQuantity * price + (currentQuantity * price * 0.5);
        }
        return total;
    }

    public void addToCart(Product product, int quantity, String size, String sugar, String ice, String temperature, String note) {
        OrderItem item = new OrderItem();
        item.setProductId(product.getId());
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.setSubtotal(product.getPrice() * quantity);
        item.setSize(size);
        item.setSugar(sugar);
        item.setIce(ice);
        item.setTemperature(temperature);
        item.setNote(note);
        item.setOrderItemStatus(OrderItemStatus.PENDING.getValue());
        IOrderItemRepo orderItemRepo = new OrderItemService(this);
        orderItemRepo.insertOrderItem(item);
    }
}