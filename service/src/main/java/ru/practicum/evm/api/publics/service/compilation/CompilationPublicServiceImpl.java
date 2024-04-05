package ru.practicum.evm.api.publics.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.evm.api.repository.CompilationRepository;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.error.exception.NotFoundException;
import ru.practicum.evm.mapper.CompilationMapper;
import ru.practicum.evm.model.Compilation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository repository;

    @Override
    public List<CompilationDto> getComplications(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> compilationPage = repository.findAllByPinned(pinned, pageable);
        return CompilationMapper.MAPPER.toDtoList(compilationPage.getContent());
    }

    @Override
    public CompilationDto getCompilation(Long compilationId) {
        return CompilationMapper.MAPPER.toDto(checkCompilation(compilationId));

    }

    private Compilation checkCompilation(Long compilationId) {
        return repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Что то пошло не так с этим id: '%s' не найдена", compilationId)));
    }
}
