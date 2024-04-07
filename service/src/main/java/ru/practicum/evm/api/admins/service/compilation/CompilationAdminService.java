package ru.practicum.evm.api.admins.service.compilation;

import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.CompilationNewDto;
import ru.practicum.evm.dto.compilation.CompilationUpdateRequest;

public interface CompilationAdminService {
    CompilationDto createCompilation(CompilationNewDto request);

    CompilationDto updateCompilationById(Long compilationId, CompilationUpdateRequest request);

    void deleteCompilationById(Long compilationId);
}
