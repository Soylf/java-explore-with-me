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
import ru.practicum.statsClient.client.StatClient;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsPublicServiceImpl implements EventsPublicService {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatClient statClient;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid,
                                            LocalDateTime startDate, LocalDateTime endDate,
                                            Boolean onlyAvailable, String sort,
                                            Integer from, Integer size, String ip, String uri) {
        checkTime(startDate, endDate);
        checkCategory(categories);
        saveInfoToStatistics(ip, uri);

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
        if (Objects.nonNull(onlyAvailable) && onlyAvailable) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        // Фильтрация по состоянию (опубликованные события)
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

        Page<Event> eventPage = eventRepository.findAll(specification, pageable);
        List<Event> events = eventPage.getContent();

        updateViewsOfEvents(events);

        return EventMapper.MAPPER.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getEventById(Long eventId, String ip, String uri) {
        // Поиск события по ID и его состоянию (опубликованное событие)
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        saveInfoToStatistics(ip, uri);
        updateViewsOfEvents(List.of(event));

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


    //методы для сохранения и обновления(stat)
    private void saveInfoToStatistics(String ip, String uri) {
        statClient.saveHit(EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .build());
    }

    private void updateViewsOfEvents(List<Event> events) {
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());

        List<StatDto> statistics = getViewsStatistics(uris);

        events.forEach(event -> {
            StatDto foundViewInStats = statistics.stream()
                    .filter(statDto -> {
                        Long eventIdFromStats = Long.parseLong(statDto.getUri().substring("/events/".length()));
                        return Objects.equals(eventIdFromStats, event.getId());
                    })
                    .findFirst()
                    .orElse(null);

            long currentCountViews = foundViewInStats == null ? 0 : foundViewInStats.getHits();
            event.setViews((int) currentCountViews + 1);
        });

        eventRepository.saveAll(events);
    }

    private List<StatDto> getViewsStatistics(List<String> uris) {
        return statClient.getStatistics(
                LocalDateTime.now().minusYears(100).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(5).format(dateTimeFormatter),
                uris,
                true);
    }
}