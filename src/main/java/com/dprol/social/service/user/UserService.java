package com.dprol.social.service.user;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.user.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto deleteUser(Long id);

    List<UserDto> getUserById(List <Long> listId);

    UserDto getUserById(Long id);

    User findUserById(Long id);
}
