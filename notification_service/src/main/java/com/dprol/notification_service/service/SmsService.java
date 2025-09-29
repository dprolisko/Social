package com.dprol.notification_service.service;

import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;
import com.dprol.notification_service.exception.SmsSendingException;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Value("${vonage.from.name}")
    private String username;

    @Override
    public void sendNotification(UserDto user, String message) {
        TextMessage textMessage = new TextMessage(username, user.getPhone(), message);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {

        }
        else {
            String error = response.getMessages().get(0).getErrorText();
            throw new SmsSendingException("SMS SEND ERROR: " + error);
        }
    }

    @Override
    public Contact getContacts() {
        return Contact.PHONE;
    }
}
