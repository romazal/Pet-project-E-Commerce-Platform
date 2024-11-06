package com.romazal.ecommerce.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {
    public NotificationResponse toNotificationResponse(Notification notification) {
        if (notification == null) return null;

        return new NotificationResponse(
                notification.getId(),
                notification.getDestinationEmail(),
                notification.getMessage(),
                notification.getType(),
                notification.getNotificationDate()
        );
    }
}
