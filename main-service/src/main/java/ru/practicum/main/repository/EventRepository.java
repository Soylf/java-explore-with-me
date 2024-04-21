package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.state.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    boolean existsByInitiatorIdAndId(Long userId, Long eventId);

    Page<Event> findAll(Specification<Event> specification, Pageable pageable);


    Optional<Event> findByIdAndState(Long eventId, EventState published);

    List<Event> findAllByIdIn(Set<Long> events);
}