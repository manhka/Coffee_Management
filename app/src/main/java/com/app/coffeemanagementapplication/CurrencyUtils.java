package com.app.coffeemanagementapplication;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatVNCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " ₫";
    }
}
