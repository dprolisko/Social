package com.dprol.post_service.controller;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.service.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void createComment_ShouldReturnDto() throws Exception {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setContent("Hello");

        Mockito.when(commentService.createComment(any())).thenReturn(dto);

        mockMvc.perform(post("/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void updateComment_ShouldReturnDto() throws Exception {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setContent("Updated");

        Mockito.when(commentService.updateComment(eq(1L))).thenReturn(dto);

        mockMvc.perform(put("/comment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated"));
    }

    @Test
    void deleteComment_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/comment/delete/{commentId}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(commentService).deleteComment(1L);
    }

    @Test
    void getCommentsByUserId_ShouldReturnList() throws Exception {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setContent("User comment");

        Mockito.when(commentService.getListCommentByUserId(10L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/comment/getComments/{userId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("User comment"));
    }
}
