package com.romazal.ecommerce.notification;

import java.time.LocalDateTime;

public record NotificationResponse(
        String id,
        String destinationEmail,
        String message,
        NotificationType type,
        LocalDateTime notificationDate
) {
}
