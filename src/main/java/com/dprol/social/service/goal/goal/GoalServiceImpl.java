package com.dprol.social.service.goal.goal;

import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.GoalNotFoundException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.goal.GoalMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.repository.goal.GoalRepository;
import com.dprol.social.service.goal.filter.GoalFilterService;
import com.dprol.social.validator.goal.GoalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    private final GoalMapper goalMapper;

    private final GoalValidator goalValidator;

    private final GoalFilterService goalFilterService;

    private final UserRepository userRepository;

    @Override
    public GoalDto createGoal(GoalDto goalDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        goalValidator.validateGoalByAmountUsers(user);
        Goal goal = goalMapper.toEntity(goalDto);
        goalRepository.save(goal);
        return goalMapper.toDto(goal);
    }

    @Override
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    @Override
    public List<GoalDto> getListGoals(Long userId, GoalFilterDto goalFilterDto) {
        goalValidator.validateGoalById(userId);
        Stream<Goal> filterGoal = goalRepository.findGoalsStream(userId);
        return goalFilterService.filterGoals(filterGoal, goalFilterDto).map(goalMapper::toDto).toList();
    }

    @Override
    public Goal findGoalById(Long goalId) {
        return goalRepository.findGoalById(goalId).orElseThrow(() -> new GoalNotFoundException(String.format("Goal with id %s not found", goalId)));
    }

    @Override
    public GoalDto updateGoal(GoalDto goalDto, Long userId, Long goalId) {
        Goal goalValid = findGoalById(goalId);
        goalValidator.validateGoalNotCompleted(goalValid);
        Goal goal = goalMapper.toEntity(goalDto);
        goal = goalRepository.save(goal);
        return goalMapper.toDto(goal);
    }
}
