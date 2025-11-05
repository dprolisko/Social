package com.dprol.social.repository.goal;

import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface GoalInvitationRepository extends JpaRepository<GoalInvitation, Long> {

    @Query(nativeQuery = true, value = """
            SELECT 
                g.*
            FROM goal_invitation g
            JOIN user_goal ug ON g.id = ug.goal_id
            WHERE ug.user_id = ?1
            """)
    Stream<GoalInvitation> findByGoalInvitationStream(Long goalInvitationId);
}
