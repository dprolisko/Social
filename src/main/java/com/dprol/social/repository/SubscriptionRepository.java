package com.dprol.social.repository;

import com.dprol.social.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface SubscriptionRepository extends JpaRepository<User, Long> {

    void followUser(Long followerId, Long followeeId);

    void unfollowUser(Long unfollowerId, Long unfolloweeId);

    Stream<User> findByFollowersId(Long followerId);

    Stream<User> findByFolloweesId(Long followeeId);

    int findByFollowerId (Long followerId);

    int findByFolloweeId (Long followeeId);

    boolean existsByFollowerId (Long followerId, Long followeeId);
}