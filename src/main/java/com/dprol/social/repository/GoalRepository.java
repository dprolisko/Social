package com.dprol.social.repository;

import com.dprol.social.entity.Goal;
import com.dprol.social.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {

    Optional<Goal> findGoalById(long id);

    Goal createGoal(String title, String description);

    List<User> findUsersByGoalId(long goalId);

    int userInvitedInGoalId(long id);
}
