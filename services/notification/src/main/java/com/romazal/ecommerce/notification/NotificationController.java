package com.romazal.ecommerce.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @GetMapping("/{notification-id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable("notification-id") String notificationId) {
        return ResponseEntity.ok(service.getNotificationById(notificationId));
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<NotificationResponse>> getAllNotificationsByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(service.getAllNotificationsByEmail(email));
    }
}
