package com.romazal.ecommerce.email;

import lombok.Getter;

public enum EmailTemplate {
    PRODUCT_THRESHOLD_NOTIFICATION("product-threshold-notification.html", "Current stock quantity of the product is lower then the specified threshold value"),
    ORDER_PAYMENT_LINK_NOTIFICATION("order-payment-link-notification.html", "The order has initiated a payment request"),
    ORDER_CONFIRMATION_NOTIFICATION("order-confirmation-notification.html", "The order has been confirmed."),
    ORDER_CANCELLATION_NOTIFICATION("order-cancellation-notification.html", "The order has been cancelled."),
    PAYMENT_CONFIRMATION_NOTIFICATION("payment-confirmation-notification.html", "The payment has been confirmed."),
    PAYMENT_REFUND_NOTIFICATION("payment-refund-notification.html", "The payment has been refunded."),
    SHIPMENT_SHIPPED_NOTIFICATION("shipment-shipped-notification.html", "A shipment for the order has been initiated."),
    SHIPMENT_DELIVERED_NOTIFICATION("shipment-delivered-notification.html", "The shipment for the order has been delivered.");

    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
