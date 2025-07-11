package com.dprol.social.controller;

import com.dprol.social.controller.subscription.SubscriptionController;
import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.service.subscription.SubscriptionService;
import com.github.dockerjava.api.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    private final SubscriptionDto subscriptionDto = new SubscriptionDto(1L, 2L);
    private UserDto userDto1;
    private UserDto userDto2;

    // Follow user tests
    @Test
    void followUser_ShouldReturnCreated() throws Exception {
        doNothing().when(subscriptionService).followUser(any(SubscriptionDto.class));

        mockMvc.perform(post("/subscription/following")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDto)))
                .andExpect(status().isCreated());

        verify(subscriptionService).followUser(any(SubscriptionDto.class));
    }

    @Test
    void followUser_InvalidInput_ShouldReturnBadRequest() throws Exception {
        SubscriptionDto invalidDto = new SubscriptionDto(null, null);

        mockMvc.perform(post("/subscription/following")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // Unfollow user tests
    @Test
    void unfollowUser_ShouldReturnOk() throws Exception {
        doNothing().when(subscriptionService).unfollowUser(any(SubscriptionDto.class));

        mockMvc.perform(delete("/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDto)))
                .andExpect(status().isOk());

        verify(subscriptionService).unfollowUser(any(SubscriptionDto.class));
    }

    // Get followers tests
    @Test
    void getFollowers_ShouldReturnList() throws Exception {
        List<UserDto> followers = Arrays.asList(userDto1, userDto2);
        when(subscriptionService.getFollowers(anyLong(), any(UserFilterDto.class)))
                .thenReturn(followers);

        mockMvc.perform(get("/subscription/followers")
                        .param("followeeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getFollowers_MissingFolloweeId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/subscription/followers"))
                .andExpect(status().isBadRequest());
    }

    // Get followings tests
    @Test
    void getFollowings_ShouldReturnList() throws Exception {
        List<UserDto> followings = Arrays.asList(userDto1);
        when(subscriptionService.getFollowings(anyLong(), any(UserFilterDto.class)))
                .thenReturn(followings);

        mockMvc.perform(get("/subscription/followings")
                        .param("followerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    // Get followers count tests
    @Test
    void getFollowersCount_ShouldReturnCount() throws Exception {
        when(subscriptionService.getFollowersCount(anyLong())).thenReturn(10);

        mockMvc.perform(get("/subscription/followers/count")
                        .param("followeeId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    // Get followings count tests
    @Test
    void getFollowingsCount_ShouldReturnCount() throws Exception {
        when(subscriptionService.getFollowingsCount(anyLong())).thenReturn(5);

        mockMvc.perform(get("/subscription/followings/count")
                        .param("followerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    // Conflict scenario test
    @Test
    void followUser_AlreadyFollowing_ShouldReturnConflict() throws Exception {
        doThrow(new ConflictException("Already following"))
                .when(subscriptionService).followUser(any(SubscriptionDto.class));

        mockMvc.perform(post("/subscription/following")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDto)))
                .andExpect(status().isConflict());
    }

    // Not found scenario test
    @Test
    void getFollowersCount_UserNotFound_ShouldReturnNotFound() throws Exception {
        when(subscriptionService.getFollowersCount(anyLong()))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/subscription/followers/count")
                        .param("followeeId", "999"))
                .andExpect(status().isNotFound());
    }
}
