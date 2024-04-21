package ru.practicum.main.service.publics.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CompilationMapper;
import ru.practicum.main.model.Compilation;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository repository;

    @Transactional
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        // Получение страницы подборок с учетом закрепленности
        Page<Compilation> compilationPage = repository.findAllByPinned(pinned, pageable);
        List<Compilation> compilations = compilationPage.getContent();

        return CompilationMapper.MAPPER.toDtoList(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compilationId) {
        return CompilationMapper.MAPPER.toDto(getCompilationById(compilationId));

    }

    //Дополнительыне методы
    private Compilation getCompilationById(Long compilationId) {
        return repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Что то пошло не так с этим id: '%s' не найдена", compilationId)));
    }
}
