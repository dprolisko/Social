package com.dprol.url_shortener_service.repository;

import com.dprol.url_shortener_service.entity.Hash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface HashRepository extends JpaRepository<Hash, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM hash")
    List<Long> findUniqueNumbers(int number);

    @Query(nativeQuery = true, value = "DELETE FROM hash WHERE hash IN (SELECT hash FROM hash ORDER BY random() LIMIT :number) RETURNING hash")
    List<String> getHash(int number);
}
