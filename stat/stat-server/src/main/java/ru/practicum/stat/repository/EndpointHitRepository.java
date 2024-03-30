package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
        @Query("select new ru.practicum.stat.dto.StatDto(e.app, e.uri, count(e.ip)) " +
                "from EndpointHit e " +
                "where e.timestamp between :start and :end " +
                "group by e.app, e.uri " +
                "order by count(e.ip) desc")
        List<StatDto> findAllWithoutUris(LocalDateTime start, LocalDateTime end);

        @Query("select new ru.practicum.stat.dto.StatDto(e.app, e.uri, count(e.ip)) " +
                "from EndpointHit e " +
                "where e.timestamp between :start and :end " +
                "and e.uri in (:uris) " +
                "group by e.app, e.uri " +
                "order by count(e.ip) desc")
        List<StatDto> findAllNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

        @Query("select new ru.practicum.stat.dto.StatDto(e.app, e.uri, count(distinct e.ip)) " +
                "from EndpointHit e " +
                "where e.timestamp between :start and :end " +
                "and e.uri in (:uris) " +
                "group by e.app, e.uri " +
                "order by count(distinct e.ip) desc")
        List<StatDto> findAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
    }