package com.dprol.social.repository.goal;

import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {

    Optional<Goal> findGoalById(long id);

    @Query(nativeQuery = true, value = """
            SELECT 
                g.*
            FROM goal g
            JOIN user_goal ug ON g.id = ug.goal_id
            WHERE ug.user_id = ?1
            """)
    Stream<Goal> findGoalsStream(long id);
}
