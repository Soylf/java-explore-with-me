package ru.practicum.evm.api.publics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.api.publics.service.compilation.CompilationPublicService;
import ru.practicum.evm.dto.compilation.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public List<CompilationDto> getComplications(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("CompilationPublicController: запрос на получение списка из Complications");
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable(name = "compId") @Positive Long compilationId) {
        log.info("CompilationPublicController: запрос на получение Complications");
        return service.getCompilation(compilationId);
    }
}
