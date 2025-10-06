package com.dprol.notification_service.client;

import com.dprol.notification_service.dto.ContactDto;
import com.dprol.notification_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;



@FeignClient(name = "user_service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    UserDto getUserById(Long userId);

    @GetMapping("/api/contact/{username}")
    ContactDto getContactByUsername(String username);
}
