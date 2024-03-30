package ru.practicum.stat.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    public EndpointHit fromEndpointHit(EndpointHitDto endpointHitDto) {
        LocalDateTime dateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .parse(endpointHitDto.getTimestamp()));

        return EndpointHit.builder()
                .id(null)
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(dateTime)
                .build();
    }
}
