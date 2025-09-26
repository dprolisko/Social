package com.dprol.post_service.controller;

import com.dprol.post_service.dto.like.CommentLikeDto;
import com.dprol.post_service.service.like.CommentLikeService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentLikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentLikeService commentLikeService;

    @InjectMocks
    private CommentLikeController commentLikeController;

    @BeforeEach
    void setUp(){mockMvc = MockMvcBuilders.standaloneSetup(commentLikeController).build();}

    @Test
    void addCommentLike_ShouldReturnDto() throws Exception {
        CommentLikeDto dto = new CommentLikeDto();
        dto.setId(1L);
        dto.setUserId(100L);

        Mockito.when(commentLikeService.addCommentLike(1L, 100L)).thenReturn(dto);

        mockMvc.perform(post("/commentLike/add/{commentLikeId}", 1L)
                        .param("commentLikeId", "1")
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(100L));
    }

    @Test
    void deleteCommentLike_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/commentLike/delete/{commentLikeId}", 1L)
                        .param("commentLikeId", "1")
                        .param("userId", "100"))
                .andExpect(status().isOk());

        Mockito.verify(commentLikeService).deleteCommentLike(1L, 100L);
    }
}
