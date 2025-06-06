package com.dprol.social.validator.skill;

import com.dprol.social.dto.SkillDto;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component

public class SkillValidatorImpl implements SkillValidator {
    private final SkillRepository skillRepository;

    @Override
    public void validateSkill(SkillDto skillDto) {
        boolean isExist = skillRepository.existsById(skillDto.getId());
        if (isExist) {
            throw new DataValidationException("Skill with id " + skillDto.getId() + " not exists");
        }
        else if (skillDto.getSkillName() == null) {
            throw new DataValidationException("Skill name doesn't exist");
        }
        else if (skillDto.getSkillName().isBlank()) {
            throw new DataValidationException("Skill name is not blank");
        }
    }

    @Override
    public void validateSkillById(Long skillId) {
        boolean isExist = skillRepository.existsById(skillId);
        if (isExist) {
            throw new DataValidationException("Skill with id " + skillId + " not exists");
        }
    }
}
