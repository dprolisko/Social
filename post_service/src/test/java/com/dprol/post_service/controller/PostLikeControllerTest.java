package com.dprol.post_service.controller;

import com.dprol.post_service.dto.like.PostLikeDto;
import com.dprol.post_service.service.like.PostLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PostLikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostLikeService postLikeService;

    @InjectMocks
    private  PostLikeController postLikeController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(postLikeController).build();
    }

    @Test
    void addPostLike_ShouldReturnDto() throws Exception {
        PostLikeDto dto = new PostLikeDto();
        dto.setId(1L);
        dto.setUserId(100L);

        Mockito.when(postLikeService.addPostLike(1L, 100L)).thenReturn(dto);

        mockMvc.perform(post("/postLike/add/{postLikeId}", 1L)
                        .param("postLikeId", "1")
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(100L));
    }

    @Test
    void deletePostLike_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/postLike/delete/{postLikeId}", 1L)
                        .param("postLikeId", "1")
                        .param("userId", "100"))
                .andExpect(status().isOk());

        Mockito.verify(postLikeService).deletePostLike(1L, 100L);
    }
}
