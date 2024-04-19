package ru.practicum.statsserver.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsdto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    public EndpointHit fromEndpointHit(EndpointHitDto endpointHitDto) {
        LocalDateTime dateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .parse(endpointHitDto.getTimestamp()));

        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(dateTime)
                .build();
    }
}
