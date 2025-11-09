package com.app.coffeemanagementapplication.models;

public class ProductFilter {
    private final int iconResId;
    private final String name;

    public ProductFilter(int iconResId, String name) {
        this.iconResId = iconResId;
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return name;
    }
}
