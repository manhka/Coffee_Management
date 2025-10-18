package com.app.coffeemanagementapplication.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.ProductFilter;
import com.app.coffeemanagementapplication.models.ProductRating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;





public class CustomerHomeActivity extends BaseActivity {

    private ActivityCustomerHomeBinding binding;
    private final Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    // Dữ liệu hiển thị
    private List<ProductRating> allProducts;      // dữ liệu gốc
    private List<ProductRating> productRatings;   // dữ liệu hiển thị
    private ProductAdapter productAdapter;

    private Integer selectedCategoryId = null;    // danh mục hiện tại
    private String currentFilterName = null;      // tên filter hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBanner();
        setupCategory();
        setupFilter();
        setupProductList();
        setupSearch();
    }

    // ------------------ BANNER ------------------
    private void setupBanner() {
        List<Integer> imageList = Arrays.asList(
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

        sliderRunnable = () -> {
            int nextPos = (binding.viewPagerBanner.getCurrentItem() + 1) % imageList.size();
            binding.viewPagerBanner.setCurrentItem(nextPos, true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        binding.viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setCurrentIndicator(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < count; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageResource(R.drawable.indicator_inactive);
            indicators[i].setLayoutParams(params);
            binding.indicatorLayout.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = binding.indicatorLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.indicatorLayout.getChildAt(i);
            imageView.setImageResource(
                    i == index ? R.drawable.indicator_active : R.drawable.indicator_inactive
            );
        }
    }

    // ------------------ CATEGORY ------------------
    private void setupCategory() {
        List<Category> categories = new ArrayList<>();

        // Thêm danh mục “Tất cả”
        Category all = new Category(-1, "Tất cả", "Hiển thị tất cả sản phẩm", "", "");
        categories.add(all);

        // Các danh mục cố định
        categories.add(new Category(1, "Cafe", "Các loại cafe", "", ""));
        categories.add(new Category(2, "Trà Sữa", "Trà sữa các vị", "", ""));
        categories.add(new Category(3, "Sinh Tố", "Sinh tố trái cây", "", ""));
        categories.add(new Category(4, "Bánh", "Bánh ngọt, bánh mì", "", ""));

        CategoryAdapter adapter = new CategoryAdapter(categories);
        binding.rcvCategory.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rcvCategory.setAdapter(adapter);

        adapter.setOnCategoryClickListener(category -> {
            selectedCategoryId = category.getId() == -1 ? null : category.getId();
            applyFilter(selectedCategoryId, currentFilterName);
        });
    }

    // ------------------ FILTER ------------------
    private void setupFilter() {
        List<ProductFilter> filters = Arrays.asList(
                new ProductFilter(R.drawable.star_ic, "Xếp hạng"),
                new ProductFilter(R.drawable.cash_ic, "Giá")
        );

        ProductFilterAdapter filterAdapter = new ProductFilterAdapter(filters);
        binding.rcvProductFilter.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rcvProductFilter.setAdapter(filterAdapter);

        filterAdapter.setOnFilterClickListener(filterName -> {
            currentFilterName = "Tất cả".equals(filterName) ? null : filterName;
            applyFilter(selectedCategoryId, currentFilterName);
        });
    }

    // ------------------ PRODUCTS ------------------
    private void setupProductList() {
        allProducts = new ArrayList<>();

        Product p1 = new Product(1, 1, "Cà phê sữa đá", "Cà phê truyền thống Việt Nam", 25000,
                "https://media.istockphoto.com/id/1140614164/vi/anh/mojito-cocktail-tr%C3%AAn-qu%E1%BA%A7y-bar.jpg", true, "", "");
        Product p2 = new Product(2, 1, "Espresso", "Cà phê Ý đậm vị", 30000,
                "https://media.istockphoto.com/id/1140614164/vi/anh/mojito-cocktail-tr%C3%AAn-qu%E1%BA%A7y-bar.jpg", true, "", "");
        Product p3 = new Product(3, 2, "Trà sữa trân châu", "Thức uống ngọt ngào", 35000,
                "https://media.istockphoto.com/id/917737514/vi/anh/barmans-tay-r%E1%BA%AFc-n%C6%B0%E1%BB%9Bc-%C3%A9p-v%C3%A0o-ly-cocktail.jpg", true, "", "");
        Product p4 = new Product(4, 3, "Sinh tố xoài", "Sinh tố tươi mát", 40000,
                "https://media.istockphoto.com/id/505168330/vi/anh/t%C3%A1ch-c%C3%A0-ph%C3%AA-latte.jpg", true, "", "");
        Product p5 = new Product(5, 4, "Bánh donut", "Bánh ngọt thơm ngon", 20000,
                "https://media.istockphoto.com/id/1126871442/vi/anh/t%C3%A1ch-c%C3%A0-ph%C3%AA.jpg", true, "", "");

        allProducts.addAll(Arrays.asList(
                new ProductRating(p1, 4.5f, 120),
                new ProductRating(p2, 5.0f, 300),
                new ProductRating(p3, 4.0f, 210),
                new ProductRating(p4, 4.8f, 190),
                new ProductRating(p5, 4.2f, 85)
        ));

        productRatings = new ArrayList<>(allProducts);
        productAdapter = new ProductAdapter(this, productRatings);
        binding.rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvProduct.setAdapter(productAdapter);
    }

    // ------------------ SEARCH ------------------
    private void setupSearch() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                applyFilter(selectedCategoryId, currentFilterName);
            }
        });
    }

    // ------------------ APPLY FILTER ------------------
    private void applyFilter(Integer selectedCategoryId, String filterName) {
        String keyword = binding.edtSearch.getText().toString().trim().toLowerCase();

        List<ProductRating> filtered = new ArrayList<>();
        for (ProductRating pr : allProducts) {
            boolean matchCategory = (selectedCategoryId == null)
                    || (pr.product.getCategoryId() == selectedCategoryId);
            boolean matchKeyword = keyword.isEmpty()
                    || pr.product.getName().toLowerCase().contains(keyword);

            if (matchCategory && matchKeyword) {
                filtered.add(pr);
            }
        }

        // Sắp xếp
        if ("Xếp hạng".equals(filterName)) {
            filtered.sort((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating())); // giảm dần
        } else if ("Giá".equals(filterName)) {
            filtered.sort(Comparator.comparingDouble(a -> a.product.getPrice())); // tăng dần
        }

        // Cập nhật adapter
        productAdapter.updateList(filtered);
        productRatings.clear();
        productRatings.addAll(filtered);
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

