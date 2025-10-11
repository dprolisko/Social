package com.dprol.achievement_service.repository;

import com.dprol.achievement_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(Long userId, Long achievementId);

    List<User> findByUserId(Long userId);
}
