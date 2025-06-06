package com.dprol.social.service.skill;

import com.dprol.social.dto.SkillDto;
import com.dprol.social.entity.skill.Skill;
import com.dprol.social.mapper.SkillMapper;
import com.dprol.social.repository.skill.SkillRepository;
import com.dprol.social.validator.skill.SkillValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    private final SkillValidator skillValidator;

    @Override
    public SkillDto createSkill(SkillDto skillDto) {
        skillValidator.validateSkill(skillDto);
        return skillMapper.toDto(skillRepository.save(skillMapper.toEntity(skillDto)));
    }

    @Override
    public void deleteSkill(Long skillId) {
        skillValidator.validateSkillById(skillId);
        skillRepository.deleteById(skillId);
    }

    @Override
    public List<SkillDto> getAllSkills(Long userId) {
        skillValidator.validateSkillById(userId);
        List<Skill> skills = skillRepository.findAllByUserId(userId);
        return skills.stream().map(skillMapper::toDto).collect(Collectors.toList());
    }
}
