package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository  extends JpaRepository<EndpointHit,Long> {
    List<EndpointHit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<EndpointHit> findAllByUniqueIp(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> findAllByUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<EndpointHit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
