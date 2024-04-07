package ru.practicum.evm.api.admins.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.api.admins.service.compilation.CompilationAdminService;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.CompilationNewDto;
import ru.practicum.evm.dto.compilation.CompilationUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationAdminService service;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody @Valid CompilationNewDto request) {
        return service.createCompilation(request);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(name = "compId") @Positive Long compilationId,
                                            @RequestBody @Valid CompilationUpdateRequest request) {
        return service.updateCompilationById(compilationId, request);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") @Positive Long compilationId) {
        service.deleteCompilationById(compilationId);
    }
}
