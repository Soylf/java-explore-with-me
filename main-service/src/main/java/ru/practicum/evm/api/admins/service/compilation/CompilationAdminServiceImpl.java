package ru.practicum.evm.api.admins.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.api.repository.CompilationRepository;
import ru.practicum.evm.api.repository.EventRepository;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.CompilationNewDto;
import ru.practicum.evm.dto.compilation.CompilationUpdateRequest;
import ru.practicum.evm.error.exception.NotFoundException;
import ru.practicum.evm.mapper.CompilationMapper;
import ru.practicum.evm.model.Compilation;
import ru.practicum.evm.model.Event;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(CompilationNewDto request) {
        Compilation compilation = CompilationMapper.MAPPER.fromNewDto(request);

        // Установка значения поля 'pinned' в false, если оно не указано в запросе
        if (request.getPinned() != null) {
            compilation.setPinned(false);
        }
        // Установка событий для подборки, если они указаны в запросе
        if (request.getEvents() != null) {
            compilation.setEvents(getEvents(request.getEvents()));
        }

        return CompilationMapper.MAPPER.toDto(compilationRepository.save(compilation));
    }

    @Override
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

        return CompilationMapper.MAPPER.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilationById(Long compilationId) {
        // Проверка существования подборки перед удалением
        checkCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    //Дополнительыне методы
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
