package com.romazal.ecommerce.email;

import com.romazal.ecommerce.kafka.payment.PaymentMethod;
import com.romazal.ecommerce.kafka.product.PurchaseRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.romazal.ecommerce.email.EmailTemplate.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendProductThresholdNotificationEmail(
            String destinationEmail,
            String storeName,
            String productName,
            UUID productId,
            String sku,
            Double stockQuantity,
            Double thresholdQuantity

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = PRODUCT_THRESHOLD_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("storeName", storeName);
        variables.put("productName", productName);
        variables.put("productId", productId);
        variables.put("sku", sku);
        variables.put("stockQuantity", stockQuantity);
        variables.put("thresholdQuantity", thresholdQuantity);

        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(PRODUCT_THRESHOLD_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - PRODUCT_THRESHOLD_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send PRODUCT_THRESHOLD_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendOrderPaymentLinkNotification(
            String destinationEmail,
            UUID orderId,
            UUID paymentId,
            String customerName,
            BigDecimal totalAmount,
            List<PurchaseRequest> orderItems

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = ORDER_PAYMENT_LINK_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        variables.put("paymentId", paymentId);
        variables.put("customerName", customerName);
        variables.put("totalAmount", totalAmount);
        variables.put("orderItems", orderItems);


        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(ORDER_PAYMENT_LINK_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - ORDER_PAYMENT_LINK_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send ORDER_PAYMENT_LINK_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendOrderConfirmationNotification(
            String destinationEmail,
            UUID orderId,
            String customerName,
            BigDecimal totalAmount,
            List<PurchaseRequest> orderItems

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = ORDER_CONFIRMATION_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        variables.put("customerName", customerName);
        variables.put("totalAmount", totalAmount);
        variables.put("orderItems", orderItems);


        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(ORDER_CONFIRMATION_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - ORDER_CONFIRMATION_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send ORDER_CONFIRMATION_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendOrderCancellationNotification(
            String destinationEmail,
            UUID orderId,
            String customerName,
            BigDecimal totalAmount,
            List<PurchaseRequest> orderItems,
            String createdDate

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = ORDER_CANCELLATION_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        variables.put("customerName", customerName);
        variables.put("totalAmount", totalAmount);
        variables.put("orderItems", orderItems);
        variables.put("createdDate", createdDate);


        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(ORDER_CANCELLATION_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - ORDER_CANCELLATION_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send ORDER_CANCELLATION_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendPaymentConfirmationNotification(
            String destinationEmail,
            UUID paymentId,
            UUID orderId,
            BigDecimal totalAmount,
            String customerName,
            PaymentMethod paymentMethod

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = PAYMENT_CONFIRMATION_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("paymentId", paymentId);
        variables.put("orderId", orderId);
        variables.put("totalAmount", totalAmount);
        variables.put("customerName", customerName);
        variables.put("paymentMethod", paymentMethod);


        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(PAYMENT_CONFIRMATION_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - PAYMENT_CONFIRMATION_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send PAYMENT_CONFIRMATION_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendPaymentRefundNotification(
            String destinationEmail,
            UUID paymentId,
            UUID orderId,
            BigDecimal totalAmount,
            String customerName

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = PAYMENT_REFUND_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("paymentId", paymentId);
        variables.put("orderId", orderId);
        variables.put("totalAmount", totalAmount);
        variables.put("customerName", customerName);

        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(PAYMENT_REFUND_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - PAYMENT_REFUND_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send PAYMENT_REFUND_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendShipmentShippedNotificationEmail(
            String destinationEmail,
            UUID shipmentId,
            UUID orderId,
            String customerName,
            String trackingNumber,
            String logisticsProvider,
            String shippedDate,
            String estimatedDeliveryDate

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = SHIPMENT_SHIPPED_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("shipmentId", shipmentId);
        variables.put("orderId", orderId);
        variables.put("customerName", customerName);
        variables.put("trackingNumber", trackingNumber);
        variables.put("logisticsProvider", logisticsProvider);
        variables.put("shippedDate", shippedDate);
        variables.put("estimatedDeliveryDate", estimatedDeliveryDate);


        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(SHIPMENT_SHIPPED_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - SHIPMENT_SHIPPED_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send SHIPMENT_SHIPPED_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }

    @Async
    public void sendShipmentDeliveredNotificationEmail(
            String destinationEmail,
            UUID shipmentId,
            UUID orderId,
            String customerName,
            String trackingNumber,
            String logisticsProvider,
            String deliveredDate

    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );

        messageHelper.setFrom("contact@romazal.com");

        final String templateName = SHIPMENT_DELIVERED_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("shipmentId", shipmentId);
        variables.put("orderId", orderId);
        variables.put("customerName", customerName);
        variables.put("trackingNumber", trackingNumber);
        variables.put("logisticsProvider", logisticsProvider);
        variables.put("deliveredDate", deliveredDate);

        Context context = new Context();
        context.setVariables(variables);

        messageHelper.setSubject(SHIPMENT_DELIVERED_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - SHIPMENT_DELIVERED_NOTIFICATION email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.warn("WARNING - Could not send SHIPMENT_DELIVERED_NOTIFICATION email to {} Error: {}", destinationEmail, e.getMessage());
        }
    }


}
