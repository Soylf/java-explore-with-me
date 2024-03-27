package ru.practicum.stat.mapper;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.model.EndpointHit;

import java.util.List;
import java.util.stream.Collectors;

public class HitMapper {

    public EndpointHitDto fromEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public EndpointHit fromEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(null)
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public List<String> StringToUris(List<EndpointHit> endpointHits) {
        return endpointHits.stream()
                .map(this::toUri)
                .collect(Collectors.toList());
    }

    private String toUri(EndpointHit endpointHit) {
        return endpointHit.getUri();
    }
}
