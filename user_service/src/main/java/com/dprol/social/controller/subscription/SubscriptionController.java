package com.dprol.social.controller.subscription;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.service.subscription.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("subscription")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("following")
    @Operation(summary = "follow to user")
    @ResponseStatus(HttpStatus.CREATED)
    public void followUser(@Valid @RequestBody SubscriptionDto subscriptionDto) {
        subscriptionService.followUser(subscriptionDto);
    }

    @DeleteMapping
    public void unfollowUser(SubscriptionDto subscriptionDto){
        subscriptionService.unfollowUser(subscriptionDto);
    }

    @GetMapping
    public List<UserDto> getFollowers(Long followeeId, UserFilterDto userFilterDto){
        return subscriptionService.getFollowers(followeeId, userFilterDto);
    }

    @GetMapping
    public List<UserDto> getFollowings(Long followerId, UserFilterDto userFilterDto){
        return subscriptionService.getFollowings(followerId, userFilterDto);
    }

    @GetMapping
    public int getFollowersCount(Long followeeId){
        return subscriptionService.getFollowersCount(followeeId);
    }

    @GetMapping
    public int getFollowingsCount(Long followerId){
        return subscriptionService.getFollowingsCount(followerId);
    }
}
