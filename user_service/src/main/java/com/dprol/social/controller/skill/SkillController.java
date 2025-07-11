package com.dprol.social.controller.skill;

import com.dprol.social.dto.SkillDto;
import com.dprol.social.service.skill.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("Skill")

public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public SkillDto createSkill(SkillDto skillDto){
        return skillService.createSkill(skillDto);
    }

    @GetMapping
    public List<SkillDto> getAllSkills(Long userId){
        return skillService.getAllSkills(userId);
    }
}
