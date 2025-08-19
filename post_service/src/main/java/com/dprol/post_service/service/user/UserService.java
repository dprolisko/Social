package com.dprol.post_service.service.user;

import com.dprol.post_service.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();
}
