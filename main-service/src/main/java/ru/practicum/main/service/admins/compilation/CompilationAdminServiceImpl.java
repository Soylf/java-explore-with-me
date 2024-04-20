package ru.practicum.main.service.admins.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.dto.compilation.CompilationDto;
import ru.practicum.main.dto.compilation.CompilationNewDto;
import ru.practicum.main.dto.compilation.CompilationUpdateRequest;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CompilationMapper;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;


import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(CompilationNewDto request) {
        Compilation compilation = CompilationMapper.MAPPER.fromNewDto(request);

        // Установка значения поля 'pinned' в false, если оно не указано в запросе
        if (!Objects.nonNull(request.getPinned())) {
            compilation.setPinned(false);
        }
        // Установка событий для подборки, если они указаны в запросе
        if (request.getEvents() != null) {
            compilation.setEvents(getEvents(request.getEvents()));
        }
        Compilation newCompilation = compilationRepository.save(compilation);

        return CompilationMapper.MAPPER.toDto(newCompilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationById(Long compilationId, CompilationUpdateRequest request) {
        Compilation compilation = getCompilation(compilationId);

        // Установка событий для подборки, если они указаны в запросе
        if (request.getEvents() != null) {
            compilation.setEvents(getEvents(request.getEvents()));
        }
        // Установка значения поля 'pinned' в false, если оно указано в запросе
        if (request.getPinned() != null) {
            compilation.setPinned(false);
        }
        // Установка нового заголовка подборки, если он указан в запросе
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        Compilation newCompilation = compilationRepository.save(compilation);

        return CompilationMapper.MAPPER.toDto(newCompilation);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compilationId) {
        // Проверка существования подборки перед удалением
        checkCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    //Дополнительные методы
    private List<Event> getEvents(Set<Long> events) {
        return eventRepository.findAllByIdIn(events);
    }

    private void checkCompilation(Long compilationId) {
        compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException(String.format("Подборка id='%s' не найдена", compilationId)));
    }

    private Compilation getCompilation(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException(String.format("Подборка с with id='%s' не найдена", compilationId)));
    }
}