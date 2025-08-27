package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    void deleteHashtagAndPostId(String hashtag, Long postId);

    void findHashtagByName(String hashtagName);

    Page<Hashtag> findHashtag(String hashtag, Pageable pageable);
}
