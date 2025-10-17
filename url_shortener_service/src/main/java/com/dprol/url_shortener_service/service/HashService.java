package com.dprol.url_shortener_service.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface HashService {

    List<String> getHashes(int count);

    CompletableFuture<List<String>> getHashesAsync(int count);

    CompletableFuture<Void> generateHashes();
}
