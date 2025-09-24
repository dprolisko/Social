package com.dprol.post_service.service.spelling;

import com.dprol.post_service.dto.SpellingCorrectorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor

public class SpellingServiceImpl implements SpellingService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Value("${spelling.url}")
    public String url;

    @Override
    @Async("spellingExecutor")
    public CompletableFuture<Optional<String>> getSpelling(String word) {
        SpellingCorrectorDto[] correctors;
        byte[] correctorBytes = restTemplate.getForObject(url+word, byte[].class);
        try {
            correctors = objectMapper.readValue(correctorBytes, SpellingCorrectorDto[].class);
        }
        catch (IOException e){
            throw new RuntimeException("Error converting JSON to POJO", e);
        }
        return CompletableFuture.completedFuture(Optional.of(correct(word, correctors)));
    }

    private String correct(String word, SpellingCorrectorDto[] correctors) {
        AtomicReference<String> corrector = new AtomicReference<>(word);
        Arrays.stream(correctors).forEach(e -> corrector.set(corrector.get().replaceFirst(e.getWord(), e.getVariantWords()[0])));
        return corrector.get();
    }
}
