package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stat.model.EndpointHit;

public interface EndpointHitRepository  extends JpaRepository<EndpointHit,Long> {
}
