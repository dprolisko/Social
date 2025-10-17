package com.dprol.url_shortener_service.repository;

import com.dprol.url_shortener_service.entity.Hash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface HashRepository extends JpaRepository<Hash, Long> {

    List<Long> findUniqueNumbers(int number);

    List<String> getHash(int number);
}
