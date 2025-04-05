package com.dprol.social.repository;

import com.dprol.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    boolean isEmailUnique(String email);

    boolean  isUsernameUnique(String username);

    boolean isPhoneUnique(String phone);

    Stream<User> findPremiumUsers();
}
