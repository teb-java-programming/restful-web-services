package com.teb.practice.event;

public class KafkaTopics {

    public static final String ORDER_CREATED = "order.created";
    public static final String INVENTORY_RESERVED = "inventory.reserved";
    public static final String PAYMENT_COMPLETED = "payment.completed";
    public static final String NOTIFICATION_SENT = "notification.sent";

    public static final String ORDER_DLQ = "order.created.dlq";
    public static final String INVENTORY_DLQ = "inventory.reserved.dlq";
    public static final String PAYMENT_DLQ = "payment.completed.dlq";
}
