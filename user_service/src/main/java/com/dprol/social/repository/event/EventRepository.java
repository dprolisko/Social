package com.dprol.social.repository.event;

import com.dprol.social.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findEventById(Long id);

    @Query("SELECT e.id FROM Event e WHERE e.eventStatus = 'COMPLETED' OR e.eventStatus = 'CANCELED'")
    List<Long> findEventByCompletedOrCancelled();

    @Query(nativeQuery = true, value = """
SELECT e.* FROM event e
            JOIN user_event ue ON ue.event_id = e.id
            WHERE ue.user_id = :userId """)
    List<Event> findParticipatedEvents(Long userId);
}
