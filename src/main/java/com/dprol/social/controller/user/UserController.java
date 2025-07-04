package com.dprol.social.controller.user;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.service.user.UserService;
import com.dprol.social.service.user.avatar.AvatarService;
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

    private final AvatarService avatarService;

    @PostMapping("{userId}")
    public UserDto createUser(@PathVariable UserDto userDto) {
        avatarService.generatedPicture(userDto);
        return userService.createUser(userDto);
    }

    @PutMapping("{userId}")
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
