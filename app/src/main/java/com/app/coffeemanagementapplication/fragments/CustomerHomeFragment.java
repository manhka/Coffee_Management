package com.app.coffeemanagementapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.CategoryAdapter;
import com.app.coffeemanagementapplication.adapters.ProductAdapter;
import com.app.coffeemanagementapplication.adapters.ProductFilterAdapter;
import com.app.coffeemanagementapplication.adapters.SliderAdapter;
import com.app.coffeemanagementapplication.databinding.FragmentCustomerHomeBinding;
import com.app.coffeemanagementapplication.models.Category;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.Product;
import com.app.coffeemanagementapplication.models.ProductFilter;
import com.app.coffeemanagementapplication.models.ProductRating;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.ICategoryRepo;
import com.app.coffeemanagementapplication.repositories.IFeedbackRepo;
import com.app.coffeemanagementapplication.repositories.IProductRepo;
import com.app.coffeemanagementapplication.repositories.IUserRepo;
import com.app.coffeemanagementapplication.services.CategoryService;
import com.app.coffeemanagementapplication.services.FeedbackService;
import com.app.coffeemanagementapplication.services.ProductService;
import com.app.coffeemanagementapplication.services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerHomeFragment extends Fragment {
    private FragmentCustomerHomeBinding binding;
    private final Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;
    private List<ProductRating> allProductRatings;        // list gốc
    private List<ProductRating> displayedProductRatings;  // list hiển thị sau filter
    private String currentSort = null;
    private ProductAdapter productAdapter;
    private Integer selectedCategoryId = null;    // danh mục hiện tại
    private String currentFilterName = null;      // tên filter hiện tại
    private IFeedbackRepo feedbackRepo;

    public CustomerHomeFragment() {
        // Required empty public constructor
    }


    public static CustomerHomeFragment newInstance(String param1, String param2) {
        CustomerHomeFragment fragment = new CustomerHomeFragment();
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
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderHandler.postDelayed(sliderRunnable, 3000);
        setupBanner();
        setupCategory();
        setupFilter();
        setupProductList();
        setupSearch();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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

        SliderAdapter adapter = new SliderAdapter(getContext(), imageList);
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
            indicators[i] = new ImageView(getContext());
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
        ICategoryRepo categoryRepo = new CategoryService(getContext());
        List<Category> categories = new ArrayList<>();

        // Thêm danh mục “Tất cả”
        Category all = new Category(-1, "Tất cả", "Hiển thị tất cả sản phẩm", "", "");
        categories.add(all);

        // Lấy danh mục từ DB
        List<Category> categoryList = categoryRepo.getAllCategories();
        categories.addAll(categoryList);

        // Gán adapter
        CategoryAdapter adapter = new CategoryAdapter(categories);
        binding.rcvCategory.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rcvCategory.setAdapter(adapter);

        // Xử lý khi click category
        adapter.setOnCategoryClickListener(category -> {
            selectedCategoryId = category.getId() == -1 ? null : category.getId();
            applyFilter();
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
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rcvProductFilter.setAdapter(filterAdapter);

        filterAdapter.setOnFilterClickListener(filterName -> {
            if ("Xếp hạng".equals(filterName)) currentSort = "rating";
            else if ("Giá".equals(filterName)) currentSort = "price";
            else currentSort = null;

            applyFilter();
        });
    }


    // ------------------ PRODUCTS ------------------
    private void setupProductList() {
        IProductRepo productRepo = new ProductService(getContext());
        IFeedbackRepo feedbackRepo = new FeedbackService(getContext());
        IUserRepo userRepo = new UserService(requireContext());

        List<Product> products = productRepo.getAllProducts();

        allProductRatings = new ArrayList<>();
        displayedProductRatings = new ArrayList<>();

        for (Product p : products) {
            Float avgRating = feedbackRepo.getAverageRatingByProduct(p.getId());
            int totalFeedback = feedbackRepo.getFeedbackCountByProduct(p.getId());
            if (avgRating == null) avgRating = 0f;

            List<Feedback> feedbacks = feedbackRepo.getFeedbackByProductId(p.getId());

            if (feedbacks.isEmpty()) {
                allProductRatings.add(new ProductRating(
                        p, avgRating, totalFeedback,
                        "Chưa có đánh giá nào", "", ""
                ));
            } else {
                Feedback latest = feedbacks.get(feedbacks.size() - 1);
                Users user = userRepo.getUserById(latest.getUserId());

                allProductRatings.add(new ProductRating(
                        p, avgRating, totalFeedback,
                        latest.getComment(),
                        user != null ? user.getFullName() : "Người dùng ẩn danh",
                        latest.getCreatedAt()
                ));
            }
        }

        displayedProductRatings.addAll(allProductRatings);

        productAdapter = new ProductAdapter(getContext(), displayedProductRatings);
        binding.rcvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvProduct.setAdapter(productAdapter);
    }


    // ------------------ SEARCH ------------------
    private void setupSearch() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                applyFilter();
            }
        });
    }

    // ------------------ APPLY FILTER ------------------
    private void applyFilter() {
        String keyword = binding.edtSearch.getText().toString().trim().toLowerCase();

        displayedProductRatings.clear();
        for (ProductRating pr : allProductRatings) {

            boolean matchCategory = (selectedCategoryId == null)
                    || pr.getProduct().getCategoryId() == selectedCategoryId;

            boolean matchKeyword = keyword.isEmpty()
                    || pr.getProduct().getName().toLowerCase().contains(keyword);

            if (matchCategory && matchKeyword) {
                displayedProductRatings.add(pr);
            }
        }

        // SORT
        if ("rating".equals(currentSort)) {
            displayedProductRatings.sort((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));
        } else if ("price".equals(currentSort)) {
            displayedProductRatings.sort(Comparator.comparingDouble(a -> a.getProduct().getPrice()));
        }
        productAdapter.updateList(displayedProductRatings);
    }


}