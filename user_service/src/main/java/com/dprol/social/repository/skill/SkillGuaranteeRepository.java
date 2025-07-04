package com.dprol.social.repository.skill;

import com.dprol.social.entity.skill.SkillGuarantee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillGuaranteeRepository extends CrudRepository<SkillGuarantee, Long> {
    @Query(nativeQuery = true, value = "insert into SkillGuarantee(skill_id, user_id, userguarantee_id) values(:skillId, :userId,:guarantorId)")
    void assignSkillGuarantee(Long skillId, Long userId, Long guarantorId);
}
