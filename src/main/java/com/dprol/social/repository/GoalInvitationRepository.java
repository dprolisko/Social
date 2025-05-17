package com.dprol.social.repository;

import com.dprol.social.entity.GoalInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalInvitationRepository extends JpaRepository<GoalInvitation, Long> {
}
