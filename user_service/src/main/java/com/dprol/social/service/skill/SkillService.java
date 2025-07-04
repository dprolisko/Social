package com.dprol.social.service.skill;

import com.dprol.social.dto.SkillDto;

import java.util.List;

public interface SkillService {

    SkillDto createSkill(SkillDto skillDto);

    void deleteSkill(Long skillId);

    List<SkillDto> getAllSkills(Long userId);
}
