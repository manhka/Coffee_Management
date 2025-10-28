package com.app.coffeemanagementapplication.services;

import android.content.Context;
import android.location.Address;

import com.app.coffeemanagementapplication.AppDatabase;
import com.app.coffeemanagementapplication.DatabaseClient;
import com.app.coffeemanagementapplication.daos.IAddressDao;
import com.app.coffeemanagementapplication.models.ShippingAddress;
import com.app.coffeemanagementapplication.repositories.IAddressRepo;

import java.util.List;

public class AddressService implements IAddressRepo {
    private final IAddressDao addressDao;

    public AddressService(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
        this.addressDao = db.addressDao();
    }


    @Override
    public ShippingAddress getAddressById(int id) {
        return addressDao.getAddressById(id);
    }

    @Override
    public void updateAddressById(int id, String addressLine, String city, String district, String ward, String note, String fullName, String phone) {
        addressDao.updateAddressById(id, addressLine, city, district, ward, note, fullName, phone);
    }

    @Override
    public void insertAddress(ShippingAddress address) {
        addressDao.insertAddress(address);
    }

    @Override
    public List<ShippingAddress> getAddressesByUser(int userId) {
        return addressDao.getAddressesByUser(userId);
    }

    @Override
    public void deleteAddress(int id) {
        addressDao.deleteAddress(id);
    }

    @Override
    public void clearDefaultAddress(int userId) {
        addressDao.clearDefaultAddress(userId);
    }

    @Override
    public void setDefaultAddress(int addressId, int userId) {
        addressDao.clearDefaultAddress(userId);
        addressDao.setDefaultAddress(addressId);
    }
}
