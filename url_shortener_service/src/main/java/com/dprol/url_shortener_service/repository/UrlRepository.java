package com.dprol.url_shortener_service.repository;

import com.dprol.url_shortener_service.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> getUrlByHash(String hash);

    List<Url> findOldUrls();
}
