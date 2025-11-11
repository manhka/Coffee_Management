package com.app.coffeemanagementapplication.models;

public enum OrderStatus {
    PENDING("PENDING"),
    PAY("PAY"),
    PREPARING("PREPARING"),
    COMPLETED("COMPLETED"),
    DELIVERED("DELIVERED");


    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
