package com.dprol.social.repository;

import com.dprol.social.entity.Premium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumRepository extends CrudRepository<Premium, Long> {

    Optional<Premium> findByUserId(Long Id);

    List<Premium> findAllByEndDateTime(LocalDateTime endDateTime);

    @Query(value = """
            SELECT p.id FROM Premium p
            WHERE p.end_date_time < CURRENT_TIMESTAMP
            """)
    List<Long> findAllExpiredId();
}
