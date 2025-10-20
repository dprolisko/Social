package com.dprol.url_shortener_service.service;

import com.dprol.url_shortener_service.entity.Hash;
import com.dprol.url_shortener_service.repository.HashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor

public class HashServiceImpl implements HashService {

    private final HashRepository hashRepository;

    private final Base62Encoder base62Encoder;

    @Value("${services.hash.batch}")
    private int batchSize;

    @Override
    public List<String> getHashes(int count) {
        try{
            return hashRepository.getHash(count);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async("generateBatchExecutor")
    public CompletableFuture<List<String>> getHashesAsync(int count) {
        return CompletableFuture.supplyAsync(()->{
            try {
                return hashRepository.getHash(count);
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async("generateHashExecutor")
    public CompletableFuture<Void> generateHashes() {
        List<Hash> generateHashes = base62Encoder.encodeSymbolsToHash(hashRepository.findUniqueNumbers(batchSize)).stream().map(Hash::new).toList();
        hashRepository.saveAll(generateHashes);
        return CompletableFuture.completedFuture(null);
    }
}
