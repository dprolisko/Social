package com.dprol.social.controller;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.service.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @GetMapping("{userId}")
    public UserDto createUser(@PathVariable UserDto userDto){
        return userService.createUser(userDto);
    }

    @PostMapping("{userId}")
    public UserDto updateUser(@PathVariable UserDto userDto){
        return userService.updateUser(userDto);
    }

    @DeleteMapping("{userId}")
    public UserDto deleteUser(@PathVariable @Positive @Parameter Long id){
        return userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDto> getUserById(@NotNull @RequestBody List <Long> listId){
        return userService.getUserById(listId);
    }

    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
