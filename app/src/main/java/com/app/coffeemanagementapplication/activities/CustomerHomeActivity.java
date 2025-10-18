package com.app.coffeemanagementapplication.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.CategoryAdapter;
import com.app.coffeemanagementapplication.adapters.ProductAdapter;
import com.app.coffeemanagementapplication.adapters.ProductFilterAdapter;
import com.app.coffeemanagementapplication.adapters.SliderAdapter;
import com.app.coffeemanagementapplication.databinding.ActivityCustomerHomeBinding;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.ProductFilter;
import com.app.coffeemanagementapplication.models.ProductRating;
import com.app.coffeemanagementapplication.services.ProductRatingBuilder;

import java.util.Arrays;
import java.util.List;

public class CustomerHomeActivity extends BaseActivity {
    private ActivityCustomerHomeBinding binding;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;
    private List<Integer> imageList;
    private ProductAdapter productAdapter;
    private List<ProductRating> productRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner4,
                R.drawable.banner5
        );
        SliderAdapter adapter = new SliderAdapter(this, imageList);
        binding.viewPagerBanner.setAdapter(adapter);
        setupIndicators(imageList.size());
        setCurrentIndicator(0);
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextPos = (binding.viewPagerBanner.getCurrentItem() + 1) % imageList.size();
                binding.viewPagerBanner.setCurrentItem(nextPos, true);
                sliderHandler.postDelayed(this, 3000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);
// when user swipe the slider
        binding.viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        // category
        List<String> animals = Arrays.asList("Tất cả", "Cafe", "Trà Sữa", "Sinh Tố", "Bánh ngọt");
        CategoryAdapter categoryAdapter = new CategoryAdapter(animals);
        binding.rcvCategory.setAdapter(categoryAdapter);
        binding.rcvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // filter
        List<ProductFilter> filterList = Arrays.asList(
                new ProductFilter(R.drawable.filter_ic, "Tất cả"),
                new ProductFilter(R.drawable.star_ic, "Xếp hạng"),
                new ProductFilter(R.drawable.cash_ic, "Giá")
        );

        ProductFilterAdapter productFilterAdapter = new ProductFilterAdapter(filterList);

        binding.rcvProductFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rcvProductFilter.setAdapter(productFilterAdapter);
        // bottom nav
//        binding.bottomNavigation.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.nav_history:
//                    Toast.makeText(this, "Trang trại", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.nav_profile:
//                    Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
//                    return true;
//            }
//            return false;
//        });

        // product list
        ProductRatingBuilder builder = new ProductRatingBuilder(this);
        productRatings = builder.buildProductRatings();
        for (int i = 1; i <= 5; i++) {
            Product dummyProduct = new Product(
                    i,
                    null,
                    "Sản phẩm " + i,
                    "Mô tả sản phẩm " + i,
                    10.0 * i,
                    "https://cdn.pixabay.com/photo/2023/06/20/10/06/cocktail-8076619_1280.jpg",
                    true,
                    "2025-01-01",
                    "2025-01-01"
            );
            productRatings.add(new ProductRating(dummyProduct, i % 5 + 1, i * 2)); // rating và totalFeedback
        }

        binding.rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productRatings);
        binding.rcvProduct.setAdapter(productAdapter);
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < count; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageResource(R.drawable.indicator_inactive);
            indicators[i].setLayoutParams(params);
            binding.indicatorLayout.addView(indicators[i]);
        }
    }

    // Đổi trạng thái chấm tròn theo ảnh hiện tại
    private void setCurrentIndicator(int index) {
        int childCount = binding.indicatorLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.indicatorLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageResource(R.drawable.indicator_active);
            } else {
                imageView.setImageResource(R.drawable.indicator_inactive);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}