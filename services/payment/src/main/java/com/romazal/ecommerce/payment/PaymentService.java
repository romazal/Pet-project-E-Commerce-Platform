package com.romazal.ecommerce.payment;

import com.romazal.ecommerce.exception.PaymentNotFoundException;
import com.romazal.ecommerce.kafka.notification.NotificationKafkaTemplate;
import com.romazal.ecommerce.kafka.notification.PaymentConfirmationNotification;
import com.romazal.ecommerce.kafka.notification.PaymentRefundNotification;
import com.romazal.ecommerce.order.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.romazal.ecommerce.payment.PaymentStatus.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderClient orderClient;
    private final NotificationKafkaTemplate notificationKafkaTemplate;


    public UUID createPayment(PaymentRequest paymentRequest) {
        repository.updatePendingPaymentStatusToFailedByOrderId (paymentRequest.orderId());

        var payment = mapper.toPayment(paymentRequest);

        return repository.save(payment).getPaymentId();
    }

    public UUID confirmPayment(UUID paymentId) {
        var payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided ID:: %s", paymentId)
                ));

        if (payment.getPaymentStatus() != PENDING) {
            throw new IllegalStateException(
                    format("Cannot confirm the payment, the payment is no longer pending, current payment status:: %s", payment.getPaymentStatus())
            );
        }

        payment.setPaymentStatus(PAID);

        orderClient.confirmOrder(payment.getOrderId());

        notificationKafkaTemplate.sendPaymentConfirmationNotification(
                new PaymentConfirmationNotification(
                      payment.getPaymentId(),
                      payment.getOrderId(),
                      payment.getTotalAmount(),
                      payment.getCustomerEmail(),
                      payment.getCustomerName(),
                      payment.getPaymentMethod()
                )
        );

        return repository.save(payment).getPaymentId();
    }

    public UUID failPayment(UUID paymentId) {
        var payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided ID:: %s", paymentId)
                ));

        if (payment.getPaymentStatus() != PENDING) {
            throw new IllegalStateException(
                    format("Cannot fail the payment, the payment is no longer pending, current payment status:: %s", payment.getPaymentStatus())
            );
        }

        payment.setPaymentStatus(FAILED);

        return repository.save(payment).getPaymentId();
    }

    public UUID refundPayment(UUID paymentId) {
        var payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided ID:: %s", paymentId)
                ));

        if (payment.getPaymentStatus() != PAID) {
            throw new IllegalStateException(
                    format("Cannot refund the payment, the payment is not confirmed, current payment status:: %s", payment.getPaymentStatus())
            );
        }

        payment.setPaymentStatus(REFUNDED);

        notificationKafkaTemplate.sendPaymentRefundNotification(
                new PaymentRefundNotification(
                        payment.getPaymentId(),
                        payment.getOrderId(),
                        payment.getTotalAmount(),
                        payment.getCustomerEmail(),
                        payment.getCustomerName()
                )
        );

        return repository.save(payment).getPaymentId();
    }

    public List<PaymentResponse> getAllPayments() {
        return repository.findAll()
                .stream()
                .map(mapper::toPaymentResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(UUID paymentId) {
        return repository.findById(paymentId).map(mapper::toPaymentResponse)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided payment ID:: %s", paymentId)
                ));
    }

    public PaymentResponse getPaymentByOrderId(UUID orderId) {
        return repository.findFirstByOrderIdOrderByCreatedDateDesc(orderId).map(mapper::toPaymentResponse)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided order ID:: %s", orderId)
                ));
    }

    public PaymentStatus getPaymentStatusByPaymentId(UUID paymentId) {
        return repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided payment ID:: %s", paymentId)
                )).getPaymentStatus();
    }


    public PaymentStatus getPaymentStatusByOrderId(UUID orderId) {
        return repository.findFirstByOrderIdOrderByCreatedDateDesc(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        format("No payment found with the provided order ID:: %s", orderId)
                )).getPaymentStatus();
    }

    public List<PaymentResponse> getPaymentHistoryByOrderId(UUID orderId) {
        return repository.findAllByOrderIdOrderByCreatedDateDesc(orderId)
                .stream()
                .map(mapper::toPaymentResponse)
                .toList();
    }
}
