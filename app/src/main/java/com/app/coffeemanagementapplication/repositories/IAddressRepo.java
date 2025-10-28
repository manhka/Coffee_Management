package com.app.coffeemanagementapplication.repositories;


import com.app.coffeemanagementapplication.models.ShippingAddress;

import java.util.List;

public interface IAddressRepo {
    ShippingAddress getAddressById(int id);


    void updateAddressById(int id,
                           String addressLine,
                           String city,
                           String district,
                           String ward,
                           String note,
                           String fullName,
                           String phone
    );

    void insertAddress(ShippingAddress address);

    List<ShippingAddress> getAddressesByUser(int userId);

    void deleteAddress(int id);

    void clearDefaultAddress(int userId);

    // Đặt 1 địa chỉ làm mặc định
    void setDefaultAddress(int addressId, int userId);
}
