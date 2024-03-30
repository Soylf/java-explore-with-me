package ru.practicum.stat.server;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    void addEndpointHit(EndpointHitDto endpointHitDto);

    List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
