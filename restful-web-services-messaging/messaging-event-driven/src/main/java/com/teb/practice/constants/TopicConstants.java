package com.teb.practice.constants;

public final class TopicConstants {

    public static final String ORDER_CREATED = "order.created";
    public static final String PAYMENT_PROCESSED = "payment.processed";
    public static final String INVENTORY_RESERVED = "inventory.reserved";
    public static final String NOTIFICATION_SENT = "notification.sent";

    private TopicConstants() {
        throw new IllegalStateException("Utility class");
    }
}
