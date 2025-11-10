package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    void deleteByHashtagAndPostId(String hashtag, long postId);

    @Query("SELECT h FROM Hashtag h WHERE h.hashtag = : hashtagName")
    void findHashtagByName(String hashtagName);

    @Query("""
            SELECT h FROM Hashtag h
            JOIN h.post p
            WHERE h.hashtag = :hashtag
            ORDER BY SIZE(p.likes) DESC
            """)
    Page<Hashtag> findHashtag(String hashtag, Pageable pageable);
}
