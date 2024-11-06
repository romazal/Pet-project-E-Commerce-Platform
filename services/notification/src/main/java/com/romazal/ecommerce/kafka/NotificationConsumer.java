package com.romazal.ecommerce.kafka;

import com.romazal.ecommerce.email.EmailService;
import com.romazal.ecommerce.kafka.order.OrderCancellationNotification;
import com.romazal.ecommerce.kafka.order.OrderConfirmationNotification;
import com.romazal.ecommerce.kafka.order.OrderPaymentLinkNotification;
import com.romazal.ecommerce.kafka.payment.PaymentConfirmationNotification;
import com.romazal.ecommerce.kafka.payment.PaymentMethod;
import com.romazal.ecommerce.kafka.payment.PaymentRefundNotification;
import com.romazal.ecommerce.kafka.product.ProductThresholdNotification;
import com.romazal.ecommerce.kafka.product.PurchaseRequest;
import com.romazal.ecommerce.kafka.shipment.ShipmentDeliveredNotification;
import com.romazal.ecommerce.kafka.shipment.ShipmentShippedNotification;
import com.romazal.ecommerce.notification.Notification;
import com.romazal.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static com.romazal.ecommerce.notification.NotificationType.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "product-threshold-topic")
    public void consumeProductThresholdNotification(ProductThresholdNotification productThresholdNotification) throws MessagingException {
        log.info("Consuming the message from product-threshold-topic Topic:: {}", productThresholdNotification);

        repository.save(
                Notification.builder()
                        .type(PRODUCT_THRESHOLD_NOTIFICATION)
                        .destinationEmail(productThresholdNotification.storeEmail())
                        .message(
                                format(
                                        """
                                        Product Threshold Notification:
                                        
                                        The current stock quantity for the product '%s' has fallen below the specified threshold value.
                                        
                                        Current Stock Quantity: %.2f
                                        Threshold Quantity: %.2f
                                        """,
                                        productThresholdNotification.productName(),
                                        productThresholdNotification.stockQuantity(),
                                        productThresholdNotification.thresholdQuantity()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .productThresholdNotification(productThresholdNotification)
                        .build()
        );

        emailService.sendProductThresholdNotificationEmail(
                productThresholdNotification.storeEmail(),
                productThresholdNotification.storeName(),
                productThresholdNotification.productName(),
                productThresholdNotification.productId(),
                productThresholdNotification.sku(),
                productThresholdNotification.stockQuantity(),
                productThresholdNotification.thresholdQuantity()
        );
    }

    @KafkaListener(topics = "shipment-shipped-topic")
    public void consumeShipmentShippedNotification(ShipmentShippedNotification shipmentShippedNotification) throws MessagingException {
        log.info("Consuming the message from shipment-shipped-topic Topic:: {}", shipmentShippedNotification);

        repository.save(
                Notification.builder()
                        .type(SHIPMENT_SHIPPED_NOTIFICATION)
                        .destinationEmail(shipmentShippedNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Shipment Shipped Notification:
                                        
                                        A shipment for order ID: %s has been initiated.
                                        
                                        Shipped Date: %s
                                        Estimated Delivery Date: %s
                                        """,
                                        shipmentShippedNotification.orderId(),
                                        shipmentShippedNotification.shippedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        shipmentShippedNotification.estimatedDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .shipmentShippedNotification(shipmentShippedNotification)
                        .build()
        );

        emailService.sendShipmentShippedNotificationEmail(
                shipmentShippedNotification.customerEmail(),
                shipmentShippedNotification.shipmentId(),
                shipmentShippedNotification.orderId(),
                shipmentShippedNotification.customerName(),
                shipmentShippedNotification.trackingNumber(),
                shipmentShippedNotification.logisticsProvider(),
                shipmentShippedNotification.shippedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                shipmentShippedNotification.estimatedDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    @KafkaListener(topics = "shipment-delivered-topic")
    public void consumeShipmentDeliveredNotification(ShipmentDeliveredNotification shipmentDeliveredNotification) throws MessagingException {
        log.info("Consuming the message from shipment-delivered-topic Topic:: {}", shipmentDeliveredNotification);

        repository.save(
                Notification.builder()
                        .type(SHIPMENT_DELIVERED_NOTIFICATION)
                        .destinationEmail(shipmentDeliveredNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Shipment Delivered Notification:
                                        
                                        The shipment for order ID: %s has been delivered.
                                        
                                        Delivered Date: %s
                                        """,
                                        shipmentDeliveredNotification.orderId(),
                                        shipmentDeliveredNotification.deliveredDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .shipmentDeliveredNotification(shipmentDeliveredNotification)
                        .build()
        );

        emailService.sendShipmentDeliveredNotificationEmail(
                shipmentDeliveredNotification.customerEmail(),
                shipmentDeliveredNotification.shipmentId(),
                shipmentDeliveredNotification.orderId(),
                shipmentDeliveredNotification.customerName(),
                shipmentDeliveredNotification.trackingNumber(),
                shipmentDeliveredNotification.logisticsProvider(),
                shipmentDeliveredNotification.deliveredDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    @KafkaListener(topics = "order-payment-link-topic")
    public void consumeOrderPaymentLinkNotification(OrderPaymentLinkNotification orderPaymentLinkNotification) throws MessagingException {
        log.info("Consuming the message from order-payment-link-topic Topic:: {}", orderPaymentLinkNotification);

        repository.save(
                Notification.builder()
                        .type(ORDER_PAYMENT_LINK_NOTIFICATION)
                        .destinationEmail(orderPaymentLinkNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Order Payment Link Notification:
                                        
                                        The order has initiated a payment request.
                                        
                                        Order ID: %s
                                        Payment ID: %s
                                        """,
                                        orderPaymentLinkNotification.orderId(),
                                        orderPaymentLinkNotification.paymentId()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .orderPaymentLinkNotification(orderPaymentLinkNotification)
                        .build()
        );

        emailService.sendOrderPaymentLinkNotification(
                orderPaymentLinkNotification.customerEmail(),
                orderPaymentLinkNotification.orderId(),
                orderPaymentLinkNotification.paymentId(),
                orderPaymentLinkNotification.customerName(),
                orderPaymentLinkNotification.totalAmount(),
                orderPaymentLinkNotification.orderItems()
        );
    }

    @KafkaListener(topics = "order-confirmation-topic")
    public void consumeOrderConfirmationNotification(OrderConfirmationNotification orderConfirmationNotification) throws MessagingException {
        log.info("Consuming the message from order-confirmation-topic Topic:: {}", orderConfirmationNotification);

        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION_NOTIFICATION)
                        .destinationEmail(orderConfirmationNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Order Confirmation Notification:
                                        
                                        The order has been confirmed.
                                        
                                        Order ID: %s
                                        """,
                                        orderConfirmationNotification.orderId()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmationNotification(orderConfirmationNotification)
                        .build()
        );

        emailService.sendOrderConfirmationNotification(
                orderConfirmationNotification.customerEmail(),
                orderConfirmationNotification.orderId(),
                orderConfirmationNotification.customerName(),
                orderConfirmationNotification.totalAmount(),
                orderConfirmationNotification.orderItems()
        );
    }

    @KafkaListener(topics = "order-cancellation-topic")
    public void consumeOrderCancellationNotification(OrderCancellationNotification orderCancellationNotification) throws MessagingException {
        log.info("Consuming the message from order-cancellation-topic Topic:: {}", orderCancellationNotification);

        repository.save(
                Notification.builder()
                        .type(ORDER_CANCELLATION_NOTIFICATION)
                        .destinationEmail(orderCancellationNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Order Cancellation Notification:
                                        
                                        The order has been cancelled.
                                        
                                        Order ID: %s
                                        """,
                                        orderCancellationNotification.orderId()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .orderCancellationNotification(orderCancellationNotification)
                        .build()
        );

        emailService.sendOrderCancellationNotification(
                orderCancellationNotification.customerEmail(),
                orderCancellationNotification.orderId(),
                orderCancellationNotification.customerName(),
                orderCancellationNotification.totalAmount(),
                orderCancellationNotification.orderItems(),
                orderCancellationNotification.createdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    @KafkaListener(topics = "payment-confirmation-topic")
    public void consumePaymentConfirmationNotification(PaymentConfirmationNotification paymentConfirmationNotification) throws MessagingException {
        log.info("Consuming the message from payment-confirmation-topic Topic:: {}", paymentConfirmationNotification);

        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION_NOTIFICATION)
                        .destinationEmail(paymentConfirmationNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Payment Confirmation Notification:
                                        
                                        The payment has been confirmed.
                                        
                                        Order ID: %s
                                        Payment ID: %s
                                        """,
                                        paymentConfirmationNotification.orderId(),
                                        paymentConfirmationNotification.paymentId()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmationNotification(paymentConfirmationNotification)
                        .build()
        );

        emailService.sendPaymentConfirmationNotification(
                paymentConfirmationNotification.customerEmail(),
                paymentConfirmationNotification.paymentId(),
                paymentConfirmationNotification.orderId(),
                paymentConfirmationNotification.totalAmount(),
                paymentConfirmationNotification.customerName(),
                paymentConfirmationNotification.paymentMethod()
        );
    }

    @KafkaListener(topics = "payment-refund-topic")
    public void consumePaymentRefundNotification(PaymentRefundNotification paymentRefundNotification) throws MessagingException {
        log.info("Consuming the message from payment-refund-topic Topic:: {}", paymentRefundNotification);

        repository.save(
                Notification.builder()
                        .type(PAYMENT_REFUND_NOTIFICATION)
                        .destinationEmail(paymentRefundNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Payment Refund Notification:
                                        
                                        The payment has been refunded.
                                        
                                        Order ID: %s
                                        Payment ID: %s
                                        """,
                                        paymentRefundNotification.orderId(),
                                        paymentRefundNotification.paymentId()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .paymentRefundNotification(paymentRefundNotification)
                        .build()
        );

        emailService.sendPaymentRefundNotification(
                paymentRefundNotification.customerEmail(),
                paymentRefundNotification.paymentId(),
                paymentRefundNotification.orderId(),
                paymentRefundNotification.totalAmount(),
                paymentRefundNotification.customerName()
        );
    }



}


