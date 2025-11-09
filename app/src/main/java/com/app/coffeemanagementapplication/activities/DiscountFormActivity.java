package com.app.coffeemanagementapplication.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.models.Discount;
import com.app.coffeemanagementapplication.services.DiscountService;
import com.google.android.material.appbar.MaterialToolbar; // THÊM
import com.google.android.material.button.MaterialButton; // THÊM
import com.google.android.material.textfield.TextInputLayout; // THÊM

import java.util.Calendar;

public class DiscountFormActivity extends AppCompatActivity {

    private EditText etName, etCode, etDescription, etCondition, etValue, etStartDate, etEndDate;
    private MaterialButton btnSave; // Đổi Button thành MaterialButton
    private DiscountService discountService;
    private Discount discount;

    // --- THÊM CÁC BIẾN MỚI ---
    private MaterialToolbar toolbar;
    private TextInputLayout layoutStartDate, layoutEndDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_form); // Dùng layout có Toolbar

        // --- 1. XỬ LÝ TOOLBAR VÀ NÚT BACK ---
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish(); // Đóng Activity này khi bấm back
        });
        // --- HẾT PHẦN TOOLBAR ---

        // Khởi tạo views
        etName = findViewById(R.id.etName);
        etCode = findViewById(R.id.etCode);
        etDescription = findViewById(R.id.etDescription);
        etCondition = findViewById(R.id.etCondition);
        etValue = findViewById(R.id.etValue);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnSave = findViewById(R.id.btnSave);
        layoutStartDate = findViewById(R.id.layoutStartDate); // Ánh xạ layout
        layoutEndDate = findViewById(R.id.layoutEndDate);     // Ánh xạ layout

        discountService = new DiscountService(this);

        int discountId = getIntent().getIntExtra("discountId", -1);

        // --- 2. THAY ĐỔI TIÊU ĐỀ (TITLE) ---
        if(discountId != -1){
            toolbar.setTitle("Sửa Khuyến Mãi"); // Đổi tiêu đề
            discount = discountService.getDiscountById(discountId);
            if(discount != null){
                etName.setText(discount.getName());
                etCode.setText(discount.getCode());
                etDescription.setText(discount.getDescription());
                etCondition.setText(String.valueOf(discount.getCondition()));
                etValue.setText(String.valueOf(discount.getValue()));
                etStartDate.setText(discount.getStartDate());
                etEndDate.setText(discount.getEndDate());
            } else {
                Toast.makeText(this, "Không tìm thấy khuyến mãi", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            toolbar.setTitle("Thêm Khuyến Mãi"); // Tiêu đề mặc định
            discount = null; // Đảm bảo discount là null khi thêm mới
        }


        // --- 3. SỬA LẠI CÁCH BẤM LỊCH (Bấm vào icon) ---
        layoutStartDate.setEndIconOnClickListener(v -> showDatePicker(etStartDate));
        layoutEndDate.setEndIconOnClickListener(v -> showDatePicker(etEndDate));

        // (Bạn vẫn có thể giữ 2 dòng này nếu muốn bấm vào ô text cũng ra lịch)
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnSave.setOnClickListener(v -> saveDiscount());
    }

    private void showDatePicker(EditText editText){
        final Calendar c = Calendar.getInstance();

        // Đoạn này hay: Nếu ô text đã có ngày, dùng ngày đó làm mặc định
        if (!editText.getText().toString().isEmpty()) {
            try {
                String[] dateParts = editText.getText().toString().split("-");
                c.set(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
            } catch (Exception e) {
                // Nếu parse lỗi, cứ dùng ngày hiện tại
            }
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format chuẩn "YYYY-MM-DD"
                    String dateStr = year1 + "-" + String.format("%02d", month1 + 1) + "-" + String.format("%02d", dayOfMonth);
                    editText.setText(dateStr);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveDiscount(){
        String name = etName.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String condStr = etCondition.getText().toString().trim();
        String valueStr = etValue.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();

        if(name.isEmpty() || code.isEmpty() || condStr.isEmpty() || valueStr.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập các trường bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 4. THÊM TRY-CATCH ĐỂ TRÁNH CRASH ---
        double condition;
        double value;
        try {
            condition = Double.parseDouble(condStr);
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Điều kiện hoặc Giá trị không phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if(discount == null){
            discount = new Discount(name, code, description, condition, value, "", startDate, endDate, true, false);
            discountService.insertDiscount(discount);
            Toast.makeText(this, "Đã thêm khuyến mãi", Toast.LENGTH_SHORT).show();
        } else { // Cập nhật
            discount.setName(name);
            discount.setCode(code);
            discount.setDescription(description);
            discount.setCondition(condition);
            discount.setValue(value);
            discount.setStartDate(startDate);
            discount.setEndDate(endDate);
            discountService.updateDiscount(discount);
            Toast.makeText(this, "Đã cập nhật khuyến mãi", Toast.LENGTH_SHORT).show();
        }

        finish(); // Đóng Activity sau khi lưu
    }
}