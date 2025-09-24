package com.dprol.post_service.service;


import com.dprol.post_service.dto.SpellingCorrectorDto;
import com.dprol.post_service.service.spelling.SpellingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpellingServiceImplTest {

    @InjectMocks
    private SpellingServiceImpl spellingService;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    private String json;
    private String string;

    @BeforeEach
    public void init(){
        SpellingCorrectorDto firstSpell = SpellingCorrectorDto.builder()
                .word("синхрафазатрон")
                .variantWords(new String[]{"синхрофазотрон", "синхрофазотрона"})
                .build();
        SpellingCorrectorDto secondSpell = SpellingCorrectorDto.builder()
                .word("Дубна")
                .variantWords(new String[]{"Дубне"})
                .build();
        try {
            json = objectMapper.writeValueAsString(new SpellingCorrectorDto[]{firstSpell, secondSpell});
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        string = "синхрафазатрон в Дубна";
    }

    @Test
    public void testCheckSpellingWithCorrecting() throws Exception{
        byte[] byteJson = json.getBytes(StandardCharsets.UTF_8);
        when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(byteJson);

        CompletableFuture<Optional<String>> result = spellingService.getSpelling(string);

        InOrder inOrder = inOrder(restTemplate, objectMapper);
        inOrder.verify(restTemplate, times(1)).getForObject(anyString(), eq(byte[].class));
        inOrder.verify(objectMapper, times(1)).readValue(any(byte[].class), eq(SpellingCorrectorDto[].class));
        assertFalse(result.get().isEmpty());
        assertEquals(result.get().get(),"синхрофазотрон в Дубне" );
    }

    @Test
    public void testCheckSpellingWithEmptyResult() throws Exception{
        json = objectMapper.writeValueAsString(new SpellingCorrectorDto[]{});
        byte[] byteJson = json.getBytes(StandardCharsets.UTF_8);
        when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(byteJson);

        CompletableFuture<Optional<String>> result = spellingService.getSpelling(string);
        assertFalse(result.get().isEmpty());
    }

    @Test
    public void testCheckSpellingWithIOException(){
        byte[] byteJson = "".getBytes(StandardCharsets.UTF_8);
        when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(byteJson);

        var exception = assertThrows(RuntimeException.class, ()-> spellingService.getSpelling(string));
        assertEquals(exception.getMessage(), "Error converting JSON to POJO");
    }
}
