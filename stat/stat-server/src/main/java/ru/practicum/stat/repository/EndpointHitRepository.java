package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    List<StatDto> findStatDtoByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<StatDto> findStatDtoByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<StatDto> findStatDtoByTimestampBetweenAndUriInAndIpIsNotNull(LocalDateTime start, LocalDateTime end, List<String> uris);
}