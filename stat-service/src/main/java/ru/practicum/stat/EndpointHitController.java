package ru.practicum.stat;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.server.EndpointHitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EndpointHitController {
    private EndpointHitService service;

    @PostMapping("/hit")
    public void addHit(@RequestBody EndpointHitDto endpointHitDto) {
        service.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getHits(Stat stat) {
        return service.getStats(stat);
    }
}
