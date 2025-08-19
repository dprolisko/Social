package com.dprol.post_service.config;

import com.dprol.post_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user_service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    UserDto getUserById(Long userId);

    @GetMapping("/api/users")
    List<UserDto> getAllUsers();
}
