package ru.practicum.main.api.publics.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.main.api.repository.CategoryRepository;
import ru.practicum.main.api.repository.EventRepository;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.error.exception.BadRequestException;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventsPublicServiceImpl implements EventsPublicService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid,
                                            LocalDateTime startDate, LocalDateTime endDate,
                                            Boolean onlyAvailable, String sort,
                                            Integer from, Integer size) {
        checkTime(startDate, endDate);
        checkCategory(categories);

        Pageable pageable = PageRequest.of(from / size, size);
        Specification<Event> specification = Specification.where(null);

        // Фильтрация по тексту в аннотации или описании
        if (text != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                            "%" + text.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + text.toLowerCase() + "%")
            ));
        }

        // Фильтрация по категориям
        if (!categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }

        // Фильтрация по дате начала события
        LocalDateTime startDateTime = Objects.requireNonNullElse(startDate, LocalDateTime.now());
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));

        // Фильтрация по дате окончания события
        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), endDate));
        }
        // Фильтрация по доступности
        if (onlyAvailable) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        // Фильтрация по состоянию (опубликованные события)
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

        Page<Event> eventPage = eventRepository.findAll(specification, pageable);
        List<Event> events = eventPage.getContent();


        return EventMapper.MAPPER.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        // Поиск события по ID и его состоянию (опубликованное событие)
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        // Возврат полной информации о событии в виде DTO
        return EventMapper.MAPPER.toEventFullDto(event);
    }

    // Дополнительные методы
    private void checkTime(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date can not after end date");
        }
    }

    private void checkCategory(List<Long> categories) {
        for (Long id : categories) {
            categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id: '%s' not found", id)));
        }
    }
}