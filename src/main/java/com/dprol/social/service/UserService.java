package com.dprol.social.service;

import com.dprol.social.dto.UserDto;
import com.dprol.social.entity.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto deleteUser(Long id);

    List<UserDto> getUserById(List <Long> listId);

    UserDto getUserById(Long id);

    User findUserById(Long id);
}
