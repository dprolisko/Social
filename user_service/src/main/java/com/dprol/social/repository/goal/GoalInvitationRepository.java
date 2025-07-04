package com.dprol.social.repository.goal;

import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface GoalInvitationRepository extends JpaRepository<GoalInvitation, Long> {

    Optional<GoalInvitation> findByGoalInvitationId(Long goalInvitationId);

    Stream<GoalInvitation> findByGoalInvitationStream(Long goalInvitationId);

    GoalInvitation createGoalInvitation(Long goalInvitationId, Long inviterId);

    List<User> findInvitedUsersByGoalInvitationId(Long goalInvitationId);
}
