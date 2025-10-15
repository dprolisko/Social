package com.dprol.achievement_service.client;


import com.dprol.achievement_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;



@FeignClient(name = "user-service", url = "${user-service.host}:${user-service.port}")
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    UserDto getUserById(Long userId);
}
