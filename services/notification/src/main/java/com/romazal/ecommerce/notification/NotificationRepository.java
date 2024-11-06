package com.romazal.ecommerce.notification;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Arrays;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByDestinationEmail(String destinationEmail);
}
