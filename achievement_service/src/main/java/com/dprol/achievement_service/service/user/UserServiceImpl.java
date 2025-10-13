package com.dprol.achievement_service.service.user;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.dto.UserDto;
import com.dprol.achievement_service.entity.Achievement;
import com.dprol.achievement_service.entity.User;
import com.dprol.achievement_service.exception.NotFoundException;
import com.dprol.achievement_service.mapper.AchievementMapper;
import com.dprol.achievement_service.mapper.UserMapper;
import com.dprol.achievement_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AchievementMapper achievementMapper;

    @Override
    public void giveAchievement(Long userId, AchievementDto achievementDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Achievement achievement = achievementMapper.toEntity(achievementDto);
        user.builder().userId(userId).achievement(achievement).build();
        userRepository.save(user);
    }

    @Override
    public boolean hasAchievement(Long userId, Long achievementId) {
        return userRepository.existsByUserId(userId, achievementId);
    }

    @Override
    public List<UserDto> getAchievementsByUserId(Long userId) {
        return userRepository.findByUserId(userId).stream().map(userMapper::toDto).collect(Collectors.toList());
    }
}
