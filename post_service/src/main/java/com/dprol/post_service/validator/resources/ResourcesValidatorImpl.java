package com.dprol.post_service.validator.resources;

import com.dprol.post_service.exception.ResourceNotFoundException;
import com.dprol.post_service.repository.ResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ResourcesValidatorImpl implements ResourcesValidator {

    private final ResourcesRepository resourcesRepository;

    @Override
    public void validateResourcesByKey(String key) {
    if (resourcesRepository.existsByKey(key)) {
        throw new ResourceNotFoundException("Resource with key " + key + " already exists");}
    }
}
