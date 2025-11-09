package com.app.coffeemanagementapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.coffeemanagementapplication.BaseActivity;
import com.app.coffeemanagementapplication.R;
import com.app.coffeemanagementapplication.databinding.ActivityAddAddressBinding;
import com.app.coffeemanagementapplication.databinding.CustomToastBinding;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;
import com.app.coffeemanagementapplication.services.AddressService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddAddressActivity extends BaseActivity {
    private ActivityAddAddressBinding binding;
    private JSONObject tinhObj, huyenObj, xaObj;

    private String selectedProvince = null;
    private String selectedDistrict = null;
    private String selectedWard = null;
    private IAddressRepo addressRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadJSONData();
        setupProvinceSpinner();
        addressRepo = new AddressService(this);

        // Nút quay lại
        binding.imvBtnBack.setOnClickListener(v -> finish());

        // Nút lưu địa chỉ
        binding.btnSave.setOnClickListener(v -> validateInputs());
    }

    // ===========================================================
    // 1. Load dữ liệu từ file JSON (Tỉnh, Huyện, Xã)
    // ===========================================================
    private void loadJSONData() {
        try {
            tinhObj = new JSONObject(loadJSONFromAsset("tinh_tp.json"));
            huyenObj = new JSONObject(loadJSONFromAsset("quan_huyen.json"));
            xaObj = new JSONObject(loadJSONFromAsset("xa_phuong.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===========================================================
    // 2. Setup Spinner Tỉnh/Thành phố
    // ===========================================================
    private void setupProvinceSpinner() {
        List<String> provinces = new ArrayList<>();

        try {
            Iterator<String> keys = tinhObj.keys();
            while (keys.hasNext()) {
                String code = keys.next();
                provinces.add(tinhObj.getJSONObject(code).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, provinces
        );
        binding.spnProvince.setDropDownBackgroundResource(R.color.grey);
        binding.spnProvince.setAdapter(adapter);
        binding.spnProvince.setThreshold(1);
        binding.spnProvince.setOnClickListener(v -> binding.spnProvince.showDropDown());

        // Khi người dùng chọn tỉnh
        binding.spnProvince.setOnItemClickListener((parent, view, position, id) -> {
            selectedProvince = provinces.get(position);
            selectedDistrict = null;
            selectedWard = null;
            setupDistrictSpinner(selectedProvince);
        });
    }

    // ===========================================================
    // 3. Setup Spinner Quận/Huyện
    // ===========================================================
    private void setupDistrictSpinner(String selectedProvince) {
        List<String> districts = new ArrayList<>();
        String provinceCode = "";

        try {
            // Tìm mã tỉnh tương ứng
            Iterator<String> keys = tinhObj.keys();
            while (keys.hasNext()) {
                String code = keys.next();
                JSONObject province = tinhObj.getJSONObject(code);
                if (province.getString("name").equals(selectedProvince)) {
                    provinceCode = province.getString("code");
                    break;
                }
            }

            // Lọc các huyện thuộc tỉnh đó
            Iterator<String> huyenKeys = huyenObj.keys();
            while (huyenKeys.hasNext()) {
                String code = huyenKeys.next();
                JSONObject huyen = huyenObj.getJSONObject(code);
                if (huyen.getString("parent_code").equals(provinceCode)) {
                    districts.add(huyen.getString("name"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (districts.isEmpty()) {
            binding.spnDistrict.setAdapter(null);
            binding.spnWard.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, districts
        );
        binding.spnDistrict.setDropDownBackgroundResource(R.color.grey);
        binding.spnDistrict.setAdapter(adapter);
        binding.spnDistrict.setThreshold(1);
        binding.spnDistrict.setOnClickListener(v -> binding.spnDistrict.showDropDown());

        // Khi người dùng chọn huyện
        binding.spnDistrict.setOnItemClickListener((parent, view, position, id) -> {
            selectedDistrict = districts.get(position);
            selectedWard = null;
            setupWardSpinner(selectedDistrict);
        });

        // Reset xã khi đổi tỉnh
        binding.spnWard.setAdapter(null);
    }

    // ===========================================================
    // 4. Setup Spinner Phường/Xã
    // ===========================================================
    private void setupWardSpinner(String selectedDistrict) {
        List<String> wards = new ArrayList<>();
        String districtCode = "";

        try {
            // Tìm mã huyện
            Iterator<String> huyenKeys = huyenObj.keys();
            while (huyenKeys.hasNext()) {
                String code = huyenKeys.next();
                JSONObject huyen = huyenObj.getJSONObject(code);
                if (huyen.getString("name").equals(selectedDistrict)) {
                    districtCode = huyen.getString("code");
                    break;
                }
            }

            // Lọc danh sách xã theo mã huyện
            Iterator<String> xaKeys = xaObj.keys();
            while (xaKeys.hasNext()) {
                String code = xaKeys.next();
                JSONObject xa = xaObj.getJSONObject(code);
                if (xa.getString("parent_code").equals(districtCode)) {
                    wards.add(xa.getString("name"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (wards.isEmpty()) {
            binding.spnWard.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, wards
        );
        binding.spnWard.setDropDownBackgroundResource(R.color.grey);
        binding.spnWard.setAdapter(adapter);
        binding.spnWard.setThreshold(1);
        binding.spnWard.setOnClickListener(v -> binding.spnWard.showDropDown());

        binding.spnWard.setOnItemClickListener((parent, view, position, id) -> {
            selectedWard = wards.get(position);
        });
    }

    // ===========================================================
    // 5. Validate dữ liệu nhập vào
    // ===========================================================
    private void validateInputs() {
        String fullName = binding.edtFullName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String addressLine = binding.edtDetailAddress.getText().toString().trim();
        String note = binding.edtNote.getText().toString().trim();

        String phoneRegex = "^(0[3|5|7|8|9])[0-9]{8}$";

        if (fullName.isEmpty()) {
            binding.edtFullName.setError("Vui lòng nhập họ và tên");
            binding.edtFullName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            binding.edtPhone.setError("Vui lòng nhập số điện thoại");
            binding.edtPhone.requestFocus();
            return;
        } else if (!phone.matches(phoneRegex)) {
            binding.edtPhone.setError("Số điện thoại không hợp lệ");
            binding.edtPhone.requestFocus();
            return;
        }

        if (selectedProvince == null) {
            binding.spnProvince.setError("Vui lòng chọn Tỉnh/Thành phố");
            binding.spnProvince.requestFocus();
            return;
        }

        if (selectedDistrict == null) {
            binding.spnDistrict.setError("Vui lòng chọn Quận/Huyện");
            binding.spnDistrict.requestFocus();
            return;
        }

        if (selectedWard == null) {
            binding.spnWard.setError("Vui lòng chọn Phường/Xã");
            binding.spnWard.requestFocus();
            return;
        }

        if (addressLine.isEmpty()) {
            binding.edtDetailAddress.setError("Vui lòng nhập địa chỉ cụ thể");
            binding.edtDetailAddress.requestFocus();
            return;
        }

        // Nếu hợp lệ
        ShippingAddress shippingAddress = new ShippingAddress(1, addressLine, selectedProvince, selectedDistrict, selectedWard, note, false, fullName, phone);
        addressRepo.insertAddress(shippingAddress);
        CustomToastBinding binding = CustomToastBinding.inflate(getLayoutInflater());
        binding.toastText.setText("Thêm địa chỉ giao hàng thành công!");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
        Intent intent = new Intent(AddAddressActivity.this, ChoosingAddressActivity.class);
        startActivity(intent);
        finish();
    }
}
