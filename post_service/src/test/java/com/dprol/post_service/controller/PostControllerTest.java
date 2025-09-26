package com.dprol.post_service.controller;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        PostDto dto = new PostDto();
        dto.setId(1L);
        dto.setContent("Hello");

        Mockito.when(postService.createPost(any(PostDto.class))).thenReturn(dto);

        mockMvc.perform(post("/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void updatePost_ShouldReturnUpdatedPost() throws Exception {
        PostDto dto = new PostDto();
        dto.setId(1L);
        dto.setContent("Updated");

        Mockito.when(postService.updatePost(eq(1L))).thenReturn(dto);

        mockMvc.perform(put("/post/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated"));
    }

    @Test
    void deletePost_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/post/1"))
                .andExpect(status().isOk());

        Mockito.verify(postService).deletePost(1L);
    }

    @Test
    void findPostById_ShouldReturnPost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test");

        Mockito.when(postService.findPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/post/find/{postId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test"));
    }

    @Test
    void findAllPostsByUserId_ShouldReturnList() throws Exception {
        PostDto dto = new PostDto();
        dto.setId(1L);
        dto.setContent("User post");

        Mockito.when(postService.getListPostsByUserId(10L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/post/findAll/{userId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("User post"));
    }
}
