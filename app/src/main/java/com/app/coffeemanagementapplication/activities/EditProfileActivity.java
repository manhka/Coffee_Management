package com.app.coffeemanagementapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Import các file cần thiết
import com.app.coffeemanagementapplication.AppConstants;
import com.app.coffeemanagementapplication.MySharePrefers;
import com.app.coffeemanagementapplication.databinding.CustomToastBinding;
import com.app.coffeemanagementapplication.databinding.ActivityEditProfileBinding;
import com.app.coffeemanagementapplication.databinding.CustomToastFailBinding;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.services.AddressService;
import com.app.coffeemanagementapplication.services.UserService;

import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// ⚠️ Kế thừa từ AppCompatActivity (như file gốc của bạn)
// Nếu bạn dùng BaseActivity, hãy đổi AppCompatActivity thành BaseActivity
public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private UserService userService;
    private AddressService addressService; // Thêm Service
    private int userIdToEdit = -1;
    private Users currentUser;
    private ShippingAddress currentAddress;

    // 1. Thêm các biến logic JSON từ AddAddressActivity
    private JSONObject tinhObj, huyenObj, xaObj;
    private String selectedProvince = null;
    private String selectedDistrict = null;
    private String selectedWard = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userService = new UserService(getApplication());
        addressService = new AddressService(getApplication()); // Khởi tạo

        // Lấy ID trực tiếp từ SharedPreferences (như logic gốc của bạn)
        userIdToEdit = MySharePrefers.getInt(AppConstants.KEY_USER_ID, -1);

        if (userIdToEdit == -1) {
            showFailToast("Không tìm thấy người dùng.");
            finish();
            return;
        }

        // 2. Thêm logic load JSON và setup Spinner (từ AddAddressActivity)
        loadJSONData();
        setupProvinceSpinner(); // Chỉ setup Tỉnh ban đầu

        loadUserData(); // Tải cả User và Address

        setupListeners();
    }

    private void loadUserData() {
        try {
            // 1. Tải thông tin User (logic gốc)
            currentUser = userService.getUserById(userIdToEdit);
            if (currentUser != null) {
                binding.editTextEditFullName.setText(currentUser.getFullName());
                binding.editTextEditPhone.setText(currentUser.getPhone());
            } else {
                showFailToast("Lỗi tải dữ liệu.");
                finish();
                return;
            }

            // 2. Tải thông tin Địa chỉ Mặc định
            currentAddress = addressService.getDefaultAddressByUserId(userIdToEdit);

            if (currentAddress != null) {
                // Nếu có địa chỉ -> Tải lên form
                populateAddressFields(currentAddress);
            }
            // (Nếu currentAddress == null, các spinner sẽ rỗng, sẵn sàng để điền mới)

        } catch (Exception e) {
            showFailToast("Lỗi tải dữ liệu.");
            Log.e("EditProfileActivity", "Error loading user data", e);
            finish();
        }
    }

    private void setupListeners() {
        // Nút Lưu (logic gốc)
        binding.buttonSaveChanges.setOnClickListener(v -> saveChanges());
        // (Bỏ qua nút back nếu bạn không dùng toolbar)

        // 3. Thêm Listeners cho Spinner (logic từ AddAddressActivity)
        binding.spnProvince.setOnItemClickListener((parent, view, position, id) -> {
            selectedProvince = (String) parent.getItemAtPosition(position);
            selectedDistrict = null;
            selectedWard = null;
            binding.spnDistrict.setText("", false); // Xóa text cũ
            binding.spnWard.setText("", false);
            setupDistrictSpinner(selectedProvince); // Tải huyện
        });

        binding.spnDistrict.setOnItemClickListener((parent, view, position, id) -> {
            selectedDistrict = (String) parent.getItemAtPosition(position);
            selectedWard = null;
            binding.spnWard.setText("", false);
            setupWardSpinner(selectedDistrict); // Tải xã
        });

        binding.spnWard.setOnItemClickListener((parent, view, position, id) -> {
            selectedWard = (String) parent.getItemAtPosition(position);
        });

        // Click để hiện dropdown (logic từ AddAddressActivity)
        binding.spnProvince.setOnClickListener(v -> binding.spnProvince.showDropDown());
        binding.spnDistrict.setOnClickListener(v -> binding.spnDistrict.showDropDown());
        binding.spnWard.setOnClickListener(v -> binding.spnWard.showDropDown());
    }

    private void saveChanges() {
        if (currentUser == null) {
            showFailToast("Dữ liệu chưa sẵn sàng.");
            return;
        }

        // 1. Lấy dữ liệu User (logic gốc)
        String newFullName = binding.editTextEditFullName.getText().toString().trim();
        String newPhone = binding.editTextEditPhone.getText().toString().trim();

        // 2. Lấy dữ liệu Địa chỉ (logic từ AddAddressActivity)
        String addressLine = binding.edtDetailAddress.getText().toString().trim();
        // selectedProvince, District, Ward đã được cập nhật bởi listeners
        // Lấy lại giá trị text phòng trường hợp người dùng không click listener


        boolean isDefault = true; // Địa chỉ trên trang Profile luôn là mặc định

        // --- Validation ---
        // Validate User
        //if (TextUtils.isEmpty(fullName)) {
           // binding.layoutInputFullName.setError("Họ và tên không được để trống");
           // binding.layoutInputFullName.requestFocus();
           // return;
       // } else {
          //  binding.layoutInputFullName.setError(null);
       // }

        String phoneRegex = "^(0[3|5|7|8|9])[0-9]{8}$"; //
       // if (TextUtils.isEmpty(phone)) {
           // binding.layoutInputPhone.setError("Số điện thoại không được để trống");
           // binding.layoutInputPhone.requestFocus();
           // return;
       // } else
        if (!newPhone.matches(phoneRegex)) {
            binding.layoutInputPhone.setError("Số điện thoại không hợp lệ");
            binding.layoutInputPhone.requestFocus();
            return;
        } else {
            binding.layoutInputPhone.setError(null);
        }

        // Validate Address (chỉ validate nếu người dùng bắt đầu nhập)
        boolean isAddressFieldsFilled = !TextUtils.isEmpty(addressLine) && !TextUtils.isEmpty(selectedProvince) && !TextUtils.isEmpty(selectedDistrict) && !TextUtils.isEmpty(selectedWard);
        boolean isAddressFieldsEmpty = TextUtils.isEmpty(addressLine) && TextUtils.isEmpty(selectedProvince) && TextUtils.isEmpty(selectedDistrict) && TextUtils.isEmpty(selectedWard);

        // Nếu người dùng đã điền 1 phần địa chỉ nhưng chưa đủ -> Báo lỗi
        if (!isAddressFieldsFilled && !isAddressFieldsEmpty) {
            showFailToast("Vui lòng hoàn tất thông tin địa chỉ (Tỉnh, Huyện, Xã và địa chỉ cụ thể)");
            if (TextUtils.isEmpty(selectedProvince)) binding.spnProvince.setError("Vui lòng chọn");
            if (TextUtils.isEmpty(selectedDistrict)) binding.spnDistrict.setError("Vui lòng chọn");
            if (TextUtils.isEmpty(selectedWard)) binding.spnWard.setError("Vui lòng chọn");
            if (addressLine.isEmpty()) binding.layoutInputAddressLine.setError("Vui lòng nhập");
            return;
        }
        // --- End Validation ---

        try {
            // --- LOGIC LƯU (SỬA LẠI) ---

            // 1. Cập nhật User (Chỉ cập nhật nếu KHÔNG RỖNG)
            if (!TextUtils.isEmpty(newFullName)) {
                currentUser.setFullName(newFullName);
            }
            if (!TextUtils.isEmpty(newPhone)) {
                currentUser.setPhone(newPhone);
            }
            // Luôn gọi updateUser, các trường rỗng sẽ không ghi đè lên đối tượng currentUser
            userService.updateUser(currentUser);

            // 2. Cập nhật/Thêm mới Địa chỉ
            if (isAddressFieldsFilled) {
                // Lấy tên và SĐT (Ưu tiên Tên/SĐT mới, nếu rỗng thì lấy Tên/SĐT cũ của User)
                String finalFullName = !TextUtils.isEmpty(newFullName) ? newFullName : currentUser.getFullName();
                String finalPhone = !TextUtils.isEmpty(newPhone) ? newPhone : currentUser.getPhone();

                if (currentAddress != null) {
                    // --- Chế độ CẬP NHẬT Địa chỉ ---
                    addressService.updateAddressById(
                            currentAddress.getId(),
                            addressLine,
                            selectedProvince,
                            selectedDistrict,
                            selectedWard,
                            currentAddress.getNote(),
                            finalFullName,
                            finalPhone
                    );
                    addressService.setDefaultAddress(currentAddress.getId(), userIdToEdit);

                } else {
                    // --- Chế độ THÊM MỚI Địa chỉ ---
                    ShippingAddress newAddress = new ShippingAddress(
                            userIdToEdit, addressLine, selectedProvince, selectedDistrict, selectedWard,
                            "", isDefault, finalFullName, finalPhone
                    );
                    addressService.insertAddress(newAddress);
                }
            }
            showCustomToast("Cập nhật thành công!");
            finish();

        } catch (Exception e) {
            showFailToast("Lỗi khi cập nhật.");
            Log.e("EditProfileActivity", "Error saving changes", e);
        }
    }

    // 4. THÊM TẤT CẢ CÁC HÀM LOGIC JSON TỪ AddAddressActivity

    private void loadJSONData() {
        try {
            tinhObj = new JSONObject(loadJSONFromAsset("tinh_tp.json"));
            huyenObj = new JSONObject(loadJSONFromAsset("quan_huyen.json"));
            xaObj = new JSONObject(loadJSONFromAsset("xa_phuong.json"));
        } catch (Exception e) {
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

    private void setupProvinceSpinner() {
        List<String> provinces = new ArrayList<>();
        try {
            Iterator<String> keys = tinhObj.keys();
            while (keys.hasNext()) {
                String code = keys.next();
                provinces.add(tinhObj.getJSONObject(code).getString("name"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, provinces
        );
        binding.spnProvince.setAdapter(adapter);
        binding.spnProvince.setThreshold(1);
    }

    private void setupDistrictSpinner(String selectedProvince) {
        List<String> districts = new ArrayList<>();
        String provinceCode = "";
        try {
            Iterator<String> keys = tinhObj.keys();
            while (keys.hasNext()) {
                String code = keys.next();
                JSONObject province = tinhObj.getJSONObject(code);
                if (province.getString("name").equals(selectedProvince)) {
                    provinceCode = province.getString("code");
                    break;
                }
            }
            Iterator<String> huyenKeys = huyenObj.keys();
            while (huyenKeys.hasNext()) {
                String code = huyenKeys.next();
                JSONObject huyen = huyenObj.getJSONObject(code);
                if (huyen.getString("parent_code").equals(provinceCode)) {
                    districts.add(huyen.getString("name"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (districts.isEmpty()) {
            binding.spnDistrict.setAdapter(null);
            binding.spnWard.setAdapter(null);
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, districts
        );
        binding.spnDistrict.setAdapter(adapter);
        binding.spnDistrict.setThreshold(1);
        binding.spnWard.setAdapter(null); // Reset xã
    }

    private void setupWardSpinner(String selectedDistrict) {
        List<String> wards = new ArrayList<>();
        String districtCode = "";
        try {
            Iterator<String> huyenKeys = huyenObj.keys();
            while (huyenKeys.hasNext()) {
                String code = huyenKeys.next();
                JSONObject huyen = huyenObj.getJSONObject(code);
                if (huyen.getString("name").equals(selectedDistrict)) {
                    districtCode = huyen.getString("code");
                    break;
                }
            }
            Iterator<String> xaKeys = xaObj.keys();
            while (xaKeys.hasNext()) {
                String code = xaKeys.next();
                JSONObject xa = xaObj.getJSONObject(code);
                if (xa.getString("parent_code").equals(districtCode)) {
                    wards.add(xa.getString("name"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (wards.isEmpty()) {
            binding.spnWard.setAdapter(null);
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, wards
        );
        binding.spnWard.setAdapter(adapter);
        binding.spnWard.setThreshold(1);
    }

    // 5. THÊM HÀM MỚI ĐỂ LOAD DỮ LIỆU CÓ SẴN
    private void populateAddressFields(ShippingAddress address) {
        // Set text cho AutoCompleteTextView.
        binding.spnProvince.setText(address.getCity(), false);
        selectedProvince = address.getCity();

        // Tải lại danh sách Huyện dựa trên Tỉnh đã chọn
        setupDistrictSpinner(address.getCity());
        binding.spnDistrict.setText(address.getDistrict(), false);
        selectedDistrict = address.getDistrict();

        // Tải lại danh sách Xã dựa trên Huyện đã chọn
        setupWardSpinner(address.getDistrict());
        binding.spnWard.setText(address.getWard(), false);
        selectedWard = address.getWard();

        // Set địa chỉ chi tiết
        binding.edtDetailAddress.setText(address.getAddressLine());
    }

    private void showCustomToast(String message) {
        com.app.coffeemanagementapplication.databinding.CustomToastBinding binding = com.app.coffeemanagementapplication.databinding.CustomToastBinding.inflate(getLayoutInflater());
        binding.toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
    }

    private void showFailToast(String message) {
        CustomToastFailBinding binding = CustomToastFailBinding.inflate(getLayoutInflater());
        binding.toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.show();
    }
}