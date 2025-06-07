package com.dprol.social.repository.goal;

import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {

    Optional<Goal> findGoalById(long id);

    Stream<Goal> findGoalsStream(long id);

    Goal createGoal(String title, String description);

    List<User> findUsersByGoalId(long goalId);

    int userInvitedInGoalId(long id);
}
