package ru.practicum.stat.server;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.model.Stat;

import java.util.List;

public interface EndpointHitService {

    void addEndpointHit(EndpointHitDto endpointHitDto);

    List<StatDto> getStats(Stat stat);
}
