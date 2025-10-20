package com.dprol.url_shortener_service.service;

import com.dprol.url_shortener_service.entity.Hash;
import com.dprol.url_shortener_service.repository.HashRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HashServiceImplTest {

    @Mock
    private HashRepository hashRepository;
    @Mock
    private Base62Encoder base62Encoder;

    @InjectMocks
    private HashServiceImpl hashService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(hashService, "batchSize", 3);
    }

    @Test
    void getHashes_ShouldReturnList_WhenRepositoryReturns() {
        when(hashRepository.getHash(3)).thenReturn(List.of("a", "b", "c"));

        List<String> result = hashService.getHashes(3);

        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        verify(hashRepository).getHash(3);
    }

    @Test
    void getHashes_ShouldThrowRuntimeException_WhenRepositoryThrows() {
        when(hashRepository.getHash(3)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> hashService.getHashes(3));
    }

    @Test
    void getHashesAsync_ShouldReturnList_WhenRepositoryReturns() throws Exception {
        when(hashRepository.getHash(2)).thenReturn(List.of("x", "y"));

        CompletableFuture<List<String>> future = hashService.getHashesAsync(2);

        List<String> result = future.get();
        assertEquals(2, result.size());
        assertEquals("x", result.get(0));
        verify(hashRepository).getHash(2);
    }

    @Test
    void getHashesAsync_ShouldThrowRuntimeException_WhenRepositoryThrows() {
        when(hashRepository.getHash(2)).thenThrow(new RuntimeException("DB error"));

        CompletableFuture<List<String>> future = hashService.getHashesAsync(2);

        assertThrows(RuntimeException.class, future::join);
    }

    @Test
    void generateHashes_ShouldCallRepositoryAndEncoder() throws Exception {
        List<Long> numbers = List.of(1L, 2L, 3L);
        List<String> encoded = List.of("a", "b", "c");
        List<Hash> hashes = encoded.stream().map(Hash::new).toList();

        when(hashRepository.findUniqueNumbers(3)).thenReturn(numbers);
        when(base62Encoder.encodeSymbolsToHash(numbers)).thenReturn(encoded);

        CompletableFuture<Void> future = hashService.generateHashes();
        future.get(); // дождаться выполнения
    }
}
