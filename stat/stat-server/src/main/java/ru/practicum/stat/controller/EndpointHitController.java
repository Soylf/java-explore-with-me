package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.server.EndpointHitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService service;

    @PostMapping("/hit")
    public void addHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("получен запрос на обработку, с данными: " + endpointHitDto);
        service.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {

        return service.getStats(start, end, uris, unique);
    }
}
