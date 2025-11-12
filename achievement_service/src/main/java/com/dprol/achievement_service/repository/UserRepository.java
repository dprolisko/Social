package com.dprol.achievement_service.repository;

import com.dprol.achievement_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT CASE WHEN COUNT(ua) > 0 THEN true ELSE false END
            FROM User ua
            WHERE ua.userId = :userId AND ua.achievement.id = :achievementId
    """)
    boolean existsByUserId(Long userId, Long achievementId);

    List<User> findByUserId(Long userId);
}
