package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.stat.dto.StatDto(h.app, h.uri, count(h.ip)) " +
            "from ItemStats h " +
            "where h.timestamp between :start and :end " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.stat.dto.StatDto(h.app, h.uri, count(h.ip)) " +
            "from ItemStats h " +
            "where h.timestamp between :start and :end " +
            "and h.uri in (:uris) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.stat.dto.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from ItemStats h " +
            "where h.timestamp between :start and :end " +
            "and h.uri in (:uris) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatDto> findAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}