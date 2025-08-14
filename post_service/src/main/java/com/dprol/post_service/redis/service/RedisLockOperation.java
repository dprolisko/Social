package com.dprol.post_service.redis.service;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.Optional;

public interface RedisLockOperation {

    <R extends KeyValueRepository<E, ID>, E, ID> Optional<E> findById(R repository, ID id);

    <R extends KeyValueRepository<E, ID>, E, ID> void deleteById(R repository, ID id);

    <R extends KeyValueRepository<E, ID>, E, ID> E updateOrSave(R repository, E entity, ID id);
}
