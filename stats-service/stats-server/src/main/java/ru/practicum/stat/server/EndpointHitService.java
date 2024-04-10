package ru.practicum.stat.server;

import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    void addEndpointHit(EndpointHitDto endpointHitDto);

    List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
