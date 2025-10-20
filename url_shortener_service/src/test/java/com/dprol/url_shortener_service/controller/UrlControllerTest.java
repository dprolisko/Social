package com.dprol.url_shortener_service.controller;

import com.dprol.url_shortener_service.dto.HashDto;
import com.dprol.url_shortener_service.dto.UrlDto;
import com.dprol.url_shortener_service.service.UrlService;
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
import org.springframework.web.servlet.view.RedirectView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UrlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
    }

    @Test
    void getRedirectView_ShouldReturnRedirect() throws Exception {
        RedirectView redirectView = new RedirectView("https://example.com");
        Mockito.when(urlService.getRedirectView(eq("abc123"))).thenReturn(redirectView);

        mockMvc.perform(get("/url/get").param("hash", "abc123"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://example.com"));
    }

    @Test
    void createUrl_ShouldReturnHashDto() throws Exception {
        HashDto hashDto = new HashDto("abc123");
        Mockito.when(urlService.createUrl(any(UrlDto.class))).thenReturn(hashDto);

        String requestBody = """
                {
                  "url": "https://example.com"
                }
                """;

        mockMvc.perform(post("/url/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hash").value("abc123"));
    }
}
