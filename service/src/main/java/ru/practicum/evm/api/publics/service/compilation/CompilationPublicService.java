package ru.practicum.evm.api.publics.service.compilation;

import ru.practicum.evm.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compilationId);
}
