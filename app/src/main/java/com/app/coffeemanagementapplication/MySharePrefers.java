package com.app.coffeemanagementapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharePrefers {
    private static final String PER_NAME = "data_app_shared_preference";
    private static SharedPreferences sharePref;

    private MySharePrefers() {
        // Ngăn tạo instance
    }

    public static void init(Context context) {
        if (sharePref == null) {
            sharePref = context.getApplicationContext()
                    .getSharedPreferences(PER_NAME, Context.MODE_PRIVATE);
        }
    }

    // ---------- SAVE KEY ----------
    public static <T> void saveKey(String key, T value) {
        SharedPreferences.Editor editor = sharePref.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }

    // ---------- GET VALUE ----------
    public static String getString(String key, String defaultValue) {
        return sharePref.getString(key, defaultValue).trim();
    }

    public static int getInt(String key, int defaultValue) {
        return sharePref.getInt(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharePref.getBoolean(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sharePref.getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue) {
        return sharePref.getFloat(key, defaultValue);
    }

    public static void removeKey(String key) {
        sharePref.edit().remove(key).apply();
    }
    // ---------- CUSTOM LOGIC ----------
    public static int getAddressId() {
        return getInt("getAddressId", -1);
    }
    public static void setAddressId(int addressId) {
        saveKey("getAddressId", addressId);
    }
    public static int getUserId() {
        return getInt("getUserId", 1);
    }
    public static void setUserId(int userId) {
        saveKey("getUserId", userId);
    }
    public static int getPaymentMethodId() {
        return getInt("getPaymentMethodId", -1);
    }
    public static void setPaymentMethodId(int paymentMethodId) {
        saveKey("getPaymentMethodId", paymentMethodId);
    }
    public static int getDiscountId() {
        return getInt("getDiscountId", -1);
    }
    public static void setDiscountId(int discountId) {
        saveKey("getDiscountId", discountId);
    }
    public static float getTotalOrder() {
        return getFloat("getTotalOrder", -1);
    }
    public static void setTotalOrder(float totalOrder) {
        saveKey("getTotalOrder", totalOrder);
    }
}
