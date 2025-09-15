package com.dprol.post_service.service.spelling;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SpellingService {

    CompletableFuture<Optional<String>> getSpelling(String word);
}
