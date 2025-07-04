package com.dprol.social.repository.skill;

import com.dprol.social.entity.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsBySkillName(String skillName);

    Skill getSkillById(long id);

    Optional<Skill> findByIdSkill(Long skillId, Long userId);

    List<Skill> findAllByUserId(Long userId);

    @Query(nativeQuery = true, value = "insert into Skill(skill_id, user_id) values(:skillId, :userId)")
    void assignSkill(Long skillId, Long userId);
}
