package com.romazal.ecommerce.email;

import lombok.Getter;

public enum EmailTemplate {
    PRODUCT_THRESHOLD_NOTIFICATION("product-threshold-notification.html", "Current stock quantity of the product is lower then the specified threshold value"),
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
