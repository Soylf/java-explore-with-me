package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stat.model.EndpointHit;
import ru.practicum.statsdto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
        @Query("select new ru.practicum.statsdto.StatDto(e.app, e.uri, count(e.ip)) " +
                "from EndpointHit e " +
                "where e.timestamp between :start and :end " +
                "group by e.app, e.uri " +
                "order by count(e.ip) desc")
        List<StatDto> findAllWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

        @Query("select new ru.practicum.statsdto.StatDto(e.app, e.uri, count(e.ip)) " +
                "from EndpointHit e " +
                "where e.timestamp between :start and :end " +
                "and e.uri in (:uris) " +
                "group by e.app, e.uri " +
                "order by count(e.ip) desc")
        List<StatDto> findAllNotUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

        @Query("select new ru.practicum.statsdto.StatDto(e.app, e.uri, count(e.ip)) " +
                "from EndpointHit e " +
                "where e.timestamp between :start and :end " +
                "and e.uri in (:uris) " +
                "group by e.app, e.uri " +
                "order by count(distinct e.ip) desc")
        List<StatDto> findAllUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}