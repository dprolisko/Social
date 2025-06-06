package com.dprol.social.validator.skill;

import com.dprol.social.dto.SkillDto;
import com.dprol.social.entity.skill.Skill;

public interface SkillValidator {

    void validateSkill(SkillDto skillDto);

    void validateSkillById(Long skillId);
}
