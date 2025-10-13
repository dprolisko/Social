package com.dprol.achievement_service.controller;

import com.dprol.achievement_service.dto.UserDto;
import com.dprol.achievement_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/achievement")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/get/{userId}")
    public List<UserDto> getAchievementsByUserId(@PathVariable Long userId) {
        return userService.getAchievementsByUserId(userId);
    }
}
