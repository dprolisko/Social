package com.dprol.url_shortener_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HashCacheTest {

    @Mock
    private HashService hashService;

    @InjectMocks
    private HashCache hashCache;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(hashCache, "cacheSize", 10);
        ReflectionTestUtils.setField(hashCache, "fillingPercent", 20);
    }

    @Test
    void init_ShouldFillCacheSynchronously() {
        when(hashService.generateHashes()).thenReturn(CompletableFuture.completedFuture(null));
        when(hashService.getHashes(anyInt())).thenReturn(List.of("a", "b", "c"));

        hashCache.init();

        String first = hashCache.getHash();
        assertNotNull(first);
        assertTrue(List.of("a", "b", "c").contains(first));

        verify(hashService).generateHashes();
        verify(hashService).getHashes(anyInt());
    }

    @Test
    void init_ShouldThrowIllegalState_WhenFillFails() {
        when(hashService.generateHashes()).thenThrow(new RuntimeException("DB error"));

        assertThrows(IllegalStateException.class, () -> hashCache.init());
    }

    @Test
    void getHash_ShouldTriggerAsyncRefill_WhenBelowThreshold() {
        // подготовка: заполнение очереди одним элементом
        when(hashService.generateHashes()).thenReturn(CompletableFuture.completedFuture(null));
        when(hashService.getHashes(anyInt())).thenReturn(List.of("x", "y", "z"));
        hashCache.init();

        // имитируем асинхронное получение новых хэшей
        when(hashService.getHashesAsync(anyInt())).thenReturn(
                CompletableFuture.completedFuture(List.of("n1", "n2"))
        );

        // опустошаем очередь, чтобы сработал refill
        hashCache.getHash();
        hashCache.getHash();
        hashCache.getHash();

        // вызов getHash должен инициировать refill
        String result = hashCache.getHash();

        // результат может быть null, но refill должен быть вызван
        verify(hashService).getHashesAsync(anyInt());
    }
}
