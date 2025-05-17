package com.dprol.social.repository;

import com.dprol.social.entity.Jira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JiraRepository extends JpaRepository<Jira, Long> {

    Optional<Jira> findByJiraId(Long id);
}
