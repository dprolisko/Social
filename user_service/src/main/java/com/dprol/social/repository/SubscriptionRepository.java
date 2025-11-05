package com.dprol.social.repository;

import com.dprol.social.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface SubscriptionRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "insert into subscription (follower_id, followee_id) values (:followerId, :followeeId)")
    @Modifying
    void followUser(Long followerId, Long followeeId);

    @Query(nativeQuery = true, value = "delete from subscription where follower_id = :followerId and followee_id = :followeeId")
    @Modifying
    void unfollowUser(Long followerId, Long followeeId);

    @Query(nativeQuery = true, value = """
            select u.* from users as u
            join subscription as subs on u.id = subs.followee_id
            where subs.follower_id = :followerId
            """)
    Stream<User> findByFollowerId(Long followerId);

    @Query(nativeQuery = true, value = """
            select u.* from users as u
            join subscription as subs on u.id = subs.follower_id
            where subs.followee_id = :followeeId
            """)
    Stream<User> findByFolloweeId(Long followeeId);

    @Query(nativeQuery = true, value = "select count(id) from subscription where follower_id = :followerId")
    int findFollowerId (Long followerId);

    @Query(nativeQuery = true, value = "select count(id) from subscription where followee_id = :followeeId")
    int findFolloweeId (Long followeeId);

    @Query(nativeQuery = true, value = "select exists(select 1 from subscription where follower_id = :followerId and followee_id = :followeeId)")
    boolean existsByFollowerId (Long followerId, Long followeeId);
}