package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    Resources findByKey(String key);

    boolean existsByKey(String key);

    void deleteByKey(String key);
}
