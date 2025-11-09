package com.app.coffeemanagementapplication.models;

public enum OrderItemStatus {
    PENDING("pending"),
    PAY("pay"),
    FINISH("finish");

    private final String value;

    OrderItemStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderItemStatus fromValue(String value) {
        for (OrderItemStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}