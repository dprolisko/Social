package com.dprol.post_service.service.user;

import com.dprol.post_service.config.UserServiceClient;
import com.dprol.post_service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDto getUserById(Long userId) {
        return userServiceClient.getUserById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userServiceClient.getAllUsers();
    }
}
