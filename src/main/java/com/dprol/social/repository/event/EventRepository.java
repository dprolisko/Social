package com.dprol.social.repository.event;

import com.dprol.social.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long id);

    Stream<Event> findAllByUserId(Long userId);

    List<Event> findEventByCompletedOrCancelled();

    List<Event> findParticipatedEvents(Long userId);
}
