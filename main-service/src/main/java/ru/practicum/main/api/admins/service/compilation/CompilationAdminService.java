package ru.practicum.main.api.admins.service.compilation;

import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.CompilationNewDto;
import ru.practicum.main.dto.compilation.CompilationUpdateRequest;

public interface CompilationAdminService {
    CompilationDto createCompilation(CompilationNewDto request);

    CompilationDto updateCompilationById(Long compilationId, CompilationUpdateRequest request);

    void deleteCompilationById(Long compilationId);
}
