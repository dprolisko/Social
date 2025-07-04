package com.dprol.social.validator.skill;

import com.dprol.social.dto.SkillDto;

public interface SkillValidator {

    void validateSkill(SkillDto skillDto);

    void validateSkillById(Long skillId);
}
