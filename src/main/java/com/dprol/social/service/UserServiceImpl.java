package com.dprol.social.service;

import com.dprol.social.dto.UserDto;
import com.dprol.social.entity.User;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.UserMapper;
import com.dprol.social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUserById(List<Long> listId) {
        return userRepository.findAllById(listId).stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toDto(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
    }
}
