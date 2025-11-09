package com.app.coffeemanagementapplication.fragments;

// ... (Bỏ import android.app.DatePickerDialog)
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.adapters.DateSelectorAdapter; // THÊM
import com.app.coffeemanagementapplication.adapters.OrderAdapter;
import com.app.coffeemanagementapplication.models.Order;
import com.app.coffeemanagementapplication.services.OrderService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; // THÊM
import java.util.List;
import java.util.Locale;

// THÊM "implements DateSelectorAdapter.OnDateSelectedListener"
public class AdminDashboardFragment extends Fragment implements DateSelectorAdapter.OnDateSelectedListener {

    private TextView txtRevenueAmount;
    private TextView txtOrdersTitle;
    private RecyclerView recyclerOrders;
    private OrderAdapter orderAdapter;
    private OrderService orderService;

    // --- Biến cho thanh ngày mới ---
    private RecyclerView recyclerDateSelector;
    private DateSelectorAdapter dateAdapter;
    private List<Date> dateList = new ArrayList<>();

    private String selectedDate; // Vẫn dùng biến này để query

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        // --- Ánh xạ view ---
        txtRevenueAmount = view.findViewById(R.id.txtRevenueAmount);
        txtOrdersTitle = view.findViewById(R.id.txtOrdersTitle); // Giờ chỉ là tiêu đề
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerDateSelector = view.findViewById(R.id.recyclerDateSelector); // View mới

        if (getContext() != null) {
            orderService = new OrderService(getContext());
        }

        // --- Cài đặt danh sách order (như cũ) ---
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(new ArrayList<>());
        recyclerOrders.setAdapter(orderAdapter);

        // --- Cài đặt thanh chọn ngày ---
        setupDateSelector();

        // Lấy ngày hôm nay làm ngày mặc định
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(dateList.get(0)); // dateList.get(0) là ngày hôm nay

        // Tải data cho ngày hôm nay
        loadOrdersForDate(selectedDate);

        // --- BỎ ĐI ---
        // txtOrdersTitle.setOnClickListener(v -> showDatePicker());

        return view;
    }

    /**
     * Hàm cài đặt cho thanh chọn ngày
     */
    private void setupDateSelector() {
        if (getContext() == null) return;

        // Tạo danh sách 7 ngày (từ hôm nay lùi về 6 ngày trước)
        dateList = getRecentDates(7);

        // "this" (tức AdminDashboardFragment) chính là listener
        dateAdapter = new DateSelectorAdapter(getContext(), dateList, this);

        recyclerDateSelector.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerDateSelector.setAdapter(dateAdapter);
    }

    /**
     * Hàm helper tạo danh sách ngày
     */
    private List<Date> getRecentDates(int days) {
        List<Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        // Thêm ngày hôm nay
        dates.add(cal.getTime());

        // Thêm các ngày trước đó
        for (int i = 1; i < days; i++) {
            cal.add(Calendar.DAY_OF_YEAR, -1); // Lùi lại 1 ngày
            dates.add(cal.getTime());
        }
        // (Bạn có thể đảo ngược list nếu muốn bắt đầu từ quá khứ)
        return dates;
    }

    /**
     * Sự kiện này được gọi từ DateSelectorAdapter khi người dùng bấm vào 1 ngày
     */
    @Override
    public void onDateSelected(Date date) {
        // Format ngày (Date) thành chuỗi (String) "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.selectedDate = sdf.format(date);

        // Tải lại dữ liệu cho ngày mới
        loadOrdersForDate(this.selectedDate);
    }

    // --- BỎ HÀM NÀY ĐI ---
    // private void showDatePicker() { ... }


    /**
     * Tải đơn hàng và doanh thu cho ngày đã chọn
     * (Hàm này giữ nguyên logic, chỉ đổi tiêu đề txtOrdersTitle)
     */
    private void loadOrdersForDate(String date) {
        if (orderService == null) return;

        List<Order> orders = orderService.getOrdersByDate(date);

        if (orders == null) {
            orders = new ArrayList<>();
        }
        orderAdapter.setOrders(orders);

        double revenue = orderService.getRevenueByDate(date);

        txtRevenueAmount.setText(String.format(Locale.GERMANY, "%,.0f₫", revenue));
        // Cập nhật tiêu đề để cho biết đang xem ngày nào
        txtOrdersTitle.setText("Đơn hàng (" + date + ")");

//        if (orders.isEmpty() && getContext() != null) {
//            Toast.makeText(getContext(), "Không có đơn hàng", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại fragment
        if (selectedDate != null) {
            loadOrdersForDate(selectedDate);
        }
    }
}