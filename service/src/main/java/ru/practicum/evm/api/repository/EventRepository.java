package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.Event;

public interface EventRepository extends JpaRepository<Event,Long> {
}
