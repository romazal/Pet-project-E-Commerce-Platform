package com.romazal.ecommerce.notification;

import com.romazal.ecommerce.exception.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public List<NotificationResponse> getAllNotifications() {
        return repository.findAll()
                .stream()
                .map(mapper::toNotificationResponse)
                .toList();
    }

    public NotificationResponse getNotificationById(String notificationId) {
        return repository.findById(notificationId).map(mapper::toNotificationResponse)
                .orElseThrow(() -> new NotificationNotFoundException(
                        String.format("No notification with such ID exists:: %s", notificationId)
                ));
    }

    public List<NotificationResponse> getAllNotificationsByEmail(String email) {
        return repository.findAllByDestinationEmail(email)
                .stream()
                .map(mapper::toNotificationResponse)
                .toList();
    }
}
