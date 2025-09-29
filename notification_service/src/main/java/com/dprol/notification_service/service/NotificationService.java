package com.dprol.notification_service.service;

import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;

public interface NotificationService {

    void sendNotification(UserDto user, String message);

    Contact getContacts();
}
